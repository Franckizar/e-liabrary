"use client";

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useTheme } from 'next-themes';
import { Button } from '@/components/ui/button';
import { 
  BookOpen, 
  Menu, 
  X, 
  Sun, 
  Moon, 
  ShoppingCart, 
  User,
  Search,
  Users,
  GraduationCap,
  LogOut,
  Settings,
  Heart
} from 'lucide-react';
import { cn } from '@/lib/utils';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Badge } from '@/components/ui/badge';

const navigationItems = [
  { name: 'Accueil', href: '/', icon: null },
  { name: 'Librairie', href: '/library', icon: BookOpen },
  { name: 'Formations', href: '/trainings', icon: GraduationCap },
  { name: 'Communauté', href: '/community', icon: Users },
  { name: 'À propos', href: '/about', icon: null },
];

export function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false); // This would come from auth context
  const [cartItemsCount, setCartItemsCount] = useState(3); // This would come from cart context
  const pathname = usePathname();
  const { theme, setTheme } = useTheme();

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 10);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleMobileMenu = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = () => {
    // Implement logout logic
    setIsLoggedIn(false);
  };

  return (
    <nav 
      className={cn(
        'fixed top-0 w-full z-50 transition-all duration-300',
        isScrolled 
          ? 'bg-white/95 dark:bg-blue-night-800/95 backdrop-blur-md shadow-medium' 
          : 'bg-transparent'
      )}
    >
      <div className="container-custom">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link href="/" className="flex items-center space-x-2 group">
            <div className="w-10 h-10 bg-gradient-to-br from-blue-night to-accent rounded-lg flex items-center justify-center group-hover:scale-110 transition-transform duration-200">
              <BookOpen className="w-6 h-6 text-white" />
            </div>
            <span className="text-xl font-bold text-blue-night dark:text-off-white">
              ÉdiNova
            </span>
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-1">
            {navigationItems.map((item) => (
              <Link
                key={item.name}
                href={item.href}
                className={cn(
                  'px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 hover:bg-blue-night/10 dark:hover:bg-off-white/10',
                  pathname === item.href
                    ? 'text-blue-night dark:text-off-white bg-blue-night/5 dark:bg-off-white/5'
                    : 'text-blue-night/70 dark:text-off-white/70 hover:text-blue-night dark:hover:text-off-white'
                )}
              >
                <div className="flex items-center space-x-2">
                  {item.icon && <item.icon className="w-4 h-4" />}
                  <span>{item.name}</span>
                </div>
              </Link>
            ))}
          </div>

          {/* Right side actions */}
          <div className="flex items-center space-x-2">
            {/* Search */}
            <Button variant="ghost" size="sm" className="hidden sm:flex">
              <Search className="w-4 h-4" />
            </Button>

            {/* Theme Toggle */}
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}
              className="hidden sm:flex"
            >
              <Sun className="w-4 h-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
              <Moon className="absolute w-4 h-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
            </Button>

            {/* Cart */}
            <Link href="/cart" className="relative">
              <Button variant="ghost" size="sm">
                <ShoppingCart className="w-4 h-4" />
                {cartItemsCount > 0 && (
                  <Badge 
                    variant="destructive" 
                    className="absolute -top-2 -right-2 w-5 h-5 flex items-center justify-center p-0 text-xs"
                  >
                    {cartItemsCount}
                  </Badge>
                )}
              </Button>
            </Link>

            {/* User Menu */}
            {isLoggedIn ? (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="sm" className="relative">
                    <User className="w-4 h-4" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-56">
                  <DropdownMenuItem asChild>
                    <Link href="/profile">
                      <User className="w-4 h-4 mr-2" />
                      Mon Profil
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/dashboard">
                      <Settings className="w-4 h-4 mr-2" />
                      Tableau de bord
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/favorites">
                      <Heart className="w-4 h-4 mr-2" />
                      Mes Favoris
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onClick={handleLogout}>
                    <LogOut className="w-4 h-4 mr-2" />
                    Se déconnecter
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            ) : (
              <div className="hidden sm:flex items-center space-x-2">
                <Button variant="ghost" size="sm" asChild>
                  <Link href="/auth/login">Connexion</Link>
                </Button>
                <Button size="sm" asChild>
                  <Link href="/auth/register">S'inscrire</Link>
                </Button>
              </div>
            )}

            {/* Mobile Menu Button */}
            <Button
              variant="ghost"
              size="sm"
              onClick={toggleMobileMenu}
              className="md:hidden"
            >
              {isOpen ? (
                <X className="w-5 h-5" />
              ) : (
                <Menu className="w-5 h-5" />
              )}
            </Button>
          </div>
        </div>

        {/* Mobile Menu */}
        {isOpen && (
          <div className="md:hidden">
            <div className="px-2 pt-2 pb-3 space-y-1 bg-white/95 dark:bg-blue-night-800/95 backdrop-blur-md rounded-b-lg shadow-medium animate-slide-down">
              {navigationItems.map((item) => (
                <Link
                  key={item.name}
                  href={item.href}
                  onClick={() => setIsOpen(false)}
                  className={cn(
                    'block px-3 py-2 rounded-md text-base font-medium transition-colors',
                    pathname === item.href
                      ? 'text-blue-night dark:text-off-white bg-blue-night/10 dark:bg-off-white/10'
                      : 'text-blue-night/70 dark:text-off-white/70 hover:text-blue-night dark:hover:text-off-white hover:bg-blue-night/5 dark:hover:bg-off-white/5'
                  )}
                >
                  <div className="flex items-center space-x-3">
                    {item.icon && <item.icon className="w-5 h-5" />}
                    <span>{item.name}</span>
                  </div>
                </Link>
              ))}
              
              {/* Mobile Auth Buttons */}
              {!isLoggedIn && (
                <div className="pt-4 border-t border-blue-night/10 dark:border-off-white/10">
                  <div className="flex flex-col space-y-2">
                    <Button variant="ghost" asChild className="justify-start">
                      <Link href="/auth/login" onClick={() => setIsOpen(false)}>
                        Connexion
                      </Link>
                    </Button>
                    <Button asChild className="justify-start">
                      <Link href="/auth/register" onClick={() => setIsOpen(false)}>
                        S'inscrire
                      </Link>
                    </Button>
                  </div>
                </div>
              )}

              {/* Mobile Theme Toggle */}
              <div className="pt-4 border-t border-blue-night/10 dark:border-off-white/10">
                <Button
                  variant="ghost"
                  onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}
                  className="w-full justify-start"
                >
                  {theme === 'dark' ? (
                    <Sun className="w-4 h-4 mr-3" />
                  ) : (
                    <Moon className="w-4 h-4 mr-3" />
                  )}
                  {theme === 'dark' ? 'Mode Clair' : 'Mode Sombre'}
                </Button>
              </div>
            </div>
          </div>
        )}
      </div>
    </nav>
  );
}