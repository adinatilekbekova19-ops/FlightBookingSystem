<!DOCTYPE html>
<html>
<head>
    <title>Вход</title>
    <style>
        .container { max-width: 400px; margin: 50px auto; padding: 20px; font-family: Arial; }
        .form-group { margin-bottom: 15px; }
        input { width: 100%; padding: 8px; margin-top: 5px; }
        button { padding: 10px 20px; background: #007bff; color: white; border: none; cursor: pointer; }
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>
<div class="container">
    <h2>Вход в систему</h2>

    <#if errorMessage??>
        <p class="error">${errorMessage}</p>
    </#if>
    <#if successMessage??>
        <p class="success">${successMessage}</p>
    </#if>

    <form action="/login" method="post">
        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="username" placeholder="Email" required>
        </div>
        <div class="form-group">
            <label>Пароль:</label>
            <input type="password" name="password" placeholder="Пароль" required>
        </div>
        <button type="submit">Войти</button>
    </form>
    <p style="margin-top: 15px;">Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
    <p><a href="/">← На главную</a></p>
</div>
</body>
</html>