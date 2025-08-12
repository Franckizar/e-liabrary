// API service configuration for ÉdiNova platform
// This will be connected to a Spring Boot backend

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// API endpoints
export const API_ENDPOINTS = {
  // Authentication
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    REFRESH: '/auth/refresh',
    FORGOT_PASSWORD: '/auth/forgot-password',
    RESET_PASSWORD: '/auth/reset-password',
    VERIFY_EMAIL: '/auth/verify-email',
  },
  
  // Books
  BOOKS: {
    LIST: '/books',
    DETAIL: '/books/:id',
    SEARCH: '/books/search',
    FEATURED: '/books/featured',
    BY_GENRE: '/books/genre/:genre',
    BY_AUTHOR: '/books/author/:authorId',
    REVIEWS: '/books/:id/reviews',
    DOWNLOAD: '/books/:id/download',
  },
  
  // Authors
  AUTHORS: {
    LIST: '/authors',
    DETAIL: '/authors/:id',
    BOOKS: '/authors/:id/books',
  },
  
  // User Management
  USER: {
    PROFILE: '/user/profile',
    UPDATE_PROFILE: '/user/profile',
    FAVORITES: '/user/favorites',
    PURCHASES: '/user/purchases',
    READING_HISTORY: '/user/reading-history',
  },
  
  // Shopping Cart
  CART: {
    GET: '/cart',
    ADD: '/cart/add',
    REMOVE: '/cart/remove/:id',
    UPDATE: '/cart/update/:id',
    CLEAR: '/cart/clear',
  },
  
  // Orders & Payments
  ORDERS: {
    CREATE: '/orders',
    LIST: '/orders',
    DETAIL: '/orders/:id',
    PAYMENT: '/orders/:id/payment',
  },
  
  // Community
  COMMUNITY: {
    FORUMS: '/community/forums',
    POSTS: '/community/posts',
    COMMENTS: '/community/posts/:id/comments',
    BOOK_CLUBS: '/community/book-clubs',
  },
  
  // Dashboard (for publishers/editors)
  DASHBOARD: {
    STATS: '/dashboard/stats',
    BOOKS: '/dashboard/books',
    SUBMISSIONS: '/dashboard/submissions',
    SALES: '/dashboard/sales',
  },
  
  // Training & Events
  TRAINING: {
    LIST: '/training',
    DETAIL: '/training/:id',
    REGISTER: '/training/:id/register',
    WEBINARS: '/training/webinars',
    CONTESTS: '/training/contests',
  },
};

// HTTP client with interceptors
class ApiClient {
  private baseURL: string;
  private token: string | null = null;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
    
    // Load token from localStorage if available
    if (typeof window !== 'undefined') {
      this.token = localStorage.getItem('auth_token');
    }
  }

  private getHeaders(): Record<string, string> {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
    };

    if (this.token) {
      headers.Authorization = `Bearer ${this.token}`;
    }

    return headers;
  }

  private async handleResponse(response: Response) {
    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'Une erreur est survenue' }));
      throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    const contentType = response.headers.get('Content-Type');
    if (contentType && contentType.includes('application/json')) {
      return response.json();
    }

    return response.text();
  }

  async get<T>(endpoint: string, params?: Record<string, any>): Promise<T> {
    const url = new URL(endpoint, this.baseURL);
    
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          url.searchParams.append(key, String(value));
        }
      });
    }

    const response = await fetch(url.toString(), {
      method: 'GET',
      headers: this.getHeaders(),
    });

    return this.handleResponse(response);
  }

  async post<T>(endpoint: string, data?: any): Promise<T> {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'POST',
      headers: this.getHeaders(),
      body: data ? JSON.stringify(data) : undefined,
    });

    return this.handleResponse(response);
  }

  async put<T>(endpoint: string, data?: any): Promise<T> {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'PUT',
      headers: this.getHeaders(),
      body: data ? JSON.stringify(data) : undefined,
    });

    return this.handleResponse(response);
  }

  async delete<T>(endpoint: string): Promise<T> {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'DELETE',
      headers: this.getHeaders(),
    });

    return this.handleResponse(response);
  }

  setToken(token: string) {
    this.token = token;
    if (typeof window !== 'undefined') {
      localStorage.setItem('auth_token', token);
    }
  }

  clearToken() {
    this.token = null;
    if (typeof window !== 'undefined') {
      localStorage.removeItem('auth_token');
    }
  }
}

// Create API client instance
export const apiClient = new ApiClient(API_BASE_URL);

// Type definitions for API responses
export interface ApiResponse<T> {
  data: T;
  message?: string;
  status: 'success' | 'error';
}

export interface PaginatedResponse<T> {
  data: T[];
  currentPage: number;
  totalPages: number;
  totalItems: number;
  hasNext: boolean;
  hasPrev: boolean;
}

export interface Book {
  id: string;
  title: string;
  description: string;
  author: Author;
  coverUrl: string;
  price: number;
  originalPrice?: number;
  rating: number;
  reviewCount: number;
  genre: string;
  language: string;
  publishedAt: string;
  isEbook: boolean;
  isPdf: boolean;
  fileSize?: number;
  pageCount?: number;
  isbn?: string;
  tags: string[];
}

export interface Author {
  id: string;
  name: string;
  bio?: string;
  profilePicture?: string;
  country?: string;
  birthDate?: string;
  website?: string;
  socialLinks?: {
    twitter?: string;
    facebook?: string;
    instagram?: string;
  };
}

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  profilePicture?: string;
  country?: string;
  preferences: {
    favoriteGenres: string[];
    language: string;
    newsletter: boolean;
  };
  role: 'reader' | 'author' | 'publisher' | 'admin';
  createdAt: string;
}

export interface CartItem {
  id: string;
  book: Book;
  quantity: number;
  addedAt: string;
}

export interface Order {
  id: string;
  items: CartItem[];
  total: number;
  status: 'pending' | 'paid' | 'cancelled' | 'refunded';
  paymentMethod: string;
  createdAt: string;
  paidAt?: string;
}

// Service functions
export const authService = {
  async login(email: string, password: string): Promise<{ user: User; token: string }> {
    const response = await apiClient.post<{ user: User; token: string }>(API_ENDPOINTS.AUTH.LOGIN, {
      email,
      password,
    });
    
    if (response.token) {
      apiClient.setToken(response.token);
    }
    
    return response;
  },

  async register(userData: {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    country?: string;
    role?: string;
  }): Promise<{ user: User; token: string }> {
    const response = await apiClient.post<{ user: User; token: string }>(API_ENDPOINTS.AUTH.REGISTER, userData);
    
    if (response.token) {
      apiClient.setToken(response.token);
    }
    
    return response;
  },

  async forgotPassword(email: string): Promise<{ message: string }> {
    return apiClient.post(API_ENDPOINTS.AUTH.FORGOT_PASSWORD, { email });
  },

  logout() {
    apiClient.clearToken();
  },
};

export const bookService = {
  async getBooks(params?: {
    page?: number;
    limit?: number;
    genre?: string;
    author?: string;
    search?: string;
    sortBy?: string;
    sortOrder?: 'asc' | 'desc';
  }): Promise<PaginatedResponse<Book>> {
    return apiClient.get<PaginatedResponse<Book>>(API_ENDPOINTS.BOOKS.LIST, params);
  },

  async getBookById(id: string): Promise<Book> {
    return apiClient.get<Book>(API_ENDPOINTS.BOOKS.DETAIL.replace(':id', id));
  },

  async getFeaturedBooks(): Promise<Book[]> {
    return apiClient.get<Book[]>(API_ENDPOINTS.BOOKS.FEATURED);
  },

  async searchBooks(query: string, filters?: Record<string, any>): Promise<PaginatedResponse<Book>> {
    return apiClient.get<PaginatedResponse<Book>>(API_ENDPOINTS.BOOKS.SEARCH, {
      q: query,
      ...filters,
    });
  },
};

export const cartService = {
  async getCart(): Promise<CartItem[]> {
    return apiClient.get<CartItem[]>(API_ENDPOINTS.CART.GET);
  },

  async addToCart(bookId: string, quantity: number = 1): Promise<CartItem> {
    return apiClient.post<CartItem>(API_ENDPOINTS.CART.ADD, { bookId, quantity });
  },

  async removeFromCart(itemId: string): Promise<void> {
    return apiClient.delete(API_ENDPOINTS.CART.REMOVE.replace(':id', itemId));
  },

  async updateCartItem(itemId: string, quantity: number): Promise<CartItem> {
    return apiClient.put<CartItem>(API_ENDPOINTS.CART.UPDATE.replace(':id', itemId), { quantity });
  },

  async clearCart(): Promise<void> {
    return apiClient.delete(API_ENDPOINTS.CART.CLEAR);
  },
};

export const userService = {
  async getProfile(): Promise<User> {
    return apiClient.get<User>(API_ENDPOINTS.USER.PROFILE);
  },

  async updateProfile(userData: Partial<User>): Promise<User> {
    return apiClient.put<User>(API_ENDPOINTS.USER.UPDATE_PROFILE, userData);
  },

  async getFavorites(): Promise<Book[]> {
    return apiClient.get<Book[]>(API_ENDPOINTS.USER.FAVORITES);
  },

  async getPurchases(): Promise<Order[]> {
    return apiClient.get<Order[]>(API_ENDPOINTS.USER.PURCHASES);
  },
};