<!DOCTYPE html>
<html>
<head>
    <title>Создание рейса</title>
    <style>
        .container { max-width: 600px; margin: 50px auto; padding: 20px; background: white; border-radius: 10px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        button { background: #28a745; color: white; padding: 12px 25px; border: none; border-radius: 5px; cursor: pointer; }
        .btn-cancel { background: #6c757d; text-decoration: none; display: inline-block; text-align: center; }
    </style>
</head>
<body>
<div class="container">
    <h2>Создание нового рейса</h2>
    <form action="/company/flights/create" method="post">
        <div class="form-group">
            <label>Номер рейса (необязательно)</label>
            <input type="text" name="flightNumber" placeholder="Например: SU1234">
        </div>
        <div class="form-group">
            <label>Город вылета *</label>
            <input type="text"
                   name="departureCity"
                   list="cities"
                   required>
        </div>

        <div class="form-group">
            <label>Город прилёта *</label>
            <input type="text"
                   name="arrivalCity"
                   list="cities"
                   required>
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

        <div class="form-group">
            <label>Дата и время вылета *</label>
            <input type="datetime-local" name="departureTime" required>
        </div>
        <div class="form-group">
            <label>Дата и время прилёта *</label>
            <input type="datetime-local" name="arrivalTime" required>
        </div>
        <button type="submit">Создать рейс</button>
        <a href="/company/dashboard" class="btn-cancel" style="margin-left: 10px;">Отмена</a>
    </form>
    <p style="margin-top: 20px; color: #666;">* При создании рейса автоматически будут сгенерированы 10 билетов (7 эконом, 3 бизнес)</p>
</div>
</body>
</html>