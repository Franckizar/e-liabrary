"use client";

import React, { useEffect, useState } from "react";

interface Book {
  id: number;
  title: string;
  price: number;
  genre: string;
  coverFileType?: string;
  coverImageBase64?: string;
  authors?: string[];
}

export default function BooksFetcher() {
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Change this to your real backend endpoint
    const API_URL = "http://localhost:8085/api/v1/auth/books";

    fetch(API_URL)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        return res.json();
      })
      .then((data: Book[]) => {
        console.log("Fetched books:", data);
        setBooks(data);
      })
      .catch((err) => {
        console.error("Error loading books:", err);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p>Chargement des livres...</p>;
  }

  return (
    <div>
      <h2>📚 Liste des livres</h2>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, 180px)", gap: "16px" }}>
        {books.map((book) => {
          const imgSrc = book.coverImageBase64
            ? `data:${book.coverFileType || "image/jpeg"};base64,${book.coverImageBase64}`
            : "https://via.placeholder.com/150x200?text=No+Image";
          return (
            <div
              key={book.id}
              style={{
                background: "#fff",
                borderRadius: "8px",
                padding: "8px",
                boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
                textAlign: "center",
              }}
            >
              <img src={imgSrc} alt={book.title} style={{ width: "100%", height: "220px", objectFit: "cover" }} />
              <h4 style={{ margin: "8px 0" }}>{book.title}</h4>
              <p style={{ margin: 0, color: "#666", fontSize: "0.85rem" }}>
                {book.authors?.join(", ") || "Auteur inconnu"}
              </p>
              <strong>{book.price ? `${book.price} FCFA` : "Prix N/A"}</strong>
            </div>
          );
        })}
      </div>
    </div>
  );
}
