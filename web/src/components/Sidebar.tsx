import React from 'react';
import { Shield, FolderLock, Image, Settings, LogOut, HardDrive } from 'lucide-react';
import './Sidebar.css';

export function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <Shield className="sidebar-logo-icon" />
        <h2>StealthVault</h2>
      </div>
      
      <nav className="sidebar-nav">
        <a href="#" className="nav-item active">
          <FolderLock className="nav-icon" />
          <span>All Files</span>
        </a>
        <a href="#" className="nav-item">
          <Image className="nav-icon" />
          <span>Media</span>
        </a>
        <a href="#" className="nav-item">
          <HardDrive className="nav-icon" />
          <span>Storage</span>
        </a>
      </nav>
      
      <div className="sidebar-footer">
        <a href="#" className="nav-item">
          <Settings className="nav-icon" />
          <span>Settings</span>
        </a>
      </div>
    </aside>
  );
}
