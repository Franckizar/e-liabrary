'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Eye, EyeOff, Lock, Mail } from 'lucide-react';

export default function LoginPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [form, setForm] = useState({ email: '', password: '', remember: false });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Login data:', form);
    // TODO: Call your authService.login(form.email, form.password)
  };

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700 dark:to-blue-night-900">
      {/* Navbar */}
      <Navbar />

      {/* Main content */}
      <main className="flex-grow pt-24 pb-16">
        <div className="container-custom max-w-md mx-auto bg-white dark:bg-blue-night-700 rounded-2xl shadow-lg p-8">
          {/* Title */}
          <h1 className="text-3xl font-bold text-center text-blue-night dark:text-off-white mb-2">
            Connexion
          </h1>
          <p className="text-center text-gray-600 dark:text-gray-300 mb-6">
            Accédez à votre compte ÉdiNova
          </p>

          {/* Login form */}
          <form onSubmit={handleLogin} className="space-y-5">
            {/* Email */}
            <div>
              <Label htmlFor="email">Adresse email</Label>
              <div className="relative mt-1">
                <Mail className="absolute left-3 top-2.5 text-gray-400 w-5 h-5" />
                <Input
                  id="email"
                  name="email"
                  type="email"
                  placeholder="votre@email.com"
                  value={form.email}
                  onChange={handleChange}
                  required
                  className="pl-10"
                />
              </div>
            </div>

            {/* Password */}
            <div>
              <Label htmlFor="password">Mot de passe</Label>
              <div className="relative mt-1">
                <Lock className="absolute left-3 top-2.5 text-gray-400 w-5 h-5" />
                <Input
                  id="password"
                  name="password"
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Votre mot de passe"
                  value={form.password}
                  onChange={handleChange}
                  required
                  className="pl-10 pr-10"
                />
                <button
                  type="button"
                  className="absolute right-3 top-2.5 text-gray-400"
                  onClick={() => setShowPassword((s) => !s)}
                  aria-label="Toggle password visibility"
                >
                  {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                </button>
              </div>
            </div>

            {/* Remember Me + Forgot Password */}
            <div className="flex items-center justify-between text-sm">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  name="remember"
                  checked={form.remember}
                  onChange={handleChange}
                  className="rounded"
                />
                Se souvenir de moi
              </label>
              <Link
                href="/auth/forgot-password"
                className="text-accent hover:underline"
              >
                Mot de passe oublié ?
              </Link>
            </div>

            {/* Submit */}
            <Button type="submit" className="w-full btn-primary">
              Se connecter
            </Button>
          </form>

          {/* Divider */}
          <div className="my-6 flex items-center space-x-4">
            <div className="flex-grow h-px bg-gray-300 dark:bg-blue-night-600"></div>
            <span className="text-gray-500 text-sm">ou</span>
            <div className="flex-grow h-px bg-gray-300 dark:bg-blue-night-600"></div>
          </div>

          {/* Social login buttons (placeholders) */}
          <div className="space-y-3">
            <Button type="button" variant="outline" className="w-full">
              Continuer avec Google
            </Button>
            <Button type="button" variant="outline" className="w-full">
              Continuer avec Facebook
            </Button>
          </div>

          {/* Register link */}
          <p className="text-center text-sm text-gray-600 dark:text-gray-300 mt-6">
            Pas encore de compte ?{' '}
            <Link href="/auth/register" className="text-accent hover:underline">
              S'inscrire
            </Link>
          </p>
        </div>
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
}
