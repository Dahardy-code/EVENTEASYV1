// App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';

import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ClientDashboard from './pages/client/Dashboard';
import Navbar from './components/Navbar'; 
import Footer from './components/Footer'; 

const ProtectedRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        return <Navigate to="/login" replace />;
    }

    if (allowedRoles && !allowedRoles.includes(userRole)) {
        console.warn(`Accès non autorisé pour le rôle ${userRole} à cette route.`);
        return <Navigate to="/login" replace />;
    }

    return <Outlet />;
};

function App() {
    return (
        <Router>
            <Navbar />
            <div className="main-content">
                <Routes>
                    {/* Routes publiques */}
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />

                    {/* Routes protégées */}
                    <Route element={<ProtectedRoute allowedRoles={['CLIENT']} />}>
                        <Route path="/client/dashboard" element={<ClientDashboard />} />
                    </Route>

                    {/* Redirection par défaut */}
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </div>
            <Footer />
        </Router>
    );
}

export default App;
