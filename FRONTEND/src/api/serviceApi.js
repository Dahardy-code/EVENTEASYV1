// src/api/serviceApi.js
export const getReservations = async () => {
    return [
      { id: 1, eventName: 'Mariage' },
      { id: 2, eventName: 'Conférence' },
      // Ajoute plus de données simulées
    ];
  };
  
  export const getEvents = async () => {
    return [
      { id: 1, name: 'Mariage' },
      { id: 2, name: 'Conférence' },
      // Ajoute plus de données simulées
    ];
  };
  
  export const getStatistics = async () => {
    return {
      totalReservations: 10,
      completedEvents: 5,
      // Ajoute d'autres statistiques simulées
    };
  };
  