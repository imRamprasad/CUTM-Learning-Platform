import { useState, useCallback, useEffect } from 'react';
import apiClient from '../services/apiClient';

export const useApi = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const request = useCallback(async (method, url, config = {}) => {
        setLoading(true);
        setError(null);
        try {
            const response = await apiClient({
                method,
                url,
                ...config,
            });
            setData(response.data);
            return response.data;
        } catch (err) {
            const errorMessage = err.response?.data?.message || err.message;
            setError(errorMessage);
            throw err;
        } finally {
            setLoading(false);
        }
    }, []);

    const get = useCallback((url, config) => request('GET', url, config), [request]);
    const post = useCallback((url, data, config) => request('POST', url, { data, ...config }), [request]);
    const put = useCallback((url, data, config) => request('PUT', url, { data, ...config }), [request]);
    const remove = useCallback((url, config) => request('DELETE', url, config), [request]);

    return { data, loading, error, get, post, put, remove };
};

export const usePagination = (initialPage = 0, pageSize = 20) => {
    const [page, setPage] = useState(initialPage);
    const [total, setTotal] = useState(0);

    const goToPage = (newPage) => {
        setPage(Math.max(0, newPage));
    };

    const nextPage = () => goToPage(page + 1);
    const prevPage = () => goToPage(Math.max(0, page - 1));
    const totalPages = Math.ceil(total / pageSize);

    return {
        page,
        setPage: goToPage,
        pageSize,
        total,
        setTotal,
        nextPage,
        prevPage,
        totalPages,
        hasNextPage: page < totalPages - 1,
        hasPrevPage: page > 0,
    };
};

export const useLocalStorage = (key, initialValue) => {
    const [storedValue, setStoredValue] = useState(() => {
        try {
            const item = window.localStorage.getItem(key);
            return item ? JSON.parse(item) : initialValue;
        } catch (error) {
            return initialValue;
        }
    });

    const setValue = (value) => {
        try {
            const valueToStore = value instanceof Function ? value(storedValue) : value;
            setStoredValue(valueToStore);
            window.localStorage.setItem(key, JSON.stringify(valueToStore));
        } catch (error) {
            // no-op
        }
    };

    return [storedValue, setValue];
};

export const useFetch = (url, options = {}) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const optionsKey = JSON.stringify(options || {});

    useEffect(() => {
        const parsedOptions = optionsKey ? JSON.parse(optionsKey) : {};
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await apiClient.get(url, parsedOptions);
                setData(response.data);
                setError(null);
            } catch (err) {
                setError(err.message);
                setData(null);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [url, optionsKey]);

    return { data, loading, error };
};
