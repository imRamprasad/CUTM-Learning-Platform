// frontend/src/components/CodeEditor.js
import React, { useRef, useEffect } from 'react';
import '../styles/CodeEditor.css';

export default function CodeEditor({ language, value, onChange, placeholder }) {
    const editorRef = useRef(null);
    const monacoRef = useRef(null);

    useEffect(() => {
        // Initialize Monaco Editor dynamically
        if (editorRef.current && !monacoRef.current && window.monaco) {
            monacoRef.current = window.monaco.editor.create(editorRef.current, {
                value: value || placeholder,
                language: getMonacoLanguage(language),
                theme: 'vs-dark',
                minimap: { enabled: false },
                fontSize: 13,
                fontFamily: 'Fira Code, Consolas, monospace',
                lineNumbers: 'on',
                automaticLayout: true,
                scrollBeyondLastLine: false,
                wordWrap: 'on'
            });

            monacoRef.current.onDidChangeModelContent(() => {
                onChange(monacoRef.current.getValue());
            });
        }

        // Update language when it changes
        if (monacoRef.current) {
            window.monaco.editor.setModelLanguage(
                monacoRef.current.getModel(),
                getMonacoLanguage(language)
            );
        }
    }, [language, onChange, placeholder, value]);

    useEffect(() => {
        if (monacoRef.current && value !== monacoRef.current.getValue()) {
            monacoRef.current.setValue(value);
        }
    }, [value]);

    return (
        <div className="code-editor-wrapper">
            <div 
                ref={editorRef}
                className="code-editor"
                style={{ width: '100%', height: '100%' }}
            />
            {/* Fallback if Monaco not loaded */}
            {!window.monaco && (
                <textarea
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    placeholder={placeholder}
                    className="code-editor-fallback"
                     spellCheck="false"
                />
            )}
        </div>
    );
}

function getMonacoLanguage(language) {
    const languageMap = {
        python: 'python',
        javascript: 'javascript',
        java: 'java',
        cpp: 'cpp'
    };
    return languageMap[language] || 'python';
}
