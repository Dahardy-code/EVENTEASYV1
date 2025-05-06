/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}", // Inclure tous les fichiers React
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#006400',     // Vert foncé
          light: '#228B22',       // Vert forêt clair
        },
        accent: {
          DEFAULT: '#fdf6e3',     // Beige clair
          dark: '#f5f5dc',        // Beige plus profond
        },
      },
    },
  },
  plugins: [],
};
