#!/bin/bash

echo "Starting automated fixes for eventeasyv1-frontend..."
echo "IMPORTANT: Ensure you have backed up your project before proceeding."
echo "-----------------------------------------------------"

# --- Detect sed -i behavior ---
SED_INPLACE='sed -i'
if sed -i.bak 's/test/test/' <<< "test string" >/dev/null 2>&1; then
  # BSD/macOS sed requires an extension for -i
  SED_INPLACE='sed -i .bak'
  echo "Detected BSD/macOS sed. Using backup files (.bak)."
else
  # GNU sed
  echo "Detected GNU sed."
fi
# --- End sed detection ---


# Issue 1: Fix Conflicting Tailwind Setup in index.html
echo "[Fix 1/7] Removing conflicting tailwind.js script from index.html..."
if [ -f "./index.html" ]; then
  $SED_INPLACE '/<script src="\/src\/assets\/js\/tailwind.js"><\/script>/d' ./index.html
  echo "  Done."
else
  echo "  Warning: ./index.html not found. Skipping."
fi
echo "-----------------------------------------------------"


# Issue 2: Fix API Authentication in statistiqueApi.js
echo "[Fix 2/7] Updating statistiqueApi.js to use authenticated apiClient..."
STAT_API_FILE="./src/api/statistiqueApi.js"
if [ -f "$STAT_API_FILE" ]; then
  # Check if import already exists
  if ! grep -q "import { apiClient }" "$STAT_API_FILE"; then
    # Add import after the axios import (using \x27 for single quotes)
    $SED_INPLACE "/import axios from \x27axios\x27;/a import { apiClient } from \x27.\/authApi\x27;" "$STAT_API_FILE"
    echo "  Added apiClient import."
  else
    echo "  apiClient import already exists."
  fi

  # Replace axios.get call (using | as delimiter and \x27 for quotes)
  # Make sure it only replaces the specific line
  $SED_INPLACE "s|const response = await axios.get('/api/statistiques/admin');|const response = await apiClient.get('/statistiques/admin');|" "$STAT_API_FILE"
  echo "  Replaced axios.get with apiClient.get."
  echo "  Done."
else
  echo "  Warning: $STAT_API_FILE not found. Skipping."
fi
echo "-----------------------------------------------------"


# Issue 3: Remove potentially incompatible ESLint Config from package.json
echo "[Fix 3/7] Removing 'eslintConfig' block from package.json..."
echo "  WARNING: Modifying JSON with sed is risky. Please review package.json carefully afterwards."
PACKAGE_JSON="./package.json"
if [ -f "$PACKAGE_JSON" ]; then
    # Attempt to remove the eslintConfig block using sed
    # This looks for "eslintConfig": { start and the next occurrence of }, or }, at the start of a line
    $SED_INPLACE '/"eslintConfig": {/,/^\s*},?$/d' "$PACKAGE_JSON"
    echo "  Removed eslintConfig block (if found). Please set up ESLint for Vite manually (e.g., 'npm init @eslint/config')."
else
  echo "  Warning: $PACKAGE_JSON not found. Skipping."
fi
echo "-----------------------------------------------------"


# Issue 4: Remove potentially unnecessary autoprefixer
echo "[Fix 4/7] Uninstalling autoprefixer..."
if [ -f "$PACKAGE_JSON" ]; then
    npm uninstall autoprefixer --save-dev
    echo "  Done. 'npm install' will run at the end."
else
    echo "  Warning: $PACKAGE_JSON not found. Skipping autoprefixer uninstall."
fi
echo "-----------------------------------------------------"


# Issue 5: Replace Hardcoded API URLs and create .env
echo "[Fix 5/7] Replacing hardcoded API URLs in authApi.js with environment variable..."
AUTH_API_FILE="./src/api/authApi.js"
if [ -f "$AUTH_API_FILE" ]; then
    # Replace API URL for auth (more specific)
    $SED_INPLACE "s|baseURL: 'http://localhost:8080/api/auth'|baseURL: \`\${import.meta.env.VITE_API_BASE_URL}/auth\`|" "$AUTH_API_FILE"
    # Replace base API URL for other calls
    $SED_INPLACE "s|baseURL: 'http://localhost:8080/api'|baseURL: import.meta.env.VITE_API_BASE_URL|" "$AUTH_API_FILE"
    echo "  Replaced URLs in $AUTH_API_FILE."

    # Create .env file if it doesn't exist
    if [ ! -f ".env" ]; then
        echo "Creating a basic .env file..."
        # Note: Using /api suffix here as the second replacement removed it
        echo "VITE_API_BASE_URL=http://localhost:8080/api" > .env
        echo "  Created .env with VITE_API_BASE_URL."
        echo "  => IMPORTANT: Verify the VITE_API_BASE_URL in .env is correct for your backend!"
    else
        echo "  .env file already exists. Please ensure VITE_API_BASE_URL is defined correctly."
    fi
    echo "  Done."
else
  echo "  Warning: $AUTH_API_FILE not found. Skipping URL replacement."
fi
echo "-----------------------------------------------------"


# Issue 6: Comment out Full Page Reload on Auth Error
echo "[Fix 6/7] Commenting out window.location redirect in authApi.js..."
if [ -f "$AUTH_API_FILE" ]; then
    # Replace the line with a comment and TODO
    # Using | delimiter and \x27 for single quotes
    $SED_INPLACE "s|window.location.href = '/login';|// TODO: Handle navigation in the calling component based on the error - window.location.href = '/login';|" "$AUTH_API_FILE"
    echo "  Commented out redirect and added a TODO."
    echo "  Done."
else
    echo "  Warning: $AUTH_API_FILE not found. Skipping redirect fix."
fi
echo "-----------------------------------------------------"


# Issue 7: Remove Duplicate PrivateRoute Component
echo "[Fix 7/7] Removing unused PrivateRoute.jsx..."
PRIVATE_ROUTE_FILE="./src/components/PrivateRoute.jsx"
if [ -f "$PRIVATE_ROUTE_FILE" ]; then
    rm -f "$PRIVATE_ROUTE_FILE"
    echo "  Removed $PRIVATE_ROUTE_FILE."
    echo "  Done."
else
  echo "  Warning: $PRIVATE_ROUTE_FILE not found or already removed. Skipping."
fi
echo "-----------------------------------------------------"


# Final Steps
echo "Running 'npm install' to update dependencies and package-lock.json..."
npm install
echo "-----------------------------------------------------"

# Clean up backup files created by sed on macOS/BSD
if [ "$SED_INPLACE" = "sed -i .bak" ]; then
    echo "Cleaning up .bak files..."
    find . -name '*.bak' -type f -delete
    echo "  Done."
fi
echo "-----------------------------------------------------"

echo "Automated fixes complete!"
echo "Please review the changes made to the files."
echo "Manual Steps Recommended:"
echo "1. Verify the VITE_API_BASE_URL in the '.env' file points to your correct backend API."
echo "2. Set up ESLint for your Vite project (e.g., run 'npm init @eslint/config' and configure it)."
echo "3. Review the commented-out redirect in 'src/api/authApi.js' and implement proper navigation handling in your components."
echo "4. Test your application thoroughly."
echo "-----------------------------------------------------"