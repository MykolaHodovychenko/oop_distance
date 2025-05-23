# Курсова робота

Завдання на курсову роботу вибирається студентом довільно з наведених нижче варіантів завдання.

### **Варіанти завдань для курсової роботи з дисципліни "Об'єктно-орієнтоване програмування"**

Кожне завдання передбачає розробку серверної частини інформаційної системи з використанням **Spring Boot**, **Spring Data JPA**, **Spring Security**, **Spring MVC**, **Hibernate** та інших технологій, що відповідають тематиці курсу.  

Вимоги до кожного варіанту:
- **6-7 сутностей** із відношеннями між ними.
- **Не менше 15 REST-запитів** (CRUD + специфічні операції).
- **2-3 операції, що потребують авторизації**.
- **Логування операцій та обробка помилок**.
- **Робота з базою даних** (PostgreSQL/MySQL/H2).
- **Захищеність API** (JWT/OAuth2).

---

### **Варіант 1: Система керування бронюванням робочих місць у коворкінгу**
#### **Сутності:**
1. `User` (Користувач)  
2. `Role` (Роль)  
3. `CoworkingSpace` (Коворкінг)  
4. `Room` (Кімната)  
5. `Workplace` (Робоче місце)  
6. `Booking` (Бронювання)  
7. `Payment` (Оплата)  

#### **Відносини:**
- `User` має `Role` (ManyToOne).  
- `CoworkingSpace` має `Room` (OneToMany).  
- `Room` містить `Workplace` (OneToMany).  
- `User` може забронювати `Workplace` (ManyToMany через `Booking`).  
- `Booking` містить `Payment` (OneToOne).  

#### **REST-запити:**
- Реєстрація користувача (`/register`).
- Авторизація користувача (`/login`).
- Створення та редагування коворкінгів (Адміністратор).
- Перегляд доступних місць для бронювання.
- Бронювання місця.
- Скасування бронювання.
- Оплата бронювання.
- Перегляд історії бронювань користувача.
- Захищена операція: **тільки адміністратор може створювати коворкінги**.
- Захищена операція: **тільки адміністратор може змінювати ціни**.
- Логування запитів до API.

---

### **Варіант 2: Система управління медичними записами**
#### **Сутності:**
1. `User` (Користувач)  
2. `Role` (Роль)  
3. `Doctor` (Лікар)  
4. `Patient` (Пацієнт)  
5. `Appointment` (Прийом)  
6. `MedicalRecord` (Медична карта)  
7. `Prescription` (Рецепт)  

#### **Відносини:**
- `User` має `Role` (ManyToOne).  
- `Doctor` веде `Patient` (OneToMany).  
- `Patient` записується на `Appointment` (OneToMany).  
- `Appointment` додається в `MedicalRecord` (OneToOne).  
- `Doctor` виписує `Prescription` (ManyToMany).  

#### **REST-запити:**
- Реєстрація та авторизація користувачів.
- Запис на прийом до лікаря.
- Перегляд історії прийомів.
- Лікар додає діагноз пацієнту.
- Лікар виписує рецепт.
- Перегляд розкладу лікаря.
- Захищена операція: **тільки лікар може додавати записи до медичної карти**.
- Захищена операція: **тільки лікар може виписувати рецепти**.
- Логування запитів до API.

---

### **Варіант 3: Система керування замовленнями у ресторані**
#### **Сутності:**
1. `User` (Користувач)  
2. `Role` (Роль)  
3. `Restaurant` (Ресторан)  
4. `MenuItem` (Позиція меню)  
5. `Order` (Замовлення)  
6. `Payment` (Оплата)  
7. `Table` (Стіл)  

#### **Відносини:**
- `User` має `Role` (ManyToOne).  
- `Restaurant` має `MenuItem` (OneToMany).  
- `User` створює `Order` (OneToMany).  
- `Order` включає `MenuItem` (ManyToMany).  
- `Order` має `Payment` (OneToOne).  

#### **REST-запити:**
- Додавання ресторану та меню (Адміністратор).
- Перегляд меню.
- Створення замовлення.
- Додавання товарів у замовлення.
- Оплата замовлення.
- Адміністратор може переглядати всі замовлення.
- Захищена операція: **тільки адміністратор може змінювати меню ресторану**.
- Захищена операція: **тільки адміністратор може переглядати всі платежі**.
- Логування запитів до API.

---

### **Варіант 4: Система управління бібліотекою**
#### **Сутності:**
1. `User` (Користувач)  
2. `Role` (Роль)  
3. `Book` (Книга)  
4. `Author` (Автор)  
5. `LibraryMember` (Член бібліотеки)  
6. `Loan` (Оренда)  
7. `Payment` (Оплата штрафу)  

#### **Відносини:**
- `User` має `Role` (ManyToOne).  
- `Book` належить `Author` (ManyToOne).  
- `LibraryMember` бере `Book` (ManyToMany через `Loan`).  
- `Loan` може мати `Payment` (OneToOne).  

#### **REST-запити:**
- Додавання книг.
- Реєстрація членів бібліотеки.
- Видача книги.
- Перегляд історії оренд.
- Оплата штрафу за прострочену книгу.
- Захищена операція: **тільки бібліотекар може видавати книги**.
- Захищена операція: **тільки бібліотекар може накладати штрафи**.
- Логування запитів до API.

---

### **Варіант 5: Система бронювання квитків на події**
#### **Сутності:**
1. `User` (Користувач)  
2. `Role` (Роль)  
3. `Event` (Подія)  
4. `Ticket` (Квиток)  
5. `Venue` (Місце проведення)  
6. `Booking` (Бронювання)  
7. `Payment` (Оплата)  

#### **Відносини:**
- `User` має `Role` (ManyToOne).  
- `Event` проходить у `Venue` (ManyToOne).  
- `User` купує `Ticket` (ManyToMany через `Booking`).  
- `Booking` має `Payment` (OneToOne).  

#### **REST-запити:**
- Додавання подій.
- Бронювання квитків.
- Оплата квитків.
- Перегляд доступних місць.
- Захищена операція: **тільки адміністратор може додавати події**.
- Захищена операція: **тільки адміністратор може скасовувати бронювання**.
- Логування запитів до API.