import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerClient, registerPrestataire } from '../../api/authApi';

function Register() {
    const [nom, setNom] = useState('');
    const [prenom, setPrenom] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('CLIENT');
    const [nomEntreprise, setNomEntreprise] = useState('');
    const [categorieService, setCategorieService] = useState('');
    const [adresse, setAdresse] = useState('');
    const [numeroTel, setNumeroTel] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        if (password !== confirmPassword) {
            setError("Les mots de passe ne correspondent pas.");
            setLoading(false);
            return;
        }

        if (password.length < 6) {
            setError("Le mot de passe doit contenir au moins 6 caractères.");
            setLoading(false);
            return;
        }

        let userData = {
            nom,
            prenom,
            email,
            password,
            role
        };

        try {
            let response;

            if (role === 'CLIENT') {
                response = await registerClient(userData);
            } else {
                if (!nomEntreprise || !categorieService) {
                    setError("Le nom de l'entreprise et la catégorie sont requis.");
                    setLoading(false);
                    return;
                }

                response = await registerPrestataire({
                    ...userData,
                    nomEntreprise,
                    categorieService,
                    adresse,
                    numeroTel
                });
            }

            if (response && response.role) {
                if (response.role === 'CLIENT') navigate('/client/dashboard');
                else if (response.role === 'PRESTATAIRE') navigate('/prestataire/dashboard');
                else if (response.role === 'ADMIN') navigate('/admin/dashboard');
                else navigate('/');
            } else {
                setError("Inscription réussie, mais redirection inconnue.");
            }
        } catch (err) {
            setError(err?.message || "Erreur d'inscription.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            className="min-h-screen flex items-center justify-center relative bg-cover bg-center"
            style={{ backgroundImage: 'url(/images/loginpic.jpg)' }}
        >
            <div className="absolute inset-0 bg-black opacity-50"></div>
            <div className="max-w-2xl w-full space-y-6 bg-white p-10 rounded-xl shadow-lg relative z-10">
                <h2 className="text-center text-3xl font-extrabold text-gray-900">Créez un compte Eventeasy</h2>
                <p className="text-center text-sm text-gray-600">
                    Ou{' '}
                    <Link to="/login" className="font-medium text-indigo-600 hover:text-indigo-500">
                        connectez-vous
                    </Link>
                </p>

                <form className="space-y-4" onSubmit={handleSubmit}>
                    <div className="flex space-x-4">
                        <label className="flex items-center">
                            <input
                                type="radio"
                                value="CLIENT"
                                checked={role === 'CLIENT'}
                                onChange={(e) => setRole(e.target.value)}
                                className="mr-2"
                            />
                            Client
                        </label>
                        <label className="flex items-center">
                            <input
                                type="radio"
                                value="PRESTATAIRE"
                                checked={role === 'PRESTATAIRE'}
                                onChange={(e) => setRole(e.target.value)}
                                className="mr-2"
                            />
                            Prestataire
                        </label>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <input
                            type="text"
                            placeholder="Nom"
                            value={nom}
                            onChange={(e) => setNom(e.target.value)}
                            required
                            className="border p-2 rounded w-full"
                        />
                        <input
                            type="text"
                            placeholder="Prénom"
                            value={prenom}
                            onChange={(e) => setPrenom(e.target.value)}
                            required
                            className="border p-2 rounded w-full"
                        />
                    </div>

                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="border p-2 rounded w-full"
                    />

                    <input
                        type="password"
                        placeholder="Mot de passe"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="border p-2 rounded w-full"
                    />

                    <input
                        type="password"
                        placeholder="Confirmer le mot de passe"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                        className="border p-2 rounded w-full"
                    />

                    {role === 'PRESTATAIRE' && (
                        <>
                            <input
                                type="text"
                                placeholder="Nom de l'entreprise *"
                                value={nomEntreprise}
                                onChange={(e) => setNomEntreprise(e.target.value)}
                                required
                                className="border p-2 rounded w-full"
                            />
                            <input
                                type="text"
                                placeholder="Catégorie de service *"
                                value={categorieService}
                                onChange={(e) => setCategorieService(e.target.value)}
                                required
                                className="border p-2 rounded w-full"
                            />
                            <input
                                type="text"
                                placeholder="Adresse (facultatif)"
                                value={adresse}
                                onChange={(e) => setAdresse(e.target.value)}
                                className="border p-2 rounded w-full"
                            />
                            <input
                                type="tel"
                                placeholder="Numéro de téléphone (facultatif)"
                                value={numeroTel}
                                onChange={(e) => setNumeroTel(e.target.value)}
                                className="border p-2 rounded w-full"
                            />
                        </>
                    )}

                    {error && (
                        <div className="text-red-600 text-sm text-center">{error}</div>
                    )}

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full py-2 px-4 bg-indigo-600 text-white font-semibold rounded hover:bg-indigo-700 transition"
                    >
                        {loading ? 'Inscription en cours...' : "S'inscrire"}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Register;
