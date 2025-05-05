import React from 'react';

const Footer = () => {
  return (
    <footer style={{ marginTop: '2rem', padding: '1rem', borderTop: '1px solid #ccc', textAlign: 'center' }}>
      <p>© {new Date().getFullYear()} Eventeasy. Tous droits réservés.</p>
      {/* Add more footer content here if needed */}
    </footer>
  );
};

export default Footer; // <-- Add this default export line!
