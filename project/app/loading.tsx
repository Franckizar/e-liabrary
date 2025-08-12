import React from 'react';
import { BookOpen } from 'lucide-react';

export default function Loading() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700 dark:to-blue-night-900 flex items-center justify-center">
      <div className="text-center space-y-6">
        {/* Animated Logo */}
        <div className="relative">
          <div className="w-16 h-16 bg-gradient-to-br from-blue-night to-accent rounded-2xl flex items-center justify-center shadow-strong animate-pulse">
            <BookOpen className="w-8 h-8 text-white animate-bounce" />
          </div>
          
          {/* Loading Ring */}
          <div className="absolute -inset-4 border-4 border-blue-night/20 dark:border-off-white/20 border-t-blue-night dark:border-t-accent rounded-full animate-spin"></div>
        </div>

        {/* Loading Text */}
        <div className="space-y-2">
          <h2 className="text-xl font-semibold text-blue-night dark:text-off-white">
            Chargement en cours...
          </h2>
          <p className="text-gray-600 dark:text-gray-300">
            Préparation de votre expérience ÉdiNova
          </p>
        </div>

        {/* Progress Indicator */}
        <div className="w-64 h-2 bg-gray-200 dark:bg-blue-night-600 rounded-full overflow-hidden">
          <div className="h-full bg-gradient-to-r from-blue-night to-accent rounded-full animate-pulse"></div>
        </div>
      </div>
    </div>
  );
}