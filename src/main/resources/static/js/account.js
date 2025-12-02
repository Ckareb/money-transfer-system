function getToken() { return localStorage.getItem('auth_token'); }
function clearToken() { localStorage.removeItem('auth_token'); }

let currentAccount = JSON.parse(sessionStorage.getItem('selectedAccount'));
if (!currentAccount) {
    window.location.href = 'home.html';
} else {
    document.getElementById('selectedAccount').textContent =
        `Счёт: ${currentAccount.accountNumber} | Баланс: ${currentAccount.balance}`;
}

// Загрузка доступа к кнопкам
async function loadAccess() {
    try {
        const resp = await fetch(`/account/${currentAccount.id}/access`, {
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!resp.ok) throw new Error('Нет доступа к действиям');
        const access = await resp.json();

        document.getElementById('btnClose').style.display = access.delete ? 'inline-block' : 'none';
        document.getElementById('btnChangeBalance').style.display = access.transfer ? 'inline-block' : 'none';
        document.getElementById('btnChangeData').style.display = access.update ? 'inline-block' : 'none';
        document.getElementById('btnHistory').style.display = 'inline-block'; // все могут смотреть историю
    } catch (err) {
        document.getElementById('actionsError').textContent = err.message;
    }
}
loadAccess();

// Закрыть счёт
document.getElementById('btnClose').addEventListener('click', async () => {
    if (!currentAccount) return;
    try {
        const resp = await fetch(`/account/close/${currentAccount.id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!resp.ok) throw new Error('Ошибка при закрытии счёта');
        alert('Счёт закрыт');
        window.location.href = 'home.html';
    } catch (err) {
        document.getElementById('actionsError').textContent = err.message;
    }
});

// Изменить баланс
document.getElementById('btnChangeBalance').addEventListener('click', async () => {
    if (!currentAccount) return;
    const amount = prompt('Введите сумму для изменения баланса:');
    if (!amount) return;
    try {
        const resp = await fetch('/account/change/balance', {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + getToken(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: currentAccount.id, amount: Number(amount) })
        });
        if (!resp.ok) throw new Error('Ошибка при изменении баланса');
        alert('Баланс изменён');
        window.location.reload();
    } catch (err) {
        document.getElementById('actionsError').textContent = err.message;
    }
});

// Изменить данные
document.getElementById('btnChangeData').addEventListener('click', async () => {
    const newName = prompt('Введите новое название счета:');
    if (!newName) return;
    try {
        const resp = await fetch('/account/change', {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + getToken(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: currentAccount.id, accountName: newName })
        });
        if (!resp.ok) throw new Error('Ошибка при изменении данных');
        alert('Данные изменены');
        window.location.reload();
    } catch (err) {
        document.getElementById('actionsError').textContent = err.message;
    }
});

// История транзакций
document.getElementById('btnHistory').addEventListener('click', () => {
    sessionStorage.setItem('selectedAccount', JSON.stringify(currentAccount));
    window.location.href = 'history.html';
});

// Logout и назад
document.getElementById('btnLogout').addEventListener('click', () => {
    clearToken();
    window.location.href = 'index.html';
});
document.getElementById('btnBack').addEventListener('click', () => {
    window.location.href = 'home.html';
});
