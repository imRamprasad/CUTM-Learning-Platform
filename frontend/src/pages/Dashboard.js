// frontend/src/pages/Dashboard.js
import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useFetch } from '../hooks';
import { useNavigate } from 'react-router-dom';
import '../styles/Dashboard.css';

export default function Dashboard() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const { data: solvedCount } = useFetch('/api/submissions/user/solved-count');

    if (!user) {
        navigate('/login');
        return null;
    }

    return (
        <div className="dashboard-container">
            <div className="dashboard-header">
                <h1>Welcome back, {user?.firstName}! 👋</h1>
                <p>Your learning journey continues here</p>
            </div>

            <div className="stats-grid">
                <StatCard
                    icon="✅"
                    title="Problems Solved"
                    value={solvedCount ?? user?.problemsSolved ?? 0}
                    color="#10b981"
                />
                <StatCard
                    icon="🏆"
                    title="Global Rank"
                    value={user?.rank || 'Unranked'}
                    color="#3b82f6"
                />
                <StatCard
                    icon="⭐"
                    title="Total Points"
                    value={user?.totalPoints || 0}
                    color="#f59e0b"
                />
                <StatCard
                    icon="🔥"
                    title="Streak"
                    value="5 days"
                    color="#ef4444"
                />
            </div>

            <div className="dashboard-sections">
                <section className="dashboard-section">
                    <h2>Recent Activity</h2>
                    <ActivityList />
                </section>

                <section className="dashboard-section">
                    <h2>Recommended Problems</h2>
                    <ProblemSuggestions />
                </section>
            </div>
        </div>
    );
}

function StatCard({ icon, title, value, color }) {
    return (
        <div className="stat-card" style={{ borderLeftColor: color }}>
            <div className="stat-icon">{icon}</div>
            <div className="stat-content">
                <p className="stat-title">{title}</p>
                <p className="stat-value">{value}</p>
            </div>
        </div>
    );
}

function ActivityList() {
    const activities = [
        { id: 1, type: 'solved', title: 'Solved Two Sum', time: '2 hours ago' },
        { id: 2, type: 'contest', title: 'Participated in Weekly Contest 50', time: '1 day ago' },
        { id: 3, type: 'discussion', title: 'Posted a solution to Binary Tree Problem', time: '2 days ago' },
    ];

    return (
        <ul className="activity-list">
            {activities.map((activity) => (
                <li key={activity.id} className="activity-item">
                    <span className={`activity-badge activity-${activity.type}`}></span>
                    <div>
                        <p className="activity-title">{activity.title}</p>
                        <p className="activity-time">{activity.time}</p>
                    </div>
                </li>
            ))}
        </ul>
    );
}

function ProblemSuggestions() {
    const suggestions = [
        { id: 1, title: 'Median of Two Sorted Arrays', difficulty: 'HARD', solveRate: 32 },
        { id: 2, title: 'Longest Substring Without Repeating', difficulty: 'MEDIUM', solveRate: 58 },
        { id: 3, title: 'Valid Parentheses', difficulty: 'EASY', solveRate: 92 },
    ];

    return (
        <div className="suggestions-list">
            {suggestions.map((problem) => (
                <div key={problem.id} className="suggestion-card">
                    <h4>{problem.title}</h4>
                    <div className="difficulty-badge" data-level={problem.difficulty}>
                        {problem.difficulty}
                    </div>
                    <p className="solve-rate">✅ {problem.solveRate}% Solve Rate</p>
                </div>
            ))}
        </div>
    );
}
