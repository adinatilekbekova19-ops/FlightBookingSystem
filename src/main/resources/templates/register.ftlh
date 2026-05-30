<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
    <style>
        .container { max-width: 400px; margin: 50px auto; padding: 20px; font-family: Arial; }
        .form-group { margin-bottom: 15px; }
        input { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; }
        input.error { border-color: red; }
        button { padding: 10px 20px; background: #28a745; color: white; border: none; cursor: pointer; border-radius: 4px; }
        button:hover { background: #218838; }
        .error-message { color: red; font-size: 12px; margin-top: 5px; }
        .success-message { color: green; font-size: 12px; margin-top: 5px; }
        .field-error { border: 1px solid red; }
    </style>
</head>
<body>
<div class="container">
    <h2>Регистрация</h2>

    <form action="/register" method="post">
        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" value="${(registrationDto.email)!''}"
                   class="${(fields?? && fields.email?? && fields.email?has_content)?then('field-error','')}">
            <#if fields?? && fields.email??>
                <div class="error-message">${fields.email}</div>
            </#if>
        </div>

        <div class="form-group">
            <label>Полное имя:</label>
            <input type="text" name="fullName" value="${(registrationDto.fullName)!''}"
                   class="${(fields?? && fields.fullName?? && fields.fullName?has_content)?then('field-error','')}">
            <#if fields?? && fields.fullName??>
                <div class="error-message">${fields.fullName}</div>
            </#if>
        </div>

        <div class="form-group">
            <label>Пароль:</label>
            <input type="password" name="password"
                   class="${(fields?? && fields.password?? && fields.password?has_content)?then('field-error','')}">
            <#if fields?? && fields.password??>
                <div class="error-message">${fields.password}</div>
            </#if>
        </div>

        <div class="form-group">
            <label>Подтверждение пароля:</label>
            <input type="password" name="confirmPassword"
                   class="${(fields?? && fields.confirmPassword?? && fields.confirmPassword?has_content)?then('field-error','')}">
            <#if fields?? && fields.confirmPassword??>
                <div class="error-message">${fields.confirmPassword}</div>
            </#if>
        </div>

        <button type="submit">Зарегистрироваться</button>
    </form>
    <p style="margin-top: 15px;">Уже есть аккаунт? <a href="/login">Войти</a></p>
    <p><a href="/">← На главную</a></p>
</div>
</body>
</html>