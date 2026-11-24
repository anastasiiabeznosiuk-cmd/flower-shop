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

## Звіт про покриття тестами (практична 1.5)

### Матриця покриття

| Сценарій | Тип тесту | Тести | Очікуваний результат |
|----------|-----------|-------|----------------------|
| findAll → список | unit + integration | `findAll_withoutCategory_returnsAllFlowers`, `findAll_withCategory_usesFilter`, `list_returnsFlowers`, `getAll_returnsExactlyWhatTestCreated` | 200, масив |
| findById існуючий | unit + integration | `findById_returnsFlower`, `getOne_returnsFlowerDetail` | 200, деталі |
| findById неіснуючий | unit + integration | `findById_throwsWhenNotFound`, `getOne_returns404WhenMissing` | 404 + тіло помилки |
| create | unit + integration | `create_savesFlowerAndReturnsItWithNewId`, `create_returns201` | 201 |
| create невалідний | integration | `create_returns400WhenBodyInvalid`, `create_invalidBody_returns400WithDetails` | 400 + перелік проблем |
| update | unit + integration | `update_throwsWhenNotFound_andDoesNotSave`, `update_returns200`, `update_returns404WhenMissing`, `update_returns400WhenBodyInvalid` | 200 / 404 / 400 |
| delete | unit + integration | `delete_removesExistingFlower`, `delete_throwsWhenNothingDeleted`, `delete_returns204`, `delete_returns404WhenMissing` | 204 / 404 |

### Що не покрито і чому

- **Успішне оновлення в модульному тесті сервісу.** Модульно перевірено лише гілку «не знайдено». Успішне оновлення перевіряє наскрізний тест `update_changesFlower`, бо логіка проста: перевірка існування і `save`.
- **Маппер у тесті сервісу не замокано.** `FlowerMapper` простий і без залежностей, тому в `FlowerServiceImplTest` взято справжній (завдання це дозволяє). Окремо маппер покрито `FlowerMapperTest`.
- **Синтаксично зламаний JSON і нечисловий id (`/api/flowers/abc`).** Такі запити повертають 400 стандартним обробником Spring. Завдання вимагало обробки 404 і 400 валідації, тому окремих обробників і тестів для них немає.
- **Сидер `FlowerSeeder`.** Окремо не тестується: він лише наповнює демо-дані, а в наскрізних тестах сховище очищується перед кожним тестом.
- **База даних.** У модулі 1 дані зберігаються в пам'яті; тести з реальною БД з'являться в модулі 2.
- **Нефункціональні тести** (навантаження, безпека, швидкодія) — поза межами завдання.
