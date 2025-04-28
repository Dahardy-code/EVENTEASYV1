import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './styles/main.css'; // Importez le fichier CSS principal contenant Tailwind CSS

const root = createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);