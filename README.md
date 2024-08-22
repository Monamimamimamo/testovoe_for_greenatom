40# testovoe_for_greenatom
### Это решение тестового задания от Гринатома
# Эндпоинты
## POST http://localhost:9010/api/file/create
### Принимает данные в формате JSON для сохранения base64 файла в БД;
### Файл кодируется в массив байтов, чтобы занимать меньшее пространство в БД;
### Каждое поле валидируется;
### В случае повторного сохранения файла возвращается статус CONFLICT;
### В случае неудачного сохранения в БД возвращается статус INTERNAL_SERVER_ERROR;
### Эндпоинт возвращает uuid файла и статус OK;
## GET http://localhost:9010/api/file/{id}
### Принимает uuid в своём URL;
### UUID проходит валидацию;
### Если файл не найден, возвращается статус NO_CONTENT;
### Эндпоинт возвращает файл и статус OK;
## GET http://localhost:9010/api/file?page=&length=
### Принимает страницу и длину страницы пагинации в query-параметрах;
### page и length валидипуются;
### Если файлов в БД нет, то возвращает статус NO_CONTENT;
### Эндпоинт возвращает список файлов и статус OK;
## В приложении также реализованы тесты для проверки работы контроллера и бизнес-логики
# Запуск приложения
## Предлагаю 2 варианта запуска приложения:
### 1) Скачивание docker-compose.yml файла из репозитория и запуск команды docker-compose up -d
### 2) Клонирование проекта и запуск через Application класс
# Примеры запросов:
![image](https://github.com/user-attachments/assets/cd5e0da4-c85d-4072-99a5-68cc3e43ecea)
![image](https://github.com/user-attachments/assets/4102f20c-6d9b-4f9f-af8a-34cb68dabc43)
![image](https://github.com/user-attachments/assets/275a85f4-bae6-4b3b-b1f7-2c7d812a5524)


