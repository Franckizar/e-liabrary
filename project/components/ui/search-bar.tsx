"use client";

import React, { useState, useRef, useEffect } from 'react';
import { Search, X, Clock, TrendingUp, BookOpen, User } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import Link from 'next/link';

interface SearchSuggestion {
  id: string;
  type: 'book' | 'author' | 'genre' | 'recent';
  title: string;
  subtitle?: string;
  imageUrl?: string;
}

const mockSuggestions: SearchSuggestion[] = [
  { id: '1', type: 'book', title: 'Les Soleils des Indépendances', subtitle: 'Ahmadou Kourouma', imageUrl: 'https://images.pexels.com/photos/694740/pexels-photo-694740.jpeg' },
  { id: '2', type: 'author', title: 'Aminata Sow Fall', subtitle: '12 livres disponibles' },
  { id: '3', type: 'book', title: 'Une si longue lettre', subtitle: 'Mariama Bâ', imageUrl: 'https://images.pexels.com/photos/1301585/pexels-photo-1301585.jpeg' },
  { id: '4', type: 'genre', title: 'Roman contemporain', subtitle: '156 livres' },
  { id: '5', type: 'author', title: 'Alain Mabanckou', subtitle: '8 livres disponibles' },
];

const recentSearches = [
  'Littérature africaine',
  'Conte traditionnel',
  'Poésie moderne',
];

interface SearchBarProps {
  className?: string;
  placeholder?: string;
  onSearch?: (query: string) => void;
  showSuggestions?: boolean;
}

export function SearchBar({ 
  className, 
  placeholder = "Rechercher des livres, auteurs, genres...",
  onSearch,
  showSuggestions = true 
}: SearchBarProps) {
  const [query, setQuery] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [suggestions, setSuggestions] = useState<SearchSuggestion[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const searchRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (searchRef.current && !searchRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Simulate API call for suggestions
  useEffect(() => {
    if (query.length > 2) {
      setIsLoading(true);
      const timer = setTimeout(() => {
        const filtered = mockSuggestions.filter(item =>
          item.title.toLowerCase().includes(query.toLowerCase()) ||
          item.subtitle?.toLowerCase().includes(query.toLowerCase())
        );
        setSuggestions(filtered);
        setIsLoading(false);
        setIsOpen(true);
      }, 300);

      return () => clearTimeout(timer);
    } else {
      setSuggestions([]);
      setIsLoading(false);
      if (query.length === 0 && isOpen) {
        setIsOpen(true); // Show recent searches when empty
      }
    }
  }, [query, isOpen]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (query.trim()) {
      onSearch?.(query.trim());
      setIsOpen(false);
      // Add to recent searches logic here
    }
  };

  const handleSuggestionClick = (suggestion: SearchSuggestion) => {
    setQuery(suggestion.title);
    setIsOpen(false);
    onSearch?.(suggestion.title);
  };

  const clearQuery = () => {
    setQuery('');
    inputRef.current?.focus();
  };

  const handleInputFocus = () => {
    setIsOpen(true);
  };

  const getSuggestionIcon = (type: string) => {
    switch (type) {
      case 'book':
        return BookOpen;
      case 'author':
        return User;
      case 'recent':
        return Clock;
      default:
        return TrendingUp;
    }
  };

  return (
    <div ref={searchRef} className={cn("relative", className)}>
      <form onSubmit={handleSubmit} className="relative">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            ref={inputRef}
            type="text"
            value={query}
            onChange={handleInputChange}
            onFocus={handleInputFocus}
            placeholder={placeholder}
            className="pl-10 pr-10 h-12 text-base bg-white dark:bg-blue-night-700 border-2 border-gray-200 dark:border-blue-night-600 rounded-xl focus:border-blue-night dark:focus:border-accent transition-colors"
          />
          {query && (
            <Button
              type="button"
              variant="ghost"
              size="sm"
              onClick={clearQuery}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 w-6 h-6 p-0 hover:bg-gray-100 dark:hover:bg-blue-night-600 rounded-full"
            >
              <X className="w-4 h-4" />
            </Button>
          )}
        </div>
      </form>

      {/* Dropdown */}
      {isOpen && showSuggestions && (
        <div className="absolute top-full left-0 right-0 mt-2 bg-white dark:bg-blue-night-700 border border-gray-200 dark:border-blue-night-600 rounded-xl shadow-strong z-50 max-h-96 overflow-y-auto animate-slide-down">
          {isLoading ? (
            <div className="p-4 text-center">
              <div className="spinner mx-auto mb-2"></div>
              <p className="text-sm text-muted-foreground">Recherche en cours...</p>
            </div>
          ) : (
            <>
              {/* Recent searches when no query */}
              {query.length === 0 && (
                <div className="p-4">
                  <h4 className="text-sm font-semibold mb-3 text-muted-foreground flex items-center">
                    <Clock className="w-4 h-4 mr-2" />
                    Recherches récentes
                  </h4>
                  <div className="space-y-2">
                    {recentSearches.map((search, index) => (
                      <button
                        key={index}
                        onClick={() => {
                          setQuery(search);
                          onSearch?.(search);
                          setIsOpen(false);
                        }}
                        className="w-full text-left px-3 py-2 rounded-lg hover:bg-gray-50 dark:hover:bg-blue-night-600 text-sm transition-colors"
                      >
                        {search}
                      </button>
                    ))}
                  </div>
                </div>
              )}

              {/* Suggestions */}
              {suggestions.length > 0 && (
                <div className="p-2">
                  {suggestions.map((suggestion) => {
                    const Icon = getSuggestionIcon(suggestion.type);
                    return (
                      <button
                        key={suggestion.id}
                        onClick={() => handleSuggestionClick(suggestion)}
                        className="w-full flex items-center space-x-3 px-3 py-3 rounded-lg hover:bg-gray-50 dark:hover:bg-blue-night-600 text-left transition-colors group"
                      >
                        {suggestion.imageUrl ? (
                          <img
                            src={suggestion.imageUrl}
                            alt={suggestion.title}
                            className="w-10 h-10 rounded-lg object-cover"
                          />
                        ) : (
                          <div className="w-10 h-10 rounded-lg bg-blue-night/10 dark:bg-off-white/10 flex items-center justify-center">
                            <Icon className="w-5 h-5 text-blue-night dark:text-off-white" />
                          </div>
                        )}
                        <div className="flex-1 min-w-0">
                          <p className="font-medium text-sm truncate group-hover:text-blue-night dark:group-hover:text-accent">
                            {suggestion.title}
                          </p>
                          {suggestion.subtitle && (
                            <p className="text-xs text-muted-foreground truncate">
                              {suggestion.subtitle}
                            </p>
                          )}
                        </div>
                        <div className="flex items-center text-xs text-muted-foreground">
                          {suggestion.type === 'book' && 'Livre'}
                          {suggestion.type === 'author' && 'Auteur'}
                          {suggestion.type === 'genre' && 'Genre'}
                        </div>
                      </button>
                    );
                  })}
                </div>
              )}

              {/* No results */}
              {query.length > 2 && suggestions.length === 0 && !isLoading && (
                <div className="p-4 text-center text-muted-foreground">
                  <Search className="w-8 h-8 mx-auto mb-2 opacity-50" />
                  <p className="text-sm">Aucun résultat pour "{query}"</p>
                  <p className="text-xs mt-1">Essayez avec d'autres mots-clés</p>
                </div>
              )}
            </>
          )}
        </div>
      )}
    </div>
  );
}