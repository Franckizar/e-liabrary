'use client';

import React from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { BookOpen, Users, Globe, Award } from 'lucide-react';

export default function AboutPage() {
  const stats = [
    { icon: BookOpen, label: 'Livres publiés', value: '25,000+' },
    { icon: Users, label: 'Auteurs africains', value: '5,000+' },
    { icon: Globe, label: 'Pays représentés', value: '20+' },
    { icon: Award, label: 'Prix littéraires', value: '150+' },
  ];

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700">
      {/* Navbar */}
      <Navbar />

      {/* Main content */}
      <main className="flex-grow">
        {/* Hero */}
        <section className="relative pt-24 pb-16 overflow-hidden">
          <div className="container-custom grid lg:grid-cols-2 gap-12 items-center">
            <div className="space-y-6">
              <Badge className="bg-gradient-to-r from-blue-night to-accent text-white">
                🌍 Notre histoire
              </Badge>
              <h1 className="text-4xl md:text-5xl font-bold text-blue-night dark:text-off-white leading-tight">
                À propos d’<span className="text-accent">ÉdiNova</span>
              </h1>
              <p className="text-lg text-gray-600 dark:text-gray-300 leading-relaxed">
                ÉdiNova est la première plateforme de publication numérique dédiée au marché francophone africain. 
                Nous connectons auteurs, lecteurs et éditeurs autour d’une mission commune : promouvoir la richesse 
                littéraire africaine et ouvrir de nouvelles opportunités aux talents émergents.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                <Button asChild className="btn-primary">
                  <Link href="/library">Découvrir la librairie</Link>
                </Button>
                <Button asChild variant="outline">
                  <Link href="/auth/register">Rejoindre la communauté</Link>
                </Button>
              </div>
            </div>

            {/* Hero image */}
            <div className="relative h-80 lg:h-full">
              <Image
                src="https://images.pexels.com/photos/694740/pexels-photo-694740.jpeg"
                alt="Présentation ÉdiNova"
                fill
                className="object-cover rounded-3xl shadow-lg"
              />
            </div>
          </div>
        </section>

        {/* Stats */}
        <section className="py-16 bg-gray-50 dark:bg-blue-night-800/50">
          <div className="container-custom grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
            {stats.map((stat) => {
              const Icon = stat.icon;
              return (
                <div key={stat.label} className="space-y-2">
                  <div className="inline-flex items-center justify-center w-14 h-14 bg-blue-night/10 dark:bg-off-white/10 rounded-xl">
                    <Icon className="w-7 h-7 text-blue-night dark:text-off-white" />
                  </div>
                  <div className="text-2xl font-bold text-blue-night dark:text-off-white">
                    {stat.value}
                  </div>
                  <div className="text-gray-600 dark:text-gray-300">{stat.label}</div>
                </div>
              );
            })}
          </div>
        </section>

        {/* Mission */}
        <section className="py-16">
          <div className="container-custom grid lg:grid-cols-2 gap-12 items-center">
            <div className="relative h-80 lg:h-full order-2 lg:order-1">
              <Image
                src="https://images.pexels.com/photos/1301585/pexels-photo-1301585.jpeg"
                alt="Mission ÉdiNova"
                fill
                className="object-cover rounded-3xl shadow-lg"
              />
            </div>
            <div className="space-y-6 order-1 lg:order-2">
              <h2 className="text-3xl font-bold text-blue-night dark:text-off-white">
                Notre mission
              </h2>
              <p className="text-lg text-gray-600 dark:text-gray-300 leading-relaxed">
                Nous croyons que les histoires africaines méritent d’être entendues partout dans le monde. 
                ÉdiNova offre aux auteurs un accès simplifié à la publication numérique, aux lecteurs une 
                bibliothèque riche et variée, et aux éditeurs une vitrine pour présenter leurs ouvrages.
              </p>
            </div>
          </div>
        </section>

        {/* Call to Action */}
        <section className="py-16 bg-gradient-to-r from-blue-night to-accent text-white text-center">
          <div className="container-custom space-y-6">
            <h2 className="text-3xl md:text-4xl font-bold">
              Prêt à écrire la prochaine page de votre histoire ?
            </h2>
            <p className="text-lg max-w-2xl mx-auto">
              Rejoignez ÉdiNova et participez à la révolution numérique de la littérature africaine.
            </p>
            <Button asChild size="lg" className="btn-accent">
              <Link href="/auth/register">Commencer maintenant</Link>
            </Button>
          </div>
        </section>
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
}
