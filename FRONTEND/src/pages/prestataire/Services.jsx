// src/pages/prestataire/Services.jsx
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getMyServices, addMyService, deleteMyService /*, updateMyService */ } from '../../api/prestataireApi';

const ManagePrestataireServices = () => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(''); // Erreur générale
    const [formError, setFormError] = useState(''); // Erreur du formulaire
    const [showForm, setShowForm] = useState(false);
    // État pour le formulaire d'ajout/modification de service
    const [currentService, setCurrentService] = useState({ id: null, titre: '', description: '', prix: '', categorie: '' });
    const navigate = useNavigate();

    const fetchServices = async () => {
        setLoading(true); setError('');
        try {
            const data = await getMyServices();
            setServices(data || []);
        } catch (err) {
            setError(err.response?.data?.message || err.message || "Impossible de charger vos services.");
            if (err.response?.status === 401 || err.response?.status === 403) navigate('/login');
        } finally { setLoading(false); }
    };

    useEffect(() => { fetchServices(); }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCurrentService(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmitService = async (e) => {
        e.preventDefault();
        setFormError(''); // Réinitialiser l'erreur du formulaire
        // TODO: Ajouter une validation plus robuste des champs
        if (!currentService.titre || !currentService.categorie || !currentService.prix) {
            setFormError("Le titre, la catégorie et le prix sont obligatoires.");
            return;
        }
        try {
            setLoading(true); // Indiquer le chargement pendant l'appel API
            if (currentService.id) {
                // TODO: Implémenter updateMyService dans prestataireApi.js et l'appeler ici
                // await updateMyService(currentService.id, currentService);
                // alert("Service mis à jour avec succès !");
                console.warn("Logique de mise à jour non implémentée.");
                alert("Mise à jour simulée !");
            } else {
                await addMyService(currentService);
                alert("Service ajouté avec succès !");
            }
            setShowForm(false);
            setCurrentService({ id: null, titre: '', description: '', prix: '', categorie: '' }); // Reset form
            fetchServices(); // Refresh list
        } catch (err) {
            setFormError(err.response?.data?.message || err.message || "Erreur lors de l'enregistrement du service.");
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteService = async (serviceId) => {
        if (window.confirm("Êtes-vous sûr de vouloir supprimer ce service ? Cette action est irréversible.")) {
            try {
                setLoading(true); // Indiquer le chargement
                await deleteMyService(serviceId);
                setServices(prev => prev.filter(s => s.id !== serviceId));
                alert("Service supprimé avec succès.");
            } catch (err) {
                setError(err.response?.data?.message || err.message || "Erreur lors de la suppression du service.");
            } finally {
                setLoading(false);
            }
        }
    };

    const handleEditService = (service) => {
        setCurrentService(service);
        setShowForm(true);
        setFormError('');
    };

    const handleCancelForm = () => {
        setShowForm(false);
        setCurrentService({ id: null, titre: '', description: '', prix: '', categorie: '' });
        setFormError('');
    }

    if (loading && services.length === 0) return <div className="text-center p-10 text-gray-600">Chargement de vos services...</div>;


    return (
        <div className="container mx-auto p-4 md:p-8">
            <div className="flex justify-between items-center mb-6 pb-4 border-b">
                <h1 className="text-3xl font-semibold text-gray-800">Mes Services / Offres</h1>
                <button onClick={() => { handleEditService({ id: null, titre: '', description: '', prix: '', categorie: '' }); }} className="bg-green-500 hover:bg-green-600 text-white font-medium py-2 px-4 rounded-md shadow">
                    Ajouter un Nouveau Service
                </button>
            </div>

            {/* Afficher l'erreur générale si elle existe et que le formulaire n'est pas montré */}
            {error && !showForm && <div className="mb-4 text-center p-3 text-red-700 bg-red-100 rounded border border-red-300">{error}</div>}


            {showForm && (
                <form onSubmit={handleSubmitService} className="mb-8 p-6 bg-gray-50 rounded-lg shadow border">
                    <h2 className="text-xl font-semibold mb-4">{currentService.id ? "Modifier le Service" : "Ajouter un Nouveau Service"}</h2>
                    {formError && <p className="text-red-600 text-sm mb-3 bg-red-50 p-2 rounded">{formError}</p>}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label htmlFor="titre" className="block text-sm font-medium text-gray-700">Titre <span className="text-red-500">*</span></label>
                            <input type="text" name="titre" id="titre" value={currentService.titre} onChange={handleInputChange} required className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 focus:ring-indigo-500 focus:border-indigo-500"/>
                        </div>
                        <div>
                            <label htmlFor="categorie" className="block text-sm font-medium text-gray-700">Catégorie <span className="text-red-500">*</span></label>
                            <input type="text" name="categorie" id="categorie" value={currentService.categorie} onChange={handleInputChange} required placeholder="Ex: Traiteur, Photographe" className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 focus:ring-indigo-500 focus:border-indigo-500"/>
                        </div>
                        <div>
                            <label htmlFor="prix" className="block text-sm font-medium text-gray-700">Prix (€) <span className="text-red-500">*</span></label>
                            <input type="number" name="prix" id="prix" value={currentService.prix} onChange={handleInputChange} required min="0.01" step="0.01" className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 focus:ring-indigo-500 focus:border-indigo-500"/>
                        </div>
                         <div className="md:col-span-2">
                            <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description (détaillée)</label>
                            <textarea name="description" id="description" value={currentService.description} onChange={handleInputChange} rows="4" className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 focus:ring-indigo-500 focus:border-indigo-500"></textarea>
                        </div>
                    </div>
                    <div className="mt-6 flex justify-end space-x-3">
                         <button type="button" onClick={handleCancelForm} className="py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">Annuler</button>
                        <button type="submit" disabled={loading} className="bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded-md shadow disabled:opacity-50">
                            {loading ? "Enregistrement..." : (currentService.id ? "Mettre à jour" : "Enregistrer le Service")}
                        </button>
                    </div>
                </form>
            )}

            {services.length === 0 && !loading && !showForm && (
                <p className="text-gray-500 italic text-center py-5">Vous n'avez aucun service enregistré. Cliquez sur "Ajouter un Nouveau Service" pour commencer.</p>
            )}

            {services.length > 0 && (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {services.map(service => (
                        <div key={service.id} className="bg-white p-5 rounded-lg shadow border flex flex-col justify-between">
                            <div>
                                <h2 className="text-xl font-semibold text-indigo-700 mb-2">{service.titre}</h2>
                                <p className="text-gray-600 text-sm">Catégorie: {service.categorie}</p>
                                <p className="text-gray-800 font-bold text-lg my-2">{service.prix} €</p>
                                <p className="text-gray-500 text-xs line-clamp-3 mb-3">{service.description}</p>
                            </div>
                            <div className="flex justify-end space-x-2 mt-auto pt-3 border-t">
                                <button onClick={() => handleEditService(service)} className="text-xs bg-blue-100 text-blue-700 hover:bg-blue-200 px-3 py-1 rounded">Modifier</button>
                                <button onClick={() => handleDeleteService(service.id)} disabled={loading} className="text-xs bg-red-100 text-red-700 hover:bg-red-200 px-3 py-1 rounded disabled:opacity-50">Supprimer</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
            <div className="mt-10">
                <Link to="/prestataire/dashboard" className="text-indigo-600 hover:text-indigo-800 font-medium">
                    ← Retour au Tableau de Bord
                </Link>
            </div>
        </div>
    );
};

export default ManagePrestataireServices;