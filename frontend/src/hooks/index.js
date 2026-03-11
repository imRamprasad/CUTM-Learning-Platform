import { useState, useEffect, useCallback } from 'react'
import { apiClient } from '../contexts/AuthContext.jsx'

// Custom hook for API calls
export const useApi = () => {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const request = useCallback(async (method, url, config = {}) => {
    setLoading(true)
    setError(null)
    try {
      const response = await apiClient({
        method,
        url,
        ...config
      })
      setData(response.data)
      return response.data
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message
      setError(errorMessage)
      throw err
    } finally {
      setLoading(false)
    }
  }, [])

  const get = useCallback((url, config) => request('GET', url, config), [request])
  const post = useCallback((url, data, config) => request('POST', url, { data, ...config }), [request])
  const put = useCallback((url, data, config) => request('PUT', url, { data, ...config }), [request])
  const remove = useCallback((url, config) => request('DELETE', url, config), [request])

  return { data, loading, error, get, post, put, remove }
}

// Custom hook for pagination
export const usePagination = (initialPage = 0, pageSize = 20) => {
  const [page, setPage] = useState(initialPage)
  const [total, setTotal] = useState(0)

  const goToPage = (newPage) => {
    setPage(Math.max(0, newPage))
  }

  const nextPage = () => goToPage(page + 1)
  const prevPage = () => goToPage(Math.max(0, page - 1))

  const totalPages = Math.ceil(total / pageSize)
  const hasPrevPage = page > 0
  const hasNextPage = page < totalPages - 1

  return {
    page,
    pageSize,
    total,
    setTotal,
    goToPage,
    nextPage,
    prevPage,
    hasPrevPage,
    hasNextPage,
    totalPages
  }
}

// Custom hook for local storage
export const useLocalStorage = (key, initialValue) => {
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key)
      return item ? JSON.parse(item) : initialValue
    } catch (error) {
      console.error(error)
      return initialValue
    }
  })

  const setValue = (value) => {
    try {
      const valueToStore = value instanceof Function ? value(storedValue) : value
      setStoredValue(valueToStore)
      window.localStorage.setItem(key, JSON.stringify(valueToStore))
    } catch (error) {
      console.error(error)
    }
  }

  return [storedValue, setValue]
}

// Custom hook for fetching data
export const useFetch = (url, options = {}) => {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchData = async () => {
      if (!url) return
      
      setLoading(true)
      try {
        const token = localStorage.getItem('token')
        const response = await apiClient.get(url, {
          ...options,
          headers: {
            ...options.headers,
            ...(token && { Authorization: `Bearer ${token}` })
          }
        })
        setData(response.data)
      } catch (err) {
        setError(err)
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [url, JSON.stringify(options)])

  return { data, loading, error }
}

// Custom hook for debounced search
export const useDebounce = (value, delay = 500) => {
  const [debouncedValue, setDebouncedValue] = useState(value)

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value)
    }, delay)

    return () => {
      clearTimeout(handler)
    }
  }, [value, delay])

  return debouncedValue
}

