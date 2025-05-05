// src/components/Chatbot.jsx
import React, { useState } from 'react';

// Placeholder for a simple Chatbot widget
const Chatbot = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleChat = () => setIsOpen(!isOpen);

  return (
    <div className="fixed bottom-5 right-5 z-50">
      {/* Chat window (conditionally rendered) */}
      {isOpen && (
        <div className="w-80 h-96 bg-white rounded-lg shadow-xl border border-gray-200 flex flex-col mb-2 transition-all duration-300 ease-out origin-bottom-right transform scale-100 opacity-100">
          {/* Header */}
          <div className="bg-indigo-600 text-white p-3 rounded-t-lg flex justify-between items-center">
            <h3 className="font-semibold text-sm">Assistance Eventeasy</h3>
            <button onClick={toggleChat} className="text-indigo-100 hover:text-white text-xl leading-none">×</button>
          </div>
          {/* Messages Area */}
          <div className="flex-grow p-4 overflow-y-auto text-sm text-gray-700 space-y-2">
            <p className="bg-gray-100 p-2 rounded-lg max-w-[80%]">Bonjour ! Comment puis-je vous aider aujourd'hui ? (Placeholder)</p>
            {/* TODO: Add chat message rendering logic */}
          </div>
          {/* Input Area */}
          <div className="p-2 border-t">
            <input
              type="text"
              placeholder="Posez votre question..."
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-indigo-500 text-sm"
              // TODO: Add state and handler for input
            />
          </div>
        </div>
      )}

       {/* Chat Toggle Button */}
      <button
        onClick={toggleChat}
        className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-full p-3 shadow-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-transform hover:scale-110"
        aria-label={isOpen ? "Fermer le chatbot" : "Ouvrir le chatbot"}
      >
        {/* Placeholder Icon (replace with actual SVG) */}
        {isOpen ? (
             <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path></svg>
        ) : (
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
        )}
      </button>
    </div>
  );
};

export default Chatbot;