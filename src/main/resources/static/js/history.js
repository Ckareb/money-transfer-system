function getToken() { return localStorage.getItem('auth_token'); }
function clearToken() { localStorage.removeItem('auth_token'); }

let currentAccount = JSON.parse(sessionStorage.getItem('selectedAccount'));
if (!currentAccount) {
    window.location.href = 'home.html';
} else {
    document.getElementById('accountInfo').textContent =
        `Счёт: ${currentAccount.accountNumber} | Баланс: ${currentAccount.balance}`;
}

async function fetchHistory() {
    try {
        const resp = await fetch(`/account/${currentAccount.id}/history-transaction`, {
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!resp.ok) throw new Error('Ошибка при загрузке истории');
        const page = await resp.json();

        const root = document.getElementById('transactionsList');
        root.innerHTML = '';
        if (!page || page.content.length === 0) {
            root.innerHTML = '<div>Транзакций не найдено</div>';
            return;
        }

        page.content.forEach(tx => {
            const div = document.createElement('div');
            div.className = 'transaction';
            div.innerHTML = `
                <div><b>ID:</b> ${tx.id}</div>
                <div><b>Сумма:</b> ${tx.amount}</div>
                <div><b>Тип:</b> ${tx.type}</div>
                <div><b>Дата:</b> ${new Date(tx.date).toLocaleString()}</div>
            `;
            root.appendChild(div);
        });
    } catch (err) {
        document.getElementById('historyError').textContent = err.message;
    }
}

document.getElementById('btnLogout').addEventListener('click', () => {
    clearToken();
    window.location.href = 'index.html';
});
document.getElementById('btnBack').addEventListener('click', () => {
    window.location.href = 'account.html';
});

fetchHistory();
