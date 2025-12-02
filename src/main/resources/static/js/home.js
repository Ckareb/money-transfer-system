// js/home.js
function getToken() { return localStorage.getItem('auth_token'); }
function clearToken() { localStorage.removeItem('auth_token'); }

// Рендер списка счетов
function renderAccounts(accounts) {
    const root = document.getElementById('accountsList');
    root.innerHTML = '';
    if (!accounts || accounts.length === 0) {
        root.innerHTML = '<div>Счета не найдены</div>';
        return;
    }

    accounts.forEach(acc => {
        const div = document.createElement('div');
        div.className = 'accountRow';
        div.innerHTML = `
            <div><b>Номер:</b> ${acc.accountNumber || ''}</div>
            <div><b>Валюта:</b> ${acc.currencyName || acc.currencyId || ''}</div>
            <div><b>Баланс:</b> ${acc.balance ?? ''}</div>
        `;
        div.addEventListener('click', () => {
            sessionStorage.setItem('selectedAccount', JSON.stringify(acc));
            window.location.href = 'account.html';
        });
        root.appendChild(div);
    });
}

// Загрузка счетов текущего пользователя
async function fetchAccounts() {
    const token = getToken();
    if (!token) { window.location.href = 'index.html'; return; }

    try {
        const resp = await fetch('/account/me', {
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (!resp.ok) throw new Error('Не удалось загрузить счета');
        const accounts = await resp.json();
        renderAccounts(accounts);
    } catch (err) {
        document.getElementById('homeError').textContent = err.message;
    }
}

// Logout
document.getElementById('btnLogout').addEventListener('click', () => {
    clearToken();
    window.location.href = 'index.html';
});

fetchAccounts();
