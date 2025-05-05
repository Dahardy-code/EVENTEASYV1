// src/App.jsx
import React, { useState, useEffect } from 'react'; // Added useState, useEffect
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';

// --- Page Imports ---
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ClientDashboard from './pages/client/Dashboard';
import HomePage from './pages/HomePage'; // <-- Import the new Home Page
// Import other dashboards when you create them
// import PrestataireDashboard from './pages/prestataire/Dashboard';
// import AdminDashboard from './pages/admin/Dashboard';

// --- Component Imports ---
import Navbar from './components/Navbar';
import Footer from './components/Footer';

// --- Protected Route Component ---
// This component checks for authentication and role before rendering nested routes
const ProtectedRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        // If no token, redirect to login page immediately
        console.log("ProtectedRoute: No token, redirecting to /login");
        return <Navigate to="/login" replace />;
    }

    if (allowedRoles && !allowedRoles.includes(userRole)) {
        // If token exists but role doesn't match, redirect to home page
        // (User is logged in, just not authorized for *this* specific page)
        console.warn(`ProtectedRoute: Role mismatch. User: ${userRole}, Allowed: ${allowedRoles}. Redirecting to /`);
        return <Navigate to="/" replace />; // Redirect to home page
    }

    // Token exists and role is allowed (or no specific roles required)
    // Render the component specified in the nested Route (e.g., <ClientDashboard />)
    console.log(`ProtectedRoute: Access granted for role ${userRole} to routes needing ${allowedRoles || 'any role'}`);
    return <Outlet />; // Renders the nested child route component
};


// --- Main App Component ---
function App() {
    // State to track authentication status for conditional rendering of '/'
    // Initialize from localStorage for initial load
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));

    // Effect to update authentication state if localStorage changes (e.g., login/logout in another tab)
    // This helps keep the root route '/' logic more responsive
    useEffect(() => {
        const checkAuthStatus = () => {
            setIsAuthenticated(!!localStorage.getItem('authToken'));
        };

        window.addEventListener('storage', checkAuthStatus); // Listen for changes from other tabs
        window.addEventListener('focus', checkAuthStatus);   // Re-check when tab gains focus

        // Initial check in case localStorage changed before listener attached
        checkAuthStatus();

        // Cleanup listener on component unmount
        return () => {
            window.removeEventListener('storage', checkAuthStatus);
            window.removeEventListener('focus', checkAuthStatus);
        };
    }, []); // Empty dependency array ensures this runs only once on mount/unmount

    return (
        <Router>
            {/* Use Flexbox to ensure Footer stays at the bottom */}
            <div className="flex flex-col min-h-screen">
                <Navbar />

                {/* Main content area that grows */}
                <main className="flex-grow">
                    {/* Optional: Add a general container for padding etc. if desired */}
                    {/* <div className="container mx-auto px-4 py-6"> */}
                        <Routes>
                            {/* --- Public Routes --- */}
                            {/* Login and Register are always accessible */}
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />

                            {/* --- Root Route (Conditional Home/Login) --- */}
                            <Route
                                path="/"
                                element={
                                    isAuthenticated ? (
                                        <HomePage /> // Show HomePage if authenticated
                                    ) : (
                                        <Navigate to="/login" replace /> // Redirect to Login if not
                                    )
                                }
                            />
                             {/* Optional: Add more public routes here if needed (e.g., /about, /contact) */}
                             {/* <Route path="/about" element={<AboutPage />} /> */}


                            {/* --- Protected Routes --- */}
                            {/* Routes requiring CLIENT role */}
                            <Route element={<ProtectedRoute allowedRoles={['CLIENT']} />}>
                                <Route path="/client/dashboard" element={<ClientDashboard />} />
                                {/* TODO: Add other client routes here, e.g.: */}
                                {/* <Route path="/client/reservations" element={<ClientReservationsPage />} /> */}
                                {/* <Route path="/client/profile" element={<ClientProfilePage />} /> */}
                                {/* <Route path="/browse-events" element={<BrowseEventsPage />} /> */} {/* If browsing requires login */}

                            </Route>

                            {/* Routes requiring PRESTATAIRE role */}
                            <Route element={<ProtectedRoute allowedRoles={['PRESTATAIRE']} />}>
                                {/* TODO: Create and import PrestataireDashboard */}
                                {/* <Route path="/prestataire/dashboard" element={<PrestataireDashboard />} /> */}
                                {/* TODO: Add other prestataire routes here */}
                            </Route>

                            {/* Routes requiring ADMIN role */}
                            <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                                {/* TODO: Create and import AdminDashboard */}
                                {/* <Route path="/admin/dashboard" element={<AdminDashboard />} /> */}
                                {/* TODO: Add other admin routes here */}
                            </Route>

                            {/* --- Fallback Route --- */}
                            {/* Redirect any unmatched routes to the home page */}
                            <Route path="*" element={<Navigate to="/" replace />} />

                        </Routes>
                    {/* </div> */}
                </main>

                <Footer />
            </div>
        </Router>
    );
}

export default App;