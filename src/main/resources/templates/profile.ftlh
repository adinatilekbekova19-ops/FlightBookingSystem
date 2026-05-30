<!DOCTYPE html>
<html>
<head>
    <title>Личный кабинет</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }

        .header { background: white; padding: 15px 20px; border-radius: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .logo { font-size: 24px; font-weight: bold; color: #007bff; }
        .user-menu a { margin-left: 15px; text-decoration: none; color: #333; }

        .profile-card { background: white; border-radius: 10px; padding: 25px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .profile-title { font-size: 24px; margin-bottom: 20px; color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        .profile-info { display: flex; gap: 30px; flex-wrap: wrap; }
        .info-item { flex: 1; min-width: 200px; }
        .info-label { font-weight: bold; color: #555; margin-bottom: 5px; }
        .info-value { font-size: 16px; color: #333; }

        .bookings-section { background: white; border-radius: 10px; padding: 25px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .section-title { font-size: 20px; margin-bottom: 20px; color: #333; border-bottom: 1px solid #ddd; padding-bottom: 10px; }
        .bookings-count { color: #666; margin-bottom: 15px; }

        .booking-card { border: 1px solid #e0e0e0; border-radius: 10px; margin-bottom: 15px; overflow: hidden; }
        .booking-header { background: #f8f9fa; padding: 12px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #e0e0e0; }
        .booking-date { color: #666; font-size: 14px; }
        .booking-status { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; }
        .status-active { background: #d4edda; color: #155724; }
        .status-cancelled { background: #f8d7da; color: #721c24; }

        .booking-body { padding: 20px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; }
        .flight-info { flex: 2; }
        .company { font-weight: bold; color: #007bff; margin-bottom: 5px; }
        .route { font-size: 18px; font-weight: bold; margin: 5px 0; }
        .time { color: #666; font-size: 13px; margin-top: 5px; }
        .ticket-info { text-align: right; min-width: 180px; }
        .seat { font-weight: bold; margin-bottom: 5px; }
        .price { font-size: 20px; font-weight: bold; color: #28a745; margin-top: 5px; }

        .no-bookings { text-align: center; padding: 40px; color: #666; }
        .btn-back { display: inline-block; margin-top: 20px; background: #6c757d; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; }
        .btn-back:hover { background: #5a6268; }

        hr { margin: 15px 0; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="logo">FlightBooking</div>
        <div class="user-menu">
            <a href="/flights">Поиск</a>
            <a href="/profile">Профиль</a>
            <a href="/logout">Выход</a>
        </div>
    </div>

    <!-- Информация о пользователе -->
    <div class="profile-card">
        <h2 class="profile-title">Личный кабинет</h2>
        <div class="profile-info">
            <div class="info-item">
                <div class="info-label">Email</div>
                <div class="info-value">${user.email!''}</div>
            </div>
            <div class="info-item">
                <div class="info-label">Полное имя</div>
                <div class="info-value">${user.fullName!''}</div>
            </div>
            <div class="info-item">
                <div class="info-label">Дата регистрации</div>
                <div class="info-value">${formattedCreatedAt!''}</div>
            </div>
        </div>
    </div>

    <!-- История бронирований -->
    <div class="bookings-section">
        <h2 class="section-title">История бронирований</h2>

        <#if bookings?has_content>
            <div class="bookings-count">Всего бронирований: ${bookingsCount}</div>

            <#list bookings as booking>
                <div class="booking-card">
                    <div class="booking-header">
                        <span class="booking-date">${booking.formattedBookingDate}</span>
                        <span class="booking-status status-active">
                            <#if booking.status == "ACTIVE">Активно<#else>Отменено</#if>
                        </span>
                    </div>
                    <div class="booking-body">
                        <div class="flight-info">
                            <div class="company">${booking.companyName!''}</div>
                            <div class="route">
                                ${booking.departureCity!''} → ${booking.arrivalCity!''}
                            </div>
                            <div class="time">
                                Вылет: ${booking.formattedDepartureTime!''} |
                                Прилёт: ${booking.formattedArrivalTime!''}
                            </div>
                            <div class="time">Рейс: ${booking.flightNumber!''}</div>
                        </div>
                        <div class="ticket-info">
                            <div class="seat">Место: ${booking.seatNumber!''}</div>
                            <div class="seat">
                                Класс: <#if booking.ticketClass == "ECONOMY">Эконом<#else>Бизнес</#if>
                            </div>
                            <div class="price">${booking.price} ₽</div>
                        </div>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="no-bookings">
                <p>У вас пока нет бронирований</p>
                <p><a href="/flights" class="btn-back">Перейти к поиску билетов</a></p>
            </div>
        </#if>
    </div>
</div>
</body>
</html>