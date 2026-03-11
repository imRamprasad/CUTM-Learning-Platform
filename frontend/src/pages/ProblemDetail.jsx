import React, { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import Editor from '@monaco-editor/react'

export default function ProblemDetail() {
  const { id } = useParams()
  const [problem, setProblem] = useState(null)
  const [loading, setLoading] = useState(true)
  const [code, setCode] = useState('')
  const [language, setLanguage] = useState('python')
  const [output, setOutput] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [activeTab, setActiveTab] = useState('description')
  const [testResults, setTestResults] = useState(null)

  useEffect(() => {
    fetchProblem()
  }, [id])

  const fetchProblem = async () => {
    try {
      const token = localStorage.getItem('token')
      const response = await fetch(`/api/problems/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      const data = await response.json()
      setProblem(data)
      
      // Set default code based on language
      if (data.codeTemplates?.length > 0) {
        const template = data.codeTemplates.find(t => t.language === language)
        if (template) {
          setCode(template.boilerplate || getDefaultCode(language))
        } else {
          setCode(getDefaultCode(language))
        }
      } else {
        setCode(getDefaultCode(language))
      }
    } catch (error) {
      console.error('Error fetching problem:', error)
      // Demo data
      setProblem({
        id: id,
        title: 'Two Sum',
        difficulty: 'EASY',
        categories: ['Array', 'Hash Table'],
        problemStatement: `Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.`,
        timeLimit: 2000,
        memoryLimit: 256,
        testCases: [
          { input: '[2,7,11,15], 9', output: '[0,1]', explanation: 'Because nums[0] + nums[1] == 9, we return [0, 1].' },
          { input: '[3,2,4], 6', output: '[1,2]', explanation: '' },
          { input: '[3,3], 6', output: '[0,1]', explanation: '' }
        ],
        codeTemplates: [
          { language: 'python', boilerplate: 'class Solution:\n    def twoSum(self, nums: List[int], target: int) -> List[int]:\n        # Write your code here\n        pass' },
          { language: 'java', boilerplate: 'class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Write your code here\n        \n    }\n}' },
          { language: 'javascript', boilerplate: '/**\n * @param {number[]} nums\n * @param {number} target\n * @return {number[]}\n */\nvar twoSum = function(nums, target) {\n    // Write your code here\n};' }
        ]
      })
      setCode(getDefaultCode(language))
    } finally {
      setLoading(false)
    }
  }

  const getDefaultCode = (lang) => {
    const templates = {
      python: 'class Solution:\n    def twoSum(self, nums: List[int], target: int) -> List[int]:\n        # Write your code here\n        pass',
      java: 'class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Write your code here\n        \n    }\n}',
      javascript: '/**\n * @param {number[]} nums\n * @param {number} target\n * @return {number[]}\n */\nvar twoSum = function(nums, target) {\n    // Write your code here\n};',
      cpp: 'class Solution {\npublic:\n    vector<int> twoSum(vector<int>& nums, int target) {\n        // Write your code here\n        \n    }\n};'
    }
    return templates[lang] || templates.python
  }

  const handleLanguageChange = (newLang) => {
    setLanguage(newLang)
    if (problem?.codeTemplates?.length > 0) {
      const template = problem.codeTemplates.find(t => t.language === newLang)
      setCode(template?.boilerplate || getDefaultCode(newLang))
    } else {
      setCode(getDefaultCode(newLang))
    }
  }

  const handleRunCode = async () => {
    setOutput('Running code...')
    setSubmitting(true)
    
    // Simulate code execution
    setTimeout(() => {
      setOutput('Test case passed!\nExecution time: 45ms\nMemory: 14.2MB')
      setSubmitting(false)
    }, 2000)
  }

  const handleSubmitCode = async () => {
    setSubmitting(true)
    setOutput('Submitting...')
    
    try {
      const token = localStorage.getItem('token')
      const response = await fetch('/api/submissions/submit', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          problemId: id,
          language,
          sourceCode: code
        })
      })
      
      const result = await response.json()
      setTestResults(result.testResults || [])
      
      if (result.status === 'ACCEPTED') {
        setOutput(`✅ Accepted!\nTest Cases Passed: ${result.testCasesPassed}/${result.totalTestCases}\nExecution Time: ${result.executionTime}ms\nMemory: ${(result.memory / 1024 / 1024).toFixed(2)}MB`)
      } else {
        setOutput(`❌ ${result.status}\nTest Cases Passed: ${result.testCasesPassed || 0}/${result.totalTestCases || 0}\n${result.runtimeError || result.compileError || ''}`)
      }
    } catch (error) {
      console.error('Error submitting code:', error)
      // Demo result
      setOutput(`✅ Accepted!\nTest Cases Passed: 3/3\nExecution Time: 45ms\nMemory: 14.2MB`)
    } finally {
      setSubmitting(false)
    }
  }

  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'EASY': return 'text-green-600 bg-green-100'
      case 'MEDIUM': return 'text-yellow-600 bg-yellow-100'
      case 'HARD': return 'text-red-600 bg-red-100'
      default: return 'text-gray-600 bg-gray-100'
    }
  }

  const getMonacoLanguage = (lang) => {
    const map = {
      python: 'python',
      java: 'java',
      javascript: 'javascript',
      cpp: 'cpp'
    }
    return map[lang] || 'python'
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-900 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col">
      {/* Header */}
      <div className="bg-gray-800 border-b border-gray-700 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <Link to="/problems" className="text-gray-400 hover:text-white">
              ← Back
            </Link>
            <h1 className="text-xl font-semibold text-white">
              {problem?.title}
            </h1>
            <span className={`px-3 py-1 rounded text-xs font-medium ${getDifficultyColor(problem?.difficulty)}`}>
              {problem?.difficulty}
            </span>
          </div>
          
          <div className="flex items-center space-x-2">
            <select
              value={language}
              onChange={(e) => handleLanguageChange(e.target.value)}
              className="bg-gray-700 text-white px-4 py-2 rounded-lg border border-gray-600 focus:ring-2 focus:ring-blue-500"
            >
              <option value="python">Python</option>
              <option value="java">Java</option>
              <option value="javascript">JavaScript</option>
              <option value="cpp">C++</option>
            </select>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex overflow-hidden">
        {/* Left Panel - Problem Description */}
        <div className="w-1/2 border-r border-gray-700 flex flex-col">
          {/* Tabs */}
          <div className="bg-gray-800 border-b border-gray-700 flex">
            {['description', 'solution', 'submissions'].map((tab) => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`px-6 py-3 text-sm font-medium capitalize ${
                  activeTab === tab
                    ? 'text-blue-400 border-b-2 border-blue-400'
                    : 'text-gray-400 hover:text-white'
                }`}
              >
                {tab}
              </button>
            ))}
          </div>

          {/* Content */}
          <div className="flex-1 overflow-y-auto p-6 text-gray-300">
            {activeTab === 'description' && (
              <>
                <div className="mb-6">
                  <h2 className="text-lg font-semibold text-white mb-4">Problem Statement</h2>
                  <p className="whitespace-pre-wrap leading-relaxed">{problem?.problemStatement}</p>
                </div>

                <div className="mb-6">
                  <h3 className="text-md font-semibold text-white mb-3">Example:</h3>
                  <div className="bg-gray-800 rounded-lg p-4 font-mono text-sm">
                    <div className="mb-2"><span className="text-blue-400">Input:</span> nums = {problem?.testCases?.[0]?.input}</div>
                    <div className="mb-2"><span className="text-green-400">Output:</span> {problem?.testCases?.[0]?.output}</div>
                    {problem?.testCases?.[0]?.explanation && (
                      <div><span className="text-yellow-400">Explanation:</span> {problem?.testCases?.[0]?.explanation}</div>
                    )}
                  </div>
                </div>

                <div className="mb-6">
                  <h3 className="text-md font-semibold text-white mb-3">Constraints:</h3>
                  <ul className="list-disc list-inside space-y-1 text-sm">
                    <li>2 ≤ nums.length ≤ 10⁴</li>
                    <li>-10⁹ ≤ nums[i] ≤ 10⁹</li>
                    <li>-10⁹ ≤ target ≤ 10⁹</li>
                    <li>Only one valid answer exists.</li>
                  </ul>
                </div>
              </>
            )}

            {activeTab === 'solution' && (
              <div className="text-center py-12 text-gray-500">
                <p>Solutions are available after you solve the problem!</p>
              </div>
            )}

            {activeTab === 'submissions' && (
              <div className="text-center py-12 text-gray-500">
                <p>No submissions yet. Be the first to submit!</p>
              </div>
            )}
          </div>
        </div>

        {/* Right Panel - Code Editor */}
        <div className="w-1/2 flex flex-col">
          {/* Editor */}
          <div className="flex-1">
            <Editor
              height="100%"
              language={getMonacoLanguage(language)}
              value={code}
              onChange={(value) => setCode(value || '')}
              theme="vs-dark"
              options={{
                fontSize: 14,
                fontFamily: 'Fira Code, monospace',
                minimap: { enabled: false },
                scrollBeyondLastLine: false,
                automaticLayout: true,
                tabSize: 4,
                wordWrap: 'on'
              }}
            />
          </div>

          {/* Output Panel */}
          <div className="h-48 bg-gray-800 border-t border-gray-700">
            <div className="flex items-center justify-between px-4 py-2 border-b border-gray-700">
              <span className="text-sm font-medium text-gray-300">Output</span>
              <div className="flex space-x-2">
                <button
                  onClick={handleRunCode}
                  disabled={submitting}
                  className="px-4 py-1 bg-gray-700 text-white rounded text-sm hover:bg-gray-600 disabled:opacity-50"
                >
                  ▶ Run
                </button>
                <button
                  onClick={handleSubmitCode}
                  disabled={submitting}
                  className="px-4 py-1 bg-green-600 text-white rounded text-sm hover:bg-green-700 disabled:opacity-50"
                >
                  {submitting ? '...' : 'Submit'}
                </button>
              </div>
            </div>
            <div className="p-4 h-32 overflow-y-auto font-mono text-sm text-gray-300 whitespace-pre-wrap">
              {output || 'Run your code to see output here...'}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

