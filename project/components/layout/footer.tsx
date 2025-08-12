import React from 'react';
import Link from 'next/link';
import { BookOpen, Mail, MapPin, Phone, Facebook, Twitter, Instagram, Linkedin, Youtube } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

const footerLinks = {
  platform: [
    { name: 'Librairie', href: '/library' },
    { name: 'Formations', href: '/trainings' },
    { name: 'Communauté', href: '/community' },
    { name: 'Devenir Éditeur', href: '/auth/register?type=publisher' },
  ],
  support: [
    { name: 'Centre d\'aide', href: '/help' },
    { name: 'FAQ', href: '/faq' },
    { name: 'Contact', href: '/contact' },
    { name: 'Signaler un problème', href: '/report' },
  ],
  legal: [
    { name: 'Mentions légales', href: '/legal' },
    { name: 'Politique de confidentialité', href: '/privacy' },
    { name: 'Conditions d\'utilisation', href: '/terms' },
    { name: 'Cookies', href: '/cookies' },
  ],
  about: [
    { name: 'À propos', href: '/about' },
    { name: 'Notre mission', href: '/mission' },
    { name: 'Équipe', href: '/team' },
    { name: 'Carrières', href: '/careers' },
  ],
};

const socialLinks = [
  { name: 'Facebook', icon: Facebook, href: 'https://facebook.com/edinova' },
  { name: 'Twitter', icon: Twitter, href: 'https://twitter.com/edinova' },
  { name: 'Instagram', icon: Instagram, href: 'https://instagram.com/edinova' },
  { name: 'LinkedIn', icon: Linkedin, href: 'https://linkedin.com/company/edinova' },
  { name: 'YouTube', icon: Youtube, href: 'https://youtube.com/@edinova' },
];

export function Footer() {
  return (
    <footer className="bg-blue-night text-off-white">
      {/* Newsletter Section */}
      <div className="border-b border-blue-night-600">
        <div className="container-custom py-12">
          <div className="max-w-4xl mx-auto text-center">
            <h3 className="text-2xl font-semibold mb-4">
              Restez connecté à l'univers ÉdiNova
            </h3>
            <p className="text-off-white-700 mb-8 text-lg">
              Recevez les dernières nouvelles, les nouveautés littéraires et les offres exclusives directement dans votre boîte mail.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 max-w-md mx-auto">
              <Input
                type="email"
                placeholder="Votre adresse email"
                className="flex-1 bg-blue-night-600 border-blue-night-500 text-off-white placeholder:text-off-white-600 focus:border-accent focus:ring-accent"
              />
              <Button className="btn-accent shrink-0">
                S'abonner
              </Button>
            </div>
            <p className="text-sm text-off-white-700 mt-4">
              En vous abonnant, vous acceptez de recevoir nos communications marketing. Vous pouvez vous désabonner à tout moment.
            </p>
          </div>
        </div>
      </div>

      {/* Main Footer Content */}
      <div className="container-custom py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-8">
          {/* Brand Section */}
          <div className="lg:col-span-2">
            <Link href="/" className="flex items-center space-x-2 mb-4 group">
              <div className="w-10 h-10 bg-gradient-to-br from-accent to-blue-night-400 rounded-lg flex items-center justify-center group-hover:scale-110 transition-transform duration-200">
                <BookOpen className="w-6 h-6 text-white" />
              </div>
              <span className="text-xl font-bold">ÉdiNova</span>
            </Link>
            <p className="text-off-white-700 mb-6 leading-relaxed">
              La première plateforme de publication numérique dédiée au marché francophone africain. 
              Nous connectons auteurs, lecteurs et éditeurs pour promouvoir la richesse littéraire africaine.
            </p>
            
            {/* Contact Info */}
            <div className="space-y-3">
              <div className="flex items-center space-x-3">
                <MapPin className="w-4 h-4 text-accent shrink-0" />
                <span className="text-sm text-off-white-700">
                  Dakar, Sénégal & Abidjan, Côte d'Ivoire
                </span>
              </div>
              <div className="flex items-center space-x-3">
                <Phone className="w-4 h-4 text-accent shrink-0" />
                <span className="text-sm text-off-white-700">
                  +221 77 123 45 67
                </span>
              </div>
              <div className="flex items-center space-x-3">
                <Mail className="w-4 h-4 text-accent shrink-0" />
                <span className="text-sm text-off-white-700">
                  contact@edinova.com
                </span>
              </div>
            </div>
          </div>

          {/* Links Sections */}
          <div>
            <h4 className="font-semibold mb-4 text-off-white">Plateforme</h4>
            <ul className="space-y-3">
              {footerLinks.platform.map((link) => (
                <li key={link.name}>
                  <Link 
                    href={link.href}
                    className="text-off-white-700 hover:text-accent transition-colors duration-200 text-sm"
                  >
                    {link.name}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4 text-off-white">Support</h4>
            <ul className="space-y-3">
              {footerLinks.support.map((link) => (
                <li key={link.name}>
                  <Link 
                    href={link.href}
                    className="text-off-white-700 hover:text-accent transition-colors duration-200 text-sm"
                  >
                    {link.name}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4 text-off-white">Légal</h4>
            <ul className="space-y-3">
              {footerLinks.legal.map((link) => (
                <li key={link.name}>
                  <Link 
                    href={link.href}
                    className="text-off-white-700 hover:text-accent transition-colors duration-200 text-sm"
                  >
                    {link.name}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4 text-off-white">À propos</h4>
            <ul className="space-y-3">
              {footerLinks.about.map((link) => (
                <li key={link.name}>
                  <Link 
                    href={link.href}
                    className="text-off-white-700 hover:text-accent transition-colors duration-200 text-sm"
                  >
                    {link.name}
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>

      {/* Bottom Section */}
      <div className="border-t border-blue-night-600">
        <div className="container-custom py-6">
          <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
            {/* Copyright */}
            <p className="text-sm text-off-white-700 text-center sm:text-left">
              © {new Date().getFullYear()} ÉdiNova. Tous droits réservés. Fait avec ❤️ pour l'Afrique.
            </p>

            {/* Social Links */}
            <div className="flex items-center space-x-4">
              <span className="text-sm text-off-white-700">Suivez-nous :</span>
              <div className="flex items-center space-x-3">
                {socialLinks.map((social) => {
                  const Icon = social.icon;
                  return (
                    <Link
                      key={social.name}
                      href={social.href}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="w-8 h-8 rounded-full bg-blue-night-600 hover:bg-accent flex items-center justify-center transition-colors duration-200 group"
                      aria-label={`Suivre ÉdiNova sur ${social.name}`}
                    >
                      <Icon className="w-4 h-4 text-off-white-700 group-hover:text-white" />
                    </Link>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}