### **Лабораторна робота №1 «Розробка простого корпоративного додатку за допомогою Java Spring»**

#### **Мета роботи:**
- Ознайомити студентів із основними компонентами фреймворку Spring.
- Навчити створювати базові корпоративні додатки з використанням Spring Boot.
- Реалізувати контролери для роботи з RESTful веб-сервісами.
- Інтегрувати базу даних за допомогою Spring Data JPA.
- Виконувати CRUD-операції через REST API.

---

### **Хід роботи:**
1. **Підготовка середовища:**
   - Встановлення JDK, Maven/Gradle, та IntelliJ IDEA (або іншого IDE).
   - Створення нового проєкту Spring Boot (Spring Initializr).
   - Додавання залежностей: Spring Web, Spring Data JPA, H2/PostgreSQL/MySQL.

2. **Розробка моделі даних:**
   - Опис сутностей у вигляді класів Java з анотаціями JPA.
   - Визначення зв’язків між сутностями (@OneToMany, @ManyToOne, @ManyToMany).

3. **Створення репозиторіїв:**
   - Використання `JpaRepository` для збереження та отримання даних.

4. **Реалізація контролерів REST:**
   - Реалізація методів для отримання, створення, оновлення та видалення сутностей.
   - Використання `@RestController` та `@RequestMapping`.

5. **Тестування REST API:**
   - Використання Postman або cURL для перевірки запитів.

6. **Розширення функціоналу (опціонально):**
   - Валідація вхідних даних (`@Valid`, `@NotNull`).
   - Використання `DTO` для передачі даних.
   - Логування та обробка винятків.

---

### **Варіанти завдань:**

#### **Варіант 1: Система управління співробітниками компанії**
- **Сутності:** `Employee`, `Department`, `Project`
- **Відносини:** 
  - `Employee` працює в `Department` (ManyToOne)
  - `Employee` бере участь у `Project` (ManyToMany)
- **Операції REST:**
  - Додавання, оновлення, видалення співробітників.
  - Отримання списку співробітників у відділі.
  - Додавання співробітника до проекту.

#### **Варіант 2: Система бронювання готелів**
- **Сутності:** `Hotel`, `Room`, `Booking`
- **Відносини:** 
  - `Hotel` містить багато `Room` (OneToMany)
  - `Booking` прив’язаний до `Room` (ManyToOne)
- **Операції REST:**
  - Додавання нових номерів у готель.
  - Бронювання номера.
  - Отримання всіх бронювань для певного готелю.

#### **Варіант 3: Управління замовленнями в інтернет-магазині**
- **Сутності:** `Customer`, `Order`, `Product`
- **Відносини:** 
  - `Customer` оформляє `Order` (OneToMany)
  - `Order` містить багато `Product` (ManyToMany)
- **Операції REST:**
  - Створення нового замовлення.
  - Додавання товарів до замовлення.
  - Отримання списку замовлень для конкретного клієнта.

#### **Варіант 4: Бібліотечна система**
- **Сутності:** `Book`, `Author`, `LibraryMember`
- **Відносини:** 
  - `Book` належить `Author` (ManyToOne)
  - `LibraryMember` бере `Book` (ManyToMany)
- **Операції REST:**
  - Додавання книг та авторів.
  - Реєстрація нового читача.
  - Видача книги читачу.

#### **Варіант 5: Платформа для управління курсами**
- **Сутності:** `Course`, `Instructor`, `Student`
- **Відносини:** 
  - `Instructor` викладає `Course` (OneToMany)
  - `Student` записується на `Course` (ManyToMany)
- **Операції REST:**
  - Додавання курсів.
  - Реєстрація студентів на курс.
  - Отримання всіх курсів викладача.

#### **Варіант 6: Система оренди автомобілів**
- **Сутності:** `Car`, `Rental`, `Customer`
- **Відносини:** 
  - `Customer` орендує `Car` (OneToMany)
  - `Rental` містить дані про оренду (ManyToOne)
- **Операції REST:**
  - Додавання нових авто.
  - Оформлення оренди.
  - Отримання списку активних оренд.

#### **Варіант 7: CRM-система для компанії**
- **Сутності:** `Client`, `Contract`, `Service`
- **Відносини:** 
  - `Client` укладає `Contract` (OneToMany)
  - `Contract` включає `Service` (ManyToMany)
- **Операції REST:**
  - Реєстрація нового клієнта.
  - Додавання контрактів.
  - Отримання послуг у рамках контракту.

#### **Варіант 8: Система управління медичним закладом**
- **Сутності:** `Doctor`, `Patient`, `Appointment`
- **Відносини:** 
  - `Doctor` має багато `Appointment` (OneToMany)
  - `Patient` записується на `Appointment` (ManyToMany)
- **Операції REST:**
  - Додавання лікарів.
  - Запис пацієнтів на прийом.
  - Отримання розкладу лікаря.

#### **Варіант 9: Система бронювання авіаквитків**
- **Сутності:** `Flight`, `Passenger`, `Ticket`
- **Відносини:** 
  - `Flight` має багато `Ticket` (OneToMany)
  - `Passenger` купує `Ticket` (ManyToMany)
- **Операції REST:**
  - Додавання нових рейсів.
  - Купівля квитка.
  - Перегляд розкладу рейсів.

#### **Варіант 10: Система управління спортивним клубом**
- **Сутності:** `Trainer`, `Member`, `TrainingSession`
- **Відносини:** 
  - `Trainer` проводить `TrainingSession` (OneToMany)
  - `Member` бере участь у `TrainingSession` (ManyToMany)
- **Операції REST:**
  - Додавання тренерів.
  - Запис членів клубу на тренування.
  - Отримання розкладу тренувань.
