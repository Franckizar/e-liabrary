'use client';

import React from 'react';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Button } from '@/components/ui/button';
import Link from 'next/link';

export default function TestPage() {
  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700 dark:to-blue-night-900">
      {/* Navbar */}
      <Navbar />

      {/* Main content */}
      <main className="flex-grow pt-24 pb-16">
        <div className="container-custom text-center space-y-6">
          <h1 className="text-4xl font-bold text-blue-night dark:text-off-white">
          formation
          </h1>
          <p className="text-lg text-gray-600 dark:text-gray-300">
            If you can see the Navbar and Footer above/below, the layout is working.
          </p>

          <Button asChild>
            <Link href="/">Go back Home</Link>
          </Button>
        </div>
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
}

// Formations
// Communauté
// À propos
