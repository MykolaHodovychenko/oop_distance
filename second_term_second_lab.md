### **Лабораторна робота №2 «Робота з даними, введення та виведення, обробка помилок»**

#### **Мета роботи:**
- Ознайомити студентів із розширеними можливостями роботи з даними у Spring.
- Навчити працювати з введенням, виведенням та обробкою помилок.
- Реалізувати безпеку в Spring-додатках за допомогою Spring Security (автентифікація, авторизація, JWT).
- Освоїти основи веброзробки за допомогою Spring MVC.

---

### **Хід роботи:**

1. **Налаштування проєкту:**
   - Створення Spring Boot проєкту.
   - Додавання необхідних залежностей: Spring Security, Spring Web, Spring Data JPA, H2/PostgreSQL/MySQL, JWT.

2. **Розробка моделі даних:**
   - Створення сутностей для роботи з базою даних.
   - Опис зв’язків між сутностями.

3. **Реалізація рівня безпеки:**
   - Налаштування Spring Security.
   - Реалізація механізмів аутентифікації та авторизації (JWT).
   - Визначення ролей користувачів (`USER`, `ADMIN`).

4. **Розробка контролерів:**
   - Реалізація API для CRUD-операцій.
   - Обмеження доступу до певних ресурсів на основі ролей.

5. **Обробка помилок та логування:**
   - Використання `@ExceptionHandler` для обробки помилок.
   - Логування запитів та відповідей (SLF4J, Logback).

6. **Тестування:**
   - Використання Postman для тестування REST API.
   - Реєстрація користувачів, отримання токенів, тестування доступу до ресурсів.

---

### **Варіанти завдань:**

#### **Варіант 1: Система управління користувачами**
- **Сутності:** `User`, `Role`, `Permission`
- **Відносини:** 
  - `User` має `Role` (ManyToOne)
  - `Role` має `Permission` (ManyToMany)
- **Операції REST:**
  - Реєстрація нового користувача.
  - Отримання переліку користувачів (тільки для адміністраторів).
  - Призначення ролей користувачам.

#### **Варіант 2: Система управління доступом до файлів**
- **Сутності:** `User`, `File`, `AccessRight`
- **Відносини:** 
  - `User` може мати `AccessRight` до `File` (ManyToMany)
- **Операції REST:**
  - Завантаження файлів на сервер.
  - Надання доступу до файлу певному користувачу.
  - Видалення файлу (тільки власником або адміністратором).

#### **Варіант 3: Система бронювання номерів у готелі з авторизацією**
- **Сутності:** `User`, `Hotel`, `Room`, `Booking`
- **Відносини:** 
  - `User` бронює `Room` (ManyToMany)
  - `Room` належить `Hotel` (ManyToOne)
- **Операції REST:**
  - Бронювання номера (авторизованим користувачем).
  - Перегляд доступних номерів.
  - Адміністратор може скасовувати бронювання.

#### **Варіант 4: Система управління замовленнями в інтернет-магазині**
- **Сутності:** `User`, `Order`, `Product`
- **Відносини:** 
  - `User` оформляє `Order` (OneToMany)
  - `Order` містить багато `Product` (ManyToMany)
- **Операції REST:**
  - Створення замовлення (тільки авторизованим користувачем).
  - Перегляд історії замовлень (тільки для власника).
  - Адміністратор може змінювати статус замовлення.

#### **Варіант 5: Система онлайн-курсів**
- **Сутності:** `User`, `Course`, `Lesson`
- **Відносини:** 
  - `User` записується на `Course` (ManyToMany)
  - `Course` має `Lesson` (OneToMany)
- **Операції REST:**
  - Запис на курс (авторизованим користувачем).
  - Перегляд доступних курсів.
  - Викладач може додавати нові уроки.

#### **Варіант 6: Система керування лікарськими прийомами**
- **Сутності:** `User`, `Doctor`, `Appointment`
- **Відносини:** 
  - `User` записується на `Appointment` (ManyToMany)
  - `Doctor` має `Appointment` (OneToMany)
- **Операції REST:**
  - Запис на прийом (авторизованим користувачем).
  - Лікар може підтверджувати запис.
  - Адміністратор може скасовувати прийом.

#### **Варіант 7: CRM-система**
- **Сутності:** `User`, `Client`, `Contract`
- **Відносини:** 
  - `User` керує `Client` (OneToMany)
  - `Client` має `Contract` (OneToMany)
- **Операції REST:**
  - Менеджер може додавати нових клієнтів.
  - Перегляд усіх клієнтів (тільки авторизованими менеджерами).
  - Адміністратор може переглядати всі контракти.

#### **Варіант 8: Система управління спортивним клубом**
- **Сутності:** `User`, `Trainer`, `WorkoutSession`
- **Відносини:** 
  - `User` записується на `WorkoutSession` (ManyToMany)
  - `Trainer` проводить `WorkoutSession` (OneToMany)
- **Операції REST:**
  - Реєстрація в систему (новий користувач).
  - Запис на тренування (авторизованим користувачем).
  - Адміністратор може керувати тренуваннями.

#### **Варіант 9: Система бронювання авіаквитків**
- **Сутності:** `User`, `Flight`, `Ticket`
- **Відносини:** 
  - `User` купує `Ticket` (ManyToMany)
  - `Flight` містить `Ticket` (OneToMany)
- **Операції REST:**
  - Купівля квитка (тільки авторизованими користувачами).
  - Перегляд доступних рейсів.
  - Адміністратор може змінювати статус рейсів.

#### **Варіант 10: Система управління бібліотекою**
- **Сутності:** `User`, `Book`, `Loan`
- **Відносини:** 
  - `User` бере `Book` в оренду (ManyToMany)
  - `Book` має `Loan` (OneToMany)
- **Операції REST:**
  - Видача книги читачу (авторизованим користувачем).
  - Перегляд списку доступних книг.
  - Адміністратор може додавати нові книги.