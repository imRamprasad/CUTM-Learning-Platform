// frontend/src/pages/Problems.js
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useFetch, usePagination } from '../hooks';
import '../styles/Problems.css';

export default function Problems() {
    const [difficulty, setDifficulty] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const pagination = usePagination(0, 20);
    const {
        page,
        pageSize,
        setTotal,
        prevPage,
        nextPage,
        hasPrevPage,
        hasNextPage,
        totalPages,
    } = pagination;

    const apiUrl = `/api/problems?page=${page}&size=${pageSize}${
        difficulty ? `&difficulty=${difficulty}` : ''
    }${searchTerm ? `&search=${searchTerm}` : ''}`;

    const { data: response, loading } = useFetch(apiUrl);

    React.useEffect(() => {
        if (response) {
            setTotal(response.totalElements);
        }
    }, [response, setTotal]);

    return (
        <div className="problems-container">
            <div className="problems-header">
                <h1>Coding Problems</h1>
                <p>Challenge yourself with {response?.totalElements || 0}+ problems</p>
            </div>

            <div className="problems-filters">
                <input
                    type="search"
                    placeholder="Search problems..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-input"
                />

                <select
                    value={difficulty}
                    onChange={(e) => setDifficulty(e.target.value)}
                    className="difficulty-select"
                >
                    <option value="">All Difficulties</option>
                    <option value="EASY">Easy</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HARD">Hard</option>
                </select>
            </div>

            {loading ? (
                <div className="loading">Loading problems...</div>
            ) : (
                <>
                    <div className="problems-grid">
                        {response?.content?.map((problem) => (
                            <ProblemCard key={problem.id} problem={problem} />
                        ))}
                    </div>

                    <div className="pagination">
                        <button 
                            onClick={prevPage}
                            disabled={!hasPrevPage}
                            className="pagination-button"
                        >
                            ← Previous
                        </button>
                        <span className="pagination-info">
                            Page {page + 1} of {totalPages}
                        </span>
                        <button 
                            onClick={nextPage}
                            disabled={!hasNextPage}
                            className="pagination-button"
                        >
                            Next →
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

function ProblemCard({ problem }) {
    return (
        <Link to={`/problems/${problem.id}`} className="problem-card">
            <div className="problem-header">
                <h3>{problem.title}</h3>
                <span className={`difficulty-badge difficulty-${problem.difficulty}`}>
                    {problem.difficulty}
                </span>
            </div>

             <div className="problem-categories">
                {problem.categories?.slice(0, 2).map((cat) => (
                    <span key={cat} className="category-tag">{cat}</span>
                ))}
            </div>

            <div className="problem-stats">
                <span className="stat">
                    ✅ Accepted: {problem.totalAccepted} ({problem.acceptanceRate?.toFixed(1)}%)
                </span>
                <span className="stat">
                    📊 Submissions: {problem.totalSubmissions}
                </span>
            </div>
        </Link>
    );
}
