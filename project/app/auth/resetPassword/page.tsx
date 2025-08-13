'use client';
import React, { useState } from 'react';
import { useSearchParams } from 'next/navigation';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Eye, EyeOff, Lock } from 'lucide-react';

export default function ResetPasswordPage() {
  const searchParams = useSearchParams();
  const token = searchParams.get('token') || '';
  const [showPassword, setShowPassword] = useState(false);
  const [newPassword, setNewPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleReset = (e: React.FormEvent) => {
    e.preventDefault();
    if (!token) {
      setMessage('Lien de réinitialisation invalide ou expiré.');
      return;
    }
    console.log('Reset for token:', token, 'new password:', newPassword);
    // TODO: Call your authService.resetPassword(token, newPassword)
    setMessage('Mot de passe réinitialisé avec succès !');
  };

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-off-white to-off-white-200 dark:from-blue-night-800 dark:to-blue-night-900">
      <Navbar />
      <main className="flex-grow pt-24 pb-16">
        <div className="container-custom max-w-md mx-auto bg-white dark:bg-blue-night-700 p-8 rounded-2xl shadow-lg">
          <h1 className="text-3xl font-bold text-center mb-6 text-blue-night dark:text-off-white">
            Réinitialiser le mot de passe
          </h1>
          <form onSubmit={handleReset} className="space-y-5">
            <div>
              <Label htmlFor="password">Nouveau mot de passe</Label>
              <div className="relative mt-1">
                <Lock className="absolute left-3 top-2.5 text-gray-400 w-5 h-5" />
                <Input
                  id="password"
                  name="password"
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Entrez le nouveau mot de passe"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  required
                  className="pl-10 pr-10"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword((s) => !s)}
                  className="absolute right-3 top-2.5 text-gray-400"
                >
                  {showPassword ? <EyeOff className="w-5 h-5"/> : <Eye className="w-5 h-5" />}
                </button>
              </div>
            </div>
            <Button type="submit" className="w-full btn-primary">
              Réinitialiser
            </Button>
          </form>
          {message && <p className="text-sm text-center mt-4 text-green-600">{message}</p>}
        </div>
      </main>
      <Footer />
    </div>
  );
}
