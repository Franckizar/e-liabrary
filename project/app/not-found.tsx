'use client'

import React from 'react';
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { BookOpen, Home, Search, ArrowLeft } from 'lucide-react';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';

export default function NotFound() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700 dark:to-blue-night-900">
      <Navbar />
      
      <main className="pt-24 pb-16">
        <div className="container-custom">
          <div className="max-w-2xl mx-auto text-center space-y-8">
            {/* 404 Illustration */}
            <div className="relative">
              <div className="text-8xl md:text-9xl font-bold text-blue-night/10 dark:text-off-white/10">
                404
              </div>
              <div className="absolute inset-0 flex items-center justify-center">
                <div className="w-24 h-24 bg-gradient-to-br from-blue-night to-accent rounded-2xl flex items-center justify-center shadow-strong">
                  <BookOpen className="w-12 h-12 text-white" />
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="space-y-4">
              <h1 className="text-3xl md:text-4xl font-bold text-blue-night dark:text-off-white">
                Page introuvable
              </h1>
              <p className="text-lg text-gray-600 dark:text-gray-300 leading-relaxed">
                Désolé, la page que vous recherchez n'existe pas ou a été déplacée. 
                Mais ne vous inquiétez pas, il y a encore beaucoup à découvrir sur ÉdiNova !
              </p>
            </div>

            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button size="lg" className="btn-primary" asChild>
                <Link href="/">
                  <Home className="w-5 h-5 mr-2" />
                  Retour à l'accueil
                </Link>
              </Button>
              <Button size="lg" variant="outline" asChild>
                <Link href="/library">
                  <Search className="w-5 h-5 mr-2" />
                  Explorer la librairie
                </Link>
              </Button>
            </div>

            {/* Suggestions */}
            <div className="bg-white dark:bg-blue-night-700 rounded-2xl p-8 shadow-soft text-left">
              <h2 className="text-xl font-semibold text-blue-night dark:text-off-white mb-4">
                Que souhaitez-vous faire ?
              </h2>
              <div className="space-y-3">
                <Link 
                  href="/library"
                  className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-50 dark:hover:bg-blue-night-600 transition-colors group"
                >
                  <div className="w-10 h-10 bg-blue-night/10 dark:bg-off-white/10 rounded-lg flex items-center justify-center">
                    <BookOpen className="w-5 h-5 text-blue-night dark:text-off-white" />
                  </div>
                  <div>
                    <p className="font-medium text-blue-night dark:text-off-white group-hover:text-accent">
                      Découvrir notre librairie
                    </p>
                    <p className="text-sm text-gray-500 dark:text-gray-400">
                      Plus de 25,000 livres disponibles
                    </p>
                  </div>
                </Link>

                <Link 
                  href="/community"
                  className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-50 dark:hover:bg-blue-night-600 transition-colors group"
                >
                  <div className="w-10 h-10 bg-accent/10 rounded-lg flex items-center justify-center">
                    <BookOpen className="w-5 h-5 text-accent" />
                  </div>
                  <div>
                    <p className="font-medium text-blue-night dark:text-off-white group-hover:text-accent">
                      Rejoindre la communauté
                    </p>
                    <p className="text-sm text-gray-500 dark:text-gray-400">
                      Échangez avec d'autres passionnés
                    </p>
                  </div>
                </Link>

                <Link 
                  href="/trainings"
                  className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-50 dark:hover:bg-blue-night-600 transition-colors group"
                >
                  <div className="w-10 h-10 bg-success/10 rounded-lg flex items-center justify-center">
                    <BookOpen className="w-5 h-5 text-success" />
                  </div>
                  <div>
                    <p className="font-medium text-blue-night dark:text-off-white group-hover:text-accent">
                      Suivre nos formations
                    </p>
                    <p className="text-sm text-gray-500 dark:text-gray-400">
                      Webinaires et ateliers d'écriture
                    </p>
                  </div>
                </Link>
              </div>
            </div>

            {/* Go Back Button */}
            <Button 
              variant="ghost" 
              onClick={() => window.history.back()}
              className="text-gray-600 dark:text-gray-300 hover:text-blue-night dark:hover:text-accent"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              Retour à la page précédente
            </Button>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}