import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import { useAuth } from '../contexts/AuthContext.jsx'

export default function Dashboard() {
  const { user } = useAuth()
  const [stats, setStats] = useState({
    solvedCount: 0,
    submissionCount: 0,
    ranking: 0,
    points: 0
  })
  const [recentProblems, setRecentProblems] = useState([])
  const [contests, setContests] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      const token = localStorage.getItem('token')
      
      // Fetch solved count
      const solvedRes = await axios.get('/api/submissions/user/solved-count', {
        headers: { Authorization: `Bearer ${token}` }
      })
      
      setStats(prev => ({ ...prev, solvedCount: solvedRes.data || 0 }))
    } catch (error) {
      console.error('Error fetching dashboard data:', error)
    } finally {
      // Demo data
      setStats({
        solvedCount: 45,
        submissionCount: 120,
        ranking: 156,
        points: 850
      })
      setRecentProblems([
        { id: '1', title: 'Two Sum', difficulty: 'EASY', status: 'SOLVED' },
        { id: '2', title: 'Add Two Numbers', difficulty: 'MEDIUM', status: 'SOLVED' },
        { id: '3', title: 'Longest Substring', difficulty: 'MEDIUM', status: 'ATTEMPTED' },
        { id: '4', title: 'Median of Two Sorted Arrays', difficulty: 'HARD', status: 'ATTEMPTED' }
      ])
      setContests([
        { id: '1', title: 'Weekly Challenge #45', status: 'UPCOMING', time: '2 days' },
        { id: '2', title: 'DP Masterclass', status: 'ACTIVE', time: 'Live now' }
      ])
      setLoading(false)
    }
  }

  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'EASY': return 'bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300'
      case 'MEDIUM': return 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900 dark:text-yellow-300'
      case 'HARD': return 'bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-300'
      default: return 'bg-gray-100 text-gray-700'
    }
  }

  const statCards = [
    { label: 'Problems Solved', value: stats.solvedCount, icon: '✅', color: 'from-green-400 to-emerald-600' },
    { label: 'Total Submissions', value: stats.submissionCount, icon: '📝', color: 'from-blue-400 to-indigo-600' },
    { label: 'Global Ranking', value: `#${stats.ranking}`, icon: '🏆', color: 'from-yellow-400 to-orange-600' },
    { label: 'Total Points', value: stats.points, icon: '⭐', color: 'from-purple-400 to-pink-600' }
  ]

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="container mx-auto px-4">
        {/* Welcome Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
            Welcome back, {user?.firstName || 'Developer'}! 👋
          </h1>
          <p className="text-gray-600 dark:text-gray-400">
            Ready to solve some problems today?
          </p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {statCards.map((stat, index) => (
            <div
              key={index}
              className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-6 hover:shadow-md transition-shadow"
            >
              <div className="flex items-center justify-between mb-4">
                <div className={`w-12 h-12 rounded-xl bg-gradient-to-r ${stat.color} flex items-center justify-center text-2xl`}>
                  {stat.icon}
                </div>
              </div>
              <div className="text-2xl font-bold text-gray-900 dark:text-white mb-1">
                {stat.value}
              </div>
              <div className="text-sm text-gray-500 dark:text-gray-400">
                {stat.label}
              </div>
            </div>
          ))}
        </div>

        {/* Main Content Grid */}
        <div className="grid lg:grid-cols-3 gap-6">
          {/* Recent Problems */}
          <div className="lg:col-span-2 bg-white dark:bg-gray-800 rounded-xl shadow-sm">
            <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center">
              <h2 className="text-xl font-semibold text-gray-900 dark:text-white">
                Continue Solving
              </h2>
              <Link to="/problems" className="text-blue-500 hover:text-blue-600 text-sm font-medium">
                View All →
              </Link>
            </div>
            <div className="divide-y divide-gray-200 dark:divide-gray-700">
              {recentProblems.map((problem) => (
                <Link
                  key={problem.id}
                  to={`/problems/${problem.id}`}
                  className="p-4 flex items-center justify-between hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                >
                  <div className="flex items-center space-x-4">
                    <div className="text-2xl">
                      {problem.status === 'SOLVED' ? '✅' : '📝'}
                    </div>
                    <div>
                      <div className="font-medium text-gray-900 dark:text-white">
                        {problem.title}
                      </div>
                      <span className={`text-xs px-2 py-0.5 rounded ${getDifficultyColor(problem.difficulty)}`}>
                        {problem.difficulty}
                      </span>
                    </div>
                  </div>
                  <span className="text-gray-400">→</span>
                </Link>
              ))}
            </div>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Quick Actions */}
            <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-6">
              <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
                Quick Actions
              </h2>
              <div className="space-y-2">
                <Link
                  to="/problems"
                  className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
                >
                  <span className="text-xl">💻</span>
                  <span className="text-gray-700 dark:text-gray-300">Practice Problems</span>
                </Link>
                <Link
                  to="/contests"
                  className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
                >
                  <span className="text-xl">🏆</span>
                  <span className="text-gray-700 dark:text-gray-300">Join Contest</span>
                </Link>
                <Link
                  to="/leaderboard"
                  className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
                >
                  <span className="text-xl">📊</span>
                  <span className="text-gray-700 dark:text-gray-300">Leaderboard</span>
                </Link>
              </div>
            </div>

            {/* Upcoming Contests */}
            <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-6">
              <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
                Upcoming Contests
              </h2>
              <div className="space-y-3">
                {contests.map((contest) => (
                  <Link
                    key={contest.id}
                    to={`/contests/${contest.id}`}
                    className="block p-3 rounded-lg border border-gray-200 dark:border-gray-700 hover:border-blue-500 transition-colors"
                  >
                    <div className="font-medium text-gray-900 dark:text-white text-sm">
                      {contest.title}
                    </div>
                    <div className="flex items-center justify-between mt-2">
                      <span className={`text-xs px-2 py-0.5 rounded ${
                        contest.status === 'ACTIVE' 
                          ? 'bg-green-100 text-green-700' 
                          : 'bg-blue-100 text-blue-700'
                      }`}>
                        {contest.status}
                      </span>
                      <span className="text-xs text-gray-500">
                        {contest.time}
                      </span>
                    </div>
                  </Link>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

