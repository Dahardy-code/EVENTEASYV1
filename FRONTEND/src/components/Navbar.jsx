// src/components/Navbar.jsx
import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom'; // Added useLocation
import { logout } from '../api/authApi'; // Import the logout function

const Navbar = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
  const [userRole, setUserRole] = useState(localStorage.getItem('userRole'));
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation(); // Get current location

  // Effect to check authentication status on mount and on location change
  // Checking on location change helps update state after navigation post-login/logout
  useEffect(() => {
    console.log("Navbar Effect: Checking auth status");
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole');
    setIsAuthenticated(!!token);
    setUserRole(role);
  }, [location.pathname]); // Re-run check when the URL path changes

  // Optional: Listener for storage changes (for multi-tab sync)
  useEffect(() => {
    const checkAuth = () => {
      console.log("Navbar Storage Listener: Auth changed");
      const token = localStorage.getItem('authToken');
      const role = localStorage.getItem('userRole');
      setIsAuthenticated(!!token);
      setUserRole(role);
    };
    window.addEventListener('storage', checkAuth);
    return () => {
      window.removeEventListener('storage', checkAuth);
    };
  }, []); // Runs only once on mount/unmount

  const handleLogout = () => {
    logout(); // Clear localStorage
    setIsAuthenticated(false); // Update state immediately
    setUserRole(null);
    setIsMenuOpen(false); // Close menu on logout
    navigate('/login'); // Redirect to login
  };

  // Determine the correct dashboard link based on role
  const getDashboardLink = () => {
    switch (userRole) {
      case 'CLIENT':
        return '/client/dashboard';
      case 'PRESTATAIRE':
        return '/prestataire/dashboard'; // Adjust if needed
      case 'ADMIN':
        return '/admin/dashboard'; // Adjust if needed
      default:
        // If authenticated but role unknown, maybe link to home?
        return '/';
    }
  };

  // Helper to close mobile menu
  const closeMobileMenu = () => setIsMenuOpen(false);

  return (
    <nav className="bg-gradient-to-r from-blue-500 to-purple-600 shadow-md text-white sticky top-0 z-50"> {/* Added sticky and z-index */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo/Brand */}
          <div className="flex-shrink-0">
            {/* Link to home page '/' */}
            <Link to="/" className="text-2xl font-bold hover:text-gray-200 transition duration-150 ease-in-out">
              Eventeasy
            </Link>
          </div>

          {/* Desktop Navigation Links */}
          <div className="hidden md:block">
            <div className="ml-10 flex items-baseline space-x-4">
              {/* Example Links (Uncomment and adapt as needed) */}
              <Link to="/" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition duration-150 ease-in-out">Accueil</Link>
              {/* <Link to="/about" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition duration-150 ease-in-out">À Propos</Link> */}
              {/* <Link to="/contact" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition duration-150 ease-in-out">Contact</Link> */}

              {isAuthenticated ? (
                <>
                  <Link to={getDashboardLink()} className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition duration-150 ease-in-out">
                    Mon Espace
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="px-3 py-2 rounded-md text-sm font-medium hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-purple-800 focus:ring-white transition duration-150 ease-in-out"
                  >
                    Déconnexion
                  </button>
                </>
              ) : (
                <>
                  <Link to="/login" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition duration-150 ease-in-out">
                    Connexion
                  </Link>
                  <Link to="/register" className="bg-white text-blue-600 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200 transition duration-150 ease-in-out">
                    Inscription
                  </Link>
                </>
              )}
            </div>
          </div>

          {/* Mobile Menu Button */}
          <div className="-mr-2 flex md:hidden">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              type="button"
              className="bg-blue-600 inline-flex items-center justify-center p-2 rounded-md text-blue-100 hover:text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-blue-800 focus:ring-white"
              aria-controls="mobile-menu"
              aria-expanded={isMenuOpen}
            >
              <span className="sr-only">Ouvrir le menu principal</span>
              {/* Icon for menu open/close */}
              {!isMenuOpen ? (
                <svg className="block h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              ) : (
                <svg className="block h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu, show/hide based on menu state */}
      {/* Use transition for smoother open/close */}
      <div
        className={`md:hidden transition-all duration-300 ease-in-out overflow-hidden ${isMenuOpen ? 'max-h-96 opacity-100' : 'max-h-0 opacity-0'}`}
        id="mobile-menu"
      >
        <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
          {/* Example Mobile Links (Uncomment and adapt) */}
          <Link to="/" className="block px-3 py-2 rounded-md text-base font-medium hover:bg-blue-700" onClick={closeMobileMenu}>Accueil</Link>
          {/* <Link to="/about" className="block px-3 py-2 rounded-md text-base font-medium hover:bg-blue-700" onClick={closeMobileMenu}>À Propos</Link> */}
          {/* <Link to="/contact" className="block px-3 py-2 rounded-md text-base font-medium hover:bg-blue-700" onClick={closeMobileMenu}>Contact</Link> */}

          {isAuthenticated ? (
            <>
              <Link to={getDashboardLink()} className="block px-3 py-2 rounded-md text-base font-medium hover:bg-blue-700" onClick={closeMobileMenu}>
                Mon Espace
              </Link>
              <button
                onClick={handleLogout} // Reuse the same logout handler
                className="block w-full text-left px-3 py-2 rounded-md text-base font-medium text-red-200 hover:bg-purple-700 hover:text-white"
              >
                Déconnexion
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="block px-3 py-2 rounded-md text-base font-medium hover:bg-blue-700" onClick={closeMobileMenu}>
                Connexion
              </Link>
              <Link to="/register" className="block px-3 py-2 rounded-md text-base font-medium bg-white text-blue-600 hover:bg-gray-200" onClick={closeMobileMenu}>
                Inscription
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;