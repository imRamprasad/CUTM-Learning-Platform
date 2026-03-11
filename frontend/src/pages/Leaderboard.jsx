import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { useAuth } from '../contexts/AuthContext.jsx'

export default function Leaderboard() {
  const [leaderboard, setLeaderboard] = useState([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const { user } = useAuth()

  useEffect(() => {
    fetchLeaderboard()
  }, [page])

  const fetchLeaderboard = async () => {
    try {
      const token = localStorage.getItem('token')
      const response = await axios.get(`/api/leaderboard?page=${page}&size=20`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      setLeaderboard(response.data)
    } catch (error) {
      console.error('Error fetching leaderboard:', error)
    } finally {
      setLoading(false)
    }
  }

  const getRankBadge = (rank) => {
    if (rank === 1) return '🥇'
    if (rank === 2) return '🥈'
    if (rank === 3) return '🥉'
    return rank
  }

  const getRankColor = (rank) => {
    if (rank === 1) return 'from-yellow-400 to-yellow-600'
    if (rank === 2) return 'from-gray-300 to-gray-500'
    if (rank === 3) return 'from-orange-400 to-orange-600'
    return 'from-gray-600 to-gray-700'
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-4">
            🏆 Leaderboard
          </h1>
          <p className="text-gray-600 dark:text-gray-400">
            Top developers ranking based on solved problems
          </p>
        </div>

        {/* Scoring Info */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-6 mb-8">
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
            Points System
          </h3>
          <div className="flex flex-wrap justify-center gap-6">
            <div className="flex items-center space-x-2">
              <span className="px-3 py-1 bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300 rounded-full text-sm font-medium">
                Easy: 10 pts
              </span>
            </div>
            <div className="flex items-center space-x-2">
              <span className="px-3 py-1 bg-yellow-100 text-yellow-700 dark:bg-yellow-900 dark:text-yellow-300 rounded-full text-sm font-medium">
                Medium: 20 pts
              </span>
            </div>
            <div className="flex items-center space-x-2">
              <span className="px-3 py-1 bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-300 rounded-full text-sm font-medium">
                Hard: 40 pts
              </span>
            </div>
          </div>
        </div>

        {/* Leaderboard Table */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden">
          {loading ? (
            <div className="p-12 text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
              <p className="mt-4 text-gray-500">Loading leaderboard...</p>
            </div>
          ) : (
            <>
              {/* Table Header */}
              <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white p-4 grid grid-cols-12 gap-4 font-semibold">
                <div className="col-span-1 text-center">Rank</div>
                <div className="col-span-5">User</div>
                <div className="col-span-2 text-center">Solved</div>
                <div className="col-span-2 text-center">Points</div>
                <div className="col-span-2 text-center">Badges</div>
              </div>

              {/* Table Body */}
              <div className="divide-y divide-gray-200 dark:divide-gray-700">
                {leaderboard.map((entry, index) => (
                  <div
                    key={entry.userId}
                    className={`p-4 grid grid-cols-12 gap-4 items-center hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors ${
                      user?.id === entry.userId ? 'bg-blue-50 dark:bg-blue-900/20' : ''
                    }`}
                  >
                    <div className="col-span-1 text-center">
                      <span className={`inline-flex items-center justify-center w-8 h-8 rounded-full bg-gradient-to-r ${getRankColor(entry.rank)} text-white font-bold text-sm`}>
                        {getRankBadge(entry.rank)}
                      </span>
                    </div>
                    <div className="col-span-5 flex items-center space-x-3">
                      <div className="w-10 h-10 rounded-full bg-gradient-to-r from-blue-400 to-purple-500 flex items-center justify-center text-white font-bold">
                        {entry.username?.charAt(0).toUpperCase()}
                      </div>
                      <div>
                        <div className="font-medium text-gray-900 dark:text-white">
                          {entry.username}
                        </div>
                        <div className="text-sm text-gray-500 dark:text-gray-400">
                          {entry.firstName} {entry.lastName}
                        </div>
                      </div>
                    </div>
                    <div className="col-span-2 text-center">
                      <span className="text-lg font-semibold text-gray-900 dark:text-white">
                        {entry.problemsSolved || 0}
                      </span>
                    </div>
                    <div className="col-span-2 text-center">
                      <span className="inline-block px-4 py-1 bg-gradient-to-r from-blue-500 to-purple-500 text-white rounded-full font-semibold">
                        {entry.totalPoints || 0} pts
                      </span>
                    </div>
                    <div className="col-span-2 text-center flex justify-center space-x-1">
                      {entry.problemsSolved >= 10 && <span>⭐</span>}
                      {entry.problemsSolved >= 50 && <span>🌟</span>}
                      {entry.problemsSolved >= 100 && <span>🏆</span>}
                    </div>
                  </div>
                ))}
              </div>

              {leaderboard.length === 0 && (
                <div className="p-12 text-center">
                  <p className="text-gray-500">No users found</p>
                </div>
              )}
            </>
          )}
        </div>

        {/* Pagination */}
        {leaderboard.length > 0 && (
          <div className="flex justify-center mt-6 space-x-2">
            <button
              onClick={() => setPage(Math.max(0, page - 1))}
              disabled={page === 0}
              className="px-4 py-2 bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 rounded-lg shadow-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 dark:hover:bg-gray-700"
            >
              Previous
            </button>
            <span className="px-4 py-2 text-gray-700 dark:text-gray-300">
              Page {page + 1}
            </span>
            <button
              onClick={() => setPage(page + 1)}
              disabled={leaderboard.length < 20}
              className="px-4 py-2 bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 rounded-lg shadow-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 dark:hover:bg-gray-700"
            >
              Next
            </button>
          </div>
        )}
      </div>
    </div>
  )
}

