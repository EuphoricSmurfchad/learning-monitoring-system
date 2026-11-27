# Learning Monitoring System

REST API для учёта преподавателей, курсов, групп, студентов и расписания занятий.

## Стек

- Java 17, Spring Boot 4
- Spring MVC, Spring Data JPA / Hibernate
- PostgreSQL 16, Liquibase (миграции в `src/main/resources/db/changelog`)
- MapStruct, Lombok, Bean Validation
- springdoc-openapi (Swagger UI)
- Тесты: JUnit 5, Testcontainers
- Сборка: Maven (Wrapper), Docker / Docker Compose

## Запуск

### 1. База данных

```bash
docker compose up -d postgres
```

Параметры подключения по умолчанию (`src/main/resources/application.yaml`):

| Параметр | Значение |
|---|---|
| URL | `jdbc:postgresql://localhost:5432/lms` |
| Пользователь | `postgres` |
| Пароль | `postgres` |

Схема создаётся и проверяется автоматически через Liquibase (`ddl-auto: validate`).

### 2. Приложение

```bash
./mvnw spring-boot:run
```

Либо собрать JAR и запустить:

```bash
./mvnw -DskipTests package
java -jar target/learning-monitoring-system-0.0.1-SNAPSHOT.jar
```

После запуска:

- API: `http://localhost:8080/api/v1/...`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Запуск в Docker

```bash
docker build -t learning-monitoring-system .
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/lms \
  --network <сеть_compose> learning-monitoring-system
```

## API

Базовый префикс: `/api/v1`.

| Ресурс | Метод и путь | Описание |
|---|---|---|
| Учителя | `POST /teachers`, `GET /teachers/{id}`, `PUT /teachers/{id}`, `DELETE /teachers/{id}` | CRUD |
| Курсы | `POST /courses`, `GET /courses/{id}`, `PUT /courses/{id}`, `DELETE /courses/{id}` | CRUD |
| Группы | `POST /groups`, `GET /groups/{id}`, `PUT /groups/{id}`, `DELETE /groups/{id}` | CRUD |
| Группы | `POST /groups/{groupId}/courses/{courseId}`, `DELETE /groups/{groupId}/courses/{courseId}` | Привязать / отвязать группу от курса |
| Студенты | `POST /students`, `GET /students/{id}`, `PUT /students/{id}`, `DELETE /students/{id}` | CRUD |
| Студенты | `POST /students/{studentId}/groups/{groupId}`, `DELETE /students/{studentId}/groups/{groupId}` | Записать / отчислить студента из группы |
| Расписание | `POST /schedules`, `GET /schedules/{id}`, `PUT /schedules/{id}`, `DELETE /schedules/{id}` | CRUD |
| Расписание | `GET /schedules/teachers/{id}?page=0&size=10`, `GET /schedules/groups/{id}?page=0&size=10` | Постраничный вывод по преподавателю / группе |

### Примеры тел запросов

```json
// POST /api/v1/teachers
{ "firstName": "Иван", "lastName": "Иванов" }

// POST /api/v1/courses
{ "name": "Математика", "description": "Базовый курс", "teacherId": 1 }

// POST /api/v1/groups
{ "name": "Группа-1" }

// POST /api/v1/students
{ "firstName": "Пётр", "lastName": "Петров", "groupIds": [1] }

// POST /api/v1/schedules
{
  "groupId": 1,
  "courseId": 1,
  "startTime": "2026-12-01T10:00:00",
  "endTime": "2026-12-01T11:30:00"
}
```

Создание возвращает `201 Created` с заголовком `Location` и DTO ответа; удаление — `204 No Content`.

### Правила и ошибки

- Расписание можно создать только для группы, привязанной к курсу; `startTime` должен быть раньше `endTime`.
- Ответы об ошибках: `{"message": "..."}` с кодами `400` (валидация), `404` (ресурс не найден), `409` (бизнес-конфликт).
- Каждую ночь в 03:00 (`app.cleanup.cron`) удаляются расписания старше одного года.

## Конфигурация

| Свойство | По умолчанию | Назначение |
|---|---|---|
| `spring.datasource.*` | PostgreSQL `lms` на `localhost:5432` | Подключение к БД |
| `spring.jpa.show-sql` | `true` | Вывод SQL в консоль |
| `app.cleanup.cron` | `0 0 3 * * *` | Расписание очистки старых записей |

Значения можно переопределить через переменные окружения, например `SPRING_DATASOURCE_URL`, `APP_CLEANUP_CRON`.

## Тесты

```bash
./mvnw test
```

Интеграционные тесты используют Testcontainers, поэтому нужен запущенный Docker.

## Структура

```
src/main/java/com/smurfchad/learning_monitoring_system/
├── controller/   # REST-контроллеры
├── service/      # бизнес-логика
├── repository/   # Spring Data JPA
├── model/        # JPA-сущности
├── dto/          # DTO и MapStruct-мапперы
├── exception/    # исключения и глобальный обработчик
├── cleaner/      # планировщик очистки старых расписаний
└── LearningMonitoringSystemApplication.java
```
