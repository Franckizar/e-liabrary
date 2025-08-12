import React from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { ArrowRight, BookOpen, Users, Award, Download, Star, TrendingUp, Globe } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { BookCard } from '@/components/ui/book-card';
import { SearchBar } from '@/components/ui/search-bar';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Badge } from '@/components/ui/badge';

// Mock data for featured books
const featuredBooks = [
  {
    id: '1',
    title: 'Les Soleils des Indépendances',
    author: 'Ahmadou Kourouma',
    coverUrl: 'https://images.pexels.com/photos/694740/pexels-photo-694740.jpeg?auto=compress&cs=tinysrgb&w=400',
    price: 12000,
    originalPrice: 15000,
    rating: 4.8,
    reviewCount: 234,
    genre: 'Roman',
    isNew: true,
    isBestseller: true,
  },
  {
    id: '2',
    title: 'Une si longue lettre',
    author: 'Mariama Bâ',
    coverUrl: 'https://images.pexels.com/photos/1301585/pexels-photo-1301585.jpeg?auto=compress&cs=tinysrgb&w=400',
    price: 8500,
    rating: 4.7,
    reviewCount: 189,
    genre: 'Littérature',
    isBestseller: true,
  },
  {
    id: '3',
    title: 'Le Ventre de l\'Atlantique',
    author: 'Fatou Diome',
    coverUrl: 'https://images.pexels.com/photos/1261728/pexels-photo-1261728.jpeg?auto=compress&cs=tinysrgb&w=400',
    price: 11000,
    rating: 4.6,
    reviewCount: 156,
    genre: 'Roman',
    isNew: true,
  },
  {
    id: '4',
    title: 'Mémoires de porc-épic',
    author: 'Alain Mabanckou',
    coverUrl: 'https://images.pexels.com/photos/1319854/pexels-photo-1319854.jpeg?auto=compress&cs=tinysrgb&w=400',
    price: 9500,
    rating: 4.5,
    reviewCount: 198,
    genre: 'Fiction',
  },
];

const stats = [
  { icon: BookOpen, label: 'Livres disponibles', value: '25,000+' },
  { icon: Users, label: 'Auteurs africains', value: '5,000+' },
  { icon: Award, label: 'Prix littéraires', value: '150+' },
  { icon: Download, label: 'Téléchargements', value: '1M+' },
];

const genres = [
  { name: 'Roman', count: 8420, color: 'bg-blue-500' },
  { name: 'Poésie', count: 3210, color: 'bg-purple-500' },
  { name: 'Essai', count: 2150, color: 'bg-green-500' },
  { name: 'Théâtre', count: 1680, color: 'bg-yellow-500' },
  { name: 'Conte', count: 2890, color: 'bg-red-500' },
  { name: 'Biographie', count: 1450, color: 'bg-indigo-500' },
];

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700 dark:to-blue-night-900">
      <Navbar />
      
      {/* Hero Section */}
      <section className="relative pt-24 pb-16 overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-r from-blue-night/5 to-accent/5"></div>
        <div className="container-custom relative">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div className="space-y-8 animate-fade-in-up">
              <div>
                <Badge className="mb-6 bg-gradient-to-r from-blue-night to-accent text-white">
                  🌍 Première plateforme africaine de publication numérique
                </Badge>
                <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-blue-night dark:text-off-white leading-tight">
                  Découvrez la 
                  <span className="text-gradient block">littérature africaine</span>
                  comme jamais auparavant
                </h1>
                <p className="text-xl text-gray-600 dark:text-gray-300 mt-6 leading-relaxed">
                  ÉdiNova connecte auteurs, lecteurs et éditeurs pour célébrer et promouvoir 
                  la richesse littéraire du continent africain francophone.
                </p>
              </div>
              
              <div className="space-y-6">
                <SearchBar 
                  className="max-w-md"
                  placeholder="Rechercher votre prochaine lecture..."
                />
                
                <div className="flex flex-col sm:flex-row gap-4">
                  <Button size="lg" className="btn-primary" asChild>
                    <Link href="/library">
                      Explorer la librairie
                      <ArrowRight className="w-5 h-5 ml-2" />
                    </Link>
                  </Button>
                  <Button size="lg" variant="outline" asChild>
                    <Link href="/auth/register">
                      Publier mon livre
                    </Link>
                  </Button>
                </div>
              </div>

              {/* Stats */}
              <div className="grid grid-cols-2 md:grid-cols-4 gap-6 pt-8 border-t border-gray-200 dark:border-blue-night-600">
                {stats.map((stat) => {
                  const Icon = stat.icon;
                  return (
                    <div key={stat.label} className="text-center">
                      <div className="inline-flex items-center justify-center w-12 h-12 bg-blue-night/10 dark:bg-off-white/10 rounded-xl mb-3">
                        <Icon className="w-6 h-6 text-blue-night dark:text-off-white" />
                      </div>
                      <div className="font-bold text-2xl text-blue-night dark:text-off-white">
                        {stat.value}
                      </div>
                      <div className="text-sm text-gray-600 dark:text-gray-300">
                        {stat.label}
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

            <div className="relative animate-fade-in-up">
              <div className="relative">
                <div className="absolute -inset-4 bg-gradient-to-r from-blue-night/20 to-accent/20 rounded-3xl blur-2xl"></div>
                <div className="relative bg-white dark:bg-blue-night-700 rounded-3xl p-8 shadow-strong">
                  <div className="grid grid-cols-2 gap-6">
                    {featuredBooks.slice(0, 4).map((book, index) => (
                      <div
                        key={book.id}
                        className={`relative ${index === 0 ? 'col-span-2' : ''}`}
                      >
                        <Image
                          src={book.coverUrl}
                          alt={book.title}
                          width={index === 0 ? 200 : 100}
                          height={index === 0 ? 280 : 140}
                          className="rounded-xl object-cover shadow-medium hover:scale-105 transition-transform duration-300 mx-auto"
                        />
                        {index === 0 && (
                          <div className="absolute -top-2 -right-2">
                            <Badge className="bg-accent text-white">
                              📚 Tendance #1
                            </Badge>
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Books Section */}
      <section className="py-16">
        <div className="container-custom">
          <div className="flex items-center justify-between mb-12">
            <div>
              <h2 className="text-3xl font-bold text-blue-night dark:text-off-white mb-2">
                Livres en vedette
              </h2>
              <p className="text-gray-600 dark:text-gray-300">
                Découvrez les œuvres les plus appréciées par notre communauté
              </p>
            </div>
            <Button variant="outline" asChild>
              <Link href="/library">
                Voir tout
                <ArrowRight className="w-4 h-4 ml-2" />
              </Link>
            </Button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {featuredBooks.map((book) => (
              <BookCard key={book.id} {...book} />
            ))}
          </div>
        </div>
      </section>

      {/* Genres Section */}
      <section className="py-16 bg-gray-50 dark:bg-blue-night-800/50">
        <div className="container-custom">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-blue-night dark:text-off-white mb-4">
              Explorez par genre
            </h2>
            <p className="text-gray-600 dark:text-gray-300 max-w-2xl mx-auto">
              De la fiction contemporaine aux essais historiques, découvrez la diversité 
              de la littérature africaine francophone
            </p>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
            {genres.map((genre) => (
              <Link
                key={genre.name}
                href={`/library?genre=${encodeURIComponent(genre.name)}`}
                className="group"
              >
                <div className="bg-white dark:bg-blue-night-700 rounded-xl p-6 shadow-soft hover:shadow-medium transition-all duration-300 card-hover text-center">
                  <div className={`w-12 h-12 ${genre.color} rounded-xl mx-auto mb-4 flex items-center justify-center group-hover:scale-110 transition-transform`}>
                    <BookOpen className="w-6 h-6 text-white" />
                  </div>
                  <h3 className="font-semibold text-blue-night dark:text-off-white mb-1">
                    {genre.name}
                  </h3>
                  <p className="text-sm text-gray-500 dark:text-gray-400">
                    {genre.count.toLocaleString()} livres
                  </p>
                </div>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Community Section */}
      <section className="py-16">
        <div className="container-custom">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div className="space-y-6">
              <div>
                <Badge className="mb-4 bg-success text-white">
                  🤝 Communauté active
                </Badge>
                <h2 className="text-3xl font-bold text-blue-night dark:text-off-white mb-4">
                  Rejoignez une communauté passionnée de littérature
                </h2>
                <p className="text-gray-600 dark:text-gray-300 text-lg leading-relaxed">
                  Échangez avec des auteurs, participez à des clubs de lecture, 
                  découvrez des critiques authentiques et partagez votre passion 
                  pour les lettres africaines.
                </p>
              </div>

              <div className="space-y-4">
                <div className="flex items-start space-x-4">
                  <div className="w-10 h-10 bg-blue-night/10 dark:bg-off-white/10 rounded-xl flex items-center justify-center shrink-0">
                    <Users className="w-5 h-5 text-blue-night dark:text-off-white" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-blue-night dark:text-off-white">
                      Forums de discussion
                    </h3>
                    <p className="text-gray-600 dark:text-gray-300 text-sm">
                      Échangez sur vos lectures favorites et découvrez de nouvelles perspectives
                    </p>
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="w-10 h-10 bg-accent/10 rounded-xl flex items-center justify-center shrink-0">
                    <Star className="w-5 h-5 text-accent" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-blue-night dark:text-off-white">
                      Critiques et avis
                    </h3>
                    <p className="text-gray-600 dark:text-gray-300 text-sm">
                      Partagez vos impressions et aidez d'autres lecteurs dans leurs choix
                    </p>
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="w-10 h-10 bg-success/10 rounded-xl flex items-center justify-center shrink-0">
                    <Globe className="w-5 h-5 text-success" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-blue-night dark:text-off-white">
                      Événements virtuels
                    </h3>
                    <p className="text-gray-600 dark:text-gray-300 text-sm">
                      Participez à des rencontres d'auteurs et des séances de lecture
                    </p>
                  </div>
                </div>
              </div>

              <Button size="lg" className="btn-primary" asChild>
                <Link href="/community">
                  Découvrir la communauté
                  <ArrowRight className="w-5 h-5 ml-2" />
                </Link>
              </Button>
            </div>

            <div className="relative">
              <div className="bg-white dark:bg-blue-night-700 rounded-3xl p-8 shadow-strong">
                <div className="space-y-6">
                  <div className="flex items-center space-x-4">
                    <div className="w-12 h-12 bg-gradient-to-br from-blue-night to-accent rounded-full"></div>
                    <div>
                      <p className="font-semibold text-blue-night dark:text-off-white">
                        Aminata K.
                      </p>
                      <p className="text-sm text-gray-500 dark:text-gray-400">
                        Lectrice passionnée
                      </p>
                    </div>
                  </div>
                  <p className="text-gray-600 dark:text-gray-300 italic">
                    "ÉdiNova m'a permis de redécouvrir la richesse de notre littérature. 
                    Les échanges avec les auteurs sont inspirants !"
                  </p>
                  <div className="flex items-center">
                    {Array.from({ length: 5 }).map((_, i) => (
                      <Star key={i} className="w-4 h-4 text-yellow-400 fill-current" />
                    ))}
                  </div>
                </div>
              </div>
              
              {/* Floating stats */}
              <div className="absolute -top-6 -right-6 bg-accent text-white rounded-2xl p-4 shadow-strong">
                <div className="flex items-center space-x-2">
                  <TrendingUp className="w-5 h-5" />
                  <div>
                    <p className="font-bold">12,450</p>
                    <p className="text-xs opacity-90">Membres actifs</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-gradient-to-r from-blue-night to-blue-night-600 text-white">
        <div className="container-custom text-center">
          <div className="max-w-3xl mx-auto space-y-8">
            <h2 className="text-3xl md:text-4xl font-bold">
              Prêt à découvrir votre prochaine lecture ?
            </h2>
            <p className="text-xl text-blue-night-100 leading-relaxed">
              Rejoignez des milliers de lecteurs qui font confiance à ÉdiNova 
              pour explorer la littérature africaine contemporaine.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button size="lg" className="btn-accent" asChild>
                <Link href="/library">
                  Commencer la lecture
                  <BookOpen className="w-5 h-5 ml-2" />
                </Link>
              </Button>
              <Button size="lg" variant="outline" className="border-white text-white hover:bg-white hover:text-blue-night" asChild>
                <Link href="/about">
                  En savoir plus
                </Link>
              </Button>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
}