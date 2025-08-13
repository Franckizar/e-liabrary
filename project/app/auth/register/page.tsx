'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Eye, EyeOff, Lock, Mail, User } from 'lucide-react';

export default function RegisterPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [form, setForm] = useState({
    firstname: '',
    lastname: '',
    email: '',
    password: '',
    role: 'READER'
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Register data:', form);
    // TODO: connect to authService.register()
  };

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-off-white via-white to-off-white-200 dark:from-blue-night-800 dark:via-blue-night-700">
      <Navbar />
      <main className="flex-grow pt-24 pb-16">
        <div className="container-custom max-w-md mx-auto bg-white dark:bg-blue-night-700 rounded-2xl shadow-lg p-8">
          <h1 className="text-3xl font-bold text-center text-blue-night dark:text-off-white mb-2">
            Créer un compte
          </h1>
          <p className="text-center text-gray-600 dark:text-gray-300 mb-6">
            Rejoignez ÉdiNova dès aujourd'hui
          </p>

          <form onSubmit={handleRegister} className="space-y-5">
            {/* Firstname */}
            <div>
              <Label htmlFor="firstname">Prénom</Label>
              <div className="relative mt-1">
                <User className="absolute left-3 top-2.5 text-gray-400 w-5 h-5" />
                <Input
                  id="firstname"
                  name="firstname"
                  type="text"
                  placeholder="Votre prénom"
                  value={form.firstname}
                  onChange={handleChange}
                  required
                  className="pl-10"
                />
              </div>
            </div>

            {/* Lastname */}
            <div>
              <Label htmlFor="lastname">Nom</Label>
              <div className="relative mt-1">
                <User className="absolute left-3 top-2.5 text-gray-400 w-5 h-5" />
                <Input
                  id="lastname"
                  name="lastname"
                  type="text"
                  placeholder="Votre nom"
                  value={form.lastname}
                  onChange={handleChange}
                  required
                  className="pl-10"
                />
              </div>
            </div>

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
                  placeholder="Créer un mot de passe"
                  value={form.password}
                  onChange={handleChange}
                  required
                  className="pl-10 pr-10"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword((s) => !s)}
                  className="absolute right-3 top-2.5 text-gray-400"
                >
                  {showPassword ? <EyeOff className="w-5 h-5"/> : <Eye className="w-5 h-5"/>}
                </button>
              </div>
            </div>

            {/* Role */}
            <div>
              <Label htmlFor="role">Rôle</Label>
              <select
                id="role"
                name="role"
                value={form.role}
                onChange={handleChange}
                className="mt-1 p-2 border rounded w-full dark:bg-blue-night-600"
              >
                <option value="READER">Lecteur</option>
                <option value="AUTHOR">Auteur</option>
                <option value="PUBLISHER">Éditeur</option>
              </select>
            </div>

            {/* Submit */}
            <Button type="submit" className="w-full btn-primary">
              S'inscrire
            </Button>
          </form>

          <p className="text-center text-sm text-gray-600 dark:text-gray-300 mt-6">
            Déjà membre ?{' '}
            <Link href="/auth/login" className="text-accent hover:underline">
              Se connecter
            </Link>
          </p>
        </div>
      </main>
      <Footer />
    </div>
  );
}
