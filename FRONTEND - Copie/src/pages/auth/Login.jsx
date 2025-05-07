import React, { useState, useContext } from 'react'; // Supposons un AuthContext
import { useNavigate } from 'react-router-dom';
import { login } from '../../api/authApi'; // Importez la fonction login
// import { AuthContext } from '../../context/AuthContext'; // Si vous utilisez un Context

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    // const { login: contextLogin } = useContext(AuthContext); // Exemple avec Context

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(''); // Réinitialiser l'erreur

        try {
            const response = await login({ email, password });
            console.log('Connexion réussie:', response);

            // Mettre à jour l'état global si vous utilisez Context/Redux
            // contextLogin(response);

            // Redirection basée sur le rôle (simple exemple)
            if (response.role === 'CLIENT') {
                navigate('/client/dashboard'); // Rediriger vers le dashboard client
            } else if (response.role === 'PRESTATAIRE') {
                navigate('/prestataire/dashboard'); // Adaptez si nécessaire
            } else if (response.role === 'ADMIN') {
                 navigate('/admin/dashboard'); // Adaptez si nécessaire
            } else {
                navigate('/'); // Redirection par défaut
            }

        } catch (err) {
            console.error("Erreur lors de la connexion:", err);
            setError(err.message || 'Échec de la connexion. Vérifiez vos identifiants.');
        }
    };

    return (
        <div>
            <h2>Connexion</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Mot de passe:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <button type="submit">Se connecter</button>
            </form>
             <p>Pas encore de compte ? <a href="/register">Inscrivez-vous</a></p>
        </div>
    );
}

export default Login;