<!DOCTYPE html>
<html>
<head>
    <title>Админ панель</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }

        .header { background: white; padding: 15px 20px; border-radius: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .logo { font-size: 24px; font-weight: bold; color: #dc3545; }
        .user-menu a { margin-left: 15px; text-decoration: none; color: #333; }

        .card { background: white; border-radius: 10px; padding: 25px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card-title { font-size: 20px; margin-bottom: 20px; color: #333; border-bottom: 2px solid #dc3545; padding-bottom: 10px; }

        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input { width: 100%; max-width: 300px; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }

        .btn { display: inline-block; padding: 8px 16px; border-radius: 5px; text-decoration: none; margin: 5px; border: none; cursor: pointer; }
        .btn-primary { background: #007bff; color: white; }
        .btn-primary:hover { background: #0056b3; }
        .btn-danger { background: #dc3545; color: white; }
        .btn-danger:hover { background: #c82333; }
        .btn-success { background: #28a745; color: white; }
        .btn-success:hover { background: #218838; }

        .company-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .company-table th, .company-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; vertical-align: top; }
        .company-table th { background: #f8f9fa; font-weight: bold; }

        .flight-list { margin-top: 10px; }
        .flight-item { padding: 5px 0; font-size: 13px; border-bottom: 1px dashed #eee; }
        .flight-item:last-child { border-bottom: none; }

        .badge { display: inline-block; padding: 3px 8px; border-radius: 12px; font-size: 11px; font-weight: bold; }
        .badge-active { background: #d4edda; color: #155724; }
        .badge-frozen { background: #f8d7da; color: #721c24; }

        .alert { padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .alert-success { background: #d4edda; color: #155724; }
        .alert-danger { background: #f8d7da; color: #721c24; }

        .text-center { text-align: center; }
        .text-muted { color: #6c757d; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="logo">Admin Panel</div>
        <div class="user-menu">
            <a href="/admin">Главная</a>
            <a href="/flights">Поиск</a>
            <a href="/logout">Выход</a>
        </div>
    </div>

    <!-- Сообщения -->
    <#if message??>
        <div class="alert alert-success">${message}</div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <!-- Создание компании -->
    <div class="card">
        <h2 class="card-title">Создать туристическую компанию</h2>
        <form action="/admin/companies/create" method="post">
            <div class="form-group">
                <label>Название компании *</label>
                <input type="text" name="name" required placeholder="Например: Аэрофлот">
            </div>
            <div class="form-group">
                <label>Email *</label>
                <input type="email" name="email" required placeholder="company@example.com">
            </div>
            <div class="form-group">
                <label>Пароль *</label>
                <input type="password" name="password" required placeholder="****">
            </div>
            <button type="submit" class="btn btn-primary">Создать компанию</button>
        </form>
    </div>

    <!-- Список компаний -->
    <div class="card">
        <h2 class="card-title">Туристические компании</h2>

        <#if companies?has_content>
            <table class="company-table">
                <thead>
                <tr>
                    <th>Название</th>
                    <th>Email</th>
                    <th>Статус</th>
                    <th>Рейсы (бронирований)</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                <#list companies as company>
                    <tr>
                        <td>
                            <strong>${company.name}</strong>
                            <#if company.logoPath?? && company.logoPath != ''>
                                <br><img src="${company.logoPath}" width="30" height="30" style="border-radius: 50%; margin-top: 5px;">
                            </#if>
                        </td>
                        <td>${company.email}</td>
                        <td>
                            <#if company.frozen>
                                <span class="badge badge-frozen">Заморожена</span>
                            <#else>
                                <span class="badge badge-active">Активна</span>
                            </#if>
                        </td>
                        <td>
                            <#if company.flights?has_content>
                                <#list company.flights as flight>
                                    <div class="flight-item">
                                        <strong>${flight.flightNumber}</strong>:
                                        ${flight.departureCity} → ${flight.arrivalCity}
                                        <span class="text-muted">(${flight.bookingsCount} бр.)</span>
                                    </div>
                                </#list>
                            <#else>
                                <span class="text-muted">Нет рейсов</span>
                            </#if>
                        </td>
                        <td>
                            <#if company.frozen>
                                <form action="/admin/companies/${company.id}/unfreeze" method="post">
                                    <button type="submit" class="btn btn-success">Разморозить</button>
                                </form>
                            <#else>
                                <form action="/admin/companies/${company.id}/freeze" method="post">
                                    <button type="submit" class="btn btn-danger">Заморозить</button>
                                </form>
                            </#if>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="text-center text-muted">Нет зарегистрированных компаний</p>
        </#if>
    </div>
</div>
</body>
</html>