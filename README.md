# Деплоймент приложения

## Вариант 1: Развертывание через Docker (рекомендуемый)

```bash
# 1. Клонировать репозиторий
git clone https://github.com/termixxx/library.git

# 2. Перейти в директорию проекта
cd library/

# 3. Запустить сборку и запуск контейнеров
docker-compose up --build

# 4. После запуска приложение будет доступно по адресу:
#    http://localhost:8080/
```

## Вариант 2: Ручное развертывание
### 1. Подготовка базы данных
```
create database library;
```
### 2. Запуск приложения
```
# 1. Клонировать репозиторий
git clone https://github.com/termixxx/library.git

# 2. Перейти в директорию проекта
cd library/

# 3. Установить зависимости и собрать проект
mvn clean package

# 4. Запустить приложение
java -jar target/library-app.jar 
