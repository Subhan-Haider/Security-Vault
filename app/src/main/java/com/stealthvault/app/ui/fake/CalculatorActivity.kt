package com.stealthvault.app.ui.fake

import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.stealthvault.app.R
import com.stealthvault.app.data.local.SecurityPreferenceManager
import com.stealthvault.app.databinding.ActivityCalculatorBinding
import com.stealthvault.app.ui.vault.VaultActivity
import com.stealthvault.app.utils.CameraHelper
import dagger.hilt.android.AndroidEntryPoint
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private var currentInput = ""

    @Inject lateinit var securityPrefs: SecurityPreferenceManager
    @Inject lateinit var cameraHelper: CameraHelper

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = ActivityCalculatorBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupBiometric()
            setupButtons()

            // Request camera permission for intruder selfie
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.CAMERA), 101
                )
            }

            // Neutral initial state — looks like a real calculator
            binding.tvDisplay.text = "0"
            binding.tvHistory.text = ""

            if (!securityPrefs.isSetupComplete) {
                // First-time setup: guide shown in history area only during setup phase
                if (securityPrefs.masterPin == null) {
                    binding.tvHistory.text = "SET PIN"
                } else if (securityPrefs.isDecoyEnabled) {
                    binding.tvHistory.text = "SET DECOY"
                } else {
                    securityPrefs.isSetupComplete = true
                    binding.tvHistory.text = ""
                }
            }

            binding.tvHistory.setOnLongClickListener {
                if (securityPrefs.isSetupComplete) {
                    biometricPrompt.authenticate(promptInfo)
                }
                true
            }
        } catch (t: Throwable) {
            // Fallback crash display for debugging
            val tv = android.widget.TextView(this).apply {
                text = "CRASH: " + android.util.Log.getStackTraceString(t)
                setTextColor(android.graphics.Color.RED)
                textSize = 14f
                setPadding(32, 32, 32, 32)
            }
            val scrollView = android.widget.ScrollView(this).apply {
                addView(tv)
                setBackgroundColor(android.graphics.Color.BLACK)
            }
            setContentView(scrollView)
        }
    }

    private fun setupBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    launchVault(false)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Vault Authentication")
            .setSubtitle("Authenticate to access your secure vault")
            .setNegativeButtonText("Use PIN")
            .build()
    }

    private fun setupButtons() {
        val numericButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9,
            binding.btnDot, binding.btnAdd, binding.btnSub, binding.btnMul, binding.btnDiv
        )

        numericButtons.forEach { button ->
            button.setOnClickListener {
                currentInput += (it as Button).text
                binding.tvDisplay.text = currentInput
            }
        }

        binding.btnClear.setOnClickListener {
            currentInput = ""
            binding.tvDisplay.text = "0"
            binding.tvHistory.text = ""
        }

        binding.btnBackspace.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                binding.tvDisplay.text = if (currentInput.isEmpty()) "0" else currentInput
            }
        }

        binding.btnEquals.setOnClickListener {
            checkUnlock()
        }
    }

    private fun checkUnlock() {
        if (!securityPrefs.isSetupComplete) {
            setupPins()
            return
        }

        val master = securityPrefs.masterPin
        val decoy = securityPrefs.decoyPin

        // Lockout state — reject silently, same as wrong PIN
        if (securityPrefs.isLockedOut) {
            // Only auto-reset after 30s, no on-screen indication
            binding.root.postDelayed({
                securityPrefs.isLockedOut = false
                securityPrefs.failedPinAttempts = 0
            }, 30_000)
            rejectEntry()
            return
        }

        when (currentInput) {
            master -> {
                securityPrefs.failedPinAttempts = 0
                securityPrefs.lastUnlockTime = System.currentTimeMillis()
                launchVault(isDecoy = false)
            }
            decoy -> {
                if (securityPrefs.isDecoyEnabled) {
                    securityPrefs.failedPinAttempts = 0
                    securityPrefs.lastUnlockTime = System.currentTimeMillis()
                    launchVault(isDecoy = true)
                } else {
                    // Fallthrough to reject if decoy is disabled but matches exactly
                    rejectEntry()
                }
            }
            else -> {
                // Only treat pure numeric strings (length ≥ 4) as PIN attempts
                if (currentInput.length >= 4 && !currentInput.contains("[+×÷−.]".toRegex())) {
                    val attempts = securityPrefs.failedPinAttempts + 1
                    securityPrefs.failedPinAttempts = attempts

                    // Silently capture intruder photo
                    cameraHelper.takeIntruderPhoto(this)

                    // Trigger lockout threshold silently
                    if (attempts >= securityPrefs.maxFailedAttempts) {
                        securityPrefs.isLockedOut = true
                    }

                    rejectEntry()
                } else {
                    // Input contains operators — treat as math
                    performMath()
                }
            }
        }
    }

    /**
     * Silently reject a wrong PIN attempt.
     * No text message, no attempt count — just a shake + vibration.
     */
    private fun rejectEntry() {
        // Shake the display
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        binding.tvDisplay.startAnimation(shake)

        // Subtle vibration (short double-pulse, similar to a physical lock click)
        try {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(
                    longArrayOf(0, 60, 60, 60), -1
                ))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 60, 60, 60), -1)
            }
        } catch (_: Exception) {}

        // Clear display silently — no error text
        currentInput = ""
        binding.tvDisplay.text = "0"
        // Keep history blank — don't expose anything
        binding.tvHistory.text = ""
    }

    private var pendingPin: String? = null

    private fun setupPins() {
        val input = currentInput
        if (input.isEmpty() || input.length < 4) {
            // Minimal feedback — just clear, no detailed instruction
            rejectEntry()
            return
        }

        if (securityPrefs.masterPin == null) {
            if (pendingPin == null) {
                pendingPin = input
                binding.tvHistory.text = "CONFIRM PIN"
                currentInput = ""
                binding.tvDisplay.text = "0"
            } else {
                if (pendingPin == input) {
                    securityPrefs.masterPin = input
                    pendingPin = null
                    if (securityPrefs.isDecoyEnabled) {
                        binding.tvHistory.text = "SET DECOY"
                        currentInput = ""
                        binding.tvDisplay.text = "0"
                    } else {
                        securityPrefs.isSetupComplete = true
                        binding.tvHistory.text = ""
                        binding.tvDisplay.text = "0"
                        currentInput = ""
                    }
                } else {
                    pendingPin = null
                    binding.tvHistory.text = "SET PIN"
                    rejectEntry()
                }
            }
        } else if (securityPrefs.decoyPin == null) {
            if (pendingPin == null) {
                if (input == securityPrefs.masterPin) {
                    // Same as master — silently reject
                    rejectEntry()
                } else {
                    pendingPin = input
                    binding.tvHistory.text = "CONFIRM DECOY"
                    currentInput = ""
                    binding.tvDisplay.text = "0"
                }
            } else {
                if (pendingPin == input) {
                    securityPrefs.decoyPin = input
                    securityPrefs.isSetupComplete = true
                    pendingPin = null
                    binding.tvHistory.text = ""
                    binding.tvDisplay.text = "0"
                    currentInput = ""
                } else {
                    pendingPin = null
                    binding.tvHistory.text = "SET DECOY"
                    rejectEntry()
                }
            }
        }
    }

    private fun performMath() {
        try {
            val expression = ExpressionBuilder(
                currentInput
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace("−", "-")
            ).build()

            val result = expression.evaluate()
            val resultStr = if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                "%.8g".format(result).trimEnd('0').trimEnd('.')
            }
            binding.tvHistory.text = currentInput
            binding.tvDisplay.text = resultStr
            currentInput = resultStr
        } catch (e: Exception) {
            binding.tvDisplay.text = "Error"
            binding.tvHistory.text = ""
            currentInput = ""
        }
    }

    private fun launchVault(isDecoy: Boolean) {
        val intent = Intent(this, VaultActivity::class.java).apply {
            putExtra("IS_DECOY", isDecoy)
        }
        startActivity(intent)
        finish()
    }
}
