<!DOCTYPE html>
<html>
<head>
    <title>Панель управления компании</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }

        .header { background: white; padding: 15px 20px; border-radius: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .logo { font-size: 24px; font-weight: bold; color: #007bff; }
        .user-menu a { margin-left: 15px; text-decoration: none; color: #333; }

        .card { background: white; border-radius: 10px; padding: 25px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card-title { font-size: 20px; margin-bottom: 20px; color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }

        .company-info { display: flex; gap: 30px; align-items: center; flex-wrap: wrap; }
        .company-logo { width: 100px; height: 100px; background: #e9ecef; border-radius: 50%; display: flex; align-items: center; justify-content: center; overflow: hidden; }
        .company-logo img { width: 100%; height: 100%; object-fit: cover; }
        .company-details { flex: 1; }

        .flight-table { width: 100%; border-collapse: collapse; }
        .flight-table th, .flight-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        .flight-table th { background: #f8f9fa; font-weight: bold; }

        .btn { display: inline-block; padding: 10px 20px; border-radius: 5px; text-decoration: none; margin-right: 10px; }
        .btn-primary { background: #007bff; color: white; }
        .btn-primary:hover { background: #0056b3; }
        .btn-success { background: #28a745; color: white; }
        .btn-success:hover { background: #218838; }

        .alert { padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .alert-success { background: #d4edda; color: #155724; }
        .alert-danger { background: #f8d7da; color: #721c24; }

        .ticket-list { margin-top: 15px; }
        .ticket-item { display: inline-block; padding: 5px 10px; margin: 5px; background: #e9ecef; border-radius: 5px; font-size: 12px; }
        .ticket-booked { background: #f8d7da; color: #721c24; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="logo">FlightBooking | Панель компании</div>
        <div class="user-menu">
            <a href="/company/dashboard">Главная</a>
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

    <!-- Информация о компании -->
    <div class="card">
        <h2 class="card-title">Информация о компании</h2>
        <div class="company-info">
            <div class="company-logo">
                <#if company.logoPath?? && company.logoPath != ''>
                    <img src="${company.logoPath}" alt="Логотип">
                <#else>
                    📷
                </#if>
            </div>
            <div class="company-details">
                <p><strong>Название:</strong> ${company.name!''}</p>
                <p><strong>Email:</strong> ${company.email!''}</p>
                <p><strong>Статус:</strong>
                    <#if company.frozen>Заморожена<#else>Активна</#if>
                </p>
            </div>
            <div>
                <form action="/company/logo/upload" method="post" enctype="multipart/form-data">
                    <input type="file" name="logo" accept="image/*" required>
                    <button type="submit" class="btn btn-primary">Загрузить логотип</button>
                </form>
            </div>
        </div>
    </div>

    <!-- Создание рейса -->
    <div class="card">
        <h2 class="card-title">Создать новый рейс</h2>
        <form action="/company/flights/create" method="post">
            <div style="display: flex; gap: 15px; flex-wrap: wrap;">
                <div style="flex: 1;">
                    <label>Номер рейса (опционально):</label>
                    <input type="text" name="flightNumber" style="width: 100%; padding: 8px;">
                </div>
                <div style="flex: 1;">
                    <label>Город вылета:</label>
                    <input type="text"
                           name="departureCity"
                           list="cities"
                           required
                           style="width: 100%; padding: 8px;">
                </div>

                <div style="flex: 1;">
                    <label>Город прилёта:</label>
                    <input type="text"
                           name="arrivalCity"
                           list="cities"
                           required
                           style="width: 100%; padding: 8px;">
                </div>

                <datalist id="cities">
                    <option value="Бишкек">
                    <option value="Москва">
                    <option value="Стамбул">
                    <option value="Алматы">
                    <option value="Ташкент">
                    <option value="Дубай">
                    <option value="Анталия">
                    <option value="Екатеринбург">
                </datalist>
            </div>
            <div style="display: flex; gap: 15px; flex-wrap: wrap; margin-top: 15px;">
                <div style="flex: 1;">
                    <label>Дата и время вылета:</label>
                    <input type="datetime-local" name="departureTime" required style="width: 100%; padding: 8px;">
                </div>
                <div style="flex: 1;">
                    <label>Дата и время прилёта:</label>
                    <input type="datetime-local" name="arrivalTime" required style="width: 100%; padding: 8px;">
                </div>
                <div style="display: flex; align-items: flex-end;">
                    <button type="submit" class="btn btn-success">Создать рейс</button>
                </div>
            </div>
        </form>
    </div>

    <!-- Список рейсов -->
    <div class="card">
        <h2 class="card-title">Мои рейсы</h2>

        <#if flights?has_content>
            <table class="flight-table">
                <thead>
                <tr>
                    <th>Номер рейса</th>
                    <th>Маршрут</th>
                    <th>Дата вылета</th>
                    <th>Дата прилёта</th>
                    <th>Билеты</th>
                    <th>Статус</th>
                </tr>
                </thead>
                <tbody>
                <#list flights as flight>
                    <tr>
                        <td>${flight.flightNumber}</td>
                        <td>${flight.departureCity} → ${flight.arrivalCity}</td>
                        <td>${flight.formattedDepartureTime}</td>
                        <td>${flight.formattedArrivalTime}</td>
                        <td>
                            <#list flight.tickets as ticket>
                                <span class="ticket-item ${ticket.isBooked?then('ticket-booked', '')}">
                                        ${ticket.seatNumber} (${ticket.price}₽)
                                    </span>
                            </#list>
                        </td>
                        <td>
                            <#if flight.availableTickets == 0>Распродан
                            <#else>${flight.availableTickets} из ${flight.tickets?size} свободно</#if>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p>У вас пока нет рейсов. Создайте первый рейс!</p>
        </#if>
    </div>
</div>
</body>
</html>