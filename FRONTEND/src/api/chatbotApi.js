export const getChatbotResponse = async (message) => {
    try {
        const response = await axios.post('/api/chatbot', { message });
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la récupération de la réponse du chatbot:", error);
        return null;
    }
};
