# Перевірка ендпоінтів магазину квітів (curl) — повний CRUD і помилки

Спершу запустіть застосунок: `./mvnw spring-boot:run` (або зелений ▶ в IntelliJ).
Сервер працює на `http://localhost:8080`. Команди виконуйте в іншій вкладці Терміналу.
Ключ `-i` показує статус-код відповіді — він у першому рядку, напр. `HTTP/1.1 201`.

| # | Операція | Метод і шлях | Код |
|---|----------|--------------|-----|
| 1 | Перелік | `GET /api/flowers` | 200 |
| 2 | Фільтр за категорією | `GET /api/flowers?category=букет` | 200 |
| 3 | Одна квітка | `GET /api/flowers/{id}` | 200 / 404 |
| 4 | Створення | `POST /api/flowers` | 201 / 400 |
| 5 | Оновлення | `PUT /api/flowers/{id}` | 200 / 404 / 400 |
| 6 | Видалення | `DELETE /api/flowers/{id}` | 204 / 404 |

Усі помилки повертаються в одному форматі (`ErrorResponse`): `status`, `error`, `message`, `details`.

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

## 3. Одна квітка — GET /{id}

```bash
curl http://localhost:8080/api/flowers/1
curl -i http://localhost:8080/api/flowers/999
```

Очікувано: для id 1 — `200 OK` і повна інформація; для id 999 — `HTTP/1.1 404` і тіло:

```json
{"status":404,"error":"Not Found","message":"Квітку з id=999 не знайдено","details":[]}
```

## 4. Створення — POST

```bash
curl -i -X POST http://localhost:8080/api/flowers \
  -H "Content-Type: application/json" \
  -d '{"name":"Ромашки польові","category":"букет","color":"білий","price":450.00,"stemsCount":21,"season":"літо","photoUrl":"/photos/chamomile.jpg","description":"Легкий літній букет із польових ромашок."}'
```

Очікувано: `HTTP/1.1 201` і JSON нової квітки з `"id":6`.

## 5. Оновлення — PUT /{id}

```bash
curl -i -X PUT http://localhost:8080/api/flowers/6 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ромашки польові","category":"букет","color":"білий","price":500.00,"stemsCount":25,"season":"літо","photoUrl":"/photos/chamomile.jpg","description":"Збільшений букет: 25 ромашок."}'
```

Очікувано: `HTTP/1.1 200` і оновлена квітка. Для неіснуючого id (напр. 999) — `HTTP/1.1 404`.

## 6. Видалення — DELETE /{id}

```bash
curl -i -X DELETE http://localhost:8080/api/flowers/6
```

Очікувано: `HTTP/1.1 204` — успіх без тіла. Повторний запит — `HTTP/1.1 404`.

## 7. Некоректні дані → 400 (валідація)

Порожня назва і від'ємна ціна:

```bash
curl -i -X POST http://localhost:8080/api/flowers \
  -H "Content-Type: application/json" \
  -d '{"name":"","category":"букет","price":-5}'
```

Очікувано: `HTTP/1.1 400` і тіло з переліком проблем:

```json
{"status":400,"error":"Bad Request","message":"Дані запиту не пройшли перевірку",
 "details":["name: Назва не може бути порожньою","price: Ціна має бути більшою за нуль"]}
```

Те саме для `PUT` з некоректним тілом.
