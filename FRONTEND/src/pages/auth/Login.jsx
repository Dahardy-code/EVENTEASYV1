import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../../api/authApi';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        if (password.length < 6) { setError("Le mot de passe doit contenir au moins 6 caractères."); setLoading(false); return; }

        const userData = { email, password };

        try {
            const response = await login(userData);
            console.log('Connexion réussie, réponse:', response);
            setLoading(false);

            if (response && response.role) {
                console.log(`Login handleSubmit: Redirection vers dashboard pour rôle : ${response.role}`);
                if (response.role === 'CLIENT') {
                    navigate('/client/dashboard');
                } else if (response.role === 'PRESTATAIRE') {
                    navigate('/prestataire/dashboard');
                } else if (response.role === 'ADMIN') {
                    navigate('/admin/dashboard');
                } else {
                    console.warn(`Login handleSubmit: Rôle non reconnu reçu: ${response.role}, Redirection vers /`);
                    navigate('/');
                }
            } else {
                console.error("Login handleSubmit: Réponse de connexion invalide ou rôle manquant:", response);
                setError("Erreur lors de la connexion.");
            }

        } catch (err) {
            console.error("Erreur détaillée lors de la connexion:", err);
            setLoading(false);
            const backendError = typeof err === 'string' ? err : (err.message || err.error || "Une erreur interne est survenue lors de la connexion.");
            setError(backendError);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center relative bg-cover bg-center" style={{ backgroundImage: 'url(/images/loginpic.jpg)' }}>
            <div className="absolute inset-0 bg-black opacity-50"></div> {/* Flou de fond */}
            <div className="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg relative z-10">
                <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">Connectez-vous à Eventeasy</h2>
                <p className="mt-2 text-center text-sm text-gray-600">Ou{' '} <Link to="/register" className="font-medium text-indigo-600 hover:text-indigo-500">créez un compte</Link></p>
                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email:</label>
                        <input type="email" id="email" value={email} autoComplete="email" onChange={(e) => setEmail(e.target.value)} required className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">Mot de passe:</label>
                        <input type="password" id="password" value={password} autoComplete="current-password" onChange={(e) => setPassword(e.target.value)} required className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" disabled={loading} />
                    </div>

                    {error && <div className="text-sm text-red-500 mt-4">{error}</div>}
                    <button type="submit" className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md shadow-sm text-lg font-medium" disabled={loading}>Se connecter</button>
                </form>
            </div>
        </div>
    );
}

export default Login;
