// frontend/src/components/Layout.js
import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import '../styles/Layout.css';

export default function Layout({ children }) {
    const { user, isAuthenticated, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const [menuOpen, setMenuOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const isActive = (path) => location.pathname === path;

    // Don't show navigation on auth pages
    if (location.pathname === '/login' || location.pathname === '/register') {
        return children;
    }

    return (
        <div className="layout">
            <nav className="navbar">
                <div className="navbar-container">
                    <Link to="/" className="navbar-brand">
                        <span className="brand-icon">💻</span>
                        <span className="brand-text">CodeHub</span>
                    </Link>

                    <button 
                        className="mobile-menu-button"
                        onClick={() => setMenuOpen(!menuOpen)}
                    >
                        ☰
                    </button>

                    <div className={`nav-menu ${menuOpen ? 'open' : ''}`}>
                        {isAuthenticated ? (
                            <>
                                <Link 
                                    to="/dashboard"
                                    className={`nav-link ${isActive('/dashboard') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Dashboard
                                </Link>
                                <Link 
                                    to="/problems"
                                    className={`nav-link ${isActive('/problems') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Problems
                                </Link>
                                <Link 
                                    to="/contests"
                                    className={`nav-link ${isActive('/contests') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Contests
                                </Link>
                                <Link 
                                    to="/leaderboard"
                                    className={`nav-link ${isActive('/leaderboard') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Leaderboard
                                </Link>
                                <Link 
                                    to="/courses"
                                    className={`nav-link ${isActive('/courses') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Courses
                                </Link>

                                <div className="nav-divider"></div>

                                <div className="user-menu">
                                    <div className="user-info">
                                        <div className="user-avatar">
                                            {user?.firstName?.charAt(0) || 'U'}
                                        </div>
                                        <div className="user-details">
                                            <div className="user-name">
                                                {user?.firstName} {user?.lastName}
                                            </div>
                                            <div className="user-email">
                                                {user?.email}
                                            </div>
                                        </div>
                                    </div>

                                    <Link 
                                        to="/profile"
                                        className={`nav-link ${isActive('/profile') ? 'active' : ''}`}
                                        onClick={() => setMenuOpen(false)}
                                    >
                                        Profile
                                    </Link>

                                    <button 
                                        onClick={() => {
                                            handleLogout();
                                            setMenuOpen(false);
                                        }}
                                        className="logout-button"
                                    >
                                        Logout
                                    </button>
                                </div>
                            </>
                        ) : (
                            <>
                                <Link 
                                    to="/login"
                                    className={`nav-link ${isActive('/login') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Sign In
                                </Link>
                                <Link 
                                    to="/register"
                                    className="nav-link nav-button"
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Sign Up
                                </Link>
                            </>
                        )}
                    </div>
                </div>
            </nav>

            <main className="main-content">
                {children}
            </main>

            <footer className="footer">
                <div className="footer-container">
                    <div className="footer-section">
                        <h3>CodeHub</h3>
                        <p>Learn, Practice, and Master Data Structures & Algorithms</p>
                    </div>
                    <div className="footer-section">
                        <h4>Quick Links</h4>
                        {isAuthenticated ? (
                            <ul>
                                <li><Link to="/problems">Problems</Link></li>
                                <li><Link to="/contests">Contests</Link></li>
                                <li><Link to="/leaderboard">Leaderboard</Link></li>
                            </ul>
                        ) : (
                            <ul>
                                <li><Link to="/login">Sign In</Link></li>
                                <li><Link to="/register">Sign Up</Link></li>
                            </ul>
                        )}
                    </div>
                    <div className="footer-section">
                        <h4>Contact</h4>
                        <p>Email: contact@codehub.com</p>
                        <p>Discord: Join our community</p>
                    </div>
                </div>
                <div className="footer-bottom">
                    <p>&copy; 2024 CodeHub. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
}
