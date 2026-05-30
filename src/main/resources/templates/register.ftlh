<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
    <style>
        .container { max-width: 400px; margin: 50px auto; padding: 20px; font-family: Arial; }
        .form-group { margin-bottom: 15px; }
        input { width: 100%; padding: 8px; margin-top: 5px; }
        button { padding: 10px 20px; background: #28a745; color: white; border: none; cursor: pointer; }
        .error { color: red; font-size: 12px; }
        .field-error { border: 1px solid red; }
    </style>
</head>
<body>
<div class="container">
    <h2>Регистрация</h2>

    <form action="/register" method="post">
        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" value="${(registrationDto.email)!''}">
            <#if errors?? && errors.hasFieldErrors('email')>
                <#assign emailError = errors.getFieldError('email')>
                <p class="error">${emailError.defaultMessage!''}</p>
            </#if>
        </div>

        <div class="form-group">
            <label>Полное имя:</label>
            <input type="text" name="fullName" value="${(registrationDto.fullName)!''}">
            <#if errors?? && errors.hasFieldErrors('fullName')>
                <#assign nameError = errors.getFieldError('fullName')>
                <p class="error">${nameError.defaultMessage!''}</p>
            </#if>
        </div>

        <div class="form-group">
            <label>Пароль:</label>
            <input type="password" name="password">
            <#if errors?? && errors.hasFieldErrors('password')>
                <#assign passError = errors.getFieldError('password')>
                <p class="error">${passError.defaultMessage!''}</p>
            </#if>
        </div>

        <div class="form-group">
            <label>Подтверждение пароля:</label>
            <input type="password" name="confirmPassword">
            <#if errors?? && errors.hasFieldErrors('confirmPassword')>
                <#assign confirmError = errors.getFieldError('confirmPassword')>
                <p class="error">${confirmError.defaultMessage!''}</p>
            </#if>
        </div>

        <button type="submit">Зарегистрироваться</button>
    </form>
    <p style="margin-top: 15px;">Уже есть аккаунт? <a href="/login">Войти</a></p>
    <p><a href="/">← На главную</a></p>
</div>
</body>
</html>