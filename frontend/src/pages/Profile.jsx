import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { useAuth } from '../contexts/AuthContext.jsx'

export default function Profile() {
  const { user } = useAuth()
  const [profile, setProfile] = useState(null)
  const [submissions, setSubmissions] = useState([])
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState('overview')
  const [editing, setEditing] = useState(false)
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    bio: ''
  })

  useEffect(() => {
    fetchProfile()
    fetchSubmissions()
  }, [])

  const fetchProfile = async () => {
    try {
      const token = localStorage.getItem('token')
      const response = await axios.get('/api/users/profile', {
        headers: { Authorization: `Bearer ${token}` }
      })
      setProfile(response.data)
      setFormData({
        firstName: response.data.firstName || '',
        lastName: response.data.lastName || '',
        bio: ''
      })
    } catch (error) {
      console.error('Error fetching profile:', error)
      // Demo data
      setProfile({
        id: '1',
        username: 'developer',
        email: 'dev@example.com',
        firstName: 'John',
        lastName: 'Doe',
        role: 'STUDENT',
        problemsSolved: 45,
        rank: 156,
        totalPoints: 850,
        profilePictureUrl: null
      })
    } finally {
      setLoading(false)
    }
  }

  const fetchSubmissions = async () => {
    try {
      const token = localStorage.getItem('token')
      const response = await axios.get('/api/submissions/user/submissions?page=0&size=10', {
        headers: { Authorization: `Bearer ${token}` }
      })
      setSubmissions(response.data)
    } catch (error) {
      console.error('Error fetching submissions:', error)
      // Demo data
      setSubmissions([
        { id: '1', problemId: '1', problemTitle: 'Two Sum', status: 'ACCEPTED', language: 'python', submittedAt: new Date().toISOString() },
        { id: '2', problemId: '2', problemTitle: 'Reverse Linked List', status: 'ACCEPTED', language: 'java', submittedAt: new Date().toISOString() }
      ])
    }
  }

  const handleUpdateProfile = async (e) => {
    e.preventDefault()
    try {
      const token = localStorage.getItem('token')
      await axios.put('/api/users/profile', formData, {
        headers: { Authorization: `Bearer ${token}` }
      })
      setProfile({ ...profile, ...formData })
      setEditing(false)
    } catch (error) {
      console.error('Error updating profile:', error)
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACCEPTED': return 'text-green-600 bg-green-100'
      case 'WRONG_ANSWER': return 'text-red-600 bg-red-100'
      case 'TIME_LIMIT_EXCEEDED': return 'text-orange-600 bg-orange-100'
      case 'RUNTIME_ERROR': return 'text-purple-600 bg-purple-100'
      default: return 'text-gray-600 bg-gray-100'
    }
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
        {/* Profile Header */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-8 mb-8">
          <div className="flex flex-col md:flex-row items-center md:items-start gap-8">
            {/* Avatar */}
            <div className="relative">
              <div className="w-32 h-32 rounded-full bg-gradient-to-r from-blue-500 to-purple-600 flex items-center justify-center text-5xl font-bold text-white">
                {profile?.username?.charAt(0).toUpperCase()}
              </div>
              <div className="absolute bottom-0 right-0 w-10 h-10 bg-green-500 rounded-full border-4 border-white dark:border-gray-800 flex items-center justify-center">
                ✓
              </div>
            </div>

            {/* Info */}
            <div className="flex-1 text-center md:text-left">
              {editing ? (
                <form onSubmit={handleUpdateProfile} className="space-y-4">
                  <div className="flex flex-col md:flex-row gap-4">
                    <input
                      type="text"
                      value={formData.firstName}
                      onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                      placeholder="First Name"
                      className="flex-1 px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                    />
                    <input
                      type="text"
                      value={formData.lastName}
                      onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                      placeholder="Last Name"
                      className="flex-1 px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                    />
                  </div>
                  <div className="flex gap-2">
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                    >
                      Save
                    </button>
                    <button
                      type="button"
                      onClick={() => setEditing(false)}
                      className="px-4 py-2 bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-300 dark:hover:bg-gray-600"
                    >
                      Cancel
                    </button>
                  </div>
                </form>
              ) : (
                <>
                  <div className="flex items-center justify-center md:justify-start gap-4 mb-2">
                    <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
                      {profile?.firstName} {profile?.lastName}
                    </h1>
                    <button
                      onClick={() => setEditing(true)}
                      className="text-blue-500 hover:text-blue-600"
                    >
                      ✏️
                    </button>
                  </div>
                  <p className="text-gray-500 dark:text-gray-400 mb-4">@{profile?.username}</p>
                </>
              )}

              <div className="flex flex-wrap justify-center md:justify-start gap-4">
                <span className="px-3 py-1 bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300 rounded-full text-sm font-medium">
                  {profile?.role}
                </span>
                <span className="px-3 py-1 bg-purple-100 dark:bg-purple-900 text-purple-700 dark:text-purple-300 rounded-full text-sm font-medium">
                  {profile?.totalPoints} points
                </span>
              </div>
            </div>

            {/* Stats */}
            <div className="grid grid-cols-3 gap-6 text-center">
              <div>
                <div className="text-3xl font-bold text-gray-900 dark:text-white">
                  {profile?.problemsSolved || 0}
                </div>
                <div className="text-sm text-gray-500 dark:text-gray-400">Solved</div>
              </div>
              <div>
                <div className="text-3xl font-bold text-gray-900 dark:text-white">
                  #{profile?.rank || '-'}
                </div>
                <div className="text-sm text-gray-500 dark:text-gray-400">Rank</div>
              </div>
              <div>
                <div className="text-3xl font-bold text-gray-900 dark:text-white">
                  {profile?.totalPoints || 0}
                </div>
                <div className="text-sm text-gray-500 dark:text-gray-400">Points</div>
              </div>
            </div>
          </div>
        </div>

        {/* Tabs */}
        <div className="flex gap-4 mb-6 border-b border-gray-200 dark:border-gray-700">
          {['overview', 'submissions', 'badges'].map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-4 py-2 font-medium capitalize transition-colors ${
                activeTab === tab
                  ? 'text-blue-500 border-b-2 border-blue-500'
                  : 'text-gray-500 hover:text-gray-700 dark:hover:text-gray-300'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Tab Content */}
        {activeTab === 'overview' && (
          <div className="grid md:grid-cols-2 gap-6">
            {/* Activity */}
            <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-6">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
                Recent Activity
              </h3>
              <div className="space-y-4">
                {submissions.slice(0, 5).map((sub) => (
                  <div key={sub.id} className="flex items-center justify-between py-2 border-b border-gray-100 dark:border-gray-700 last:border-0">
                    <div>
                      <div className="font-medium text-gray-900 dark:text-white">
                        {sub.problemTitle || 'Problem'}
                      </div>
                      <div className="text-sm text-gray-500">
                        {new Date(sub.submittedAt).toLocaleDateString()}
                      </div>
                    </div>
                    <span className={`px-2 py-1 rounded text-xs font-medium ${getStatusColor(sub.status)}`}>
                      {sub.status}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Badges Preview */}
            <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-6">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
                Achievements
              </h3>
              <div className="grid grid-cols-4 gap-4">
                {profile?.problemsSolved >= 10 && (
                  <div className="text-center">
                    <div className="text-4xl mb-1">⭐</div>
                    <div className="text-xs text-gray-500">First 10</div>
                  </div>
                )}
                {profile?.problemsSolved >= 50 && (
                  <div className="text-center">
                    <div className="text-4xl mb-1">🌟</div>
                    <div className="text-xs text-gray-500">50 Solved</div>
                  </div>
                )}
                {profile?.problemsSolved >= 100 && (
                  <div className="text-center">
                    <div className="text-4xl mb-1">🏆</div>
                    <div className="text-xs text-gray-500">Century</div>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'submissions' && (
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden">
            <table className="w-full">
              <thead className="bg-gray-50 dark:bg-gray-700">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Problem
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Language
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Time
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
                {submissions.map((sub) => (
                  <tr key={sub.id} className="hover:bg-gray-50 dark:hover:bg-gray-700">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900 dark:text-white">
                        {sub.problemTitle || 'Problem'}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(sub.status)}`}>
                        {sub.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                      {sub.language}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                      {new Date(sub.submittedAt).toLocaleDateString()}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {activeTab === 'badges' && (
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-8 text-center">
            <div className="text-6xl mb-4">🏅</div>
            <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
              Keep Solving!
            </h3>
            <p className="text-gray-500 dark:text-gray-400">
              Solve more problems to unlock badges and achievements.
            </p>
          </div>
        )}
      </div>
    </div>
  )
}

