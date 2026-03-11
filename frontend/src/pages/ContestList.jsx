import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'

export default function ContestList() {
  const [contests, setContests] = useState([])
  const [activeTab, setActiveTab] = useState('upcoming')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchContests()
  }, [activeTab])

  const fetchContests = async () => {
    try {
      const token = localStorage.getItem('token')
      const endpoint = activeTab === 'active' ? '/api/contests/active' : '/api/contests/upcoming'
      const response = await axios.get(endpoint, {
        headers: { Authorization: `Bearer ${token}` }
      })
      setContests(response.data)
    } catch (error) {
      console.error('Error fetching contests:', error)
      // Fallback demo data
      setContests([
        {
          id: '1',
          title: 'Weekly Coding Challenge #45',
          description: 'Solve algorithmic problems under time pressure',
          startTime: new Date(Date.now() + 86400000).toISOString(),
          endTime: new Date(Date.now() + 172800000).toISOString(),
          participants: [],
          status: 'SCHEDULED'
        },
        {
          id: '2',
          title: 'Dynamic Programming Master',
          description: 'Focus on DP problems ranging from easy to hard',
          startTime: new Date(Date.now() + 604800000).toISOString(),
          endTime: new Date(Date.now() + 691200000).toISOString(),
          participants: [],
          status: 'SCHEDULED'
        }
      ])
    } finally {
      setLoading(false)
    }
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const getTimeRemaining = (startTime) => {
    const now = new Date()
    const start = new Date(startTime)
    const diff = start - now

    if (diff <= 0) return 'Started'

    const days = Math.floor(diff / (1000 * 60 * 60 * 24))
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))

    if (days > 0) return `${days}d ${hours}h`
    return `${hours}h`
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-4">
            🏆 Coding Contests
          </h1>
          <p className="text-gray-600 dark:text-gray-400">
            Compete with developers worldwide
          </p>
        </div>

        {/* Tabs */}
        <div className="flex justify-center mb-8">
          <div className="bg-white dark:bg-gray-800 rounded-xl p-1 shadow-sm">
            <button
              onClick={() => setActiveTab('upcoming')}
              className={`px-6 py-2 rounded-lg font-medium transition-all ${
                activeTab === 'upcoming'
                  ? 'bg-blue-500 text-white'
                  : 'text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700'
              }`}
            >
              Upcoming
            </button>
            <button
              onClick={() => setActiveTab('active')}
              className={`px-6 py-2 rounded-lg font-medium transition-all ${
                activeTab === 'active'
                  ? 'bg-blue-500 text-white'
                  : 'text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700'
              }`}
            >
              Active
            </button>
          </div>
        </div>

        {/* Contests Grid */}
        {loading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
          </div>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {contests.map((contest) => (
              <div
                key={contest.id}
                className="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
              >
                <div className="p-6">
                  <div className="flex items-start justify-between mb-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      contest.status === 'ACTIVE' 
                        ? 'bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300'
                        : 'bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300'
                    }`}>
                      {contest.status === 'ACTIVE' ? '🟢 Live' : '📅 Upcoming'}
                    </span>
                    <span className="text-sm text-gray-500 dark:text-gray-400">
                      {getTimeRemaining(contest.startTime)}
                    </span>
                  </div>

                  <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">
                    {contest.title}
                  </h3>
                  <p className="text-gray-600 dark:text-gray-400 mb-4 line-clamp-2">
                    {contest.description}
                  </p>

                  <div className="space-y-2 mb-6">
                    <div className="flex items-center text-sm text-gray-500 dark:text-gray-400">
                      <span className="mr-2">📅</span>
                      {formatDate(contest.startTime)}
                    </div>
                    <div className="flex items-center text-sm text-gray-500 dark:text-gray-400">
                      <span className="mr-2">👥</span>
                      {contest.participants?.length || 0} participants
                    </div>
                  </div>

                  <Link
                    to={`/contests/${contest.id}`}
                    className="block w-full text-center px-4 py-2 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-lg font-medium hover:from-blue-600 hover:to-purple-700 transition-all"
                  >
                    {contest.status === 'ACTIVE' ? 'Join Contest' : 'View Details'}
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}

        {contests.length === 0 && !loading && (
          <div className="text-center py-12">
            <div className="text-6xl mb-4">🎯</div>
            <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
              No {activeTab} contests
            </h3>
            <p className="text-gray-500 dark:text-gray-400">
              Check back later for new contests
            </p>
          </div>
        )}
      </div>
    </div>
  )
}

