# Об'єктно-орієнтоване програмування
## Семестр 2. Основи Spring Boot
### Теми 1–3. Розширений конспект для заочної форми навчання

Цей матеріал є стислим, але достатньо пояснювальним конспектом для самостійного опрацювання. Його мета — не просто показати анотації Spring, а пояснити, навіщо вони потрібні, як пов'язані з об'єктно-орієнтованим програмуванням і яку проблему вирішують.

---

# Тема 1. Dependency Injection та Inversion of Control у Java-застосунках

## 1.1. Від окремих класів до системи об'єктів

Під час вивчення основ ООП ми часто працюємо з невеликими прикладами:

```java
public class User {
    private String name;
}
```

або:

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

У реальних програмах ситуація інша. Класи взаємодіють між собою. Один об'єкт викликає інший, другий звертається до третього, а третій може працювати з базою даних.

Наприклад:

```text
OrderController
      |
      v
OrderService
      |
      v
PaymentService
      |
      v
PaymentRepository
```

Тобто застосунок — це не просто набір класів, а мережа пов'язаних між собою об'єктів.

Саме тому важливо не лише вміти створити окремий клас, а й правильно організувати залежності між класами.

---

## 1.2. Що таке залежність між класами

Розглянемо приклад:

```java
public class OrderService {

    private PaymentService paymentService;

    public OrderService() {
        paymentService = new PaymentService();
    }

    public void createOrder() {
        paymentService.pay();
    }
}
```

Клас `OrderService` використовує `PaymentService`.

Отже:

```text
OrderService залежить від PaymentService
```

Інакше кажучи, `OrderService` не може виконати свою роботу без об'єкта `PaymentService`.

Така залежність сама по собі є нормальною. Проблема виникає в тому, **як саме створюється залежний об'єкт**.

У прикладі вище `OrderService` сам вирішує:

```java
paymentService = new PaymentService();
```

Тобто клас виконує одразу дві ролі:

1. реалізує бізнес-логіку;
2. керує створенням іншого об'єкта.

Це збільшує зв'язаність класів.

---

## 1.3. Що таке сильна зв'язаність

Сильна зв'язаність означає, що один клас дуже залежить від конкретної реалізації іншого.

Наприклад:

```java
public class OrderService {

    private final CardPaymentService paymentService;

    public OrderService() {
        this.paymentService = new CardPaymentService();
    }
}
```

Тут `OrderService` жорстко прив'язаний до `CardPaymentService`.

Якщо завтра потрібно перейти на:

```java
PayPalPaymentService
```

доведеться змінювати код `OrderService`.

У невеликій навчальній програмі це здається неважливим. Але у великому застосунку таких класів можуть бути сотні.

Тоді кожна заміна реалізації породжує багато змін.

---

## 1.4. Ідея слабкої зв'язаності

Мета хорошої архітектури — зробити так, щоб класи якомога менше знали про конкретні реалізації інших класів.

Наприклад, створимо інтерфейс:

```java
public interface PaymentService {
    void pay();
}
```

Реалізація:

```java
public class CardPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Оплата карткою");
    }
}
```

Інша реалізація:

```java
public class CashPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Оплата готівкою");
    }
}
```

Тепер `OrderService` залежить не від конкретного класу, а від абстракції:

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void createOrder() {
        paymentService.pay();
        System.out.println("Замовлення створено");
    }
}
```

Це важливий момент.

`OrderService` знає лише:

```text
мені потрібен об'єкт, який уміє виконувати pay()
```

А який саме це буде об'єкт — `CardPaymentService`, `CashPaymentService` чи інший — для нього неважливо.

---

## 1.5. Dependency Injection

Dependency Injection, скорочено DI, означає **впровадження залежностей**.

Головна ідея:

> Об'єкт не повинен сам створювати свої залежності. Він повинен отримувати їх ззовні.

Порівняємо.

### Варіант без DI

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService() {
        this.paymentService = new CardPaymentService();
    }
}
```

### Варіант із DI

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Тепер створення об'єктів відбувається окремо:

```java
PaymentService paymentService = new CardPaymentService();
OrderService orderService = new OrderService(paymentService);
```

Або:

```java
PaymentService paymentService = new CashPaymentService();
OrderService orderService = new OrderService(paymentService);
```

Клас `OrderService` при цьому не змінюється.

---

## 1.6. Чому DI важливий

Dependency Injection дає декілька важливих переваг.

### 1. Клас відповідає лише за свою логіку

`OrderService` повинен працювати із замовленням, а не вирішувати, який платіжний сервіс створити.

### 2. Реалізацію можна замінити

Можна передати інший об'єкт, що реалізує той самий інтерфейс.

### 3. Код легше підтримувати

Зміни в одному компоненті менше впливають на інші.

### 4. Залежності видно явно

Коли залежність передається через конструктор, одразу видно, без яких об'єктів клас не може працювати.

---

## 1.7. Constructor Injection

Найбільш зрозумілий спосіб DI — через конструктор.

```java
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notifyUser() {
        emailService.sendEmail();
    }
}
```

Тут залежність передається при створенні об'єкта.

Це добре з кількох причин.

По-перше, неможливо створити `NotificationService` без `EmailService`.

По-друге, поле можна зробити `final`:

```java
private final EmailService emailService;
```

Це означає, що посилання не буде випадково змінено після створення об'єкта.

По-третє, всі залежності класу видно в одному місці — у конструкторі.

---

## 1.8. Приклад із кількома залежностями

У реальному сервісі залежність часто не одна.

```java
public class OrderService {

    private final PaymentService paymentService;
    private final NotificationService notificationService;

    public OrderService(
            PaymentService paymentService,
            NotificationService notificationService) {

        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public void createOrder() {
        paymentService.pay();
        notificationService.notifyUser();
    }
}
```

Щоб створити цей об'єкт вручну:

```java
PaymentService paymentService = new CardPaymentService();
NotificationService notificationService = new NotificationService();

OrderService orderService =
        new OrderService(paymentService, notificationService);
```

Якщо залежностей стає багато, ручне створення всієї структури стає незручним.

Саме тут і з'являється Spring.

---

## 1.9. Inversion of Control

Inversion of Control, або IoC, перекладають як **інверсія керування**.

У звичайному Java-коді програміст сам керує створенням об'єктів:

```java
PaymentService paymentService = new CardPaymentService();
OrderService orderService = new OrderService(paymentService);
```

Тобто програма сама контролює:

- коли створити об'єкт;
- який саме клас створити;
- які залежності передати.

При використанні Spring частина цього керування передається контейнеру Spring.

Тобто:

```text
Було:

наш код -> створює об'єкти -> зв'язує їх

Стало:

Spring Container -> створює об'єкти -> зв'язує їх -> передає нашому коду
```

У цьому і є «інверсія»: керування переходить від нашого коду до фреймворку.

---

## 1.10. Зв'язок між IoC та DI

Ці терміни часто плутають.

Найпростіше запам'ятати так:

```text
IoC — загальний принцип.

DI — один із способів реалізації IoC.
```

Тобто Spring використовує Dependency Injection для того, щоб реалізувати Inversion of Control.

---

## 1.11. Що робитиме Spring у наступних темах

Припустимо, є:

```java
public class UserRepository {
}
```

```java
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

Без Spring ми повинні написати:

```java
UserRepository repository = new UserRepository();
UserService service = new UserService(repository);
```

У Spring ми повідомимо:

```text
UserRepository — це компонент
UserService — це компонент
UserService залежить від UserRepository
```

А Spring сам виконає створення об'єктів.

---

## 1.12. Важливий момент: Spring не скасовує ООП

Іноді після першого знайомства зі Spring здається, що анотації замінюють звичайний Java-код.

Це неправильне розуміння.

Spring працює **поверх звичайного ООП**.

Усе ще існують:

- класи;
- об'єкти;
- інтерфейси;
- конструктори;
- композиція;
- поліморфізм.

Spring лише автоматизує створення та зв'язування об'єктів.

Тому знання ООП безпосередньо потрібне для розуміння Spring.

---

## 1.13. Типова помилка початківця

Поганий варіант:

```java
public class OrderService {

    public void createOrder() {
        PaymentService paymentService = new PaymentService();
        paymentService.pay();
    }
}
```

Чому це погано:

- об'єкт створюється всередині методу;
- клас жорстко прив'язаний до конкретної реалізації;
- залежність не видно із структури класу;
- її складніше замінити.

Краще:

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void createOrder() {
        paymentService.pay();
    }
}
```

---

## 1.14. Підсумок теми

Після цієї теми потрібно розуміти:

```text
Залежність
    ↓
один клас використовує інший
    ↓
якщо клас сам створює залежність
    ↓
зростає зв'язаність
    ↓
Dependency Injection
    ↓
залежність передається ззовні
    ↓
IoC
    ↓
керування створенням об'єктів передається контейнеру
```

---

# Тема 2. Основи Spring Framework: контейнер, біни та компоненти

## 2.1. Що таке Spring Framework

Spring Framework — це Java-фреймворк, який допомагає створювати складні застосунки з великою кількістю взаємопов'язаних компонентів.

Для початківця не потрібно знати весь Spring.

На цьому етапі достатньо зрозуміти три основні речі:

1. Spring створює об'єкти.
2. Spring зберігає ці об'єкти в контейнері.
3. Spring автоматично передає одні об'єкти іншим як залежності.

Саме це є основою Spring-застосунку.

---

## 2.2. Що таке Spring Container

Spring Container — це частина фреймворку, яка керує об'єктами.

Можна уявити контейнер як спеціальне сховище об'єктів.

Наприклад:

```text
Spring Container
|
+-- UserService
+-- OrderService
+-- PaymentService
+-- UserRepository
```

Проте контейнер не просто зберігає їх.

Він також:

- створює об'єкти;
- визначає їх залежності;
- передає залежності;
- керує життєвим циклом об'єктів.

Основним контейнером Spring є `ApplicationContext`.

---

## 2.3. Що таке Bean

Spring Bean — це **об'єкт, створенням і життєвим циклом якого керує Spring**.

Наприклад, звичайний об'єкт:

```java
UserService service = new UserService();
```

Цей об'єкт створили ми самі.

Spring Bean — це той самий звичайний Java-об'єкт, але його створив Spring.

Наприклад:

```java
@Service
public class UserService {
}
```

Spring знаходить цей клас і створює його екземпляр.

Отже, важливо:

> Bean — це не спеціальний тип класу. Це звичайний Java-об'єкт, яким керує Spring.

---

## 2.4. Як Spring розуміє, які класи потрібно створити

Для цього використовуються анотації.

Найбільш загальна:

```java
@Component
```

Наприклад:

```java
import org.springframework.stereotype.Component;

@Component
public class FileStorage {
}
```

Spring бачить `@Component` і розуміє:

```text
цей клас потрібно створити як Bean
```

---

## 2.5. Спеціалізовані компоненти

У Spring є кілька анотацій, які фактично є спеціалізованими варіантами `@Component`.

### @Service

```java
@Service
public class OrderService {
}
```

Зазвичай використовується для бізнес-логіки.

### @Repository

```java
@Repository
public class UserRepository {
}
```

Використовується для роботи з даними.

### @Controller

```java
@Controller
public class PageController {
}
```

Використовується у класичних MVC-вебзастосунках.

### @RestController

```java
@RestController
public class UserController {
}
```

Використовується для REST API.

На технічному рівні Spring міг би працювати і з `@Component`, але спеціалізовані анотації роблять код зрозумілішим.

---

## 2.6. Навіщо розділяти @Service, @Repository та @Controller

Розглянемо структуру:

```text
controller
service
repository
```

Кожен шар має свою відповідальність.

### Controller

Приймає запит від клієнта.

```java
@RestController
public class UserController {
}
```

### Service

Виконує бізнес-логіку.

```java
@Service
public class UserService {
}
```

### Repository

Працює з даними.

```java
@Repository
public class UserRepository {
}
```

Таким чином код стає структурованим.

---

## 2.7. Component Scanning

Spring повинен знайти класи з анотаціями.

Цей процес називається **Component Scanning**.

Наприклад, проєкт має структуру:

```text
com.example.demo
|
+-- controller
|   +-- UserController
|
+-- service
|   +-- UserService
|
+-- repository
|   +-- UserRepository
|
+-- DemoApplication
```

Якщо головний клас знаходиться в пакеті:

```text
com.example.demo
```

то Spring Boot автоматично сканує цей пакет та всі вкладені пакети.

Тому рекомендується розміщувати головний клас у кореневому пакеті проєкту.

---

## 2.8. Приклад DI у Spring

Створимо компонент:

```java
@Service
public class EmailService {

    public void send() {
        System.out.println("Email sent");
    }
}
```

І ще один:

```java
@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notifyUser() {
        emailService.send();
    }
}
```

Що робить Spring:

```text
1. знаходить EmailService
2. створює об'єкт EmailService
3. знаходить NotificationService
4. бачить конструктор NotificationService(EmailService)
5. знаходить готовий EmailService
6. передає його в NotificationService
```

Ми не пишемо:

```java
new EmailService()
```

Саме це і є практична реалізація DI.

---

## 2.9. Чи потрібен @Autowired

Можна зустріти старіший стиль:

```java
@Autowired
private EmailService emailService;
```

Це називається field injection.

У навчальних проєктах краще використовувати constructor injection:

```java
private final EmailService emailService;

public NotificationService(EmailService emailService) {
    this.emailService = emailService;
}
```

Якщо в класі один конструктор, сучасний Spring сам розуміє, що його потрібно використовувати для DI.

Тобто:

```java
@Autowired
```

над конструктором зазвичай не потрібен.

---

## 2.10. Що відбувається, якщо Bean не знайдено

Припустимо:

```java
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

А клас `UserRepository` не позначений:

```java
public class UserRepository {
}
```

Spring не створить для нього Bean.

Тоді під час запуску буде помилка: Spring не зможе знайти залежність типу `UserRepository`.

Правильно:

```java
@Repository
public class UserRepository {
}
```

---

## 2.11. Що відбувається, якщо є дві реалізації

Інтерфейс:

```java
public interface MessageService {
    void send(String text);
}
```

Дві реалізації:

```java
@Service
public class EmailMessageService implements MessageService {
    public void send(String text) {
        System.out.println("Email: " + text);
    }
}
```

```java
@Service
public class SmsMessageService implements MessageService {
    public void send(String text) {
        System.out.println("SMS: " + text);
    }
}
```

І сервіс:

```java
@Service
public class NotificationService {

    private final MessageService messageService;

    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

Тепер Spring бачить дві відповідні реалізації.

Він не знає, яку вибрати.

Для розв'язання використовують, наприклад:

```java
@Primary
```

або:

```java
@Qualifier
```

На базовому курсі достатньо розуміти саму проблему.

---

## 2.12. Singleton у Spring

За замовчуванням Bean у Spring має scope `singleton`.

Це означає:

> Для одного Bean Spring зазвичай створює один об'єкт у межах контейнера.

Наприклад:

```java
@Service
public class UserService {
}
```

Якщо цей сервіс потрібен у двох різних класах, Spring зазвичай передасть один і той самий екземпляр.

Це важливий момент.

Тому в Spring Service не варто без необхідності зберігати змінний стан, пов'язаний з конкретним користувачем або запитом.

Наприклад, небажано:

```java
@Service
public class UserService {

    private String currentUserName;
}
```

Бо один Bean може використовуватися багатьма запитами.

---

## 2.13. Життєвий цикл Bean

Спрощено життєвий цикл виглядає так:

```text
запуск застосунку
      |
      v
сканування компонентів
      |
      v
створення Bean
      |
      v
встановлення залежностей
      |
      v
Bean готовий до використання
      |
      v
завершення застосунку
```

На базовому курсі не потрібно глибоко вивчати всі callback-методи життєвого циклу.

Головне — розуміти, що Bean створює та контролює не наш код, а Spring.

---

## 2.14. ApplicationContext

`ApplicationContext` — центральний інтерфейс Spring Container.

Теоретично можна вручну отримати Bean:

```java
UserService service =
        applicationContext.getBean(UserService.class);
```

Але в нормальному Spring Boot-застосунку так роблять рідко.

Правильніший підхід — передавати залежності через конструктор.

---

## 2.15. Типова архітектура

Вже на цьому етапі корисно запам'ятати типову схему:

```text
HTTP request
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
Database
```

Поки що база даних ще не потрібна, але саме до такої структури ми поступово прийдемо.

---

## 2.16. Основна думка теми

Spring Framework потрібен не тому, що Java «не вміє» створювати об'єкти.

Java чудово вміє:

```java
new UserService()
```

Spring потрібен тому, що у великому застосунку дуже багато об'єктів і залежностей.

Spring бере на себе рутинне керування цією структурою.

---

# Тема 3. Вступ до Spring Boot. Структура та запуск проєкту

## 3.1. Spring Framework і Spring Boot — не одне й те саме

Це дуже важливий момент.

Spring Framework — це основний фреймворк.

Spring Boot — це інструмент, який значно спрощує використання Spring Framework.

Можна умовно записати:

```text
Spring Framework
    +
автоконфігурація
    +
готові starter-залежності
    +
вбудований сервер
    =
Spring Boot
```

Spring Boot не замінює Spring Framework.

Він працює на його основі.

---

## 3.2. Навіщо з'явився Spring Boot

Раніше створення Spring-застосунку вимагало значної кількості конфігурації.

Потрібно було:

- підключати багато бібліотек вручну;
- налаштовувати сервер;
- описувати конфігурацію;
- створювати додаткові XML-файли.

Spring Boot спрощує старт проєкту.

Тепер можна створити застосунок, додати одну starter-залежність і одразу запустити сервер.

---

## 3.3. Що потрібно встановити

Для базового курсу достатньо:

- JDK;
- IntelliJ IDEA або іншої IDE;
- браузера;
- доступу до Інтернету для першого завантаження залежностей Maven.

База даних на перших трьох темах не потрібна.

Вона буде встановлюватися пізніше, коли почнеться JPA та Spring Data JPA.

---

## 3.4. Встановлення JDK

Spring Boot працює на Java, тому спочатку потрібно мати JDK.

Для сучасних навчальних проєктів доцільно використовувати Java 17 або Java 21, залежно від версії Spring Boot.

Перевірити Java:

```bash
java -version
```

Перевірити компілятор:

```bash
javac -version
```

Приклад:

```text
openjdk version "21.0.4"
```

Якщо команда не знайдена, JDK не встановлений або не доданий у `PATH`.

---

## 3.5. Що таке JDK

JDK — Java Development Kit.

Він містить інструменти для розробки Java-програм.

Зокрема:

```text
java  — запуск програми
javac — компіляція Java-коду
```

Не потрібно плутати JDK та JVM.

Спрощено:

```text
JDK — набір інструментів для розробника
JVM — середовище виконання Java bytecode
```

---

## 3.6. Встановлення IntelliJ IDEA

Для навчального курсу зручно використовувати IntelliJ IDEA.

Після встановлення необхідно переконатися, що IDE бачить JDK.

У проєкті повинна бути вибрана правильна версія Java.

Типова проблема початківця:

```text
JDK встановлений у системі,
але IDE використовує іншу версію Java
```

Тому при помилках компіляції варто перевірити Project SDK.

---

## 3.7. Створення проєкту через Spring Initializr

Spring Initializr — це генератор Spring Boot-проєктів.

У ньому потрібно вибрати:

```text
Project: Maven
Language: Java
Packaging: Jar
Java: 17 або 21
```

Приклад:

```text
Group: com.example
Artifact: demo
Name: demo
```

У Dependencies для першого проєкту додамо:

```text
Spring Web
```

Після генерації буде завантажено ZIP-архів.

Його потрібно:

1. розпакувати;
2. відкрити папку проєкту в IDE;
3. дочекатися, поки Maven завантажить залежності.

---

## 3.8. Що відбувається при першому відкритті

Коли IntelliJ IDEA відкриває Maven-проєкт, вона читає:

```text
pom.xml
```

і бачить список залежностей.

Потім Maven завантажує необхідні бібліотеки.

Тому перший запуск може бути повільнішим.

Якщо залежності не завантажилися, код Spring може підсвічуватися червоним.

Це не завжди означає помилку в коді.

Іноді потрібно дочекатися завершення Maven import.

---

## 3.9. Структура Spring Boot-проєкту

Типова структура:

```text
demo
|
+-- src
|   |
|   +-- main
|   |   |
|   |   +-- java
|   |   |   |
|   |   |   +-- com.example.demo
|   |   |       |
|   |   |       +-- DemoApplication.java
|   |   |
|   |   +-- resources
|   |       |
|   |       +-- application.properties
|   |
|   +-- test
|
+-- pom.xml
+-- mvnw
+-- mvnw.cmd
```

---

## 3.10. src/main/java

Тут знаходиться Java-код програми.

Наприклад:

```text
src/main/java/com/example/demo
```

Це основний пакет застосунку.

Саме всередині нього доцільно створювати:

```text
controller
service
repository
model
dto
```

---

## 3.11. src/main/resources

Тут зберігаються ресурси та конфігураційні файли.

Найважливіший на початку:

```text
application.properties
```

або альтернативно:

```text
application.yml
```

На цьому курсі можна використовувати `application.properties`, оскільки він простіший для початківця.

---

## 3.12. pom.xml

`pom.xml` — конфігураційний файл Maven.

У ньому описуються:

- назва проєкту;
- версія;
- Java;
- залежності;
- плагіни збірки.

Наприклад:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Ця залежність підключає набір бібліотек для створення вебзастосунку.

---

## 3.13. Що таке starter

Starter — це готовий набір узгоджених залежностей.

Наприклад:

```text
spring-boot-starter-web
```

підключає все основне, що потрібно для вебзастосунку.

Без starter довелося б вручну підключати кілька різних бібліотек.

Пізніше ми використаємо:

```text
spring-boot-starter-data-jpa
spring-boot-starter-validation
```

---

## 3.14. Головний клас застосунку

Spring Initializr створює:

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

Це точка входу в програму.

---

## 3.15. @SpringBootApplication

Ця анотація дуже важлива.

```java
@SpringBootApplication
```

Вона об'єднує кілька механізмів Spring Boot.

На базовому рівні достатньо розуміти, що вона:

- позначає головний клас;
- вмикає автоматичну конфігурацію;
- запускає пошук компонентів;
- допомагає створити Spring Container.

---

## 3.16. SpringApplication.run

Рядок:

```java
SpringApplication.run(DemoApplication.class, args);
```

запускає Spring Boot.

Спрощено відбувається так:

```text
main()
  |
  v
SpringApplication.run()
  |
  v
створення ApplicationContext
  |
  v
пошук компонентів
  |
  v
створення Bean
  |
  v
запуск вебсервера
```

---

## 3.17. Вбудований Tomcat

Коли використовується:

```text
spring-boot-starter-web
```

Spring Boot зазвичай запускає вбудований Tomcat.

Тобто окремо встановлювати Tomcat для базового проєкту не потрібно.

Це важлива перевага Spring Boot.

Після запуску сервер зазвичай працює на:

```text
http://localhost:8080
```

---

## 3.18. Що таке localhost

`localhost` означає:

```text
цей комп'ютер
```

Тобто:

```text
http://localhost:8080
```

означає:

```text
звернутися до вебсервера на цьому ж комп'ютері через порт 8080
```

---

## 3.19. Що таке порт

На одному комп'ютері можуть одночасно працювати різні мережеві програми.

Порт допомагає визначити, до якої саме програми звертається клієнт.

Наприклад:

```text
localhost:8080
```

де:

```text
localhost — комп'ютер
8080      — порт
```

---

## 3.20. Перший запуск

Запустіть:

```java
DemoApplication.main()
```

У консолі з'являться повідомлення Spring.

Серед них важливо побачити приблизно:

```text
Tomcat started on port 8080
Started DemoApplication
```

Це означає, що програма запущена.

---

## 3.21. Чому при відкритті localhost:8080 може бути помилка

Якщо ми ще не створили жодного Controller, то при відкритті:

```text
http://localhost:8080
```

можемо побачити сторінку помилки.

Це нормально.

Сервер працює, але в застосунку ще немає маршруту для `/`.

---

## 3.22. Перший REST Controller

Створимо пакет:

```text
controller
```

І клас:

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }
}
```

Після цього відкриваємо:

```text
http://localhost:8080/hello
```

Отримаємо:

```text
Hello Spring Boot!
```

---

## 3.23. Що робить @RestController

```java
@RestController
```

повідомляє Spring:

> Цей клас приймає HTTP-запити і повертає дані клієнту.

Spring сам створить об'єкт `HelloController`, тому вручну писати:

```java
new HelloController()
```

не потрібно.

Це знову зв'язок із темами 1 і 2.

---

## 3.24. Що робить @GetMapping

```java
@GetMapping("/hello")
```

означає:

> Якщо клієнт виконає HTTP GET-запит на `/hello`, потрібно викликати цей метод.

Метод:

```java
public String hello() {
    return "Hello Spring Boot!";
}
```

повертає дані у HTTP-відповіді.

---

## 3.25. Повний шлях запиту

Якщо сервер працює на:

```text
localhost:8080
```

а mapping:

```java
@GetMapping("/hello")
```

то адреса буде:

```text
http://localhost:8080/hello
```

---

## 3.26. Повернення Java-об'єкта

REST API зазвичай повертає не простий текст, а структуровані дані.

Наприклад:

```java
public class User {

    private Long id;
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

Контролер:

```java
@RestController
public class UserController {

    @GetMapping("/user")
    public User getUser() {
        return new User(1L, "Anna");
    }
}
```

Результат:

```json
{
  "id": 1,
  "name": "Anna"
}
```

---

## 3.27. Звідки береться JSON

Ми не пишемо код для перетворення:

```java
User
```

у:

```json
{
  "id": 1,
  "name": "Anna"
}
```

Spring Boot робить це автоматично за допомогою бібліотеки Jackson, яка входить до Web Starter.

Це приклад автоматизації, яку надає Spring Boot.

---

## 3.28. application.properties

Файл:

```text
src/main/resources/application.properties
```

містить налаштування.

Наприклад:

```properties
spring.application.name=demo
```

Зміна порту:

```properties
server.port=8081
```

Після цього адреса:

```text
http://localhost:8081
```

---

## 3.29. Типова помилка: порт зайнятий

Якщо порт `8080` уже використовується іншою програмою, Spring Boot не запуститься.

Можна побачити повідомлення, що порт уже зайнятий.

Найпростіше рішення для навчального проєкту:

```properties
server.port=8081
```

---

## 3.30. Maven Wrapper

У проєкті є:

```text
mvnw
mvnw.cmd
```

Це Maven Wrapper.

Він дозволяє запускати Maven-команди без окремого глобального встановлення Maven.

Windows:

```bat
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

---

## 3.31. Збірка проєкту

Windows:

```bat
mvnw.cmd clean package
```

Linux/macOS:

```bash
./mvnw clean package
```

Maven:

1. очищає попередню збірку;
2. компілює код;
3. збирає `.jar`.

Файл з'явиться в:

```text
target
```

---

## 3.32. Запуск JAR

Наприклад:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Після цього застосунок запускається так само, як через IDE.

Це важливо: Spring Boot-програму можна запускати як звичайний виконуваний Java JAR.

---

## 3.33. Рекомендована структура пакетів

Для подальшого курсу:

```text
com.example.demo
|
+-- controller
+-- service
+-- repository
+-- model
+-- dto
|
+-- DemoApplication.java
```

Призначення:

```text
controller — приймає HTTP-запити
service    — містить бізнес-логіку
repository — працює з даними
model      — описує об'єкти предметної області
dto        — використовується для передавання даних
```

---

## 3.34. Важлива рекомендація щодо пакетів

Не варто створювати пакети за межами кореневого пакета головного класу.

Добре:

```text
com.example.demo
com.example.demo.controller
com.example.demo.service
```

Погано:

```text
com.example.demo
org.other.controller
```

У другому випадку Spring може не знайти компоненти автоматично.

---

## 3.35. Як пов'язані теми 1, 2 і 3

Це не три незалежні теми.

Вони утворюють одну логічну послідовність.

```text
Тема 1:
об'єкти мають залежності
        ↓
DI допомагає передавати залежності

Тема 2:
Spring Container автоматично створює об'єкти
        ↓
ці об'єкти називаються Bean

Тема 3:
Spring Boot спрощує запуск Spring
        ↓
ми створюємо перший вебзастосунок
```

Якщо ця логіка зрозуміла, подальші анотації Spring сприйматимуться набагато простіше.

---

# Короткий практичний приклад, що об'єднує три теми

Створимо сервіс:

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String getGreeting() {
        return "Hello from service!";
    }
}
```

Створимо контролер:

```java
package com.example.demo.controller;

import com.example.demo.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greeting")
    public String greeting() {
        return greetingService.getGreeting();
    }
}
```

Що тут відбувається:

```text
@Service
   ↓
Spring створює GreetingService

@RestController
   ↓
Spring створює GreetingController

конструктор GreetingController(GreetingService)
   ↓
Spring передає GreetingService у Controller
```

Коли користувач відкриває:

```text
http://localhost:8080/greeting
```

Spring викликає:

```java
greeting()
```

а той звертається до:

```java
greetingService.getGreeting()
```

Це вже маленький, але повноцінний приклад Spring Boot-архітектури.

---

# Типові помилки студентів

## 1. Створення Spring-компонента через new

Наприклад:

```java
GreetingService service = new GreetingService();
```

Усередині Spring-компонентів цього зазвичай робити не потрібно.

Якщо клас є Bean, його повинен створювати Spring.

---

## 2. Відсутня анотація компонента

```java
public class UserService {
}
```

Якщо Spring повинен керувати цим класом, потрібно, наприклад:

```java
@Service
public class UserService {
}
```

---

## 3. Controller створений не в тому пакеті

Якщо компонент знаходиться поза зоною component scanning, Spring його не знайде.

---

## 4. Використання неправильного порту

Якщо в:

```properties
server.port=8081
```

то адреса:

```text
http://localhost:8081
```

а не `8080`.

---

## 5. Maven ще не завантажив залежності

Якщо імпорти Spring червоні, потрібно перевірити Maven import і доступ до Інтернету.

---

# Що потрібно знати після тем 1–3

Студент повинен уміти пояснити:

- що таке залежність між класами;
- що таке Dependency Injection;
- що таке Inversion of Control;
- чому constructor injection кращий за ручне створення залежностей;
- що таке Spring Bean;
- що таке Spring Container;
- як Spring знаходить компоненти;
- для чого використовуються `@Component`, `@Service`, `@Repository`, `@RestController`;
- що таке Spring Boot;
- навіщо потрібен Spring Initializr;
- що містить `pom.xml`;
- що таке starter;
- як запустити Spring Boot-застосунок;
- що таке `localhost` і порт;
- як створити простий `GET` endpoint;
- як Spring перетворює Java-об'єкт у JSON.

---

# Питання для самоперевірки

1. Що таке залежність між класами?
2. Чому жорстке створення залежності через `new` може бути проблемою?
3. Що означає Dependency Injection?
4. Які переваги constructor injection?
5. Що таке Inversion of Control?
6. Який зв'язок між IoC і DI?
7. Що таке Spring Bean?
8. Що робить Spring Container?
9. Що таке `ApplicationContext`?
10. Для чого використовується `@Service`?
11. Для чого використовується `@Repository`?
12. Для чого використовується `@RestController`?
13. Що таке Component Scanning?
14. Чому головний клас Spring Boot доцільно розміщувати в кореневому пакеті?
15. Що таке Spring Boot?
16. Чим Spring Boot відрізняється від Spring Framework?
17. Для чого використовується Spring Initializr?
18. Для чого потрібен `pom.xml`?
19. Що таке Spring Boot Starter?
20. Для чого використовується `application.properties`?
21. Що означає `localhost`?
22. Що таке порт?
23. Для чого використовується `@GetMapping`?
24. Чому Spring Boot не потребує окремого встановлення Tomcat для базового Web-проєкту?
25. Як зібрати Spring Boot-проєкт у `.jar`?

# Тема 4. Конфігурація Spring Boot-застосунку

## 4.1. Навіщо застосунку конфігурація

У програмі є значення, які не повинні бути жорстко записані безпосередньо в Java-коді.

Наприклад:

- порт вебсервера;
- ім'я застосунку;
- адреса бази даних;
- логін до бази даних;
- рівень логування;
- адреса зовнішнього сервісу;
- параметри, що відрізняються для локального і серверного запуску.

Поганий приклад:

```java
public class AppSettings {

    private final String apiUrl = "http://localhost:9000";
}
```

Якщо адреса зміниться, доведеться змінювати код і повторно збирати програму.

Краще винести такі значення у конфігурацію.

Основна ідея:

```text
Java-код
   +
зовнішня конфігурація
   =
налаштований застосунок
```

---

## 4.2. application.properties

У Spring Boot одним із головних конфігураційних файлів є:

```text
src/main/resources/application.properties
```

Приклад:

```properties
spring.application.name=demo
server.port=8081
```

Тут кожне налаштування записується у форматі:

```text
ключ=значення
```

Наприклад:

```properties
app.title=Student Management
```

де:

```text
app.title — ім'я властивості
Student Management — її значення
```

---

## 4.3. Зміна порту сервера

За замовчуванням Spring Boot Web-застосунок зазвичай запускається на порту `8080`.

Це можна змінити:

```properties
server.port=8081
```

Тепер застосунок буде доступний за адресою:

```text
http://localhost:8081
```

Якщо порт `8080` зайнятий іншою програмою, зміна `server.port` є найпростішим способом запустити навчальний проєкт.

---

## 4.4. Ім'я застосунку

Можна вказати ім'я:

```properties
spring.application.name=student-service
```

Саме по собі це налаштування не змінює бізнес-логіку, але ім'я може використовуватися Spring та іншими компонентами.

Для навчального проєкту це також допомагає явно бачити, який застосунок запускається.

---

## 4.5. Власні параметри конфігурації

Spring Boot дозволяє створювати власні властивості.

Наприклад:

```properties
app.title=Student Application
app.version=1.0
```

Це не стандартні властивості Spring Boot. Ми придумали їх самі.

Потім можемо використати їх у Java-коді.

---

## 4.6. Отримання параметра через @Value

Для простих випадків можна використати `@Value`.

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppInfo {

    @Value("${app.title}")
    private String title;

    public String getTitle() {
        return title;
    }
}
```

Запис:

```java
@Value("${app.title}")
```

означає:

> Знайти властивість `app.title` у конфігурації та передати її значення в це поле.

Якщо у `application.properties`:

```properties
app.title=Student Application
```

то поле `title` отримає значення:

```text
Student Application
```

---

## 4.7. Значення за замовчуванням

Можна вказати значення, яке буде використано, якщо властивість відсутня.

```java
@Value("${app.title:My Application}")
private String title;
```

Тут:

```text
app.title
```

— ім'я параметра,

а:

```text
My Application
```

— значення за замовчуванням.

Це зручно для необов'язкових налаштувань.

---

## 4.8. Чому не варто зловживати @Value

Для двох-трьох простих параметрів `@Value` цілком підходить.

Але якщо параметрів багато:

```properties
app.name=Demo
app.author=Student
app.version=1.0
app.max-users=100
app.enabled=true
```

розкидати багато `@Value` по різних класах стає незручно.

У більших проєктах часто використовують `@ConfigurationProperties`.

На цьому курсі достатньо розуміти сам принцип:

```text
конфігурація знаходиться окремо від бізнес-логіки
```

---

## 4.9. application.yml

Крім `.properties`, Spring Boot підтримує YAML.

Наприклад, такий файл:

```properties
server.port=8081
spring.application.name=demo
```

можна записати у `application.yml`:

```yaml
server:
  port: 8081

spring:
  application:
    name: demo
```

YAML зручний для ієрархічних налаштувань.

Проте для початкового курсу `application.properties` часто простіший, тому надалі можна використовувати саме його.

Важливо не дублювати без потреби однакові властивості одночасно в `application.properties` і `application.yml`.

---

## 4.10. Зовнішня конфігурація

Spring Boot може отримувати конфігурацію не лише з файлу всередині проєкту.

Наприклад, параметри можуть надходити з:

- `application.properties`;
- `application.yml`;
- змінних середовища;
- параметрів командного рядка.

Це важливо, тому що один і той самий `.jar` можна запускати з різними налаштуваннями.

Наприклад:

```bash
java -jar demo.jar --server.port=9090
```

У цьому запуску сервер працюватиме на порту `9090`.

---

## 4.11. Чому це зручно

Уявімо, що є один застосунок.

Локально він працює:

```text
localhost:8080
```

А на сервері потрібно:

```text
server:9000
```

Поганий підхід — змінювати Java-код.

Краще залишити код однаковим і змінити тільки конфігурацію.

Це один із важливих принципів серверної розробки.

---

## 4.12. Профілі Spring

Іноді потрібні різні налаштування для різних середовищ.

Наприклад:

```text
development — локальна розробка
production  — робочий сервер
```

Spring підтримує профілі.

Можна мати:

```text
application.properties
application-dev.properties
application-prod.properties
```

Наприклад:

`application-dev.properties`:

```properties
server.port=8080
```

`application-prod.properties`:

```properties
server.port=9000
```

Активний профіль можна задати:

```properties
spring.profiles.active=dev
```

Для базового курсу достатньо знати, що профілі дозволяють мати різні конфігурації для різних середовищ.

---

## 4.13. Конфігурація і паролі

У реальному проєкті не варто зберігати секретні дані безпосередньо в Git-репозиторії.

Наприклад, небажано публікувати:

```properties
db.password=123456
```

У навчальному локальному проєкті простий пароль може використовуватися для демонстрації, але важливо вже зараз розуміти:

> Конфігураційний файл може містити чутливі дані, тому з ним потрібно поводитися обережно.

У реальних середовищах секрети часто передаються через змінні середовища або спеціальні системи керування секретами.

---

## 4.14. Типова помилка: неправильне ім'я властивості

Припустимо, у конфігурації:

```properties
app.title=Demo
```

а в коді:

```java
@Value("${app.name}")
private String title;
```

Spring шукає `app.name`, але такої властивості немає.

Якщо значення за замовчуванням не вказане, застосунок може не запуститися.

Тому назви параметрів мають збігатися.

---

## 4.15. Типова помилка: файл не в resources

Правильне розташування:

```text
src/main/resources/application.properties
```

Якщо файл випадково створити, наприклад, у:

```text
src/main/java
```

Spring Boot не буде використовувати його як стандартний конфігураційний файл.

---

## 4.16. Приклад конфігурації в контролері

`application.properties`:

```properties
app.title=OOP Spring Boot Course
```

Компонент:

```java
@Component
public class AppInfo {

    @Value("${app.title}")
    private String title;

    public String getTitle() {
        return title;
    }
}
```

Контролер:

```java
@RestController
public class InfoController {

    private final AppInfo appInfo;

    public InfoController(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @GetMapping("/info")
    public String info() {
        return appInfo.getTitle();
    }
}
```

Запит:

```text
GET http://localhost:8080/info
```

Відповідь:

```text
OOP Spring Boot Course
```

Тут одночасно використовуються:

- Bean;
- Dependency Injection;
- конфігурація;
- REST Controller.

---

## 4.17. Що важливо запам'ятати

Конфігурація потрібна для параметрів, які можуть змінюватися незалежно від Java-коду.

Основний файл:

```text
application.properties
```

Типові приклади:

```properties
spring.application.name=demo
server.port=8081
```

Прості власні значення можна отримати через:

```java
@Value("${app.title}")
```

---

# Тема 5. Основи HTTP і клієнт-серверного взаємодії

## 5.1. Навіщо Spring Boot потрібен HTTP

Коли ми створюємо REST API, Java-застосунок зазвичай працює як сервер.

До нього звертається клієнт.

Клієнтом може бути:

- браузер;
- мобільний застосунок;
- JavaScript frontend;
- інший сервер;
- Postman;
- інша програма.

Схема:

```text
Client
   |
   | HTTP request
   v
Spring Boot Application
   |
   | HTTP response
   v
Client
```

Щоб розуміти `@GetMapping`, `@PostMapping` та інші анотації, спочатку потрібно зрозуміти сам HTTP.

---

## 5.2. Що таке HTTP

HTTP — Hypertext Transfer Protocol.

Це протокол, за яким клієнт і сервер обмінюються повідомленнями.

У найпростішому випадку:

```text
клієнт надсилає запит
сервер повертає відповідь
```

Наприклад, браузер звертається:

```text
GET /students
```

Сервер відповідає:

```json
[
  {
    "id": 1,
    "name": "Anna"
  }
]
```

---

## 5.3. HTTP request

HTTP-запит містить кілька важливих частин.

Спрощено:

```text
METHOD /path
Headers

Body
```

Наприклад:

```text
POST /students
Content-Type: application/json

{
  "name": "Anna"
}
```

Тут:

```text
POST                — HTTP-метод
/students           — шлях
Content-Type        — заголовок
{ "name": "Anna" }  — тіло запиту
```

---

## 5.4. HTTP response

Сервер повертає відповідь.

Наприклад:

```text
HTTP 201 Created
Content-Type: application/json

{
  "id": 1,
  "name": "Anna"
}
```

У відповіді є:

- статус;
- заголовки;
- тіло відповіді.

---

## 5.5. Основні HTTP-методи

Для базового REST API найчастіше використовують:

```text
GET
POST
PUT
DELETE
```

Їх часто пов'язують із CRUD.

CRUD:

```text
Create
Read
Update
Delete
```

Відповідність:

```text
POST   -> Create
GET    -> Read
PUT    -> Update
DELETE -> Delete
```

Це не абсолютно жорстке правило, але для базового REST API така схема дуже корисна.

---

## 5.6. GET

`GET` використовується для отримання даних.

Наприклад:

```text
GET /students
```

може означати:

> отримати список студентів.

А:

```text
GET /students/5
```

може означати:

> отримати студента з id = 5.

Зазвичай `GET` не повинен змінювати стан даних на сервері.

---

## 5.7. POST

`POST` часто використовується для створення нового ресурсу.

Наприклад:

```text
POST /students
```

Тіло:

```json
{
  "name": "Anna",
  "age": 20
}
```

Сервер створює нового студента.

---

## 5.8. PUT

`PUT` часто використовується для оновлення ресурсу.

Наприклад:

```text
PUT /students/5
```

Тіло:

```json
{
  "name": "Anna",
  "age": 21
}
```

Це може означати:

> оновити дані студента з id = 5.

---

## 5.9. DELETE

`DELETE` використовується для видалення.

```text
DELETE /students/5
```

означає:

> видалити студента з id = 5.

---

## 5.10. URL і шлях

Розглянемо:

```text
http://localhost:8080/students/5
```

Тут:

```text
http        — протокол
localhost   — сервер
8080        — порт
/students/5 — шлях
```

У Spring нас часто найбільше цікавить саме шлях.

Наприклад:

```java
@GetMapping("/students/5")
```

---

## 5.11. Path Variable

Частина шляху може бути змінною.

Наприклад:

```text
/students/1
/students/2
/students/100
```

Тут остання частина — ідентифікатор.

У Spring:

```java
@GetMapping("/students/{id}")
public String getStudent(@PathVariable Long id) {
    return "Student id = " + id;
}
```

Запит:

```text
GET /students/25
```

Spring передасть:

```text
25
```

у параметр:

```java
Long id
```

---

## 5.12. Query Parameters

Інший спосіб передати параметри — query string.

Наприклад:

```text
/students?name=Anna
```

Тут:

```text
name=Anna
```

— параметр запиту.

У Spring:

```java
@GetMapping("/students")
public String findStudent(@RequestParam String name) {
    return "Name = " + name;
}
```

---

## 5.13. Path Variable чи Request Parameter

Умовно:

```text
/students/5
```

використовують, коли `5` є ідентифікатором конкретного ресурсу.

А:

```text
/students?name=Anna
```

— коли передаємо критерій пошуку або додатковий параметр.

Наприклад:

```text
/students/5
/students?group=AI-251
/students?age=20
```

---

## 5.14. Тіло HTTP-запиту

Для `POST` або `PUT` дані часто передаються у body.

Наприклад:

```json
{
  "name": "Anna",
  "age": 20
}
```

Це JSON.

У Spring такий JSON можна автоматично перетворити на Java-об'єкт.

---

## 5.15. JSON

JSON — популярний формат обміну даними.

Об'єкт:

```json
{
  "id": 1,
  "name": "Anna",
  "active": true
}
```

Масив:

```json
[
  {
    "id": 1,
    "name": "Anna"
  },
  {
    "id": 2,
    "name": "Ivan"
  }
]
```

JSON добре підходить для REST API, тому що його легко передавати через HTTP і обробляти в різних мовах програмування.

---

## 5.16. Content-Type

Заголовок:

```text
Content-Type: application/json
```

означає:

> тіло повідомлення містить JSON.

Коли клієнт надсилає JSON у Spring Boot, зазвичай потрібно, щоб запит мав правильний `Content-Type`.

---

## 5.17. HTTP Status Codes

Крім даних, сервер повертає статус.

Найважливіші для початку:

```text
200 OK
201 Created
204 No Content
400 Bad Request
404 Not Found
500 Internal Server Error
```

---

## 5.18. 200 OK

Операція виконана успішно.

Наприклад:

```text
GET /students/1
```

Якщо студент існує:

```text
200 OK
```

---

## 5.19. 201 Created

Ресурс успішно створено.

Наприклад:

```text
POST /students
```

може повернути:

```text
201 Created
```

---

## 5.20. 400 Bad Request

Клієнт передав неправильні дані.

Наприклад:

```json
{
  "age": -5
}
```

Якщо за правилами вік не може бути від'ємним, сервер може повернути:

```text
400 Bad Request
```

---

## 5.21. 404 Not Found

Ресурс не знайдено.

Наприклад:

```text
GET /students/999
```

Якщо такого студента немає:

```text
404 Not Found
```

---

## 5.22. 500 Internal Server Error

Це означає, що під час обробки запиту на сервері сталася непередбачена помилка.

Для користувача це сигнал:

```text
проблема на стороні сервера
```

---

## 5.23. Stateless-підхід

REST API часто будують так, щоб кожен запит містив достатньо інформації для його обробки.

Сервер не повинен покладатися на те, що попередній запит «залишив» якісь тимчасові дані всередині Controller.

Це особливо важливо, тому що Spring Bean зазвичай є singleton.

Поганий приклад:

```java
@RestController
public class StudentController {

    private String currentStudentName;
}
```

Не варто зберігати дані конкретного HTTP-запиту у звичайному полі singleton Controller.

---

## 5.24. Як перевіряти HTTP-запити

Для простого `GET` достатньо браузера.

Наприклад:

```text
http://localhost:8080/hello
```

Але браузером незручно надсилати `POST`, `PUT`, `DELETE` із JSON.

Для цього можна використовувати:

- Postman;
- IntelliJ HTTP Client;
- `curl`.

Для базового курсу достатньо будь-якого одного інструмента.

---

## 5.25. Простий приклад curl

GET:

```bash
curl http://localhost:8080/hello
```

POST:

```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Anna","age":20}'
```

На Windows синтаксис командного рядка може відрізнятися, тому для початку часто простіше використовувати Postman або вбудований HTTP Client IDE.

---

## 5.26. Що важливо запам'ятати

```text
HTTP request
    |
    +-- method
    +-- URL/path
    +-- headers
    +-- body

HTTP response
    |
    +-- status
    +-- headers
    +-- body
```

Основні методи:

```text
GET    — отримання
POST   — створення
PUT    — оновлення
DELETE — видалення
```

---

# Тема 6. Створення REST API у Spring Boot

## 6.1. Що таке REST API

API — Application Programming Interface.

У контексті веброзробки API дозволяє одній програмі взаємодіяти з іншою.

REST API — це поширений стиль побудови HTTP API, де ми працюємо з ресурсами.

Наприклад, ресурс:

```text
student
```

може мати такі адреси:

```text
GET    /students
GET    /students/5
POST   /students
PUT    /students/5
DELETE /students/5
```

Такий набір endpoint'ів виглядає передбачувано і зрозуміло.

---

## 6.2. Що таке endpoint

Endpoint — конкретна точка API, яка визначається HTTP-методом і шляхом.

Наприклад:

```text
GET /students
```

і:

```text
POST /students
```

— це два різних endpoint'и, хоча шлях однаковий.

Різниця — у HTTP-методі.

---

## 6.3. @RestController

Створимо контролер:

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
}
```

`@RestController` означає, що клас:

- є Spring-компонентом;
- може приймати HTTP-запити;
- повертає дані у HTTP-відповідь.

Spring сам створить об'єкт цього класу.

---

## 6.4. GET endpoint

```java
@GetMapping("/students")
public String getStudents() {
    return "Students";
}
```

Повний контролер:

```java
@RestController
public class StudentController {

    @GetMapping("/students")
    public String getStudents() {
        return "Students";
    }
}
```

Запит:

```text
GET http://localhost:8080/students
```

Відповідь:

```text
Students
```

---

## 6.5. Повернення об'єкта

Створимо модель:

```java
public class Student {

    private Long id;
    private String name;
    private int age;

    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

Контролер:

```java
@GetMapping("/student")
public Student getStudent() {
    return new Student(1L, "Anna", 20);
}
```

Відповідь:

```json
{
  "id": 1,
  "name": "Anna",
  "age": 20
}
```

Spring Boot автоматично серіалізує Java-об'єкт у JSON.

---

## 6.6. Повернення списку

```java
@GetMapping("/students")
public List<Student> getStudents() {

    return List.of(
        new Student(1L, "Anna", 20),
        new Student(2L, "Ivan", 21)
    );
}
```

JSON:

```json
[
  {
    "id": 1,
    "name": "Anna",
    "age": 20
  },
  {
    "id": 2,
    "name": "Ivan",
    "age": 21
  }
]
```

---

## 6.7. @RequestMapping на рівні класу

Щоб не повторювати `/students` у кожному методі:

```java
@RestController
@RequestMapping("/students")
public class StudentController {
}
```

Тоді методи можуть виглядати коротше:

```java
@GetMapping
public List<Student> getAll() {
    // ...
}
```

```java
@GetMapping("/{id}")
public Student getById(@PathVariable Long id) {
    // ...
}
```

У результаті:

```text
GET /students
GET /students/{id}
```

---

## 6.8. @PathVariable

```java
@GetMapping("/{id}")
public String getById(@PathVariable Long id) {
    return "Student id = " + id;
}
```

Запит:

```text
GET /students/10
```

Результат:

```text
Student id = 10
```

Spring бере `10` зі шляху і перетворює його на `Long`.

---

## 6.9. @RequestParam

Наприклад, хочемо шукати за ім'ям:

```java
@GetMapping("/search")
public String search(@RequestParam String name) {
    return "Search: " + name;
}
```

Запит:

```text
GET /students/search?name=Anna
```

Spring передасть `"Anna"` у параметр `name`.

---

## 6.10. Необов'язковий RequestParam

Можна задати значення за замовчуванням:

```java
@GetMapping("/search")
public String search(
        @RequestParam(defaultValue = "all") String name) {

    return "Search: " + name;
}
```

Якщо викликати:

```text
GET /students/search
```

отримаємо:

```text
Search: all
```

---

## 6.11. POST і @RequestBody

Для створення об'єкта клієнт передає JSON.

Наприклад:

```json
{
  "name": "Anna",
  "age": 20
}
```

Spring може перетворити його в Java-об'єкт.

```java
@PostMapping
public Student create(@RequestBody Student student) {
    return student;
}
```

`@RequestBody` означає:

> Прочитати тіло HTTP-запиту і перетворити його на Java-об'єкт.

---

## 6.12. Що потрібно для перетворення JSON

Для простого навчального класу зручніше мати:

- поля;
- конструктор без параметрів;
- getters/setters.

Наприклад:

```java
public class Student {

    private Long id;
    private String name;
    private int age;

    public Student() {
    }

    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

Це спрощує автоматичне перетворення JSON ↔ Java.

---

## 6.13. PUT

```java
@PutMapping("/{id}")
public Student update(
        @PathVariable Long id,
        @RequestBody Student student) {

    student.setId(id);
    return student;
}
```

Запит:

```text
PUT /students/5
```

Тіло:

```json
{
  "name": "Anna",
  "age": 21
}
```

Метод отримує:

```text
id = 5
```

і Java-об'єкт `student`.

---

## 6.14. DELETE

```java
@DeleteMapping("/{id}")
public String delete(@PathVariable Long id) {
    return "Student " + id + " deleted";
}
```

Запит:

```text
DELETE /students/5
```

---

## 6.15. Навчальний CRUD без бази даних

На цьому етапі ще можна не використовувати базу даних.

Для демонстрації можна зберігати дані у списку.

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student(1L, "Anna", 20));
        students.add(new Student(2L, "Ivan", 21));
    }

    @GetMapping
    public List<Student> getAll() {
        return students;
    }
}
```

Це лише навчальний приклад.

Після перезапуску застосунку всі зміни у списку зникнуть.

Пізніше список буде замінено на Repository і базу даних.

---

## 6.16. Додавання нового студента

```java
@PostMapping
public Student create(@RequestBody Student student) {

    students.add(student);
    return student;
}
```

Запит:

```text
POST /students
```

Тіло:

```json
{
  "id": 3,
  "name": "Olena",
  "age": 19
}
```

---

## 6.17. Пошук за id

```java
@GetMapping("/{id}")
public Student getById(@PathVariable Long id) {

    for (Student student : students) {
        if (student.getId().equals(id)) {
            return student;
        }
    }

    return null;
}
```

Такий код підходить лише як перший приклад.

Проблема:

```text
якщо студента немає, повертається null
```

У правильному REST API краще повернути:

```text
404 Not Found
```

Обробку помилок ми розглянемо в окремій темі.

---

## 6.18. Оновлення елемента

```java
@PutMapping("/{id}")
public Student update(
        @PathVariable Long id,
        @RequestBody Student updatedStudent) {

    for (Student student : students) {

        if (student.getId().equals(id)) {
            student.setName(updatedStudent.getName());
            student.setAge(updatedStudent.getAge());

            return student;
        }
    }

    return null;
}
```

---

## 6.19. Видалення елемента

```java
@DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {

    students.removeIf(
        student -> student.getId().equals(id)
    );
}
```

Це вже простий CRUD API без бази даних.

---

## 6.20. Чому не варто залишати всю логіку в Controller

Для першого прикладу можна написати список прямо в Controller.

Але у нормальній архітектурі Controller не повинен містити всю бізнес-логіку.

Погана структура:

```text
StudentController
|
+-- приймає запит
+-- шукає дані
+-- перевіряє правила
+-- змінює дані
+-- працює з БД
```

Краще:

```text
Controller
   |
   v
Service
   |
   v
Repository
```

Це буде наступним важливим кроком курсу.

---

## 6.21. ResponseEntity

Spring дозволяє керувати не лише тілом відповіді, але й HTTP-статусом.

Наприклад:

```java
@GetMapping("/{id}")
public ResponseEntity<Student> getById(@PathVariable Long id) {

    Student student = findStudent(id);

    if (student == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(student);
}
```

Тут можна явно повернути:

```text
200 OK
```

або:

```text
404 Not Found
```

Для початку достатньо розуміти, що `ResponseEntity` представляє повну HTTP-відповідь.

---

## 6.22. Статус при створенні

При створенні ресурсу логічно використовувати `201 Created`.

Наприклад:

```java
@PostMapping
public ResponseEntity<Student> create(
        @RequestBody Student student) {

    students.add(student);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(student);
}
```

---

## 6.23. Хороші назви endpoint'ів

Краще:

```text
GET /students
GET /students/1
POST /students
DELETE /students/1
```

Гірше:

```text
GET /getAllStudents
GET /getStudentById/1
POST /createNewStudent
GET /deleteStudent/1
```

У REST зазвичай використовують іменники в URL, а дію виражає HTTP-метод.

Тобто:

```text
/students
```

— ресурс,

а:

```text
GET
POST
PUT
DELETE
```

— дія.

---

## 6.24. Чому DELETE через GET — погана ідея

Не слід робити:

```java
@GetMapping("/delete/{id}")
public void delete(@PathVariable Long id) {
}
```

`GET` семантично призначений для отримання даних, а не для їх видалення.

Правильно:

```java
@DeleteMapping("/{id}")
```

Це робить API зрозумілішим і передбачуванішим.

---

## 6.25. Один Controller — одна група ресурсів

Наприклад:

```text
StudentController -> /students
CourseController  -> /courses
TeacherController -> /teachers
```

Не потрібно створювати один величезний Controller для всього застосунку.

Це знову принцип розподілу відповідальності.

---

## 6.26. Приклад повного простого Controller

```java
package com.example.demo.controller;

import com.example.demo.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student(1L, "Anna", 20));
        students.add(new Student(2L, "Ivan", 21));
    }

    @GetMapping
    public List<Student> getAll() {
        return students;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(
            @PathVariable Long id) {

        for (Student student : students) {
            if (student.getId().equals(id)) {
                return ResponseEntity.ok(student);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Student> create(
            @RequestBody Student student) {

        students.add(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @PathVariable Long id,
            @RequestBody Student updatedStudent) {

        for (Student student : students) {
            if (student.getId().equals(id)) {

                student.setName(updatedStudent.getName());
                student.setAge(updatedStudent.getAge());

                return ResponseEntity.ok(student);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        boolean removed = students.removeIf(
                student -> student.getId().equals(id)
        );

        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
```

Цей код уже дозволяє виконати базові CRUD-операції.

Однак це ще не правильна кінцева архітектура.

У наступній темі логіку буде винесено з Controller у Service.

---

# Як перевірити API

Після запуску застосунку можна перевірити:

```text
GET http://localhost:8080/students
```

Очікуваний результат:

```json
[
  {
    "id": 1,
    "name": "Anna",
    "age": 20
  },
  {
    "id": 2,
    "name": "Ivan",
    "age": 21
  }
]
```

Перевірити одного студента:

```text
GET http://localhost:8080/students/1
```

Створити:

```text
POST http://localhost:8080/students
```

Body:

```json
{
  "id": 3,
  "name": "Olena",
  "age": 19
}
```

Оновити:

```text
PUT http://localhost:8080/students/3
```

Body:

```json
{
  "name": "Olena",
  "age": 20
}
```

Видалити:

```text
DELETE http://localhost:8080/students/3
```

---

# Типові помилки студентів у темах 4–6

## 1. Неправильний порт

У конфігурації:

```properties
server.port=8081
```

але запит виконується:

```text
http://localhost:8080
```

Потрібно використовувати:

```text
http://localhost:8081
```

---

## 2. Неправильний HTTP-метод

У Controller:

```java
@PostMapping("/students")
```

а в браузері виконується звичайний GET-запит.

У результаті endpoint не знаходиться.

Потрібно враховувати не тільки URL, а й HTTP-метод.

---

## 3. Забутий @RequestBody

Неправильно:

```java
@PostMapping
public Student create(Student student) {
}
```

Для JSON у body:

```java
@PostMapping
public Student create(@RequestBody Student student) {
}
```

---

## 4. Забутий @PathVariable

Неправильно:

```java
@GetMapping("/{id}")
public Student get(Long id) {
}
```

Краще явно:

```java
@GetMapping("/{id}")
public Student get(@PathVariable Long id) {
}
```

---

## 5. Невідповідний JSON

Якщо Java-клас очікує:

```java
private String name;
private int age;
```

JSON повинен містити відповідні поля:

```json
{
  "name": "Anna",
  "age": 20
}
```

---

## 6. Уся логіка знаходиться у Controller

Для першої демонстрації це допустимо.

Але в нормальному проєкті Controller повинен бути тонким:

```text
отримати запит
    ↓
передати роботу Service
    ↓
повернути відповідь
```

---

# Зв'язок тем 4–6

```text
Тема 4
Конфігурація
    |
    v
налаштовуємо поведінку застосунку

Тема 5
HTTP
    |
    v
розуміємо, як клієнт спілкується із сервером

Тема 6
REST API
    |
    v
реалізуємо HTTP-взаємодію у Spring Boot
```

---

# Що потрібно знати після тем 4–6

Студент повинен уміти пояснити:

- навіщо потрібна зовнішня конфігурація;
- де знаходиться `application.properties`;
- як змінити порт Spring Boot;
- як отримати власну властивість через `@Value`;
- що таке HTTP request та HTTP response;
- що таке HTTP method;
- призначення `GET`, `POST`, `PUT`, `DELETE`;
- що таке URL, path, query parameter;
- що таке JSON;
- що таке HTTP status code;
- що означають `200`, `201`, `400`, `404`, `500`;
- що таке REST API;
- що таке endpoint;
- для чого потрібен `@RestController`;
- для чого використовуються `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`;
- для чого потрібні `@PathVariable`, `@RequestParam`, `@RequestBody`;
- навіщо використовується `ResponseEntity`;
- чому не варто зберігати всю бізнес-логіку в Controller.

---

# Питання для самоперевірки

1. Навіщо виносити параметри з Java-коду в конфігурацію?
2. Де зазвичай знаходиться `application.properties`?
3. Як змінити порт Spring Boot-застосунку?
4. Для чого використовується `@Value`?
5. Що таке профіль Spring?
6. Що таке HTTP?
7. Що називається HTTP request?
8. Що називається HTTP response?
9. Яке призначення HTTP-методу `GET`?
10. Для чого використовується `POST`?
11. Для чого використовується `PUT`?
12. Для чого використовується `DELETE`?
13. Що таке JSON?
14. Для чого потрібен заголовок `Content-Type`?
15. Що означає статус `200 OK`?
16. Що означає `201 Created`?
17. Що означає `404 Not Found`?
18. Що таке REST API?
19. Що таке endpoint?
20. Для чого використовується `@RestController`?
21. Для чого використовується `@RequestMapping`?
22. У чому різниця між `@PathVariable` та `@RequestParam`?
23. Для чого потрібен `@RequestBody`?
24. Що робить `ResponseEntity`?
25. Чому `GET /deleteStudent/1` є поганим стилем REST API?
26. Чому тимчасовий список у Controller не є заміною бази даних?
27. Чому в реальному застосунку бізнес-логіку потрібно переносити з Controller у Service?

# Тема 7. Багатошарова архітектура: Controller, Service, Repository

## 7.1. Чому не можна залишати всю логіку в Controller

Розглянемо такий Controller:

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();

    @PostMapping
    public Student create(@RequestBody Student student) {

        if (student.getAge() < 16) {
            throw new RuntimeException("Invalid age");
        }

        students.add(student);

        return student;
    }
}
```

У цьому класі одночасно знаходяться:

- приймання HTTP-запиту;
- перевірка даних;
- бізнес-логіка;
- зберігання даних.

Для дуже маленького прикладу це допустимо.

Але якщо додати:

- пошук;
- сортування;
- роботу з базою даних;
- перевірку прав;
- додаткові бізнес-правила,

Controller стане занадто великим.

---

## 7.2. Принцип розподілу відповідальності

У хорошій архітектурі кожен клас повинен мати зрозумілу відповідальність.

Для Spring Boot часто використовують таку структуру:

```text
Controller
    |
    v
Service
    |
    v
Repository
```

Кожен шар виконує свою роль.

---

## 7.3. Controller

Controller відповідає за HTTP-взаємодію.

Він:

- приймає запит;
- отримує параметри;
- отримує JSON;
- викликає Service;
- повертає HTTP-відповідь.

Наприклад:

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }
}
```

Controller не повинен сам реалізовувати складну бізнес-логіку.

---

## 7.4. Service

Service містить бізнес-логіку.

Наприклад:

```java
@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();

    public List<Student> getAll() {
        return students;
    }
}
```

У майбутньому тут можуть бути:

- перевірки;
- розрахунки;
- бізнес-правила;
- виклики Repository.

---

## 7.5. Repository

Repository відповідає за роботу з даними.

На цьому етапі можна уявити його як окремий клас, який зберігає дані.

```java
@Repository
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public List<Student> findAll() {
        return students;
    }
}
```

Пізніше Repository буде працювати з реальною базою даних через Spring Data JPA.

---

## 7.6. Повна схема

```text
HTTP request
      |
      v
StudentController
      |
      v
StudentService
      |
      v
StudentRepository
      |
      v
Data
```

Приклад потоку:

```text
GET /students
      |
      v
StudentController.getAll()
      |
      v
StudentService.getAll()
      |
      v
StudentRepository.findAll()
      |
      v
List<Student>
```

---

## 7.7. Чому це краще

Багатошарова архітектура дає:

- менші класи;
- зрозумілу структуру;
- простіше внесення змін;
- кращий поділ відповідальності;
- можливість замінити спосіб зберігання даних.

Наприклад, сьогодні Repository працює зі списком:

```java
List<Student>
```

а завтра — з PostgreSQL.

Controller при цьому майже не зміниться.

---

## 7.8. Приклад моделі

```java
public class Student {

    private Long id;
    private String name;
    private int age;

    public Student() {
    }

    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

---

## 7.9. Repository зі списком

```java
@Repository
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public StudentRepository() {
        students.add(new Student(1L, "Anna", 20));
        students.add(new Student(2L, "Ivan", 21));
    }

    public List<Student> findAll() {
        return students;
    }
}
```

---

## 7.10. Service

```java
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }
}
```

---

## 7.11. Controller

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }
}
```

Тут вже чітко видно Dependency Injection:

```text
Controller <- Service <- Repository
```

Spring сам створює всі три компоненти і зв'язує їх.

---

## 7.12. Інтерфейс для Service

У деяких проєктах використовують інтерфейси:

```java
public interface StudentService {

    List<Student> getAll();
}
```

Реалізація:

```java
@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public List<Student> getAll() {
        return List.of();
    }
}
```

Для простого навчального проєкту це не завжди обов'язково.

Не варто створювати інтерфейс лише тому, що "так прийнято".

Інтерфейс корисний, коли:

- є кілька реалізацій;
- потрібна абстракція;
- архітектура цього вимагає.

---

## 7.13. Бізнес-логіка належить Service

Наприклад, студент не може бути молодшим за 16 років.

Це бізнес-правило.

Логічніше розмістити його в Service:

```java
@Service
public class StudentService {

    public Student create(Student student) {

        if (student.getAge() < 16) {
            throw new IllegalArgumentException(
                "Student age must be at least 16"
            );
        }

        return student;
    }
}
```

Controller лише передає дані:

```java
@PostMapping
public Student create(@RequestBody Student student) {
    return studentService.create(student);
}
```

---

## 7.14. Типова помилка: Service викликає Controller

Правильний напрямок залежностей:

```text
Controller -> Service -> Repository
```

Неправильно:

```text
Service -> Controller
```

Service не повинен знати про HTTP та Controller.

---

## 7.15. Типова помилка: Repository містить бізнес-логіку

Repository повинен працювати з даними.

Наприклад:

```java
findById()
findAll()
save()
delete()
```

А ось правило:

```text
студент не може бути молодшим за 16 років
```

повинно знаходитися в Service.

---

## 7.16. Рекомендована структура пакетів

```text
com.example.demo
|
+-- controller
|   +-- StudentController.java
|
+-- service
|   +-- StudentService.java
|
+-- repository
|   +-- StudentRepository.java
|
+-- model
|   +-- Student.java
|
+-- DemoApplication.java
```

Ця структура дуже поширена у навчальних і невеликих Spring Boot-проєктах.

---

# Тема 8. Передавання даних і використання DTO

## 8.1. Проблема прямого використання моделі

Припустимо, є клас:

```java
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
}
```

Якщо Controller повертає цей об'єкт напряму:

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userService.getById(id);
}
```

у JSON потенційно може потрапити:

```json
{
  "id": 1,
  "name": "Anna",
  "email": "anna@example.com",
  "password": "secret"
}
```

Це погано.

Клієнту не потрібно бачити пароль.

---

## 8.2. Що таке DTO

DTO — Data Transfer Object.

Це спеціальний об'єкт для передавання даних між частинами системи або між сервером і клієнтом.

Наприклад:

```java
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
}
```

Тут немає пароля.

---

## 8.3. Чому DTO корисний

DTO дозволяє:

- не показувати зайві поля;
- не прив'язувати API до внутрішньої моделі;
- мати різні структури для запиту і відповіді;
- спростити валідацію;
- контролювати формат API.

---

## 8.4. Request DTO

Для створення користувача можна використовувати:

```java
public class CreateUserRequest {

    private String name;
    private String email;
    private String password;

    public CreateUserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

Цей DTO описує дані, які клієнт повинен передати серверу.

---

## 8.5. Response DTO

```java
public class UserResponse {

    private Long id;
    private String name;
    private String email;

    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
```

У відповіді немає пароля.

---

## 8.6. Перетворення DTO вручну

Для базового курсу достатньо ручного mapping.

Наприклад:

```java
public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail()
    );
}
```

Це просто і зрозуміло.

---

## 8.7. Перетворення Request DTO у модель

```java
public User toUser(CreateUserRequest request) {

    User user = new User();

    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());

    return user;
}
```

---

## 8.8. Де виконувати mapping

У маленькому навчальному проєкті mapping можна виконувати в Service.

Наприклад:

```java
@Service
public class UserService {

    public UserResponse create(CreateUserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        // збереження

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
}
```

---

## 8.9. Controller з DTO

```java
@PostMapping
public UserResponse create(
        @RequestBody CreateUserRequest request) {

    return userService.create(request);
}
```

Тепер API чітко визначає:

```text
що приймаємо
і
що повертаємо
```

---

## 8.10. Різні DTO для різних операцій

Не обов'язково мати один DTO для всього.

Наприклад:

```text
CreateStudentRequest
UpdateStudentRequest
StudentResponse
```

Створення може вимагати:

```text
name
age
email
```

А оновлення:

```text
name
age
```

API стає більш зрозумілим.

---

## 8.11. Чому не треба передавати id при створенні

При створенні нового об'єкта id зазвичай формує сервер або база даних.

Тому краще:

```json
{
  "name": "Anna",
  "age": 20
}
```

а не:

```json
{
  "id": 1,
  "name": "Anna",
  "age": 20
}
```

Пізніше, при роботі з JPA, id буде генеруватися автоматично.

---

## 8.12. DTO і внутрішня модель — це різні речі

Важливо:

```text
Entity/Model != DTO
```

Model описує внутрішній об'єкт предметної області.

DTO описує структуру даних для обміну.

Навіть якщо сьогодні вони виглядають однаково, їхня роль різна.

---

## 8.13. Приклад Student DTO

```java
public class StudentRequest {

    private String name;
    private int age;

    public StudentRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

Response:

```java
public class StudentResponse {

    private Long id;
    private String name;
    private int age;

    public StudentResponse(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

---

## 8.14. Приклад Service

```java
@Service
public class StudentService {

    public StudentResponse create(StudentRequest request) {

        Student student = new Student();

        student.setId(1L);
        student.setName(request.getName());
        student.setAge(request.getAge());

        return new StudentResponse(
            student.getId(),
            student.getName(),
            student.getAge()
        );
    }
}
```

---

## 8.15. Controller

```java
@PostMapping
public StudentResponse create(
        @RequestBody StudentRequest request) {

    return studentService.create(request);
}
```

---

## 8.16. Чому DTO особливо важливий із базою даних

Пізніше ми створимо Entity.

Entity буде пов'язана зі структурою бази даних.

Якщо напряму повертати Entity в API, API почне залежати від структури БД.

DTO створює додатковий рівень незалежності.

---

## 8.17. Типова помилка: один клас для всього

Початківці часто використовують:

```java
Student
```

одночасно як:

- Entity;
- request;
- response;
- внутрішню модель.

У маленькому прикладі це працює.

Але з ростом проєкту це стає незручно.

Тому DTO — важливий крок до чистішої архітектури.

---

# Тема 9. Валідація вхідних даних

## 9.1. Чому дані потрібно перевіряти

Клієнт може передати будь-які дані.

Наприклад:

```json
{
  "name": "",
  "age": -10,
  "email": "abc"
}
```

Сервер не повинен сліпо приймати все, що отримав.

Потрібно перевіряти, чи відповідають дані правилам.

Цей процес називається валідацією.

---

## 9.2. Два типи перевірок

Умовно можна розділити перевірки на:

### Технічну валідацію

Наприклад:

```text
ім'я не порожнє
email має правильний формат
вік не від'ємний
```

### Бізнес-правила

Наприклад:

```text
у групі не може бути більше 30 студентів
email повинен бути унікальним
курс не можна видалити, якщо на нього записані студенти
```

Прості технічні перевірки зручно робити через Bean Validation.

Бізнес-правила зазвичай залишаються в Service.

---

## 9.3. Додавання Validation

Для Spring Boot використовується starter:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Якщо проєкт створюється через Spring Initializr, можна додати залежність:

```text
Validation
```

---

## 9.4. @NotNull

```java
@NotNull
private Integer age;
```

Означає:

```text
значення не може бути null
```

Але:

```text
0
```

допускається, якщо немає інших обмежень.

---

## 9.5. @NotBlank

Для рядків:

```java
@NotBlank
private String name;
```

Не дозволяє:

```text
null
""
"   "
```

Для імені це зазвичай краще, ніж просто `@NotNull`.

---

## 9.6. @NotEmpty

```java
@NotEmpty
private String value;
```

Не дозволяє `null` і порожнє значення.

Але рядок із пробілів:

```text
"   "
```

може вважатися непорожнім.

Тому для текстових полів часто використовують `@NotBlank`.

---

## 9.7. @Size

```java
@Size(min = 2, max = 50)
private String name;
```

Означає, що довжина рядка повинна бути від 2 до 50 символів.

---

## 9.8. @Min і @Max

```java
@Min(16)
private int age;
```

```java
@Max(100)
private int age;
```

Разом:

```java
@Min(16)
@Max(100)
private int age;
```

---

## 9.9. @Email

```java
@Email
private String email;
```

Перевіряє базову відповідність формату email.

Наприклад:

```text
student@example.com
```

---

## 9.10. DTO з валідацією

```java
public class StudentRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Min(16)
    @Max(100)
    private int age;

    @NotBlank
    @Email
    private String email;

    public StudentRequest() {
    }

    // getters і setters
}
```

---

## 9.11. @Valid у Controller

Самих анотацій у DTO недостатньо.

Потрібно повідомити Spring, що об'єкт треба перевірити.

```java
@PostMapping
public StudentResponse create(
        @Valid @RequestBody StudentRequest request) {

    return studentService.create(request);
}
```

`@Valid` запускає перевірку об'єкта.

---

## 9.12. Що відбувається при помилці

Наприклад, клієнт надсилає:

```json
{
  "name": "",
  "age": 10,
  "email": "abc"
}
```

Порушено кілька правил.

Spring не передасть такий об'єкт далі як коректний запит.

Зазвичай буде повернено:

```text
400 Bad Request
```

---

## 9.13. Власні повідомлення

Можна задати повідомлення:

```java
@NotBlank(message = "Name must not be blank")
private String name;
```

```java
@Min(value = 16, message = "Age must be at least 16")
private int age;
```

```java
@Email(message = "Invalid email format")
private String email;
```

Це робить помилки зрозумілішими.

---

## 9.14. Повний DTO

```java
public class StudentRequest {

    @NotBlank(message = "Name is required")
    @Size(
        min = 2,
        max = 50,
        message = "Name must contain 2 to 50 characters"
    )
    private String name;

    @Min(
        value = 16,
        message = "Age must be at least 16"
    )
    @Max(
        value = 100,
        message = "Age must be at most 100"
    )
    private int age;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    public StudentRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

---

## 9.15. Чому краще валідовати DTO, а не Entity

Request DTO описує дані, які приходять від клієнта.

Саме там природно розміщувати правила формату вхідних даних.

Наприклад:

```text
CreateStudentRequest
```

може вимагати email.

А внутрішня Entity може мати інші правила.

Це ще одна перевага DTO.

---

## 9.16. Integer і int при валідації

Важливий момент.

Якщо:

```java
private int age;
```

то `age` ніколи не буде `null`.

Якщо JSON не містить `age`, може бути отримано значення `0`.

Якщо потрібно перевіряти саме відсутність значення:

```java
@NotNull
private Integer age;
```

Тоді можна відрізнити:

```text
поле відсутнє
```

від:

```text
age = 0
```

---

## 9.17. Приклад

```java
@NotNull(message = "Age is required")
@Min(value = 16, message = "Age must be at least 16")
private Integer age;
```

Це хороший варіант для request DTO.

---

## 9.18. Валідація не замінює бізнес-логіку

Припустимо:

```java
@Email
private String email;
```

Це перевіряє формат.

Але правило:

```text
email повинен бути унікальним
```

не можна повноцінно перевірити простою анотацією без доступу до даних.

Це бізнес-логіка:

```java
if (repository.existsByEmail(email)) {
    throw new IllegalArgumentException("Email already exists");
}
```

Таке правило повинно знаходитися в Service.

---

## 9.19. Що де перевіряти

Корисна схема:

```text
DTO validation
|
+-- формат
+-- null
+-- довжина
+-- min/max
+-- email

Service validation
|
+-- бізнес-правила
+-- унікальність
+-- залежність від інших даних
```

---

## 9.20. Controller після додавання валідації

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentResponse create(
            @Valid @RequestBody StudentRequest request) {

        return studentService.create(request);
    }
}
```

Controller залишається дуже простим.

Це хороший знак.

---

# Повний приклад тем 7–9

## StudentRequest

```java
public class StudentRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Min(16)
    @Max(100)
    private Integer age;

    @NotBlank
    @Email
    private String email;

    // getters/setters
}
```

## StudentResponse

```java
public class StudentResponse {

    private Long id;
    private String name;
    private Integer age;
    private String email;

    public StudentResponse(
            Long id,
            String name,
            Integer age,
            String email) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // getters
}
```

## Repository

```java
@Repository
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public List<Student> findAll() {
        return students;
    }

    public Student save(Student student) {
        students.add(student);
        return student;
    }
}
```

## Service

```java
@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponse create(StudentRequest request) {

        Student student = new Student();

        student.setId(System.currentTimeMillis());
        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());

        Student saved = repository.save(student);

        return new StudentResponse(
            saved.getId(),
            saved.getName(),
            saved.getAge(),
            saved.getEmail()
        );
    }
}
```

## Controller

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public StudentResponse create(
            @Valid @RequestBody StudentRequest request) {

        return service.create(request);
    }
}
```

---

# Типові помилки студентів

## 1. Уся логіка залишається в Controller

Controller стає великим.

Краще:

```text
Controller -> Service -> Repository
```

---

## 2. Controller напряму працює з Repository

Іноді це допустимо для дуже простого CRUD, але в навчальному курсі краще дотримуватися:

```text
Controller -> Service -> Repository
```

Це чіткіше показує архітектуру.

---

## 3. DTO не використовується

Наприклад, у відповідь повертається Entity з полями, які клієнт не повинен бачити.

DTO дозволяє контролювати API.

---

## 4. Забутий @Valid

Є:

```java
@NotBlank
@Email
@Min
```

але в Controller:

```java
public Response create(@RequestBody Request request)
```

Тоді очікувана автоматична валідація може не запуститися.

Потрібно:

```java
@Valid @RequestBody
```

---

## 5. @NotNull використовується для String, але порожній рядок проходить

```java
@NotNull
private String name;
```

не забороняє:

```text
""
```

або:

```text
"   "
```

Для імені краще:

```java
@NotBlank
```

---

## 6. Уся бізнес-логіка записана анотаціями валідації

Bean Validation добре підходить для простих правил формату.

Але складні правила повинні залишатися в Service.

---

# Зв'язок тем 7–9

```text
Тема 7
Багатошарова архітектура
        |
        v
розділяємо відповідальність

Тема 8
DTO
        |
        v
відокремлюємо внутрішню модель від API

Тема 9
Validation
        |
        v
перевіряємо дані до виконання бізнес-логіки
```

У результаті отримуємо більш правильну структуру:

```text
HTTP Request
     |
     v
Controller
     |
     | DTO + @Valid
     v
Service
     |
     | business logic
     v
Repository
     |
     v
Data
```

---

# Що потрібно знати після тем 7–9

Студент повинен уміти пояснити:

- що таке багатошарова архітектура;
- роль Controller;
- роль Service;
- роль Repository;
- чому бізнес-логіку не слід зберігати в Controller;
- як працює Dependency Injection між шарами;
- що таке DTO;
- різницю між Model/Entity і DTO;
- навіщо потрібні Request DTO та Response DTO;
- чому не слід повертати чутливі поля;
- як вручну перетворювати DTO в модель і навпаки;
- що таке валідація;
- для чого використовуються `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Email`;
- для чого потрібен `@Valid`;
- чим технічна валідація відрізняється від бізнес-правил;
- чому `Integer` іноді зручніший за `int` у request DTO.

---

# Питання для самоперевірки

1. Яку роль виконує Controller?
2. Яку роль виконує Service?
3. Яку роль виконує Repository?
4. Чому не варто зберігати всю логіку в Controller?
5. Яким має бути напрямок залежностей між шарами?
6. Чи повинен Service залежати від Controller?
7. Що таке DTO?
8. Навіщо використовувати DTO?
9. У чому різниця між Request DTO і Response DTO?
10. Чому не варто повертати пароль у response?
11. Чи обов'язково використовувати автоматичні mapper-бібліотеки?
12. Що таке валідація?
13. Для чого використовується `@NotNull`?
14. У чому різниця між `@NotNull` і `@NotBlank`?
15. Для чого потрібен `@Size`?
16. Для чого потрібні `@Min` і `@Max`?
17. Для чого використовується `@Email`?
18. Що робить `@Valid`?
19. Який HTTP-статус зазвичай повертається при некоректному запиті?
20. Чому унікальність email — це не просто форматна валідація?
21. Де краще перевіряти складні бізнес-правила?
22. Чому для request DTO іноді краще використовувати `Integer`, а не `int`?

# Тема 10. Основи JPA та об'єктно-реляційного відображення

## 10.1. Навіщо потрібна база даних

Більшість серверних застосунків повинні зберігати дані постійно.

Наприклад:

- користувачів;
- студентів;
- замовлення;
- товари;
- курси;
- оцінки.

Якщо зберігати їх лише в пам'яті програми:

```java
List<Student> students = new ArrayList<>();
```

то після завершення програми дані буде втрачено.

База даних дозволяє зберігати інформацію незалежно від життєвого циклу Java-застосунку.

---

## 10.2. Реляційна база даних

У реляційній базі дані зберігаються у таблицях.

Наприклад, таблиця:

```text
students
```

може містити:

```text
+----+-------+-----+
| id | name  | age |
+----+-------+-----+
| 1  | Anna  | 20  |
| 2  | Ivan  | 21  |
+----+-------+-----+
```

У Java ми працюємо з об'єктами:

```java
Student student = new Student();
student.setId(1L);
student.setName("Anna");
student.setAge(20);
```

Отже виникає питання:

> Як перетворювати Java-об'єкти у записи таблиці і навпаки?

Саме цю задачу вирішує ORM.

---

## 10.3. Що таке ORM

ORM — Object-Relational Mapping.

Українською можна сказати:

**об'єктно-реляційне відображення**.

ORM пов'язує:

```text
Java class <-> database table
Java object <-> table row
Java field <-> table column
```

Наприклад:

```java
public class Student {
    private Long id;
    private String name;
    private Integer age;
}
```

може відповідати таблиці:

```text
students
```

---

## 10.4. Навіщо ORM

Без ORM програмісту довелося б вручну:

- відкривати з'єднання з БД;
- писати SQL;
- виконувати запит;
- отримувати ResultSet;
- вручну створювати Java-об'єкт.

Приблизно так:

```text
SQL -> ResultSet -> Student
```

ORM автоматизує значну частину цієї роботи.

---

## 10.5. Що таке JPA

JPA — Java Persistence API.

JPA — це специфікація, тобто набір правил та API для роботи з ORM у Java.

Важливий момент:

> JPA сама по собі не є конкретною бібліотекою, яка все виконує.

Потрібна реалізація JPA.

Найпоширеніша реалізація у Spring Boot — Hibernate.

Схема:

```text
Spring Data JPA
      |
      v
JPA
      |
      v
Hibernate
      |
      v
Database
```

---

## 10.6. Що таке Hibernate

Hibernate — ORM-фреймворк, який реалізує JPA.

Він уміє:

- перетворювати Entity у SQL;
- виконувати INSERT;
- виконувати SELECT;
- виконувати UPDATE;
- виконувати DELETE;
- створювати об'єкти з даних БД;
- працювати зі зв'язками між таблицями.

У Spring Boot ми часто не працюємо з Hibernate напряму.

Більшість базових задач виконується через Spring Data JPA.

---

## 10.7. Додавання залежності

Для роботи з JPA у Spring Boot потрібен starter:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Також потрібен драйвер конкретної бази даних.

Наприклад, для PostgreSQL:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 10.8. Яку базу даних використати

Для навчального курсу можна використати PostgreSQL.

Це популярна безкоштовна реляційна база даних.

Інші варіанти:

- MySQL;
- MariaDB;
- H2.

H2 дуже проста для демонстрацій, але PostgreSQL краще показує роботу з реальною СУБД.

---

## 10.9. Встановлення PostgreSQL

Потрібно завантажити PostgreSQL з офіційного сайту для своєї операційної системи.

Під час встановлення зазвичай потрібно:

1. вибрати каталог;
2. встановити сервер PostgreSQL;
3. задати пароль користувача `postgres`;
4. залишити стандартний порт `5432`;
5. завершити встановлення.

Важливо запам'ятати пароль.

Типові параметри:

```text
Host: localhost
Port: 5432
User: postgres
Password: ваш пароль
```

---

## 10.10. Створення бази даних

Можна використати pgAdmin.

Створимо базу:

```text
oop_db
```

У SQL це виглядало б:

```sql
CREATE DATABASE oop_db;
```

Для початку достатньо створити базу через графічний інтерфейс.

---

## 10.11. Налаштування Spring Boot

У `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oop_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Замість:

```text
your_password
```

потрібно вказати власний пароль.

---

## 10.12. Що означає datasource.url

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oop_db
```

Розберемо:

```text
jdbc:postgresql
```

— використовуємо PostgreSQL через JDBC.

```text
localhost
```

— база працює на цьому комп'ютері.

```text
5432
```

— стандартний порт PostgreSQL.

```text
oop_db
```

— ім'я бази даних.

---

## 10.13. ddl-auto

```properties
spring.jpa.hibernate.ddl-auto=update
```

Це налаштування дозволяє Hibernate оновлювати структуру таблиць на основі Entity.

Для навчального проєкту це зручно.

Можливі значення:

```text
none
validate
update
create
create-drop
```

На початку достатньо використовувати:

```text
update
```

---

## 10.14. Обережно з create

Якщо використати:

```properties
spring.jpa.hibernate.ddl-auto=create
```

таблиці можуть створюватися заново при запуску.

Це може призвести до втрати даних.

Для навчального проєкту безпечніше:

```properties
update
```

---

## 10.15. Entity

Клас, який відображається на таблицю, називається Entity.

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    private Long id;

    private String name;

    private Integer age;
}
```

Анотація:

```java
@Entity
```

означає:

> Цей клас є JPA-сутністю.

---

## 10.16. @Id

Кожна Entity повинна мати ідентифікатор.

```java
@Id
private Long id;
```

Поле з `@Id` є первинним ключем.

У таблиці:

```text
id PRIMARY KEY
```

---

## 10.17. Автоматична генерація id

Зазвичай id генерує база даних.

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

Тепер при створенні нового об'єкта не потрібно вручну задавати id.

Наприклад:

```java
Student student = new Student();
student.setName("Anna");
student.setAge(20);
```

Після збереження Entity отримає id.

---

## 10.18. Повна Entity

```java
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```

---

## 10.19. Навіщо потрібен порожній конструктор

JPA потребує конструктора без параметрів.

Тому Entity повинна мати:

```java
public Student() {
}
```

У навчальному коді краще робити його явно.

---

## 10.20. @Table

За замовчуванням JPA формує ім'я таблиці на основі класу.

Можна вказати явно:

```java
@Entity
@Table(name = "students")
public class Student {
}
```

Це робить схему зрозумілішою.

---

## 10.21. @Column

Поле можна пов'язати з конкретною колонкою:

```java
@Column(name = "full_name")
private String name;
```

Можна також вказати обмеження:

```java
@Column(nullable = false)
private String name;
```

---

## 10.22. Entity не повинна бути DTO

Після появи JPA особливо важливо не змішувати Entity і DTO.

Entity:

```text
описує структуру даних для persistence
```

DTO:

```text
описує дані API
```

Наприклад:

```java
@Entity
public class Student {
}
```

і:

```java
public class StudentResponse {
}
```

— це різні класи з різними ролями.

---

## 10.23. Що відбувається при запуску

Якщо:

```properties
spring.jpa.hibernate.ddl-auto=update
```

і є:

```java
@Entity
@Table(name = "students")
public class Student {
}
```

Hibernate може створити або оновити таблицю.

Спрощено:

```text
Entity
   |
   v
Hibernate
   |
   v
CREATE/ALTER TABLE
```

---

## 10.24. Типова помилка: немає @Entity

Якщо клас не позначений:

```java
@Entity
```

JPA не буде сприймати його як сутність.

---

## 10.25. Типова помилка: немає @Id

Entity без первинного ключа не є коректною JPA Entity.

Потрібно мати:

```java
@Id
```

---

## 10.26. Типова помилка: база не запущена

Якщо PostgreSQL не запущений, Spring Boot не зможе підключитися.

У консолі можна побачити помилки connection refused.

Потрібно перевірити:

```text
чи працює PostgreSQL
чи правильний порт
чи правильний пароль
чи існує база
```

---

# Тема 11. Робота з базою даних через Spring Data JPA

## 11.1. Навіщо Spring Data JPA

JPA та Hibernate вже сильно спрощують роботу з БД.

Але Spring Data JPA йде ще далі.

Воно дозволяє створювати Repository майже без коду.

Раніше ми писали вручну:

```java
@Repository
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();
}
```

Тепер Repository може працювати з реальною таблицею.

---

## 11.2. JpaRepository

Створимо інтерфейс:

```java
public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
```

І цього вже достатньо для базових CRUD-операцій.

Spring сам створить реалізацію.

---

## 11.3. Що означають типи

```java
JpaRepository<Student, Long>
```

Тут:

```text
Student
```

— тип Entity.

```text
Long
```

— тип id.

Якщо:

```java
@Id
private Long id;
```

то Repository має використовувати `Long`.

---

## 11.4. Основні методи JpaRepository

Ми одразу отримуємо:

```java
findAll()
findById()
save()
deleteById()
existsById()
count()
```

Не потрібно писати SQL для базових операцій.

---

## 11.5. findAll

```java
List<Student> students = repository.findAll();
```

Приблизно відповідає SQL:

```sql
SELECT * FROM students;
```

---

## 11.6. findById

```java
Optional<Student> student =
        repository.findById(1L);
```

Результат — `Optional<Student>`.

Це важливо, тому що запис може не існувати.

---

## 11.7. Optional

Неправильно припускати, що Entity завжди існує.

```java
Student student = repository.findById(id).get();
```

Якщо запису немає, буде помилка.

Краще:

```java
Student student = repository.findById(id)
        .orElseThrow();
```

Пізніше ми створимо власний exception.

---

## 11.8. save

Створення:

```java
Student student = new Student();
student.setName("Anna");
student.setAge(20);

Student saved = repository.save(student);
```

Hibernate виконає `INSERT`.

Після збереження:

```java
saved.getId()
```

міститиме згенерований id.

---

## 11.9. Оновлення

Для JPA метод `save()` використовується також при оновленні.

Наприклад:

```java
Student student = repository.findById(id)
        .orElseThrow();

student.setName("Olena");

repository.save(student);
```

У результаті буде виконано оновлення запису.

---

## 11.10. deleteById

```java
repository.deleteById(id);
```

Приблизно відповідає:

```sql
DELETE FROM students WHERE id = ?;
```

---

## 11.11. Repository

Повний код:

```java
package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
```

Зверніть увагу:

```java
@Repository
```

тут писати не обов'язково.

Spring Data сам розпізнає Repository-інтерфейс.

---

## 11.12. Service з Repository

```java
@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAll() {
        return repository.findAll();
    }
}
```

---

## 11.13. Пошук одного об'єкта

```java
public Student getById(Long id) {

    return repository.findById(id)
            .orElseThrow(
                () -> new RuntimeException("Student not found")
            );
}
```

Пізніше RuntimeException краще замінити на власний виняток.

---

## 11.14. Створення

```java
public Student create(Student student) {
    return repository.save(student);
}
```

Однак якщо використовуємо DTO, краще:

```java
public StudentResponse create(StudentRequest request) {

    Student student = new Student();

    student.setName(request.getName());
    student.setAge(request.getAge());
    student.setEmail(request.getEmail());

    Student saved = repository.save(student);

    return toResponse(saved);
}
```

---

## 11.15. Оновлення

```java
public StudentResponse update(
        Long id,
        StudentRequest request) {

    Student student = repository.findById(id)
            .orElseThrow();

    student.setName(request.getName());
    student.setAge(request.getAge());
    student.setEmail(request.getEmail());

    Student saved = repository.save(student);

    return toResponse(saved);
}
```

---

## 11.16. Видалення

```java
public void delete(Long id) {

    if (!repository.existsById(id)) {
        throw new RuntimeException("Student not found");
    }

    repository.deleteById(id);
}
```

---

## 11.17. Controller

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PostMapping
    public StudentResponse create(
            @Valid @RequestBody StudentRequest request) {

        return service.create(request);
    }
}
```

---

## 11.18. Власні методи Repository

Spring Data JPA дозволяє створювати методи за ім'ям.

Наприклад:

```java
List<Student> findByName(String name);
```

Spring автоматично розуміє:

```text
find
By
Name
```

і формує запит.

---

## 11.19. Приклад

Repository:

```java
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    List<Student> findByName(String name);
}
```

Service:

```java
public List<Student> findByName(String name) {
    return repository.findByName(name);
}
```

---

## 11.20. Пошук за віком

```java
List<Student> findByAge(Integer age);
```

---

## 11.21. Пошук за кількома полями

```java
List<Student> findByNameAndAge(
        String name,
        Integer age
);
```

---

## 11.22. Існування за email

```java
boolean existsByEmail(String email);
```

Це корисно для бізнес-перевірки унікальності.

Наприклад:

```java
if (repository.existsByEmail(request.getEmail())) {
    throw new IllegalArgumentException(
        "Email already exists"
    );
}
```

---

## 11.23. SQL у консолі

Якщо:

```properties
spring.jpa.show-sql=true
```

у консолі можна бачити SQL, який виконує Hibernate.

Це корисно для навчання.

Наприклад:

```text
select ...
insert into ...
update ...
```

---

## 11.24. Repository не повинен містити HTTP-логіку

Repository не повинен знати:

- що таке HTTP;
- що таке ResponseEntity;
- який status code повернути.

Його задача — робота з даними.

---

## 11.25. Service не повинен працювати з SQL вручну без потреби

У базовому курсі краще використовувати можливості Spring Data JPA.

Наприклад:

```java
repository.findAll();
```

замість ручного SQL для простої CRUD-операції.

---

## 11.26. Повний потік даних

```text
POST /students
      |
      v
StudentRequest
      |
      v
Controller
      |
      v
Service
      |
      v
Student Entity
      |
      v
StudentRepository.save()
      |
      v
PostgreSQL
```

Відповідь:

```text
PostgreSQL
      |
      v
Student Entity
      |
      v
StudentResponse
      |
      v
HTTP JSON
```

---

# Тема 12. Зв'язки між сутностями у JPA

## 12.1. Навіщо потрібні зв'язки

У реальній предметній області об'єкти пов'язані.

Наприклад:

```text
Student -> Group
Order -> Customer
Order -> OrderItems
Course -> Students
```

У базі даних ці зв'язки реалізуються через зовнішні ключі.

У Java ми хочемо працювати з об'єктами.

JPA дозволяє описати ці зв'язки анотаціями.

---

## 12.2. Основні типи зв'язків

JPA має:

```text
@OneToOne
@OneToMany
@ManyToOne
@ManyToMany
```

Розглянемо їх на простих прикладах.

---

## 12.3. Many-to-One

Найзручніше почати з прикладу:

```text
багато студентів -> одна група
```

Тобто багато `Student` можуть належати одному `Group`.

На стороні Student:

```java
@ManyToOne
private Group group;
```

---

## 12.4. Entity Group

```java
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Group() {
    }

    // getters/setters
}
```

---

## 12.5. Student з Group

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {
    }

    // getters/setters
}
```

---

## 12.6. Що робить @JoinColumn

```java
@JoinColumn(name = "group_id")
```

означає, що в таблиці `students` буде колонка:

```text
group_id
```

Вона міститиме id групи.

Схематично:

```text
groups
+----+--------+
| id | name   |
+----+--------+

students
+----+-------+----------+
| id | name  | group_id |
+----+-------+----------+
```

---

## 12.7. One-to-Many

З іншого боку:

```text
одна група -> багато студентів
```

У Group:

```java
@OneToMany(mappedBy = "group")
private List<Student> students = new ArrayList<>();
```

---

## 12.8. Що означає mappedBy

```java
mappedBy = "group"
```

означає:

> Зв'язком керує поле `group` у класі Student.

Тобто зовнішній ключ знаходиться в таблиці `students`.

---

## 12.9. Двонапрямний зв'язок

Якщо:

```java
Student -> Group
```

і:

```java
Group -> List<Student>
```

то це двонапрямний зв'язок.

Можна переходити в обидві сторони.

Але двонапрямні зв'язки складніші.

Для початкового курсу іноді краще починати з однонапрямного:

```java
@ManyToOne
private Group group;
```

і не додавати `List<Student>` у Group без потреби.

---

## 12.10. One-to-One

Приклад:

```text
User -> Profile
```

Один користувач має один профіль.

```java
@OneToOne
@JoinColumn(name = "profile_id")
private Profile profile;
```

---

## 12.11. Приклад Profile

```java
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;
}
```

User:

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
```

---

## 12.12. Many-to-Many

Приклад:

```text
Student <-> Course
```

Один студент може відвідувати багато курсів.

Один курс може мати багато студентів.

```java
@ManyToMany
private List<Course> courses;
```

Для цього в БД потрібна проміжна таблиця.

Наприклад:

```text
student_courses
```

---

## 12.13. Приклад

```java
@ManyToMany
@JoinTable(
    name = "student_courses",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private List<Course> courses = new ArrayList<>();
```

---

## 12.14. Чому Many-to-Many краще не ускладнювати

Для початкового курсу достатньо розуміти саму ідею.

У реальних системах many-to-many часто замінюють окремою Entity.

Наприклад:

```text
StudentCourse
```

Це дозволяє додати:

- дату запису;
- статус;
- оцінку.

---

## 12.15. Cascade

JPA дозволяє вказувати каскадні операції.

Наприклад:

```java
@OneToMany(
    mappedBy = "group",
    cascade = CascadeType.ALL
)
private List<Student> students;
```

Це означає, що деякі операції з Group можуть автоматично застосовуватися і до Student.

Але `CascadeType.ALL` потрібно використовувати обережно.

На базовому курсі достатньо знати, що каскади існують і впливають на пов'язані Entity.

---

## 12.16. FetchType

Зв'язки можуть завантажуватися:

```text
EAGER
LAZY
```

`EAGER`:

```text
завантажити пов'язаний об'єкт одразу
```

`LAZY`:

```text
завантажити пізніше, коли він знадобиться
```

На початку достатньо розуміти цю різницю концептуально.

---

## 12.17. Проблема JSON і двонапрямних зв'язків

Припустимо:

```text
Group -> Students
Student -> Group
```

Якщо напряму повертати Entity у JSON, може виникнути цикл:

```text
Group
 -> Student
    -> Group
       -> Student
          ...
```

Саме тому DTO особливо важливі при роботі зі зв'язками.

---

## 12.18. DTO для Student

Замість повернення всієї Group:

```java
public class StudentResponse {

    private Long id;
    private String name;
    private Long groupId;
    private String groupName;
}
```

Це значно простіше для API.

---

## 12.19. Створення Student з groupId

Request:

```java
public class StudentRequest {

    private String name;
    private Long groupId;
}
```

У Service:

```java
Group group = groupRepository.findById(
        request.getGroupId()
).orElseThrow();

Student student = new Student();
student.setName(request.getName());
student.setGroup(group);

studentRepository.save(student);
```

Це типовий підхід.

---

## 12.20. Чому не варто передавати всю Group у request

Не дуже зручно:

```json
{
  "name": "Anna",
  "group": {
    "id": 5,
    "name": "AI-251"
  }
}
```

Для створення Student зазвичай достатньо:

```json
{
  "name": "Anna",
  "groupId": 5
}
```

Service сам знайде Group.

---

## 12.21. Повний приклад Many-to-One

Group:

```java
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Group() {
    }

    // getters/setters
}
```

Student:

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {
    }

    // getters/setters
}
```

Repository:

```java
public interface GroupRepository
        extends JpaRepository<Group, Long> {
}
```

```java
public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
```

---

## 12.22. Service

```java
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentService(
            StudentRepository studentRepository,
            GroupRepository groupRepository) {

        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public StudentResponse create(StudentRequest request) {

        Group group = groupRepository
                .findById(request.getGroupId())
                .orElseThrow(
                    () -> new RuntimeException(
                        "Group not found"
                    )
                );

        Student student = new Student();

        student.setName(request.getName());
        student.setGroup(group);

        Student saved =
                studentRepository.save(student);

        return new StudentResponse(
            saved.getId(),
            saved.getName(),
            saved.getGroup().getId(),
            saved.getGroup().getName()
        );
    }
}
```

---

## 12.23. Що важливо розуміти про зв'язки

Не потрібно запам'ятовувати всі анотації механічно.

Краще спочатку визначити реальний зв'язок.

Наприклад:

```text
багато студентів належать одній групі
```

Тоді:

```text
Student -> @ManyToOne -> Group
```

Якщо:

```text
один користувач має один профіль
```

тоді:

```text
@OneToOne
```

---

## 12.24. Типова помилка: неправильна сторона зв'язку

Потрібно спочатку сформулювати зв'язок словами.

Наприклад:

```text
many students -> one group
```

Отже на стороні Student:

```java
@ManyToOne
```

Не потрібно вибирати анотацію навмання.

---

## 12.25. Типова помилка: повернення Entity напряму

При простих Entity це може працювати.

Але зі зв'язками виникають:

- зайві поля;
- рекурсія;
- складний JSON;
- залежність API від структури БД.

Тому DTO стає ще важливішим.

---

## 12.26. Типова помилка: CascadeType.ALL всюди

Не слід автоматично додавати:

```java
cascade = CascadeType.ALL
```

до кожного зв'язку.

Це може призвести до небажаного видалення або збереження пов'язаних сутностей.

Для початкового курсу краще використовувати каскади лише там, де чітко зрозуміла їхня мета.

---

# Повний зв'язок тем 10–12

```text
Тема 10
JPA та ORM
    |
    v
Java object <-> database row

Тема 11
Spring Data JPA
    |
    v
Repository без ручного CRUD SQL

Тема 12
Зв'язки Entity
    |
    v
моделювання реальної предметної області
```

---

# Архітектура після теми 12

Тепер наш застосунок виглядає приблизно так:

```text
Client
  |
  v
REST Controller
  |
  v
DTO
  |
  v
Service
  |
  v
Entity
  |
  v
JpaRepository
  |
  v
Hibernate
  |
  v
PostgreSQL
```

Це вже основа типового Spring Boot CRUD-застосунку.

---

# Що потрібно знати після тем 10–12

Студент повинен уміти пояснити:

- навіщо потрібна база даних;
- що таке ORM;
- що таке JPA;
- роль Hibernate;
- що таке Entity;
- для чого потрібні `@Entity`, `@Id`, `@GeneratedValue`;
- для чого використовується `@Table`;
- як підключити PostgreSQL до Spring Boot;
- що робить `spring.jpa.hibernate.ddl-auto`;
- що таке `JpaRepository`;
- які базові CRUD-методи він надає;
- як працюють `findAll`, `findById`, `save`, `deleteById`;
- що таке `Optional`;
- як створювати query methods за іменем;
- що означають `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`;
- що робить `@JoinColumn`;
- навіщо потрібен `mappedBy`;
- чому DTO важливі при роботі зі зв'язками;
- чому не слід бездумно використовувати `CascadeType.ALL`.

---

# Питання для самоперевірки

1. Чому звичайний `List` не підходить для постійного зберігання даних?
2. Що таке реляційна база даних?
3. Що таке ORM?
4. Що таке JPA?
5. Яку роль виконує Hibernate?
6. Що таке Entity?
7. Для чого використовується `@Entity`?
8. Для чого потрібен `@Id`?
9. Що робить `@GeneratedValue`?
10. Для чого використовується `@Table`?
11. Що означає `spring.jpa.hibernate.ddl-auto=update`?
12. Які параметри потрібні для підключення до PostgreSQL?
13. Що таке `JpaRepository`?
14. Що означають параметри `JpaRepository<Student, Long>`?
15. Для чого використовується `findAll()`?
16. Чому `findById()` повертає `Optional`?
17. Для чого використовується `save()`?
18. Для чого використовується `deleteById()`?
19. Як працює метод `findByName()` у Spring Data JPA?
20. Що означає `@ManyToOne`?
21. Наведіть приклад зв'язку `OneToMany`.
22. Для чого використовується `@JoinColumn`?
23. Що означає `mappedBy`?
24. Що таке `@OneToOne`?
25. Що таке `@ManyToMany`?
26. Чому двонапрямні зв'язки можуть створювати проблеми при серіалізації у JSON?
27. Чому DTO особливо важливі при роботі зі зв'язками між Entity?
28. Чому не варто автоматично додавати `CascadeType.ALL` до всіх зв'язків?

# Тема 13. Пошук, фільтрація та прості запити до даних

## 13.1. Навіщо потрібен пошук

У реальному застосунку недостатньо лише:

```text
отримати всі записи
отримати запис за id
створити
оновити
видалити
```

Користувачі часто хочуть:

- знайти студента за ім'ям;
- отримати студентів певного віку;
- знайти всіх студентів певної групи;
- відфільтрувати записи за кількома параметрами;
- відсортувати список.

Для цього Spring Data JPA дозволяє створювати запити дуже просто.

## 13.2. Query methods

Spring Data JPA аналізує назву методу Repository і автоматично формує запит.

Наприклад:

```java
List<Student> findByName(String name);
```

Spring розуміє:

```text
find
By
Name
```

і створює відповідний запит.

## 13.3. Пошук за ім'ям

Repository:

```java
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    List<Student> findByName(String name);
}
```

Service:

```java
public List<Student> findByName(String name) {
    return repository.findByName(name);
}
```

Controller:

```java
@GetMapping("/search")
public List<Student> search(
        @RequestParam String name) {

    return studentService.findByName(name);
}
```

Запит:

```text
GET /students/search?name=Anna
```

## 13.4. Пошук без урахування регістру

```java
List<Student> findByNameIgnoreCase(String name);
```

Тоді значення:

```text
anna
Anna
ANNA
```

можуть оброблятися однаково.

## 13.5. Пошук за частиною рядка

```java
List<Student> findByNameContaining(String text);
```

Наприклад, пошук `ann` може знайти `Anna` або `Joanna`.

## 13.6. Пошук за числовими полями

```java
List<Student> findByAge(Integer age);
```

Старші за заданий вік:

```java
List<Student> findByAgeGreaterThan(Integer age);
```

Молодші:

```java
List<Student> findByAgeLessThan(Integer age);
```

У діапазоні:

```java
List<Student> findByAgeBetween(
        Integer minAge,
        Integer maxAge
);
```

## 13.7. Кілька умов

```java
List<Student> findByNameAndAge(
        String name,
        Integer age
);
```

Spring сформує умову з `AND`.

Для `OR`:

```java
List<Student> findByNameOrEmail(
        String name,
        String email
);
```

## 13.8. Пошук за пов'язаною Entity

Якщо:

```java
@ManyToOne
private Group group;
```

можна написати:

```java
List<Student> findByGroupId(Long groupId);
```

Таким чином можна отримати всіх студентів певної групи.

## 13.9. existsBy і countBy

Перевірка існування:

```java
boolean existsByEmail(String email);
```

Наприклад:

```java
if (repository.existsByEmail(request.getEmail())) {
    throw new IllegalArgumentException(
        "Email already exists"
    );
}
```

Підрахунок:

```java
long countByGroupId(Long groupId);
```

## 13.10. Сортування

Найпростіший варіант:

```java
List<Student> findAllByOrderByNameAsc();
```

Але гнучкіше використовувати `Sort`:

```java
List<Student> students =
        repository.findAll(
            Sort.by("name")
        );
```

За спаданням:

```java
Sort.by(
    Sort.Direction.DESC,
    "age"
)
```

## 13.11. @Query

Якщо назва методу стає надто складною, можна явно описати запит:

```java
@Query("""
    select s
    from Student s
    where s.age >= :age
""")
List<Student> findAdults(
        @Param("age") Integer age
);
```

Тут використовується JPQL.

## 13.12. Що таке JPQL

JPQL схожий на SQL, але працює з Entity та їх полями.

```java
select s
from Student s
where s.name = :name
```

Тут `Student` — це Java Entity.

## 13.13. Native Query

Можна виконувати і звичайний SQL:

```java
@Query(
    value = """
        select *
        from students
        where age >= :age
    """,
    nativeQuery = true
)
List<Student> findAdultsNative(
        @Param("age") Integer age
);
```

Для базового курсу краще починати з query methods і простого JPQL.

## 13.14. Пошук з необов'язковими параметрами

Controller:

```java
@GetMapping
public List<StudentResponse> getStudents(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer age) {

    return service.search(name, age);
}
```

Service:

```java
public List<Student> search(
        String name,
        Integer age) {

    if (name != null && age != null) {
        return repository.findByNameAndAge(
            name,
            age
        );
    }

    if (name != null) {
        return repository.findByName(name);
    }

    if (age != null) {
        return repository.findByAge(age);
    }

    return repository.findAll();
}
```

Для невеликого навчального проєкту цього достатньо.

## 13.15. Коли не треба ускладнювати

У великих системах існують:

- Specifications;
- Criteria API;
- QueryDSL;
- складні динамічні фільтри.

Але для базового курсу головне навчитися:

```text
query methods
@RequestParam
простий @Query
Sort
```

## 13.16. Типові помилки

Не варто переносити всю логіку пошуку в Controller. Правильніше:

```text
Controller -> Service -> Repository
```

Також не варто створювати надто довгі назви Repository-методів. Якщо метод важко прочитати, краще перейти до `@Query`.

---

# Тема 14. Обробка винятків і помилок у REST API

## 14.1. Чому помилки неминучі

У серверному застосунку можуть виникати ситуації:

- запис не знайдено;
- клієнт передав некоректні дані;
- email уже існує;
- не знайдено пов'язану групу;
- порушено бізнес-правило;
- сталася непередбачена помилка.

API має повідомляти про ці ситуації зрозуміло.

## 14.2. Власний exception

Замість звичайного:

```java
repository.findById(id).orElseThrow();
```

краще створити власний тип помилки:

```java
public class StudentNotFoundException
        extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Student with id " + id + " not found");
    }
}
```

Service:

```java
public Student getById(Long id) {
    return repository.findById(id)
            .orElseThrow(
                () -> new StudentNotFoundException(id)
            );
}
```

## 14.3. Чому RuntimeException

Для бізнес-помилок у Spring-застосунках часто використовують unchecked exceptions:

```java
extends RuntimeException
```

Це дозволяє не додавати `throws` до кожного методу.

## 14.4. @ExceptionHandler

Виняток можна перетворити на HTTP-відповідь:

```java
@ExceptionHandler(StudentNotFoundException.class)
public ResponseEntity<String> handleNotFound(
        StudentNotFoundException ex) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
}
```

Тепер клієнт отримає:

```text
404 Not Found
```

## 14.5. @RestControllerAdvice

Щоб не дублювати обробку в кожному Controller, створюють глобальний handler:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

Приклад:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
        StudentNotFoundException.class
    )
    public ResponseEntity<String> handleStudentNotFound(
            StudentNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
```

## 14.6. Структурована відповідь про помилку

Краще повертати JSON:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Student with id 10 not found"
}
```

DTO:

```java
public class ErrorResponse {

    private int status;
    private String error;
    private String message;

    public ErrorResponse(
            int status,
            String error,
            String message) {

        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
```

Handler:

```java
@ExceptionHandler(
    StudentNotFoundException.class
)
public ResponseEntity<ErrorResponse>
        handleStudentNotFound(
            StudentNotFoundException ex) {

    ErrorResponse error = new ErrorResponse(
        404,
        "Not Found",
        ex.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}
```

## 14.7. Конфлікт даних

Наприклад, email має бути унікальним.

```java
public class EmailAlreadyExistsException
        extends RuntimeException {

    public EmailAlreadyExistsException(
            String email) {

        super(
            "Email " + email + " already exists"
        );
    }
}
```

Service:

```java
if (repository.existsByEmail(request.getEmail())) {
    throw new EmailAlreadyExistsException(
        request.getEmail()
    );
}
```

Для такої ситуації можна використовувати:

```text
409 Conflict
```

## 14.8. Валідаційні помилки

Коли DTO позначено `@Valid`, Spring може згенерувати `MethodArgumentNotValidException`.

Його можна обробити глобально:

```java
@ExceptionHandler(
    MethodArgumentNotValidException.class
)
public ResponseEntity<Map<String, String>>
        handleValidation(
            MethodArgumentNotValidException ex) {

    Map<String, String> errors =
            new HashMap<>();

    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error ->
          errors.put(
              error.getField(),
              error.getDefaultMessage()
          )
      );

    return ResponseEntity
            .badRequest()
            .body(errors);
}
```

Приклад відповіді:

```json
{
  "name": "Name is required",
  "age": "Age must be at least 16"
}
```

## 14.9. Основні HTTP-статуси помилок

```text
400 Bad Request
```

— некоректні вхідні дані.

```text
404 Not Found
```

— ресурс не знайдено.

```text
409 Conflict
```

— конфлікт стану, наприклад дубль унікального email.

```text
500 Internal Server Error
```

— непередбачена внутрішня помилка.

## 14.10. Не треба повертати 200 для помилки

Погано:

```text
200 OK
```

і в body:

```json
{
  "success": false,
  "message": "Student not found"
}
```

Краще використовувати справжній HTTP-статус:

```text
404 Not Found
```

## 14.11. Не треба показувати stack trace клієнту

Клієнту потрібні:

- статус;
- тип помилки;
- зрозуміле повідомлення.

Внутрішній stack trace має залишатися в логах сервера.

## 14.12. Де повинна виникати помилка

Корисна схема:

```text
Repository
   |
   v
Service
   |
   | throw business exception
   v
GlobalExceptionHandler
   |
   v
HTTP response
```

Controller при цьому залишається простим.

Погано:

```java
@GetMapping("/{id}")
public ResponseEntity<?> getById(
        @PathVariable Long id) {

    try {
        return ResponseEntity.ok(
            service.getById(id)
        );
    } catch (Exception ex) {
        return ResponseEntity
                .status(500)
                .body("Error");
    }
}
```

Краще:

```java
@GetMapping("/{id}")
public StudentResponse getById(
        @PathVariable Long id) {

    return service.getById(id);
}
```

а винятки обробляти централізовано.

---

# Тема 15. Документування REST API за допомогою OpenAPI та Swagger UI

## 15.1. Навіщо документувати API

Коли API має багато endpoint'ів, потрібно знати:

- які адреси існують;
- які HTTP-методи використовуються;
- які параметри передавати;
- який JSON очікується;
- яку відповідь повертає сервер;
- які статуси можливі.

Без документації доводиться читати код Controller.

## 15.2. Що таке OpenAPI

OpenAPI — стандарт опису HTTP API.

Він дозволяє описати:

```text
endpoint
HTTP method
request parameters
request body
response
status codes
```

## 15.3. Що таке Swagger UI

Swagger UI — вебінтерфейс для перегляду OpenAPI-документації.

У ньому можна:

- побачити всі endpoint'и;
- переглянути параметри;
- переглянути DTO;
- побачити можливі відповіді;
- виконати HTTP-запит прямо з браузера.

## 15.4. OpenAPI і Swagger

Спрощено:

```text
OpenAPI
```

— стандарт опису API.

```text
Swagger UI
```

— інтерфейс для відображення цього опису.

## 15.5. Додавання springdoc-openapi

Для Spring MVC-проєкту використовується starter `springdoc-openapi`.

Приклад залежності:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>
        springdoc-openapi-starter-webmvc-ui
    </artifactId>
    <version>актуальна_версія</version>
</dependency>
```

Версію бібліотеки потрібно брати з актуальної документації `springdoc-openapi`, оскільки з часом вона змінюється.

## 15.6. Swagger UI після запуску

Після підключення бібліотеки Swagger UI зазвичай доступний за одним зі стандартних шляхів:

```text
/swagger-ui.html
```

або:

```text
/swagger-ui/index.html
```

Точний шлях може залежати від версії та конфігурації.

## 15.7. Автоматичне формування документації

Якщо є:

```java
@RestController
@RequestMapping("/students")
public class StudentController {

    @GetMapping
    public List<StudentResponse> getAll() {
        ...
    }
}
```

springdoc автоматично знаходить цей endpoint.

Тому навіть без додаткових анотацій Swagger UI вже може показати API.

## 15.8. @Operation

Для опису endpoint:

```java
@Operation(
    summary = "Отримати всіх студентів"
)
@GetMapping
public List<StudentResponse> getAll() {
    return service.getAll();
}
```

Розширений варіант:

```java
@Operation(
    summary = "Створити нового студента",
    description =
        "Створює студента на основі переданих даних"
)
@PostMapping
public StudentResponse create(
        @Valid
        @RequestBody
        StudentRequest request) {

    return service.create(request);
}
```

## 15.9. @Parameter

```java
@GetMapping("/{id}")
public StudentResponse getById(
        @Parameter(
            description = "Ідентифікатор студента"
        )
        @PathVariable Long id) {

    return service.getById(id);
}
```

## 15.10. @Schema

DTO також можна описувати:

```java
@Schema(
    description = "Ім'я студента",
    example = "Anna"
)
private String name;
```

Повніший приклад:

```java
public class StudentRequest {

    @Schema(
        description = "Ім'я студента",
        example = "Anna"
    )
    @NotBlank
    private String name;

    @Schema(
        description = "Вік студента",
        example = "20"
    )
    @NotNull
    @Min(16)
    private Integer age;

    @Schema(
        description = "Email студента",
        example = "anna@example.com"
    )
    @Email
    private String email;
}
```

## 15.11. @ApiResponse

Можна описати можливі статуси:

```java
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Студента знайдено"
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Студента не знайдено"
    )
})
```

Повний приклад:

```java
@Operation(
    summary = "Отримати студента за id"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Студента знайдено"
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Студента не знайдено"
    )
})
@GetMapping("/{id}")
public StudentResponse getById(
        @Parameter(
            description = "ID студента"
        )
        @PathVariable Long id) {

    return service.getById(id);
}
```

## 15.12. Try it out

Swagger UI дозволяє виконати endpoint прямо з браузера.

Наприклад для:

```text
POST /students
```

можна передати:

```json
{
  "name": "Anna",
  "age": 20,
  "email": "anna@example.com"
}
```

і одразу побачити:

- URL;
- request;
- response body;
- status code.

Це дуже зручно для навчального проєкту.

## 15.13. Чи замінює Swagger UI Postman

Не повністю.

Swagger UI добре підходить для:

- документації;
- швидкої перевірки;
- демонстрації API;
- навчання.

Postman або HTTP Client зручніші для великих колекцій і складніших сценаріїв.

Для базового курсу Swagger UI цілком достатньо.

## 15.14. Не треба документувати очевидне надмірно

Не потрібно додавати десятки анотацій до кожного простого методу.

Документація має пояснювати важливе:

- призначення endpoint;
- параметри;
- обмеження;
- можливі статуси;
- приклади.

---

# Підсумкова архітектура курсу

Після всіх тем типовий застосунок має приблизно таку структуру:

```text
Client
   |
   | HTTP / JSON
   v
REST Controller
   |
   | Request DTO + Validation
   v
Service
   |
   | Business Logic
   v
Repository
   |
   | Spring Data JPA
   v
Hibernate
   |
   v
PostgreSQL
```

Окремо:

```text
GlobalExceptionHandler
        |
        v
структуровані HTTP-помилки
```

і:

```text
OpenAPI
   |
   v
Swagger UI
```

---

# Приклад завершеного API

```text
GET    /students
GET    /students/{id}
POST   /students
PUT    /students/{id}
DELETE /students/{id}

GET    /students/search?name=Anna
GET    /students?groupId=2
```

Рекомендована структура пакетів:

```text
controller
dto
service
repository
model
exception
```

---

# Типові помилки студентів у темах 13–15

## 1. Увесь пошук реалізований у Controller

Краще:

```text
Controller -> Service -> Repository
```

## 2. Repository-метод має надто довгу назву

Якщо назву важко читати, варто використати `@Query`.

## 3. Для відсутнього ресурсу повертається 500

Для відсутнього ресурсу логічніше:

```text
404 Not Found
```

## 4. Для будь-якої помилки повертається 200

HTTP-статус повинен відповідати реальному результату.

## 5. У Controller багато try-catch

Краще використовувати:

```java
@RestControllerAdvice
```

та:

```java
@ExceptionHandler
```

## 6. Клієнту повертається stack trace

Технічні деталі повинні залишатися на сервері.

## 7. Swagger використовується замість нормального дизайну API

Документація не виправляє погану структуру endpoint'ів.

Спочатку API має бути логічним, а потім — документованим.

---

# Зв'язок тем 13–15

```text
Тема 13
Пошук і фільтрація
      |
      v
робимо API кориснішим

Тема 14
Обробка помилок
      |
      v
робимо API передбачуваним

Тема 15
OpenAPI і Swagger UI
      |
      v
робимо API зрозумілим і документованим
```

---

# Що потрібно знати після тем 13–15

Студент повинен уміти пояснити:

- як працюють query methods у Spring Data JPA;
- як створити `findByName`;
- як використовувати `Containing`, `IgnoreCase`, `GreaterThan`, `Between`;
- як шукати за пов'язаною Entity;
- як використовувати `existsBy`;
- як сортувати через `Sort`;
- для чого потрібен `@Query`;
- що таке JPQL;
- навіщо створювати власні exceptions;
- для чого потрібен `@ExceptionHandler`;
- що робить `@RestControllerAdvice`;
- як повертати структуровану помилку;
- коли використовувати 400, 404, 409, 500;
- що таке OpenAPI;
- що таке Swagger UI;
- навіщо потрібен springdoc-openapi;
- для чого використовуються `@Operation`, `@Parameter`, `@Schema`, `@ApiResponse`;
- як перевіряти REST API через Swagger UI.

---

# Питання для самоперевірки

1. Що таке query method у Spring Data JPA?
2. Як працює `findByName()`?
3. Для чого використовується `Containing`?
4. Що робить `IgnoreCase`?
5. Для чого використовується `GreaterThan`?
6. Як знайти записи за діапазоном значень?
7. Як шукати Student за `groupId`?
8. Для чого використовується `existsBy`?
9. Як виконати сортування через `Sort`?
10. Коли краще використовувати `@Query`?
11. Що таке JPQL?
12. Чому не варто писати всю логіку фільтрації в Controller?
13. Навіщо створювати власні exceptions?
14. Що робить `@ExceptionHandler`?
15. Для чого використовується `@RestControllerAdvice`?
16. Який статус слід повернути, якщо ресурс не знайдено?
17. Який статус логічно повернути при дублюванні унікального email?
18. Чому не варто повертати stack trace клієнту?
19. Що таке OpenAPI?
20. Що таке Swagger UI?
21. Для чого потрібен springdoc-openapi?
22. Що робить `@Operation`?
23. Для чого використовується `@Parameter`?
24. Для чого потрібен `@Schema`?
25. Чи можна виконувати HTTP-запити прямо зі Swagger UI?
26. Чи замінює Swagger UI Postman у всіх випадках?
27. Які основні шари має завершений Spring Boot CRUD-застосунок?
