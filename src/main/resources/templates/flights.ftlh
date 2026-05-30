<!DOCTYPE html>
<html>
<head>
    <title>Поиск авиабилетов</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }

        .header { background: white; padding: 15px 20px; border-radius: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .logo { font-size: 24px; font-weight: bold; color: #007bff; }
        .user-menu a { margin-left: 15px; text-decoration: none; color: #333; }

        .search-form { background: white; padding: 25px; border-radius: 10px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .form-row { display: flex; gap: 15px; flex-wrap: wrap; align-items: flex-end; }
        .form-group { flex: 1; min-width: 180px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; font-size: 12px; text-transform: uppercase; }
        .form-group input, .form-group select { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; }
        .date-range { display: flex; gap: 10px; }
        .search-btn { background: #007bff; color: white; border: none; padding: 12px 30px; border-radius: 5px; cursor: pointer; font-size: 16px; }
        .search-btn:hover { background: #0056b3; }

        .ticket-card { background: white; border-radius: 10px; margin-bottom: 15px; padding: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .flight-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #eee; }
        .company-name { font-weight: bold; font-size: 16px; }
        .flight-route { display: flex; align-items: center; gap: 20px; margin-bottom: 15px; }
        .city { font-weight: bold; font-size: 20px; }
        .time { color: #666; font-size: 14px; margin-top: 5px; }
        .arrow { font-size: 24px; color: #007bff; }

        .tickets-list { margin-top: 15px; }
        .ticket-row { display: flex; justify-content: space-between; align-items: center; padding: 12px; background: #f8f9fa; border-radius: 8px; margin-bottom: 8px; }
        .seat-info { display: flex; gap: 20px; }
        .seat { font-weight: bold; }
        .class { color: #666; }
        .price { font-size: 20px; font-weight: bold; color: #28a745; }
        .book-btn { background: #ff6b35; color: white; border: none; padding: 8px 20px; border-radius: 5px; cursor: pointer; }
        .book-btn:hover { background: #e55a2b; }
        .no-tickets { color: #999; padding: 10px; text-align: center; }

        .pagination { margin-top: 30px; text-align: center; }
        .pagination a, .pagination span { display: inline-block; padding: 8px 12px; margin: 0 5px; border: 1px solid #ddd; border-radius: 5px; text-decoration: none; color: #007bff; }
        .pagination .current { background: #007bff; color: white; border-color: #007bff; }

        .alert { padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .alert-success { background: #d4edda; color: #155724; }
        .alert-warning { background: #fff3cd; color: #856404; }

        hr { margin: 15px 0; border-color: #eee; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="logo">FlightBooking</div>
        <div class="user-menu">
            <a href="/flights">Поиск</a>
            <#if user?? && user.isAuthenticated()>
                <a href="/profile">Личный кабинет</a>
                <a href="/logout">Выход</a>
            <#else>
                <a href="/login">Вход</a>
                <a href="/register">Регистрация</a>
            </#if>
        </div>
    </div>

    <!-- Форма поиска -->
    <div class="search-form">
        <form action="/flights" method="get">
            <div class="form-row">
                <div class="form-group">
                    <label>Город вылета</label>
                    <select name="departureCity">
                        <option value="">Любой</option>
                        <#list departureCities as city>
                            <option value="${city}" <#if searchDto.departureCity?? && searchDto.departureCity == city>selected</#if>>${city}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
                    <label>Город прилёта</label>
                    <select name="arrivalCity">
                        <option value="">Любой</option>
                        <#list arrivalCities as city>
                            <option value="${city}" <#if searchDto.arrivalCity?? && searchDto.arrivalCity == city>selected</#if>>${city}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
                    <label>Когда?</label>
                    <div class="date-range">
                        <input type="date" name="startDate" value="${(searchDto.startDate)!''}">
                        <span style="padding-top: 12px;">-</span>
                        <input type="date" name="endDate" value="${(searchDto.endDate)!''}">
                    </div>
                </div>
                <div class="form-group">
                    <button type="submit" class="search-btn">Найти билеты</button>
                </div>
            </div>
        </form>
    </div>

    <!-- Сообщения -->
    <#if message??>
        <div class="alert alert-success">${message}</div>
    </#if>
    <#if error??>
        <div class="alert alert-warning">${error}</div>
    </#if>

    <!-- Результаты поиска -->
    <h3>Найдено рейсов: ${flights?size}</h3>
    <hr>

    <#if flights?size == 0>
        <div class="alert alert-warning">По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска.</div>
    </#if>

    <#list flights as flight>
        <div class="ticket-card">
            <div class="flight-header">
                <span class="company-name">${flight.companyName!''}</span>
                <span>Рейс: ${flight.flightNumber}</span>
            </div>

            <div class="flight-route">
                <div>
                    <div class="city">${flight.departureCity}</div>
                    <div class="time">${flight.departureTimeFormatted}</div>
                </div>
                <div class="arrow">→</div>
                <div>
                    <div class="city">${flight.arrivalCity}</div>
                    <div class="time">${flight.arrivalTimeFormatted}</div>
                </div>
            </div>

            <div class="tickets-list">
                <#if flight.tickets?has_content>
                    <#list flight.tickets as ticket>
                        <div class="ticket-row">
                            <div class="seat-info">
                                <span class="seat">Место: ${ticket.seatNumber}</span>
                                <span class="class">
                                    <#if ticket.ticketClass == "ECONOMY">Эконом<#else>Бизнес</#if>
                                </span>
                            </div>
                            <div class="price">${ticket.price} ₽</div>
                            <a href="/flights/book/${ticket.id}" class="book-btn">Забронировать</a>
                        </div>
                    </#list>
                <#else>
                    <div class="no-tickets">Нет доступных билетов</div>
                </#if>
            </div>
        </div>
    </#list>

    <!-- Пагинация -->
    <#if totalPages gt 1>
        <div class="pagination">
            <#if currentPage gt 0>
                <a href="?page=${currentPage-1}&departureCity=${(searchDto.departureCity)!''}&arrivalCity=${(searchDto.arrivalCity)!''}&startDate=${(searchDto.startDate)!''}&endDate=${(searchDto.endDate)!''}">← Предыдущая</a>
            </#if>
            <span class="current">Страница ${currentPage+1} из ${totalPages}</span>
            <#if currentPage+1 lt totalPages>
                <a href="?page=${currentPage+1}&departureCity=${(searchDto.departureCity)!''}&arrivalCity=${(searchDto.arrivalCity)!''}&startDate=${(searchDto.startDate)!''}&endDate=${(searchDto.endDate)!''}">Следующая →</a>
            </#if>
        </div>
    </#if>
</div>
</body>
</html>