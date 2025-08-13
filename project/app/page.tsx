"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import Image from "next/image";
import {
  ArrowRight,
  BookOpen,
  Users,
  Award,
  Download,
  Star,
  TrendingUp,
  Globe,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { BookCard } from "@/components/ui/book-card";
import { SearchBar } from "@/components/ui/search-bar";
import { Navbar } from "@/components/layout/navbar";
import { Footer } from "@/components/layout/footer";
import { Badge } from "@/components/ui/badge";

interface Book {
  id: number;
  title: string;
  isbn: string;
  description: string;
  price: number;
  originalPrice?: number;
  format: string;
  rating: number;
  reviewCount: number;
  genre: string;
  coverFileType?: string;
  coverImageBase64?: string;
  authors?: string[];
  categories?: string[];
  publisherName?: string;
  isNew?: boolean;
  isBestseller?: boolean;
}

const stats = [
  { icon: BookOpen, label: "Livres disponibles", value: "?" },
  { icon: Users, label: "Auteurs africains", value: "5,000+" },
  { icon: Award, label: "Prix littéraires", value: "150+" },
  { icon: Download, label: "Téléchargements", value: "1M+" },
];

const genres = [
  { name: "Roman", count: 8420, color: "bg-blue-500" },
  { name: "Poésie", count: 3210, color: "bg-purple-500" },
  { name: "Essai", count: 2150, color: "bg-green-500" },
  { name: "Théâtre", count: 1680, color: "bg-yellow-500" },
  { name: "Conte", count: 2890, color: "bg-red-500" },
  { name: "Biographie", count: 1450, color: "bg-indigo-500" },
];

export default function HomePage() {
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8085/api/v1/auth/books")
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data: Book[]) => {
        // Sort bestsellers first
        const bestsellers = data.filter((b) => b.isBestseller);
        const others = data.filter((b) => !b.isBestseller);
        const sorted = [...bestsellers, ...others].slice(0, 4); // Keep only 4
        setBooks(sorted);
      })
      .catch((err) => {
        console.error("Error fetching books:", err);
      })
      .finally(() => setLoading(false));
  }, []);

  const statsWithCount = stats.map((s) =>
    s.label === "Livres disponibles" ? { ...s, value: books.length.toString() } : s
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700 dark:to-blue-night-900">
      <Navbar />

      {/* Hero Section */}
      <section className="relative pt-24 pb-16 overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-r from-blue-night/5 to-accent/5" />
        <div className="container-custom relative">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            {/* Left */}
            <div className="space-y-8 animate-fade-in-up">
              <Badge className="mb-6 bg-gradient-to-r from-blue-night to-accent text-white">
                🌍 Première plateforme africaine de publication numérique
              </Badge>
              <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-blue-night dark:text-off-white leading-tight">
                Découvrez la <span className="text-gradient block">littérature africaine</span>
              </h1>
              <p className="text-xl text-gray-600 dark:text-gray-300 mt-6 leading-relaxed">
                ÉdiNova connecte auteurs, lecteurs et éditeurs pour célébrer la richesse littéraire africaine.
              </p>

              <SearchBar
                className="max-w-md"
                placeholder="Rechercher votre prochaine lecture..."
              />

              <div className="flex flex-col sm:flex-row gap-4">
                <Button size="lg" className="btn-primary" asChild>
                  <Link href="/library">
                    Explorer la librairie <ArrowRight className="w-5 h-5 ml-2" />
                  </Link>
                </Button>
                <Button size="lg" variant="outline" asChild>
                  <Link href="/auth/register">Publier mon livre</Link>
                </Button>
              </div>

              {/* Stats */}
              <div className="grid grid-cols-2 md:grid-cols-4 gap-6 pt-8 border-t border-gray-200 dark:border-blue-night-600">
                {statsWithCount.map((stat) => {
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

            {/* Right — Top 4 books */}
            <div className="relative animate-fade-in-up">
              <div className="absolute -inset-4 bg-gradient-to-r from-blue-night/20 to-accent/20 rounded-3xl blur-2xl" />
              <div className="relative bg-white dark:bg-blue-night-700 rounded-3xl p-8 shadow-strong">
                <div className="grid grid-cols-2 gap-6">
                  {books.map((book, index) => {
                    const imgSrc = book.coverImageBase64
                      ? `data:${book.coverFileType || "image/jpeg"};base64,${book.coverImageBase64}`
                      : "https://via.placeholder.com/200x250?text=No+Image";
                    return (
                      <div key={book.id} className={`relative ${index === 0 ? "col-span-2" : ""}`}>
                        <Image
                          src={imgSrc}
                          alt={book.title}
                          width={index === 0 ? 200 : 100}
                          height={index === 0 ? 280 : 140}
                          className="rounded-xl object-cover shadow-medium hover:scale-105 transition-transform duration-300 mx-auto"
                        />
                        {book.isBestseller && (
                          <div className="absolute -top-2 -right-2">
                            <Badge className="bg-accent text-white">🔥 Bestseller</Badge>
                          </div>
                        )}
                      </div>
                    );
                  })}
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
                Découvrez notre sélection spéciale
              </p>
            </div>
            <Button variant="outline" asChild>
              <Link href="/library">
                Voir tout <ArrowRight className="w-4 h-4 ml-2" />
              </Link>
            </Button>
          </div>

          {loading ? (
            <p>Chargement...</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
              {books.map((book) => {
                const imgSrc = book.coverImageBase64
                  ? `data:${book.coverFileType || "image/jpeg"};base64,${book.coverImageBase64}`
                  : "https://via.placeholder.com/200x250?text=No+Image";
                return (
                  <BookCard
                    key={book.id}
                    id={book.id.toString()}
                    title={book.title}
                    author={book.authors?.join(", ") || "Auteur inconnu"}
                    coverUrl={imgSrc}
                    price={book.price}
                    originalPrice={book.originalPrice}
                    rating={book.rating}
                    reviewCount={book.reviewCount}
                    genre={book.genre}
                    isNew={book.isNew}
                    isBestseller={book.isBestseller}
                  />
                );
              })}
            </div>
          )}
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
              littéraire.
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
                  <div
                    className={`w-12 h-12 ${genre.color} rounded-xl mx-auto mb-4 flex items-center justify-center group-hover:scale-110 transition-transform`}
                  >
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

      <Footer />
    </div>
  );
}
