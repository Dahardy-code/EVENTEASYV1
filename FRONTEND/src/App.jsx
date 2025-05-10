// src/App.jsx
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';

// --- Page Imports ---
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import HomePage from './pages/HomePage'; // <-- Votre HomePage
import ClientDashboard from './pages/client/Dashboard';
import PrestataireDashboard from './pages/prestataire/Dashboard';
import ManagePrestataireServices from './pages/prestataire/Services';
/*************  ✨ Windsurf Command ⭐  *************/
/**
 * The main application component that sets up the routes and navigation for the app.
 * It includes:
 * - A Navbar component for navigation.
 * - Route definitions for Home, Login, Register, and Dashboard pages.
 * - PrivateRoute components to protect dashboard routes based on user roles.
 * - The PrestataireDashboard is accessible to users with the 'PRESTATAIRE' role.
 * - The ClientDashboard is accessible to users with the 'CLIENT' role.
 */

/*******  ef815652-a6d9-467b-b3f3-9a0dafd1b92d  *******/import PrestataireMesEvenements from './pages/prestataire/MesEvenements';
import GererDisponibilites from './pages/prestataire/GererDisponibilites';
import VoirPrestataireStatistiques from './pages/prestataire/VoirStatistiques';
// import AdminDashboard from './pages/admin/Dashboard';

// --- Component Imports ---
import Navbar from './components/Navbar';
import Footer from './components/Footer';

// --- Protected Route Component ---
const ProtectedRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        return <Navigate to="/login" replace />;
    }
    if (allowedRoles && !allowedRoles.includes(userRole)) {
        return <Navigate to="/" replace />; // Redirige vers l'accueil si mauvais rôle
    }
    return <Outlet />;
};

// --- Main App Component ---
function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));

    useEffect(() => {
        const checkAuthStatus = () => {
            const currentAuth = !!localStorage.getItem('authToken');
            setIsAuthenticated(prev => prev === currentAuth ? prev : currentAuth);
        };
        window.addEventListener('storage', checkAuthStatus);
        window.addEventListener('focus', checkAuthStatus);
        checkAuthStatus();
        return () => {
            window.removeEventListener('storage', checkAuthStatus);
            window.removeEventListener('focus', checkAuthStatus);
        };
    }, []);

    return (
        <Router>
            <div className="flex flex-col min-h-screen">
                <Navbar />
                <main className="flex-grow">
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/" element={isAuthenticated ? <HomePage /> : <Navigate to="/login" replace />} />

                        <Route element={<ProtectedRoute allowedRoles={['CLIENT']} />}>
                            <Route path="/client/dashboard" element={<ClientDashboard />} />
                        </Route>

                        <Route element={<ProtectedRoute allowedRoles={['PRESTATAIRE']} />}>
                            <Route path="/prestataire/dashboard" element={<PrestataireDashboard />} />
                            <Route path="/prestataire/services" element={<ManagePrestataireServices />} />
                            <Route path="/prestataire/mes-evenements" element={<PrestataireMesEvenements />} />
                            <Route path="/prestataire/disponibilites" element={<GererDisponibilites />} />
                            <Route path="/prestataire/statistiques" element={<VoirPrestataireStatistiques />} />
                        </Route>

                        {/* <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                            <Route path="/admin/dashboard" element={<AdminDashboard />} />
                        </Route> */}
                        <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}
export default App;