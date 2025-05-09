import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { logout } from '../api/authApi';

const Navbar = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
  const [userRole, setUserRole] = useState(localStorage.getItem('userRole'));
  const navigate = useNavigate();
  const location = useLocation(); // Utilisation de useLocation pour obtenir l'URL actuelle

  // Vérifie les informations d'authentification et de rôle à chaque changement de localStorage
  useEffect(() => {
    const checkAuth = () => {
      const token = localStorage.getItem('authToken');
      const role = localStorage.getItem('userRole');
      setIsAuthenticated(!!token);
      setUserRole(role);
    };
    checkAuth();
    window.addEventListener('storage', checkAuth);
    return () => {
      window.removeEventListener('storage', checkAuth);
    };
  }, []);

  const handleLogout = () => {
    logout();
    setIsAuthenticated(false);
    setUserRole(null);
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    navigate('/login');  // Redirection vers la page de login
  };

  // Gérer la redirection après connexion ou inscription
  const redirectAfterLoginOrSignup = () => {
    if (userRole === 'CLIENT' || userRole === 'PRESTATAIRE') {
      navigate('/home');  // Rediriger vers la page HomePage.jsx après connexion ou inscription
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      redirectAfterLoginOrSignup();
    }
  }, [isAuthenticated, userRole]);

  // Conditionner l'affichage de la navbar en fonction de la route
  if (location.pathname === '/login') {
    return (
      <nav className="navbar shadow-md text-white sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex-shrink-0">
              <Link to="/" className="text-2xl font-bold text-white hover:text-gray-200 transition duration-150 ease-in-out">
                Eventeasy
              </Link>
            </div>
            <div className="ml-10 flex items-baseline space-x-4">
              <Link to="/register" className="bg-white text-brown-800 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200 transition duration-150 ease-in-out">
                Inscription
              </Link>
            </div>
          </div>
        </div>
      </nav>
    );
  }

  if (location.pathname === '/register') {
    return (
      <nav className="navbar shadow-md text-white sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex-shrink-0">
              <Link to="/" className="text-2xl font-bold text-white hover:text-gray-200 transition duration-150 ease-in-out">
                Eventeasy
              </Link>
            </div>
            <div className="ml-10 flex items-baseline space-x-4">
              <Link to="/login" className="bg-white text-brown-800 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200 transition duration-150 ease-in-out">
                Connexion
              </Link>
            </div>
          </div>
        </div>
      </nav>
    );
  }

  // Déterminer l'URL du tableau de bord en fonction du rôle
  const getDashboardLink = () => {
    if (userRole === 'CLIENT') {
      return '/client/dashboard';
    } else if (userRole === 'PRESTATAIRE') {
      return '/prestataire/dashboard';
    }
    return '/home'; // Par défaut rediriger vers HomePage
  };

  // Si l'utilisateur est authentifié, afficher la navbar avec Accueil, Mon Espace et Déconnexion
  return (
    <nav className="navbar shadow-md text-white sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex-shrink-0">
            <Link to="/" className="text-2xl font-bold text-white hover:text-gray-200 transition duration-150 ease-in-out">
              Eventeasy
            </Link>
          </div>
          <div className="ml-10 flex items-baseline space-x-4">
            <Link to="/home" className="bg-white text-brown-800 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200 transition duration-150 ease-in-out">
              Accueil
            </Link>
            <Link to={getDashboardLink()} className="bg-white text-brown-800 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200 transition duration-150 ease-in-out">
              Mon Espace
            </Link>
            <button
              onClick={handleLogout}
              className="bg-white text-brown-800 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200 transition duration-150 ease-in-out"
            >
              Déconnexion
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
