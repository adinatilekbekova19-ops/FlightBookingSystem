<!DOCTYPE html>
<html>
<head>
    <title>Вход</title>
    <style>
        .container { max-width: 400px; margin: 50px auto; padding: 20px; font-family: Arial; }
        .form-group { margin-bottom: 15px; }
        input { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        button { padding: 10px 20px; background: #007bff; color: white; border: none; cursor: pointer; border-radius: 4px; }
        .error { color: red; margin-bottom: 15px; padding: 10px; background: #f8d7da; border-radius: 4px; }
        .success { color: green; margin-bottom: 15px; padding: 10px; background: #d4edda; border-radius: 4px; }
        .info { color: #856404; margin-bottom: 15px; padding: 10px; background: #fff3cd; border-radius: 4px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Вход в систему</h2>

    <#if errorMessage??>
        <div class="error">${errorMessage}</div>
    </#if>
    <#if successMessage??>
        <div class="success">${successMessage}</div>
    </#if>
    <#if infoMessage??>
        <div class="info">${infoMessage}</div>
    </#if>

    <form action="/login" method="post">
        <div class="form-group">
            <label>Логин или Email</label>
            <input type="text" name="username" placeholder="admin или email@example.com" required>
        </div>
        <div class="form-group">
            <label>Пароль</label>
            <input type="password" name="password" placeholder="Введите пароль" required>
        </div>
        <button type="submit">Войти</button>
    </form>
    <p style="margin-top: 15px;">Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
    <p><a href="/">← На главную</a></p>
</div>
</body>
</html>