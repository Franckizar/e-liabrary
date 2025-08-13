'use client';
import React, { useState } from 'react';
import { Navbar } from '@/components/layout/navbar';
import { Footer } from '@/components/layout/footer';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Mail } from 'lucide-react';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Call your API: authService.forgotPassword(email)
    console.log('Forgot password for:', email);
    setMessage('Si un compte existe pour cet email, un lien de réinitialisation a été envoyé.');
  };

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-off-white to-off-white-200 dark:from-blue-night-800 dark:to-blue-night-900">
      <Navbar />
      <main className="flex-grow pt-24 pb-16">
        <div className="container-custom max-w-md mx-auto bg-white dark:bg-blue-night-700 p-8 rounded-2xl shadow-lg">
          <h1 className="text-3xl font-bold text-center mb-6 text-blue-night dark:text-off-white">
            Mot de passe oublié ?
          </h1>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <Label htmlFor="email">Adresse email</Label>
              <div className="relative mt-1">
                <Mail className="absolute left-3 top-2.5 text-gray-400 w-5 h-5"/>
                <Input
                  id="email"
                  name="email"
                  type="email"
                  placeholder="votre@email.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  className="pl-10"
                />
              </div>
            </div>
            <Button type="submit" className="w-full btn-primary">
              Envoyer le lien de réinitialisation
            </Button>
          </form>
          {message && <p className="text-sm text-center mt-4 text-green-600">{message}</p>}
        </div>
      </main>
      <Footer />
    </div>
  );
}
