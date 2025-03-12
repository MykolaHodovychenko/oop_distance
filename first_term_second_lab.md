# Друга лабораторна робота.

## Частина 1. Повторне використання коду. Успадкування та композиція

### Завдання 1. Клас `DiscountBill`

Дан клас `GroceryBill`, який моделює чек та зберігає список товарів, який купляє людина у магазині.

```java
// ------------ EMPLOYEE

public class Employee {
  private String name;
  
  public Employee(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}

// ------------ GROCERY BILL

import java.util.ArrayList;
import java.util.List;

public class GroceryBill {
  private Employee clerk;
  
  private List<Item> receipt;
  
  private double total;
  
  public GroceryBill(Employee clerk) {
    this.clerk = clerk;
    this.receipt = new ArrayList<>();
    this.total = 0.0D;
  }
  
  public void add(Item i) {
    this.receipt.add(i);
    this.total += i.getPrice();
  }
  
  public double getTotal() {
    return Math.rint(this.total * 100.0D) / 100.0D;
  }
  
  public Employee getClerk() {
    return this.clerk;
  }
}

// ------------ ITEM

public class Item {
  private String name;
  
  private double price;
  
  private double discount;
  
  public Item(String name, double price, double discount) {
    this.name = name;
    this.price = price;
    this.discount = discount;
  }
  
  public double getPrice() {
    return this.price;
  }
  
  public double getDiscount() {
    return this.discount;
  }
}
```

Розробіть клас `DiscountBill`, який розширює клас `GroceryBill` та додає логіку для обліку знижок для постійних клієнтів. Конструктор класу `DiscountBill` повинен приймати на вхід аргумент, який вказує, чи є покупець постійним.

Клас `DiscountBill` повинен реалізувати власну логіку методу `getTotal()` для постійних клієнтів. Наприклад, якщо повна сума становить 80 гривень, а знижка для постійного клієнта становить 20 гривень, метод повинен повернути 60 гривень.

Крім того, вам потрібно відслідковувати кількість товарів зі знижкою (які мають розмір знижки більше `0.0`), а також загальну знижку, як у гривнях, так і в відсотках від суми в чеку (тобто наскільки в відсотках постійний покупець заплатив менше, ніж у випадку коли б він був звичайним покупцем).

Окрім перевизначених методів, клас `DiscountBill` повинен мати наступні конструктори та публічні методи:

- `public DiscountBill (Employee clerk, boolean regularCustomer)` - створює об'єкт `DiscountBill` для даного `clerk`;
- `public int getDiscountCount()` - повертає кількість товарів зі знижкою;
- `public double getDiscountAmount()` - повертає загальну знижку в гривнях;
- `public double getDiscountPercent()` - повертає відсоток знижки на товари (на скільки відсотків покупець заплатив менше). Відсоток можна обчислити за формулою: `100 - (ціна зі знижкою * 100) / повна ціна`.

Якщо покупець не є регулярним, клас `DiscountBill` повинен поводити себе так, ніби загальна знижка становить `0.0` та всі товари повинні враховуватися за повну вартість.

### Завдання 2. Клас `MinMaxAccount`

Компанія розробила великий та складний клас `BankingAccount` з багатьма методами.

```java
// ------------ BANKING ACCOUNT

import java.util.LinkedList;
import java.util.List;

public class BankingAccount {
  private int balance;
  
  private List<String> historyTransaction;
  
  private List<String> historyBalance;
  
  public BankingAccount(Startup s) {
    this.balance = s.getBalance();
    this.historyTransaction = new LinkedList<>();
    this.historyBalance = new LinkedList<>();
    this.historyTransaction.add(valueToHistory(s.getBalance()));
    this.historyBalance.add(toString());
  }
  
  public void debit(Debit d) {
    this.balance += d.getBalance();
    this.historyTransaction.add(valueToHistory(d.getBalance()));
    this.historyBalance.add(toString());
  }
  
  public void credit(Credit c) {
    this.balance += c.getBalance();
    this.historyTransaction.add(valueToHistory(c.getBalance()));
    this.historyBalance.add(toString());
  }
  
  public int getBalance() {
    return this.balance;
  }
  
  public boolean equals(Object o) {
    if (o instanceof BankingAccount)
      return (getBalance() == ((BankingAccount)o).getBalance()); 
    return false;
  }
  
  private String valueToHistory(int value) {
    int absValue = Math.abs(value);
    return ((value < 0) ? "(-" : "") + ((value < 0) ? "(-" : "") + "." + absValue / 100 + absValue % 100 / 10 + absValue % 100 % 10;
  }
  
  public String toString() {
    int absBalance = Math.abs(this.balance);
    return ((this.balance < 0) ? "-" : "") + "$" + ((this.balance < 0) ? "-" : "") + "." + absBalance / 100 + absBalance % 100 / 10;
  }
}

// ------------ DEBIT

public class Debit {
  private int balance;
  
  public Debit(int balance) {
    this.balance = balance;
  }
  
  public int getBalance() {
    return this.balance;
  }
}

// ------------ CREDIT

public class Credit {
  private int balance;
  
  public Credit(int balance) {
    this.balance = balance;
  }
  
  public int getBalance() {
    return this.balance;
  }
}

// ------------ STARTUP

public class Startup {
  private int balance;
  
  public Startup(int balance) {
    this.balance = balance;
  }
  
  public int getBalance() {
    return this.balance;
  }
}
```

Розробіть новий клас `MinMaxAccount`, об'єкти якого можуть бути використані замість об'єктів класу `BankingAccount`. Клас `MinMaxAccount` включає додаткову поведінку - він запам'ятовує мінімальне та максимальне значення балансу, яке було зафіксоване на балансі рахунку. Ви повинні надати ті ж самі методи, що і суперклас, а також наступну нову поведінку:

- `public MinMaxAccount(Startup s)` - створює об'єкт `MinMaxAccount`, використовуючи інформацію в об'єкті класу `Startup`
- `public int getMin()` - повертає мінімальне значення балансу у копійках
- `public int getMax()` - повертає максимальне значення балансу у копійках

Конструктор `BankingAccount` визначає початкове значення балансу, виходячи з інформації в об'єкті класу `Startup`.

В рамках задачі визначено, що значення балансу змінюється тільки в методах `debit()` та `credit()`.


## Частина 2. Лямбда-вирази. Параметрізація поведінки


### 1. Функціональний інтерфейс `Predicate` 

 `Predicate` - вбудований функціональний інтерфейс, доданий до Java 8 до пакету `java.util.function`. Інтерфейс описує один метод `test()`, який приймає значення на вході, перевіряє його стан і повертає `boolean` у якості результату. Інтерфейс `Predicate` повертає `true` або `false` відповідно до деякого значення.

Інтерфейс виглядає наступним чином

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
```

> Поки що не звертайте увагу на запис `<T>` и `(T t)`, вважайте, що там `Object`.

Функціональний дескріптор інтерфейсу

```java
T -> boolean
```

Наступний код

```java
Predicate negative = i -> (int)i < 0;

System.out.println(negative.test(2));
System.out.println(negative.test(-2));
System.out.println(negative.test(0));
```

Виведе в консоль

```
false
true
false
```

Розглянемо реальний приклад, коли нам може знадобитися фунцкіональний інтерфейс `Predicate`.

Представим, что нам надо реализовать метод для фильтрации массива целых чисел. Если число удовлетворяет условию, то оно попадает в итоговый массив,
а числа, не прошедшие фильтр, отсеиваются. Код такого метода может выглядеть следующим образом

Уявіть, що нам потрібно реалізувати метод фільтрації масиву цілих чисел. Якщо число задовольняє умові, то воно потрапляє до вихідного масиву, а числа, які не пройшли фільтр, відсіваються. Код цього методу може виглядати наступним чином

```java
public int[] filter(int[] input) {
    int[] result = new int[input.length];

    int counter = 0;
    for (int i : input) {
        if ( [перевірка_умови] ) {
            result[counter] = i;
            counter++;
        }
    }

    return Arrays.copyOfRange(result, 0, counter);
}
```

Особливість цього завдання полягає в тому, що нам надзвичайно бажано описати лише один метод, який міг би фільтрувати дані по-різному при тому чи іншому виклику методу.

Ми можемо реалізувати це за допомогою поліморфізму та механізму перевизначення методу, але такий код буде занадто громіздким і не забезпечить належної гнучкості коду.

Для нас було б надзвичайно доцільно передати в метод логіку, за допомогою якої необхідно фільтрувати вхідний масив. Оскільки логіка визначається блоком коду, то нам фактично потрібно передати у якості аргумента методу блок коду, який би визначав, чи задовольняє умові фільтра елемент масиву.

**Механизм функціональних інтерфейсів та лямбда-виразів дозволяє легко передавати поведінку як аргумент методу. За допомогою передачі поведінки, мі можемо писати гнучкі методи, робота яких може змінюватися в залежності від поведінки, яка передається до методу.**

Таку задачу можна реалізувати за допомогою функціонального інтерфейсу `Predicate`. Додамо посилання на об'єкт типу `Predicate` у якості вхідного аргументу функції та будемо викликати у цього об'єкту метод `test()`, передаючи йому елемент для фільтрації. Якщо метод поверне `true`, то елемент проходить крізь фільтр та додається до вихідного масиву.

```java
public int[] filter(int[] input, Predicate p) {
    int[] result = new int[input.length];

    int counter = 0;
    for (int i : input) {
        if (p.test(i)) {
            result[counter] = i;
            counter++;
        }
    }

    return Arrays.copyOfRange(result, 0, counter);
}
```

Тепер ми можемо передавати логіку порівняння за допомогою лямбда-виразу.

```java
int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

// Знаходимо парні числа
Predicate pr1 = o -> {
    int value = (int) o;
    return value % 2 == 0;
};

int[] res = filter(test, pr1);
System.out.println(Arrays.toString(res));

// Знаходимо числа, які діляться на 3 без остачі
Predicate pr2 = o -> {
    int value = (int) o;
    return value % 3 == 0;
};

res = filter(test, pr2);
System.out.println(Arrays.toString(res));
```

Додаток виведе в консоль наступне

```
[2, 4, 6, 8, 10]
[3, 6, 9]
```

### 2. Функціональний інтерфейс `Consumer`

`Consumer` - вбудований функціональний інтерфейс, доданий до Java 8 до пакету `java.util.function`. Він приймає деяке значення у якості аргументу та нічого не повертає

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
```

Функціональний дескріптор інтерфейсу

```java
T -> void
```

Інтерфейс `Consumer` використовується, якщо необхідно виконати певну дію з деяким об'єктом без повернення результату. Один з випадків використання цього інтерфейсу - вивід в поток виводу.

```java
Consumer consumer = (i) -> System.out.println(i);

void forEach(int[] input, Consumer action) {
    for (int i : input) {
        action.accept(i);
    }
}

...

int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
forEach(test, consumer);
```

### 3. Функціональний інтерфейс `Function`

`Function` - це вбудований функціональний інтерфейс, який був доданий у версії Java SE 8 до пакету `java.util.function`. Інтерфейс приймає значення одного типу у якості аргументу та повертає значення іншого типу.

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```

Функціональний дескріптор інтерфейсу

```java
T -> R
```

Даний інтерфейс часто використовується для перетворення одного значення в інше. В інтерфейсі описаний один абстрактний метод `apply()`, який приймає об'єкт одного типу та повертає об'єкт іншого (типи вхідного та вихідного об'єктів можуть бути різними або співпадати).

```java
int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

int[] result = processArray(test, function);

System.out.println(Arrays.toString(result));

...

Function function = o -> {
    int value = (int) o;
    return value * 10;
};

int[] processArray(int[] input, Function function) {
    int[] result = new int[input.length];

    for (int i = 0; i < input.length; i++)
        result[i] = (int) function.apply(input[i]);

    return result;
}
```

## Завдання на лабораторну роботу

### Завдання 1

1. Напишіть предикат, який повертає `true`, якщо число є простим.

### Завдання 2

1. Дан наступний клас `Student`. У випадку необхідності додайте потрібні гетери, сетери та конструктори.

```java
class Student {
    private String name;
    private String group;
    private int[] marks;
}
```

2. Напишіть метод фільтрації масиву студентів.
3. Перевірте роботу методу фільтрації на прикладі предикату, який відсіює студентів, які мають 1 та більше заборгованостей (оцінка менше 60 балів).

### Завдання 3

1. Напишіть метод фільтрації за двома умовами (два предикати). Елемент проходить через фільтр, якщо задовольняє двум умовам.

### Завдання 4

1. Напишіть інтерфейс `Consumer`, який приймає на вхід об'єкт типу `Student` та виводить в консоль рядок виду `ПРІЗВИЩЕ + ІМ'Я`. Створіть масив з кількох студентів та перевірте роботу функції `forEach()`

### Завдання 5

1. Напишіть метод, який приймає `Predicate` та `Consumer`. Дія в Consumer виконується тільки тоді, якщо умова в `Predicate` виконується. Створіть масив з цілих чисел, придумайте два лямбда-вирази та перевірте роботу цього методу.

### Завдання 6

1. Напишіть `Function`, який приймає на вхід ціле число n та повертає ціле число 2^n. Створіть масив з 10 цілих чисел та перевірте роботу методу.

### Завдання 7

1. Напишіть метод `stringify()`, який приймає на вхід масив цілих чисел від 0 до 9 та `Function`. Напишіть `Function`, який приймає на вхід ціле число від 0 до 9 та повертає його значення у вигляді рядку ("нуль", "один", "два", "три" і так далі). Створіть масив з 10 цілих чисел та перевірте роботу методу.