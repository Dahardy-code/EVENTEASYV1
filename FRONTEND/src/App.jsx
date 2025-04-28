import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';

// Importez vos pages/composants
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ClientDashboard from './pages/client/Dashboard';
// Importez d'autres pages (Admin, Prestataire, Accueil, etc.)
// import HomePage from './pages/HomePage'; // Exemple
// import AdminDashboard from './pages/admin/Dashboard';
// import PrestataireDashboard from './pages/prestataire/Dashboard';
import Navbar from './components/Navbar'; // Supposons que vous avez une Navbar
import Footer from './components/Footer'; // Supposons que vous avez un Footer

// Composant pour protéger les routes nécessitant une authentification
const ProtectedRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole'); // Assurez-vous de stocker le rôle au login

    if (!token) {
        // Pas de token, rediriger vers login
        return <Navigate to="/login" replace />;
    }

    if (allowedRoles && !allowedRoles.includes(userRole)) {
         // Rôle non autorisé, rediriger vers une page non autorisée ou l'accueil
         console.warn(`Accès non autorisé pour le rôle ${userRole} à cette route.`);
         // Pourrait rediriger vers une page d'erreur ou le dashboard par défaut de l'utilisateur
         // Ici, on redirige vers login pour la simplicité, mais ce n'est pas idéal
         return <Navigate to="/login" replace />; // Ou vers '/' ou une page '/unauthorized'
    }

    // Token existe et rôle autorisé (ou pas de rôle spécifique requis)
    return <Outlet />; // Rend le composant enfant (la page protégée)
};

function App() {
    return (
        <Router>
            <Navbar /> {/* Affiche la Navbar sur toutes les pages */}
            <div className="main-content"> {/* Pour le style si besoin */}
                <Routes>
                    {/* Routes Publiques */}
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    {/* <Route path="/" element={<HomePage />} /> */} {/* Page d'accueil */}

                    {/* Routes Protégées pour Client */}
                    <Route element={<ProtectedRoute allowedRoles={['CLIENT']} />}>
                        <Route path="/client/dashboard" element={<ClientDashboard />} />
                        {/* Ajoutez d'autres routes client ici */}
                        {/* <Route path="/client/reservations" element={<ClientReservations />} /> */}
                    </Route>

                    {/* Routes Protégées pour Prestataire (Exemple) */}
                    {/* <Route element={<ProtectedRoute allowedRoles={['PRESTATAIRE']} />}>
                        <Route path="/prestataire/dashboard" element={<PrestataireDashboard />} />
                        <Route path="/prestataire/services" element={<PrestataireServices />} />
                    </Route> */}

                    {/* Routes Protégées pour Admin (Exemple) */}
                    {/* <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                        <Route path="/admin/dashboard" element={<AdminDashboard />} />
                    </Route> */}

                    {/* Route par défaut ou page 404 */}
                    <Route path="*" element={<Navigate to="/" replace />} /> {/* Ou une page 404 */}
                </Routes>
            </div>
            <Footer /> {/* Affiche le Footer sur toutes les pages */}
        </Router>
    );
}

export default App;