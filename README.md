# Тестовое задание - Разработка Fabric мода под Minecraft для вакансии Java/Minecraft разработчик в ООО ИОТ. 

## Задания
- **Задание 1**: Простой Screen с полем ввода текстового сообщения и кнопкой отправки
- **Задание 2**: Отправка закодированного в Protobuf 3 сообщения
- **Задание 3**: Декодирование на сервере сообщения и запись его содержимого в БД Postgres
  
[ТЗ подробнее](./Тестовое_задание.md)

## Технологический стек
- Minecraft 1.21.8 (loader 0.17.3, loom 1.11)
- Fabric API 1.21.8
- PostgreSQL 
- Java 17
- Hiberneate
- Gradle 8.14
- Docker + Docker Compose (для PostgreSQL)

## Описание хода выполнения
в [отчете](./report.md)

## Настройка базы данных PostgreSQL

### Запуск PostgreSQL в Docker

```bash
# Создайте и запустите контейнер
docker-compose up -d

# Проверьте что контейнер запущен
docker ps

# Остановка контейнера
docker-compose down
```

**Если Hibernate не создает таблицу автоматически, выполните SQL команду:**

```bash
# Подключитесь к базе данных
\c minecraft_db

# Создайте таблицу messages
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    uuid UUID NOT NULL,
    text VARCHAR(256) NOT NULL
);

# Проверьте создание таблицы
\dt

# Посмотрите структуру таблицы
\d messages
```
Или Используйте готовый SQL скрипт init.sql

**После запуска мода проверьте что данные сохраняются**:
```bash
SELECT * FROM messages;
```


