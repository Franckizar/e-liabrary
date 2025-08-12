import './globals.css';
import type { Metadata } from 'next';
import { Montserrat, Roboto } from 'next/font/google';
import { ThemeProvider } from '@/components/ui/theme-provider';
import { Toaster } from '@/components/ui/sonner';

const montserrat = Montserrat({ 
  subsets: ['latin'],
  variable: '--font-montserrat',
});

const roboto = Roboto({ 
  subsets: ['latin'],
  weight: ['300', '400', '500', '700'],
  variable: '--font-roboto',
});

export const metadata: Metadata = {
  title: {
    default: 'ÉdiNova - Plateforme de Publication Numérique Africaine',
    template: '%s | ÉdiNova',
  },
  description: 'ÉdiNova est la première plateforme de publication numérique dédiée au marché francophone africain. Découvrez, publiez et partagez la littérature africaine contemporaine.',
  keywords: ['édition', 'publication', 'Afrique', 'francophone', 'littérature', 'ebooks', 'manuscrits'],
  authors: [{ name: 'ÉdiNova Team' }],
  creator: 'ÉdiNova',
  publisher: 'ÉdiNova',
  openGraph: {
    type: 'website',
    locale: 'fr_FR',
    url: 'https://edinova.com',
    title: 'ÉdiNova - Plateforme de Publication Numérique Africaine',
    description: 'Découvrez la première plateforme de publication numérique dédiée au marché francophone africain.',
    siteName: 'ÉdiNova',
  },
  twitter: {
    card: 'summary_large_image',
    title: 'ÉdiNova - Plateforme de Publication Numérique Africaine',
    description: 'Découvrez la première plateforme de publication numérique dédiée au marché francophone africain.',
    creator: '@edinova',
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      'max-video-preview': -1,
      'max-image-preview': 'large',
      'max-snippet': -1,
    },
  },
  verification: {
    google: 'your-google-verification-code',
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="fr" suppressHydrationWarning>
      <body className={`${montserrat.variable} ${roboto.variable} antialiased`}>
        <ThemeProvider
          attribute="class"
          defaultTheme="light"
          enableSystem
          disableTransitionOnChange={false}
        >
          {children}
          <Toaster 
            position="bottom-right"
            toastOptions={{
              style: {
                background: 'hsl(var(--background))',
                color: 'hsl(var(--foreground))',
                border: '1px solid hsl(var(--border))',
              },
            }}
          />
        </ThemeProvider>
      </body>
    </html>
  );
}