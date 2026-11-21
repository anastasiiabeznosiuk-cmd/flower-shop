# Перевірка ендпоінтів магазину квітів (curl)

Спершу запустіть застосунок (клас `FlowerShopApplication`, зелений ▶ в IntelliJ).
Сервер працює на `http://localhost:8080`. Далі виконайте команди в терміналі.

## 1. Перелік квітів

```bash
curl http://localhost:8080/api/flowers
```

Очікувано: `200 OK` і масив із 5 карток (поля `id`, `name`, `color`, `price`, `photoUrl`):

```json
[
  {"id":1,"name":"Троянда Red Naomi","color":"червоний","price":1850.00,"photoUrl":"/photos/red-naomi.jpg"},
  {"id":2,"name":"Тюльпани Strong Gold","color":"жовтий","price":950.00,"photoUrl":"/photos/strong-gold.jpg"},
  ...
]
```

## 2. Одна квітка з деталями

```bash
curl http://localhost:8080/api/flowers/1
```

Очікувано: `200 OK` і повна інформація про квітку (додатково `stemsCount`, `season`, `description`):

```json
{"id":1,"name":"Троянда Red Naomi","color":"червоний","price":1850.00,"stemsCount":25,
 "season":"цілий рік","photoUrl":"/photos/red-naomi.jpg",
 "description":"Класичний букет із 25 насичено-червоних троянд з великим бутоном. Стоїть у вазі до 10 днів."}
```

## 3. Неіснуюча квітка → 404

```bash
curl -i http://localhost:8080/api/flowers/999
```

Очікувано: перший рядок відповіді `HTTP/1.1 404`, а в тілі — повідомлення `Квітку з id=999 не знайдено`.
Ключ `-i` показує заголовки відповіді, тому видно статус-код.
