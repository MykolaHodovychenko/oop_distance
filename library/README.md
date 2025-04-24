# LibraryManagementSystem 📚

Це курсова робота з дисципліни **"Об'єктно-орієнтоване програмування"** 
(викладач: М.А. Годовиченко), виконана студенткою групи ЗАІ-231 **Сянською 
Дар'єю**.

## 🔧 Технології:
- Java 17
- Spring Boot 3.2.5
- Spring Security + JWT
- Spring Data JPA
- H2 Database
- Swagger (OpenAPI)
- Lombok
- Postman

## 🧩 Сутності:
- `User` (автентифікація, ролі USER/ADMIN)
- `Author`
- `Book`
- `LibraryMember`
- `Loan` (видача та повернення книг)
- `Payment` (оплата)

## 🚀 Основний функціонал:
- Аутентифікація та авторизація з JWT
- CRUD-операції для всіх сутностей
- Видача та повернення книг
- Оплати за позики
- Swagger-документація
- Обробка помилок
- Postman-колекції для тестування API

## 📁 Структура:
- `src/main/java/...` — основний код (контролери, сервіси, DTO, моделі, 
безпека)
- `src/main/resources` — налаштування
- `Postman JSON` — колекції запитів для API

## 📦 Postman колекції:
- `LibraryMember API.postman_collection.json`
- `LibraryManagementSystem - Book.postman_collection.json`
- `LibraryManagementSystem - Loan.postman_collection.json`
- `LibraryManagementSystem - Payments.postman_collection.json`

## 🧪 Як запустити:
1. Відкрити проєкт у IDE
2. Запустити клас `LibraryManagementSystemApplication`
3. Перейти за посиланням `http://localhost:8080/swagger-ui.html` для 
перегляду документації

---

##  Автор:
Сянська Дар’я, ЗАІ-231  
Національний університет «Одеська політехніка»  
Кафедра інформаційних систем
