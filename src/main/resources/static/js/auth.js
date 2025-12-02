// js/auth.js

// Сохранение JWT
function saveToken(token) {
    localStorage.setItem("auth_token", token);
}

// Логика отправки формы
document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    const errorField = document.getElementById("error");
    errorField.textContent = "";

    if (!username || !password) {
        errorField.textContent = "Введите имя пользователя и пароль";
        return;
    }

    try {
        // Создаём Basic Auth заголовок
        const basic = btoa(username + ":" + password);

        const response = await fetch("/auth/login", {
            method: "POST",
            headers: {
                "Authorization": "Basic " + basic
            }
        });

        if (!response.ok) {
            const text = await response.text().catch(() => null);
            throw new Error(text || "Ошибка авторизации");
        }

        // Бек возвращает токен как plain text
        const token = await response.text();

        saveToken(token);

        // Переход на защищённую страницу
        window.location.href = "home.html";

    } catch (err) {
        errorField.textContent = err.message || "Ошибка";
    }
});
