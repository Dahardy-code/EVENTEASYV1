import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ children, role, requiredRole }) => {
  if (!role) {
    // Si aucun rôle n'est défini (utilisateur non connecté)
    return <Navigate to="/login" replace />;
  }

  if (role !== requiredRole) {
    // Si le rôle ne correspond pas
    return <Navigate to="/login" replace />;
  }

  // Si l'accès est autorisé
  return children;
};

export default PrivateRoute;