# flower-shop — інтернет-магазин квітів (backend)

Навчальний проєкт з дисципліни «Розробка застосунків на Spring Boot».
Лише backend (REST API), без інтерфейсу.

**Стек:** Java 21, Spring Boot 3.4, Maven. Дані зберігаються в пам'яті (без бази даних).

## Структура

| Шар | Пакет | Що містить |
|-----|-------|------------|
| domain | `domain` | модель `Flower` (без анотацій Spring) |
| repository | `repository` | інтерфейс `FlowerRepository` і реалізація в пам'яті |
| service | `service` | інтерфейс `FlowerService`, `FlowerServiceImpl`, маппер `FlowerMapper` |
| web | `web` | `FlowerController`, `GlobalExceptionHandler`, DTO |
| config | `config` | `FlowerSeeder` — 5 стартових квітів |

## Ендпоінти

| Метод | Шлях | Код |
|-------|------|-----|
| GET | `/api/flowers` (необов'язково `?category=букет`) | 200 |
| GET | `/api/flowers/{id}` | 200 / 404 |
| POST | `/api/flowers` | 201 / 400 |
| PUT | `/api/flowers/{id}` | 200 / 404 / 400 |
| DELETE | `/api/flowers/{id}` | 204 / 404 |

Приклади запитів — у файлі [requests.md](requests.md).

## Запуск

```bash
./mvnw spring-boot:run
```

Сервер працює на `http://localhost:8080`.

## Обробка помилок

Усі помилки обробляє `GlobalExceptionHandler` (`@RestControllerAdvice`) і повертає один формат — `ErrorResponse`:

```json
{"status":404,"error":"Not Found","message":"Квітку з id=999 не знайдено","details":[]}
```

```json
{"status":400,"error":"Bad Request","message":"Дані запиту не пройшли перевірку",
 "details":["name: Назва не може бути порожньою","price: Ціна має бути більшою за нуль"]}
```

Правила валідації вхідного DTO `FlowerRequest`: `name` і `category` — `@NotBlank`; `price` — `@NotNull` і `@Positive`; `stemsCount` — `@Positive`.

## Тести

Тести побудовані за пірамідою тестування: найбільше швидких модульних тестів, менше інтеграційних.

| Рівень | Клас | Інструменти | Що перевіряє |
|--------|------|-------------|--------------|
| Unit (модульний) | `FlowerServiceImplTest` | JUnit 5, Mockito (репозиторій — заглушка) | логіку сервісу в ізоляції |
| Unit (модульний) | `FlowerMapperTest` | JUnit 5 | перетворення між `Flower` і DTO |
| Integration — зріз веб-шару | `FlowerControllerTest` | `@WebMvcTest`, `MockMvc`, `@MockBean`, `jsonPath` | адреси, коди відповіді, JSON, валідацію, формат помилок |
| Integration — наскрізний | `FlowerApiIntegrationTest` | `@SpringBootTest`, `@AutoConfigureMockMvc` | усі шари разом зі справжніми бінами |

### Покриті сценарії

**Сервіс (`FlowerServiceImplTest`):**
- `findById` повертає квітку, якщо вона є;
- `findById` кидає `FlowerNotFoundException`, якщо квітки немає;
- `findAll` без категорії повертає всі квіти, з категорією — викликає фільтр;
- `create` зберігає квітку і повертає її з новим id;
- `update` неіснуючої квітки кидає виняток і нічого не зберігає;
- `delete` видаляє наявну квітку і кидає виняток, якщо видаляти нічого.

**Маппер (`FlowerMapperTest`):** `toDomain` не встановлює id; `toCard` і `toDetail` копіюють поля.

**Контролер (`FlowerControllerTest`):**
- `GET /api/flowers` → 200 і список; параметр `category` передається в сервіс;
- `GET /api/flowers/1` → 200; `GET /api/flowers/999` → **404** зі структурованим тілом (`status`, `error`, `message`, порожній `details`);
- `POST` → 201; `POST` з порожньою назвою і від'ємною ціною → **400**, `details` — масив із 2 проблем, сервіс не викликається;
- `PUT` → 200; `PUT` неіснуючої квітки → **404**; `PUT` з некоректним тілом → **400**;
- `DELETE` → 204; `DELETE` неіснуючої квітки → **404**.

**Наскрізний (`FlowerApiIntegrationTest`):** створення, читання, фільтр, оновлення, видалення (після нього `GET` → 404), `PUT` неіснуючої квітки → 404, некоректне тіло → 400.

### Автономність інтеграційних тестів

- Перед кожним тестом сховище очищується: `@BeforeEach` → `repository.deleteAll()`.
- Кожен тест сам створює свої дані через API (`givenFlower(...)`) і бере справжній id із відповіді.
- Тест перевіряє лише те, що сам створив, тому не залежить від стартових даних і від інших тестів.

### Запуск тестів

```bash
./mvnw test
```

З випадковим порядком методів (перевірка автономності):

```bash
./mvnw test '-Djunit.jupiter.testmethod.order.default=org.junit.jupiter.api.MethodOrderer$Random'
```

Очікувано: `BUILD SUCCESS`, `Failures: 0, Errors: 0`.
