// auth-helper.js - Authentication helper for all protected pages

/**
 * Check if user is authenticated and redirect if not
 */
function checkAuthentication() {
    const isAuthenticated = sessionStorage.getItem('isAuthenticated');
    const currentUser = sessionStorage.getItem('currentUser');
    
    if (isAuthenticated !== 'true' || !currentUser) {
        // Redirect to login page
        window.location.href = 'index.html';
        return false;
    }
    
    return true;
}

/**
 * Get current user from session storage
 */
function getCurrentUser() {
    try {
        const userStr = sessionStorage.getItem('currentUser');
        return userStr ? JSON.parse(userStr) : null;
    } catch (error) {
        console.error('Error parsing current user:', error);
        return null;
    }
}

/**
 * Get session token
 */
function getSessionToken() {
    return sessionStorage.getItem('sessionToken');
}

/**
 * Check if user has a specific permission
 * SPECIAL CASE: If username is "admin", always return true
 */
function hasPermission(permissionName) {
    const user = getCurrentUser();
    if (!user) return false;
    
    // SPECIAL: If username is "admin", grant all permissions
    if (user.username === 'admin') {
        console.log('Admin user detected - granting permission: ' + permissionName);
        return true;
    }
    
    // Check roles for permission
    if (!user.roles) return false;
    
    return user.roles.some(role => {
        switch(permissionName) {
            case 'canAddPatients':
                return role.canAddPatients === true;
            case 'canViewTrials':
                return role.canViewTrials === true;
            case 'canManageTrials':
                return role.canManageTrials === true;
            case 'canViewReports':
                return role.canViewReports === true;
            default:
                return false;
        }
    });
}

/**
 * Check if current user is admin by username
 */
function isAdmin() {
    const user = getCurrentUser();
    return user && user.username === 'admin';
}

/**
 * Add user welcome message to header
 */
function addUserWelcome() {
    const user = getCurrentUser();
    if (!user) return;
    
    const header = document.querySelector('header');
    if (header && !document.getElementById('user-welcome-section')) {
        // Create welcome section
        const welcomeSection = document.createElement('div');
        welcomeSection.id = 'user-welcome-section';
        welcomeSection.style.cssText = 'position: absolute; right: 20px; top: 50%; transform: translateY(-50%); display: flex; align-items: center; gap: 15px;';
        
        const welcomeText = document.createElement('span');
        welcomeText.style.cssText = 'color: white; font-size: 14px;';
        welcomeText.textContent = `Welcome, ${user.firstName}!`;
        
        const logoutBtn = document.createElement('button');
        logoutBtn.textContent = 'Logout';
        logoutBtn.style.cssText = 'background: #f44336; padding: 6px 12px; font-size: 13px; cursor: pointer;';
        logoutBtn.onclick = logout;
        
        welcomeSection.appendChild(welcomeText);
        welcomeSection.appendChild(logoutBtn);
        
        // Make header relative positioned
        header.style.position = 'relative';
        header.appendChild(welcomeSection);
    }
}

/**
 * Logout function
 */
async function logout() {
    const sessionToken = getSessionToken();
    const API_BASE = 'http://localhost:8080/api';
    
    if (sessionToken) {
        try {
            await fetch(`${API_BASE}/auth/logout`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${sessionToken}`,
                    'Content-Type': 'application/json'
                }
            });
        } catch (error) {
            console.error('Logout error:', error);
        }
    }
    
    // Clear session storage
    sessionStorage.clear();
    
    // Redirect to login
    window.location.href = 'index.html';
}

/**
 * Hide navigation items based on permissions
 */
function hideUnauthorizedNavItems() {
    const user = getCurrentUser();
    if (!user) return;
    
    console.log('Checking permissions for user:', user.username);
    console.log('Is admin:', isAdmin());
    console.log('Can add patients:', hasPermission('canAddPatients'));
    
    // If user is admin, show everything
    if (isAdmin()) {
        console.log('Admin user - showing all nav items');
        return; // Admin can see everything
    }
    
    // For non-admin users, check permissions
    if (!hasPermission('canAddPatients')) {
        console.log('User cannot add patients - hiding nav items');
        
        // Hide add patient and modify patient nav items
        const addPatientLink = document.querySelector('nav a[href="add_patient.html"]');
        const modifyPatientLink = document.querySelector('nav a[href="modify_patient.html"]');
        
        if (addPatientLink) {
            addPatientLink.style.display = 'none';
            console.log('Hid add patient link');
        }
        if (modifyPatientLink) {
            modifyPatientLink.style.display = 'none';
            console.log('Hid modify patient link');
        }
    }
}

/**
 * Check if user has permission to access current page
 * Redirects to home if unauthorized
 */
function checkPagePermission() {
    const currentPage = window.location.pathname.split('/').pop();
    const user = getCurrentUser();
    
    if (!user) {
        window.location.href = 'index.html';
        return false;
    }
    
    // Admin can access everything
    if (isAdmin()) {
        return true;
    }
    
    // Check specific page permissions
    const restrictedPages = {
        'add_patient.html': 'canAddPatients',
        'modify_patient.html': 'canAddPatients'
    };
    
    const requiredPermission = restrictedPages[currentPage];
    
    if (requiredPermission && !hasPermission(requiredPermission)) {
        console.log('User does not have permission to access:', currentPage);
        alert('You do not have permission to access this page.');
        window.location.href = 'home.html';
        return false;
    }
    
    return true;
}

/**
 * Initialize authentication for protected pages
 * Call this at the beginning of each protected page
 */
function initAuth() {
    if (checkAuthentication()) {
        addUserWelcome();
        hideUnauthorizedNavItems();
        return true;
    }
    return false;
}

// Auto-check authentication when script loads
document.addEventListener('DOMContentLoaded', () => {
    // Don't check auth on login page
    if (!window.location.pathname.includes('index.html') && 
        window.location.pathname !== '/' &&
        !window.location.pathname.includes('login')) {
        initAuth();
    }
});