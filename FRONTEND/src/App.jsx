// src/App.jsx
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';

// --- Page Imports ---
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ClientDashboard from './pages/client/Dashboard';
import HomePage from './pages/HomePage';
// *** DÉCOMMENTER L'IMPORT ***
import PrestataireDashboard from './pages/prestataire/Dashboard';
// import AdminDashboard from './pages/admin/Dashboard'; // Décommentez quand prêt

// --- Component Imports ---
import Navbar from './components/Navbar';
import Footer from './components/Footer';

// --- Protected Route Component (Identique) ---
const ProtectedRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        console.log("ProtectedRoute: No token, redirecting to /login");
        return <Navigate to="/login" replace />;
    }
    if (allowedRoles && !allowedRoles.includes(userRole)) {
        console.warn(`ProtectedRoute: Role mismatch. User: ${userRole}, Allowed: ${allowedRoles}. Redirecting to /`);
        return <Navigate to="/" replace />;
    }
    console.log(`ProtectedRoute: Access granted for role ${userRole} to routes needing ${allowedRoles || 'any role'}`);
    return <Outlet />;
};


// --- Main App Component ---
function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));

    useEffect(() => {
        const checkAuthStatus = () => {
            const currentAuth = !!localStorage.getItem('authToken');
            // Mettre à jour seulement si l'état change réellement
            setIsAuthenticated(prev => prev === currentAuth ? prev : currentAuth);
        };
        window.addEventListener('storage', checkAuthStatus);
        window.addEventListener('focus', checkAuthStatus);
        checkAuthStatus();
        return () => {
            window.removeEventListener('storage', checkAuthStatus);
            window.removeEventListener('focus', checkAuthStatus);
        };
    }, []); // Tableau vide OK ici

    return (
        <Router>
            <div className="flex flex-col min-h-screen">
                <Navbar />
                <main className="flex-grow">
                    <Routes>
                        {/* --- Public Routes --- */}
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />

                        {/* --- Root Route --- */}
                        <Route
                            path="/"
                            element={isAuthenticated ? <HomePage /> : <Navigate to="/login" replace />}
                        />

                        {/* --- Protected Routes --- */}
                        {/* CLIENT */}
                        <Route element={<ProtectedRoute allowedRoles={['CLIENT']} />}>
                            <Route path="/client/dashboard" element={<ClientDashboard />} />
                            {/* Autres routes CLIENT */}
                        </Route>

                        {/* PRESTATAIRE */}
                        <Route element={<ProtectedRoute allowedRoles={['PRESTATAIRE']} />}>
                            {/* *** DÉCOMMENTER LA ROUTE *** */}
                            <Route path="/prestataire/dashboard" element={<PrestataireDashboard />} />
                            {/* TODO: Ajoutez ici les autres routes spécifiques au prestataire */}
                            {/* <Route path="/prestataire/services" element={<ManageServicesPage />} /> */}
                        </Route>

                        {/* ADMIN */}
                        {/* <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}> */}
                            {/* <Route path="/admin/dashboard" element={<AdminDashboard />} /> */}
                            {/* Autres routes ADMIN */}
                        {/* </Route> */}

                        {/* --- Fallback Route --- */}
                        <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;