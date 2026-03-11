import React, { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import axios from 'axios'

export default function ContestDetail() {
  const { id } = useParams()
  const [contest, setContest] = useState(null)
  const [problems, setProblems] = useState([])
  const [loading, setLoading] = useState(true)
  const [joined, setJoined] = useState(false)

  useEffect(() => {
    fetchContest()
  }, [id])

  const fetchContest = async () => {
    try {
      const token = localStorage.getItem('token')
      const response = await axios.get(`/api/contests/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      setContest(response.data.contest)
      setProblems(response.data.problems || [])
    } catch (error) {
      console.error('Error fetching contest:', error)
      // Demo data
      setContest({
        id: id,
        title: 'Weekly Coding Challenge #45',
        description: 'Solve algorithmic problems under time pressure. This contest features problems from various categories including arrays, strings, dynamic programming, and graphs.',
        startTime: new Date(Date.now() + 3600000).toISOString(),
        endTime: new Date(Date.now() + 90000000).toISOString(),
        participants: ['user1', 'user2'],
        status: 'SCHEDULED'
      })
      setProblems([
        { id: '1', title: 'Two Sum', difficulty: 'EASY', categories: ['Arrays', 'Hash Table'] },
        { id: '2', title: 'Longest Substring Without Repeating Characters', difficulty: 'MEDIUM', categories: ['Strings', 'Sliding Window'] },
        { id: '3', title: 'Median of Two Sorted Arrays', difficulty: 'HARD', categories: ['Arrays', 'Binary Search'] }
      ])
    } finally {
      setLoading(false)
    }
  }

  const handleJoin = async () => {
    try {
      const token = localStorage.getItem('token')
      await axios.post(`/api/contests/${id}/join`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      })
      setJoined(true)
    } catch (error) {
      console.error('Error joining contest:', error)
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

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="container mx-auto px-4">
        {/* Back Button */}
        <Link to="/contests" className="inline-flex items-center text-blue-500 hover:text-blue-600 mb-6">
          ← Back to Contests
        </Link>

        {/* Contest Header */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-8 mb-8">
          <div className="flex items-start justify-between mb-4">
            <div>
              <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                contest?.status === 'ACTIVE' 
                  ? 'bg-green-100 text-green-700'
                  : 'bg-blue-100 text-blue-700'
              }`}>
                {contest?.status === 'ACTIVE' ? '🟢 Live' : '📅 Upcoming'}
              </span>
            </div>
            <button
              onClick={handleJoin}
              disabled={joined}
              className={`px-6 py-2 rounded-lg font-medium transition-all ${
                joined
                  ? 'bg-green-500 text-white'
                  : 'bg-gradient-to-r from-blue-500 to-purple-600 text-white hover:from-blue-600 hover:to-purple-700'
              }`}
            >
              {joined ? '✓ Joined' : 'Join Contest'}
            </button>
          </div>

          <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-4">
            {contest?.title}
          </h1>
          <p className="text-gray-600 dark:text-gray-400 mb-6">
            {contest?.description}
          </p>

          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-gray-50 dark:bg-gray-700 rounded-lg p-4">
              <div className="text-sm text-gray-500 dark:text-gray-400 mb-1">Start Time</div>
              <div className="font-semibold text-gray-900 dark:text-white">
                {formatDate(contest?.startTime)}
              </div>
            </div>
            <div className="bg-gray-50 dark:bg-gray-700 rounded-lg p-4">
              <div className="text-sm text-gray-500 dark:text-gray-400 mb-1">End Time</div>
              <div className="font-semibold text-gray-900 dark:text-white">
                {formatDate(contest?.endTime)}
              </div>
            </div>
            <div className="bg-gray-50 dark:bg-gray-700 rounded-lg p-4">
              <div className="text-sm text-gray-500 dark:text-gray-400 mb-1">Participants</div>
              <div className="font-semibold text-gray-900 dark:text-white">
                {contest?.participants?.length || 0}
              </div>
            </div>
            <div className="bg-gray-50 dark:bg-gray-700 rounded-lg p-4">
              <div className="text-sm text-gray-500 dark:text-gray-400 mb-1">Problems</div>
              <div className="font-semibold text-gray-900 dark:text-white">
                {problems.length}
              </div>
            </div>
          </div>
        </div>

        {/* Problems List */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden">
          <div className="p-6 border-b border-gray-200 dark:border-gray-700">
            <h2 className="text-xl font-bold text-gray-900 dark:text-white">
              Contest Problems
            </h2>
          </div>
          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {problems.map((problem, index) => (
              <Link
                key={problem.id}
                to={`/problems/${problem.id}`}
                className="p-6 flex items-center justify-between hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
              >
                <div className="flex items-center space-x-4">
                  <span className="text-lg font-medium text-gray-500">
                    {index + 1}.
                  </span>
                  <div>
                    <div className="font-medium text-gray-900 dark:text-white">
                      {problem.title}
                    </div>
                    <div className="flex items-center space-x-2 mt-1">
                      <span className={`px-2 py-0.5 rounded text-xs font-medium ${getDifficultyColor(problem.difficulty)}`}>
                        {problem.difficulty}
                      </span>
                      {problem.categories?.map((cat) => (
                        <span key={cat} className="text-xs text-gray-500">
                          {cat}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>
                <span className="text-gray-400">→</span>
              </Link>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

