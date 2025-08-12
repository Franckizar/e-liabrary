"use client";

import React from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { Star, Download, Heart, ShoppingCart, Eye, User } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { cn } from '@/lib/utils';

interface BookCardProps {
  id: string;
  title: string;
  author: string;
  coverUrl: string;
  price: number;
  originalPrice?: number;
  rating: number;
  reviewCount: number;
  genre: string;
  isEbook?: boolean;
  isPdf?: boolean;
  isNew?: boolean;
  isBestseller?: boolean;
  className?: string;
  variant?: 'default' | 'compact' | 'featured';
}

export function BookCard({
  id,
  title,
  author,
  coverUrl,
  price,
  originalPrice,
  rating,
  reviewCount,
  genre,
  isEbook = true,
  isPdf = true,
  isNew = false,
  isBestseller = false,
  className,
  variant = 'default'
}: BookCardProps) {
  const [isFavorite, setIsFavorite] = React.useState(false);
  const [isInCart, setIsInCart] = React.useState(false);

  const handleToggleFavorite = (e: React.MouseEvent) => {
    e.preventDefault();
    setIsFavorite(!isFavorite);
  };

  const handleAddToCart = (e: React.MouseEvent) => {
    e.preventDefault();
    setIsInCart(!isInCart);
    // Add to cart logic here
  };

  const discount = originalPrice ? Math.round((1 - price / originalPrice) * 100) : 0;

  if (variant === 'compact') {
    return (
      <Link href={`/book/${id}`} className={cn("group block", className)}>
        <div className="flex space-x-4 p-4 rounded-xl hover:bg-gray-50 dark:hover:bg-blue-night-700 transition-colors">
          <div className="relative shrink-0">
            <Image
              src={coverUrl}
              alt={title}
              width={60}
              height={90}
              className="rounded-lg object-cover shadow-soft"
            />
          </div>
          <div className="flex-1 min-w-0">
            <h3 className="font-semibold text-sm line-clamp-2 group-hover:text-blue-night dark:group-hover:text-accent transition-colors">
              {title}
            </h3>
            <p className="text-xs text-muted-foreground mt-1">{author}</p>
            <div className="flex items-center mt-2">
              <div className="flex items-center">
                {Array.from({ length: 5 }).map((_, i) => (
                  <Star
                    key={i}
                    className={cn(
                      "w-3 h-3",
                      i < rating ? "text-yellow-400 fill-current" : "text-gray-300"
                    )}
                  />
                ))}
              </div>
              <span className="text-xs text-muted-foreground ml-2">({reviewCount})</span>
            </div>
            <div className="flex items-center justify-between mt-3">
              <div className="flex items-center space-x-2">
                <span className="font-bold text-sm text-blue-night dark:text-off-white">
                  {price.toLocaleString()} FCFA
                </span>
                {originalPrice && (
                  <span className="text-xs text-muted-foreground line-through">
                    {originalPrice.toLocaleString()} FCFA
                  </span>
                )}
              </div>
              <Badge variant="secondary" className="text-xs">
                {genre}
              </Badge>
            </div>
          </div>
        </div>
      </Link>
    );
  }

  return (
    <div className={cn("group relative", className)}>
      <Link href={`/book/${id}`} className="block">
        <div className="bg-white dark:bg-blue-night-700 rounded-2xl shadow-soft group-hover:shadow-strong transition-all duration-300 overflow-hidden card-hover">
          {/* Cover Image */}
          <div className="relative aspect-[3/4] overflow-hidden">
            <Image
              src={coverUrl}
              alt={title}
              fill
              className="object-cover group-hover:scale-105 transition-transform duration-300"
            />
            
            {/* Badges */}
            <div className="absolute top-3 left-3 flex flex-col gap-2">
              {isNew && (
                <Badge className="bg-success text-white text-xs px-2 py-1">
                  Nouveau
                </Badge>
              )}
              {isBestseller && (
                <Badge className="bg-accent text-white text-xs px-2 py-1">
                  Bestseller
                </Badge>
              )}
              {discount > 0 && (
                <Badge className="bg-warning text-white text-xs px-2 py-1">
                  -{discount}%
                </Badge>
              )}
            </div>

            {/* Favorite Button */}
            <Button
              variant="ghost"
              size="sm"
              onClick={handleToggleFavorite}
              className={cn(
                "absolute top-3 right-3 w-8 h-8 p-0 rounded-full backdrop-blur-sm transition-all opacity-0 group-hover:opacity-100",
                isFavorite 
                  ? "bg-red-500 text-white hover:bg-red-600" 
                  : "bg-white/80 text-gray-600 hover:bg-white"
              )}
            >
              <Heart className={cn("w-4 h-4", isFavorite && "fill-current")} />
            </Button>

            {/* Quick Actions (visible on hover) */}
            <div className="absolute bottom-3 left-3 right-3 opacity-0 group-hover:opacity-100 transition-all duration-300 transform translate-y-2 group-hover:translate-y-0">
              <div className="flex gap-2">
                <Button size="sm" variant="secondary" className="flex-1 backdrop-blur-sm">
                  <Eye className="w-4 h-4 mr-2" />
                  Aperçu
                </Button>
                <Button 
                  size="sm" 
                  onClick={handleAddToCart}
                  className={cn(
                    "flex-1 backdrop-blur-sm",
                    isInCart && "bg-success text-white"
                  )}
                >
                  <ShoppingCart className="w-4 h-4 mr-2" />
                  {isInCart ? 'Ajouté' : 'Panier'}
                </Button>
              </div>
            </div>

            {/* Format indicators */}
            <div className="absolute bottom-3 right-3 flex gap-1">
              {isEbook && (
                <Badge variant="secondary" className="text-xs px-2 py-1 bg-white/80 text-gray-700">
                  EPUB
                </Badge>
              )}
              {isPdf && (
                <Badge variant="secondary" className="text-xs px-2 py-1 bg-white/80 text-gray-700">
                  PDF
                </Badge>
              )}
            </div>
          </div>

          {/* Content */}
          <div className="p-4 space-y-3">
            {/* Genre */}
            <Badge variant="outline" className="text-xs">
              {genre}
            </Badge>

            {/* Title and Author */}
            <div>
              <h3 className="font-semibold text-lg line-clamp-2 group-hover:text-blue-night dark:group-hover:text-accent transition-colors">
                {title}
              </h3>
              <div className="flex items-center mt-1 text-muted-foreground">
                <User className="w-3 h-3 mr-1" />
                <span className="text-sm">{author}</span>
              </div>
            </div>

            {/* Rating */}
            <div className="flex items-center">
              <div className="flex items-center">
                {Array.from({ length: 5 }).map((_, i) => (
                  <Star
                    key={i}
                    className={cn(
                      "w-4 h-4",
                      i < rating ? "text-yellow-400 fill-current" : "text-gray-300"
                    )}
                  />
                ))}
              </div>
              <span className="text-sm text-muted-foreground ml-2">
                {rating} ({reviewCount} avis)
              </span>
            </div>

            {/* Price and Download */}
            <div className="flex items-center justify-between pt-2 border-t border-gray-100 dark:border-blue-night-600">
              <div className="flex flex-col">
                <span className="font-bold text-lg text-blue-night dark:text-off-white">
                  {price.toLocaleString()} FCFA
                </span>
                {originalPrice && (
                  <span className="text-sm text-muted-foreground line-through">
                    {originalPrice.toLocaleString()} FCFA
                  </span>
                )}
              </div>
              <div className="flex items-center text-sm text-muted-foreground">
                <Download className="w-4 h-4 mr-1" />
                <span>Téléchargeable</span>
              </div>
            </div>
          </div>
        </div>
      </Link>
    </div>
  );
}