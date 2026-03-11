import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

export default function Problems() {
  const [problems, setProblems] = useState([])
  const [loading, setLoading] = useState(true)
  const [difficulty, setDifficulty] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [page, setPage] = useState(0)
  const [totalProblems, setTotalProblems] = useState(0)

  useEffect(() => {
    fetchProblems()
  }, [difficulty, page])

  const fetchProblems = async () => {
    try {
      const token = localStorage.getItem('token')
      const params = new URLSearchParams({
        page,
        size: 20,
        ...(difficulty && { difficulty })
      })
      
      const response = await fetch(`/api/problems?${params}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      const data = await response.json()
      setProblems(data.content || [])
      setTotalProblems(data.totalElements || 0)
    } catch (error) {
      console.error('Error fetching problems:', error)
      // Demo data
      setProblems([
        { id: '1', title: 'Two Sum', difficulty: 'EASY', categories: ['Array', 'Hash Table'], totalSubmissions: 1200, totalAccepted: 800, acceptanceRate: 66.7 },
        { id: '2', title: 'Add Two Numbers', difficulty: 'MEDIUM', categories: ['Linked List', 'Math'], totalSubmissions: 900, totalAccepted: 450, acceptanceRate: 50 },
        { id: '3', title: 'Longest Substring Without Repeating Characters', difficulty: 'MEDIUM', categories: ['String', 'Sliding Window'], totalSubmissions: 800, totalAccepted: 320, acceptanceRate: 40 },
        { id: '4', title: 'Median of Two Sorted Arrays', difficulty: 'HARD', categories: ['Array', 'Binary Search'], totalSubmissions: 600, totalAccepted: 180, acceptanceRate: 30 },
        { id: '5', title: 'Reverse Linked List', difficulty: 'EASY', categories: ['Linked List'], totalSubmissions: 1100, totalAccepted: 880, acceptanceRate: 80 },
        { id: '6', title: 'Climbing Stairs', difficulty: 'EASY', categories: ['Dynamic Programming'], totalSubmissions: 950, totalAccepted: 760, acceptanceRate: 80 },
        { id: '7', title: 'Binary Tree Inorder Traversal', difficulty: 'EASY', categories: ['Tree', 'Stack'], totalSubmissions: 700, totalAccepted: 560, acceptanceRate: 80 },
        { id: '8', title: 'Validate Binary Search Tree', difficulty: 'MEDIUM', categories: ['Tree', 'DFS'], totalSubmissions: 650, totalAccepted: 260, acceptanceRate: 40 }
      ])
      setTotalProblems(50)
    } finally {
      setLoading(false)
    }
  }

  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'EASY': return 'text-green-600 bg-green-100 dark:bg-green-900 dark:text-green-300'
      case 'MEDIUM': return 'text-yellow-600 bg-yellow-100 dark:bg-yellow-900 dark:text-yellow-300'
      case 'HARD': return 'text-red-600 bg-red-100 dark:bg-red-900 dark:text-red-300'
      default: return 'text-gray-600 bg-gray-100'
    }
  }

  const filteredProblems = problems.filter(p => 
    p.title?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const totalPages = Math.ceil(totalProblems / 20)

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
            💻 Coding Problems
          </h1>
          <p className="text-gray-600 dark:text-gray-400">
            Practice {totalProblems}+ problems across all difficulty levels
          </p>
        </div>

        {/* Filters */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-4 mb-6">
          <div className="flex flex-col md:flex-row gap-4">
            {/* Search */}
            <div className="flex-1">
              <input
                type="text"
                placeholder="Search problems..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500"
              />
            </div>
            
            {/* Difficulty Filter */}
            <select
              value={difficulty}
              onChange={(e) => setDifficulty(e.target.value)}
              className="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Difficulties</option>
              <option value="EASY">Easy</option>
              <option value="MEDIUM">Medium</option>
              <option value="HARD">Hard</option>
            </select>
          </div>
        </div>

        {/* Problems Table */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden">
          {loading ? (
            <div className="p-12 text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
              <p className="mt-4 text-gray-500">Loading problems...</p>
            </div>
          ) : (
            <>
              {/* Table Header */}
              <div className="bg-gray-50 dark:bg-gray-700 p-4 grid grid-cols-12 gap-4 text-sm font-medium text-gray-500 dark:text-gray-400">
                <div className="col-span-1">#</div>
                <div className="col-span-5">Title</div>
                <div className="col-span-2 text-center">Difficulty</div>
                <div className="col-span-2 text-center">Acceptance</div>
                <div className="col-span-2 text-center">Submissions</div>
              </div>

              {/* Table Body */}
              <div className="divide-y divide-gray-200 dark:divide-gray-700">
                {filteredProblems.map((problem, index) => (
                  <Link
                    key={problem.id}
                    to={`/problems/${problem.id}`}
                    className="p-4 grid grid-cols-12 gap-4 items-center hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                  >
                    <div className="col-span-1 text-gray-500 dark:text-gray-400">
                      {page * 20 + index + 1}
                    </div>
                    <div className="col-span-5">
                      <span className="font-medium text-gray-900 dark:text-white hover:text-blue-500">
                        {problem.title}
                      </span>
                      <div className="flex gap-2 mt-1">
                        {problem.categories?.slice(0, 2).map((cat) => (
                          <span key={cat} className="text-xs text-gray-500">
                            {cat}
                          </span>
                        ))}
                      </div>
                    </div>
                    <div className="col-span-2 text-center">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${getDifficultyColor(problem.difficulty)}`}>
                        {problem.difficulty}
                      </span>
                    </div>
                    <div className="col-span-2 text-center">
                      <span className="text-gray-700 dark:text-gray-300">
                        {problem.acceptanceRate?.toFixed(1) || 0}%
                      </span>
                    </div>
                    <div className="col-span-2 text-center">
                      <span className="text-gray-700 dark:text-gray-300">
                        {problem.totalSubmissions || 0}
                      </span>
                    </div>
                  </Link>
                ))}
              </div>

              {filteredProblems.length === 0 && (
                <div className="p-12 text-center">
                  <p className="text-gray-500">No problems found</p>
                </div>
              )}
            </>
          )}
        </div>

        {/* Pagination */}
        {!loading && totalPages > 1 && (
          <div className="flex justify-center mt-6 space-x-2">
            <button
              onClick={() => setPage(Math.max(0, page - 1))}
              disabled={page === 0}
              className="px-4 py-2 bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 rounded-lg shadow-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 dark:hover:bg-gray-700"
            >
              Previous
            </button>
            <span className="px-4 py-2 text-gray-700 dark:text-gray-300">
              Page {page + 1} of {totalPages}
            </span>
            <button
              onClick={() => setPage(page + 1)}
              disabled={page >= totalPages - 1}
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

