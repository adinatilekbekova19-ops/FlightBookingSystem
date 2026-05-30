<!DOCTYPE html>
<html>
<head>
    <title>Бронирование билета</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; display: flex; justify-content: center; align-items: center; min-height: 100vh; }

        .modal-container { max-width: 500px; width: 100%; margin: 20px; }
        .modal-content { background: white; border-radius: 15px; padding: 30px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }

        h2 { color: #333; margin-bottom: 20px; border-bottom: 2px solid #007bff; padding-bottom: 10px; }

        .flight-info { background: #f8f9fa; padding: 15px; border-radius: 10px; margin-bottom: 20px; }
        .flight-info p { margin: 5px 0; }
        .company { font-weight: bold; color: #007bff; }
        .route { font-size: 18px; font-weight: bold; margin: 10px 0; }
        .price { font-size: 24px; font-weight: bold; color: #28a745; margin: 10px 0; }

        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; }
        select, input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; }

        .seat-diagram { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-top: 10px; }
        .seat { padding: 10px; text-align: center; background: #e9ecef; border-radius: 8px; cursor: pointer; }
        .seat:hover { background: #007bff; color: white; }
        .seat.selected { background: #28a745; color: white; }
        .seat.booked { background: #dc3545; color: white; cursor: not-allowed; opacity: 0.6; }

        .buttons { display: flex; gap: 15px; margin-top: 25px; }
        .btn-confirm { background: #28a745; color: white; border: none; padding: 12px 25px; border-radius: 8px; cursor: pointer; font-size: 16px; flex: 1; }
        .btn-confirm:hover { background: #218838; }
        .btn-cancel { background: #6c757d; color: white; border: none; padding: 12px 25px; border-radius: 8px; cursor: pointer; font-size: 16px; flex: 1; text-align: center; text-decoration: none; display: inline-block; }
        .btn-cancel:hover { background: #5a6268; }

        .error-message { color: #dc3545; margin-top: 10px; }
    </style>
</head>
<body>
<div class="modal-container">
    <div class="modal-content">
        <h2>Бронирование билета</h2>

        <div class="flight-info">
            <p class="company">${ticket.companyName!''}</p>
            <p>Рейс: ${ticket.flightNumber!''}</p>
            <div class="route">
                ${ticket.departureCity!''} → ${ticket.arrivalCity!''}
            </div>
            <div class="price">
                ${ticket.price} ₽
            </div>
            <p>Класс: <#if ticket.ticketClass == "ECONOMY">Эконом<#else>Бизнес</#if></p>
        </div>

        <form action="/flights/book/confirm" method="post">
            <input type="hidden" name="ticketId" value="${ticket.id}">

            <div class="form-group">
                <label>Выберите место</label>
                <select name="seatNumber" id="seatSelect" required>
                    <option value="">Выберите место</option>
                    <#list availableSeats as seat>
                        <option value="${seat}" <#if seat == ticket.seatNumber>selected</#if>>${seat}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>Схема салона (доступные места)</label>
                <div class="seat-diagram" id="seatDiagram">
                    <#list availableSeats as seat>
                        <div class="seat" data-seat="${seat}">${seat}</div>
                    </#list>
                </div>
            </div>

            <div class="buttons">
                <button type="submit" class="btn-confirm">Подтвердить бронирование</button>
                <a href="/flights" class="btn-cancel">Отмена</a>
            </div>
        </form>
    </div>
</div>

<script>
    // Клик по схеме мест - выбираем место
    var seats = document.querySelectorAll('.seat');
    var seatSelect = document.getElementById('seatSelect');

    seats.forEach(function(seat) {
        seat.addEventListener('click', function() {
            // Убираем выделение со всех
            seats.forEach(function(s) {
                s.classList.remove('selected');
            });
            // Выделяем текущее
            this.classList.add('selected');
            // Обновляем select
            seatSelect.value = this.dataset.seat;
        });
    });

    // Если в select уже выбрано место - выделяем его в схеме
    var selectedValue = seatSelect.value;
    if (selectedValue) {
        var selectedSeats = document.querySelectorAll('.seat[data-seat="' + selectedValue + '"]');
        if (selectedSeats.length > 0) {
            selectedSeats[0].classList.add('selected');
        }
    }
</script>
</body>
</html>