// frontend/src/pages/ProblemDetail.js
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useFetch, useApi } from '../hooks';
import { useAuth } from '../contexts/AuthContext';
import CodeEditor from '../components/CodeEditor';
import '../styles/ProblemDetail.css';

export default function ProblemDetail() {
    const { id } = useParams();
    const { user } = useAuth();
    const { post } = useApi();
    const { data: problem, loading: problemLoading } = useFetch(`/api/problems/${id}`);
    
    const [selectedLanguage, setSelectedLanguage] = useState('python');
    const [code, setCode] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [result, setResult] = useState(null);
    const [showResultModal, setShowResultModal] = useState(false);

    const handleSubmit = async () => {
        if (!code.trim()) {
            alert('Please write some code');
            return;
        }

        setSubmitting(true);
        try {
            const response = await post('/submissions/submit', {
                problemId: id,
                sourceCode: code,
                language: selectedLanguage,
                userId: user?.id
            });
            setResult(response);
            setShowResultModal(true);
        } catch (error) {
            alert(`Submission failed: ${error.message}`);
        } finally {
            setSubmitting(false);
        }
    };

    if (problemLoading) return <div className="loading">Loading problem...</div>;

    return (
        <div className="problem-detail-container">
            <div className="problem-split">
                {/* Left Panel - Problem Statement */}
                <div className="problem-statement">
                    <div className="problem-header">
                        <h1>{problem?.title}</h1>
                        <div className="problem-meta">
                            <span className={`difficulty difficulty-${problem?.difficulty}`}>
                                {problem?.difficulty}
                            </span>
                            <span className="acceptance">
                                {problem?.acceptanceRate?.toFixed(1)}% Acceptance
                            </span>
                        </div>
                    </div>

                    <div className="problem-description">
                        <h3>Description</h3>
                        <p>{problem?.description}</p>
                    </div>

                    {problem?.examples && (
                        <div className="problem-examples">
                            <h3>Examples</h3>
                            {problem.examples.map((example, idx) => (
                                <div key={idx} className="example">
                                    <div className="example-io">
                                        <strong>Input:</strong>
                                        <code>{example.input}</code>
                                    </div>
                                    <div className="example-io">
                                        <strong>Output:</strong>
                                        <code>{example.output}</code>
                                    </div>
                                    {example.explanation && (
                                        <div className="example-explanation">
                                            <strong>Explanation:</strong>
                                            <p>{example.explanation}</p>
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}

                    {problem?.constraints && (
                        <div className="problem-constraints">
                            <h3>Constraints</h3>
                            <ul>
                                {problem.constraints.map((constraint, idx) => (
                                    <li key={idx}>{constraint}</li>
                                ))}
                            </ul>
                        </div>
                    )}

                    <div className="problem-stats">
                        <div className="stat-item">
                            <span className="stat-label">Accepted:</span>
                            <span className="stat-value">{problem?.totalAccepted}</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-label">Submissions:</span>
                            <span className="stat-value">{problem?.totalSubmissions}</span>
                        </div>
                    </div>
                </div>

                {/* Right Panel - Code Editor */}
                <div className="editor-panel">
                    <div className="editor-toolbar">
                        <select 
                            value={selectedLanguage}
                            onChange={(e) => setSelectedLanguage(e.target.value)}
                            className="language-select"
                        >
                            <option value="python">Python 3</option>
                            <option value="javascript">JavaScript</option>
                            <option value="java">Java</option>
                            <option value="cpp">C++</option>
                        </select>

                        <button 
                            onClick={handleSubmit}
                            disabled={submitting}
                            className="submit-button"
                        >
                            {submitting ? 'Submitting...' : 'Submit'}
                        </button>
                    </div>

                    <CodeEditor 
                        language={selectedLanguage}
                        value={code}
                        onChange={setCode}
                        placeholder={getBoilerplate(selectedLanguage, problem?.codeTemplates)}
                    />
                </div>
            </div>

            {/* Result Modal */}
            {showResultModal && result && (
                <ResultModal 
                    result={result}
                    onClose={() => setShowResultModal(false)}
                />
            )}
        </div>
    );
}

function ResultModal({ result, onClose }) {
    const statusColor = {
        'ACCEPTED': '#4CAF50',
        'WRONG_ANSWER': '#FF6B6B',
        'TIME_LIMIT_EXCEEDED': '#FF9800',
        'RUNTIME_ERROR': '#F44336',
        'COMPILATION_ERROR': '#2196F3'
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Submission Result</h2>
                    <button className="modal-close" onClick={onClose}>✕</button>
                </div>

                <div className="result-status" style={{ borderColor: statusColor[result.status] }}>
                    <h3 style={{ color: statusColor[result.status] }}>{result.status}</h3>
                    <p>Runtime: {result.executionTime}ms | Memory: {result.memoryUsed}MB</p>
                </div>

                <div className="test-results">
                    <h4>Test Cases ({result.testResults?.length || 0})</h4>
                    {result.testResults?.map((testResult, idx) => (
                        <div 
                            key={idx}
                            className={`test-case ${testResult.passed ? 'passed' : 'failed'}`}
                        >
                            <span className="test-case-num">Case {idx + 1}</span>
                            <span className="test-case-status">
                                {testResult.passed ? '✓ Passed' : '✗ Failed'}
                            </span>
                            {!testResult.passed && (
                                <>
                                    <div className="test-case-expected">
                                        <strong>Expected:</strong> {testResult.expectedOutput}
                                    </div>
                                    <div className="test-case-actual">
                                        <strong>Got:</strong> {testResult.actualOutput}
                                    </div>
                                </>
                            )}
                        </div>
                    ))}
                </div>

                <div className="submission-info">
                    <p><strong>Submitted at:</strong> {new Date(result.submittedAt).toLocaleString()}</p>
                    <p><strong>Language:</strong> {result.language}</p>
                </div>

                <button className="modal-action-button" onClick={onClose}>
                    Try Again
                </button>
            </div>
        </div>
    );
}

function getBoilerplate(language, templates) {
    const boilerplates = {
        python: `def solution(args):
    # Write your solution here
    pass
`,
        javascript: `function solution(args) {
    // Write your solution here
}`,
        java: `class Solution {
    public void solve(String[] args) {
        // Write your solution here
    }
}`,
        cpp: `#include <bits/stdc++.h>
using namespace std;

int main() {
    // Write your solution here
    return 0;
}`
    };

    return templates?.[language] || boilerplates[language];
}
