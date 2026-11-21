# Перевірка ендпоінтів магазину квітів (curl) — повний CRUD

Спершу запустіть застосунок: `./mvnw spring-boot:run` (або зелений ▶ в IntelliJ).
Сервер працює на `http://localhost:8080`. Команди виконуйте в іншій вкладці Терміналу.
Ключ `-i` показує статус-код відповіді — він у першому рядку, напр. `HTTP/1.1 201`.

| # | Операція | Метод і шлях | Код |
|---|----------|--------------|-----|
| 1 | Перелік | `GET /api/flowers` | 200 |
| 2 | Фільтр за категорією | `GET /api/flowers?category=букет` | 200 |
| 3 | Одна квітка | `GET /api/flowers/{id}` | 200 / 404 |
| 4 | Створення | `POST /api/flowers` | 201 |
| 5 | Оновлення | `PUT /api/flowers/{id}` | 200 / 404 |
| 6 | Видалення | `DELETE /api/flowers/{id}` | 204 / 404 |

## 1. Перелік квітів — GET

```bash
curl http://localhost:8080/api/flowers
```

Очікувано: `200 OK` і масив із 5 карток (`id`, `name`, `category`, `color`, `price`, `photoUrl`).

## 2. Фільтр за категорією — GET з параметром `category`

```bash
curl -G http://localhost:8080/api/flowers --data-urlencode "category=букет"
```

Очікувано: `200 OK` і лише букети (id 1, 2, 4). Регістр не важливий: `Букет` теж спрацює.
Інші категорії: `композиція`, `у горщику`. Якщо параметр не передати, повертається повний перелік.

`-G` разом із `--data-urlencode` додає до адреси `?category=...` і правильно кодує кирилицю.
У браузері можна просто відкрити `http://localhost:8080/api/flowers?category=букет`.

## 3. Одна квітка — GET /{id}

```bash
curl http://localhost:8080/api/flowers/1
curl -i http://localhost:8080/api/flowers/999
```

Очікувано: для id 1 — `200 OK` і повна інформація; для id 999 — `HTTP/1.1 404` і повідомлення `Квітку з id=999 не знайдено`.

## 4. Створення — POST

```bash
curl -i -X POST http://localhost:8080/api/flowers \
  -H "Content-Type: application/json" \
  -d '{"name":"Ромашки польові","category":"букет","color":"білий","price":450.00,"stemsCount":21,"season":"літо","photoUrl":"/photos/chamomile.jpg","description":"Легкий літній букет із польових ромашок."}'
```

Очікувано: `HTTP/1.1 201` і JSON нової квітки з `"id":6`.
Поля `id` у запиті немає: його призначає сервер.

## 5. Оновлення — PUT /{id}

```bash
curl -i -X PUT http://localhost:8080/api/flowers/6 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ромашки польові","category":"букет","color":"білий","price":500.00,"stemsCount":25,"season":"літо","photoUrl":"/photos/chamomile.jpg","description":"Збільшений букет: 25 ромашок."}'
```

Очікувано: `HTTP/1.1 200` і оновлена квітка (ціна 500.00, 25 стебел). PUT замінює квітку повністю, тому передаємо всі поля.

Оновлення неіснуючої квітки:

```bash
curl -i -X PUT http://localhost:8080/api/flowers/999 \
  -H "Content-Type: application/json" \
  -d '{"name":"Тест","category":"букет","color":"білий","price":100.00,"stemsCount":1,"season":"літо","photoUrl":"/photos/test.jpg","description":"Тест"}'
```

Очікувано: `HTTP/1.1 404`.

## 6. Видалення — DELETE /{id}

```bash
curl -i -X DELETE http://localhost:8080/api/flowers/6
```

Очікувано: `HTTP/1.1 204` — успіх без тіла відповіді.

Повторне видалення тієї самої квітки:

```bash
curl -i -X DELETE http://localhost:8080/api/flowers/6
```

Очікувано: `HTTP/1.1 404`, бо квітки з id 6 вже немає.
