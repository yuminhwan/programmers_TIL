## Interface

> 모든 메서드가 추상메서드로 구성된 클래스
> 

선언만 하며 구현부는 없는 클래스 

구현의 경우 자식 클래스에서 오버라이드 해서 사용

## Interface의 기능

### 1. 구현을 강제

```java
// Runnable.java 간단히 
public interface Runnable {
	public void run();
}

// Main.java
public class Main implements Runnable {
    public static void main(String[] args) {
        Main m = new Main();
        // Runnable m = new Main();
        m.run();
    }

    @Override
    public void run() {
        System.out.println("Hello World");
    }
}
```

위의 코드는 `Runnable` 인터페이스를 implements 하고 있으니 run()메서드의 구현을 강제하고 있다.

추가적으로 Main은 Runnable을 상속하고 있으니 `Runnable m = new Main()`과 같이도 사용할 수 있다.

### 2. 다형성을 제공

```java
interface MyRunnable {
    void myRun();
}

interface YourRunnable {
    void yourRun();
}

public class Main implements MyRunnable, YourRunnable {
    public static void main(String[] args) {
        Main m = new Main();
        m.myRun();
        m.yourRun();
    }

    @Override
    public void myRun() {
        System.out.println("myRun");
    }

    @Override
    public void yourRun() {
        System.out.println("yourRun");
    }
}
```

`Main m  = new Main()`와 같이 선언하게 되면 myRun, yourRun 메서드를 모두를 사용할 수 있다.

하지만 참조변수 `m`의 타입을 다음과 바꾸면 어떻게 될까?

```java
MyRunnable m = new Main();
m.myRun();
m.yourRun();  // 불가능!

YourRunnable m = new Main();
m.myRun();  // 불가능! 
m.yourRun();  
```

`MyRunnable`은 선언되어 있는 메서드가 myRun밖에 없으니 myRun만 실행할 수 있고 `YourRunnable`에 선언되어 있는 메서드가 yourRun밖에 없으니 yourRun만 실행할 수 있다. 

이처럼 타입을 통해 호출할 수 있는 메서드를 제한할 수 있다.

그렇다면 이를 어떻게 활용할까? 아래 예제를 보자 

```java
 public interface Login {
    void login();
}

public class KakaoLogin implements Login {
    @Override
    public void login() {
        System.out.println("카카오 로그인");
    }
}

public class NaverLogin implements Login {
    @Override
    public void login() {
        System.out.println("네이버 로그인");
    }
}

public class Main {
    public static void main(String[] args) {
        // 로그인 해야지 ~

        // 카카오로 로그인 한다면?
        KakaoLogin user1 = new KakaoLogin();
        user1.login();

        // 네이버로 로그인 한다면?
        NaverLogin user2 = new NaverLogin();
        user2.login();
    }
}
```

해당 코드에서는 유저가 로그인을 할 때 카카오, 네이버에 따라 인스턴스를 달리하여 login 메서드를 호출해야한다. 즉, 특정 메서드가 하나의 기능에 강하게 결합되어 있다는 것이다. 하지만 만약 구글, 야후 등 다른 로그인을 하고 싶다면 앞서 했던 것처럼 인스턴스를 또 만들어서 호출해야 할 것이다. 그렇게 된다면 점점 코드는 커지고 유지보수가 힘들어 질 것이다. 

그러면 **상황에 따라 유연하게 하는 방법은 없을까?**  

```java
public class Main {
    public static void main(String[] args) {
        // 설정파일, config
        new Main().run(LoginType.KAKAO); // 호스트 코드
    }

    private void run(LoginType type) {
        Login user = getLogin(type);
        user.login();
    }

    // factory 패턴 
    private Login getLogin(LoginType type) {
        if (type == LoginType.KAKAO) {
            return new KakaoLogin();
        }

        return new NaverLogin();
    }
}
```

먼저 user의 타입을 `Login`으로 바꾸어 주고 **user가 참조할 인스턴스를 결정하는 부분을 메서드로 분리( factory 패턴 )** 해준다. 

또한, 로그인을 해야할 타입을 LoginType이라는 **enum**으로 모아서 구분하게 된다면 유연하게 로그인을 할 수 있게 될 것이다.

구조를 본다면 run, getLogin 메서드에서 어떤 인스턴스를 반환할 지 결정하지 않는다. 결정은 실행하는 호스트 코드에서 하는 것이다. 만약 설정파일을 읽어서 네이버로 로그인해야 한다면 NAVER를 넣어주고 카카오로 로그인해야 한다면 KAKAO를 넣어주기만 하면 되는 것이다.

### 3. 결합도를 낮추는 효과 (의존성을 역전)

```java
public class UserService implements Login {
    private Login login;

    public UserService(Login login) {
        this.login = login;
    }

    @Override
    public void login() {
        login.login();
    }
}

public class Main {
    public static void main(String[] args) {
        // 카카오로?
        UserService userService = new UserService(new KakaoLogin());

        // 네이버로?
        // UserService userService = new UserService(new NaverLogin());
    }
}
```

위의 예제에 이어서 `Login`을 상속받는 `UserService`를 만들었다.

해당 코드는 로그인의 기능을 가진 구현체가 들어오면 그 구현체를 통해 로그인을 진행한다. 해당 구현체가 카카오든 네이버든 그외의 다른 로그인이 들어오더라도 모두 `Login`이라는 타입으로 받게 되니 어떤 것이든 받을 수 있고 로그인 할 수 있다.

즉, **`UserService`는 `Login`에 의존하고 있고 해당 의존체를 자신이 생성하는 것이 아닌 외부에서 결정하도록 맡기고 있다. ( 의존성을 외부에 맡기는 것을 의존도를 낮춘다고 표현한다. )**

만약 자신이 생성한다면 어떻게 될까? 

```java
private Login login = new KakaoLogin; 
```

`Userservice`는 카카오만 로그인할 수 있게 되어 결합성이 강하게 결합되 유연하지 못하게 된다.

하지만 자신이 생성하지 않고 외부에 맡겨 추상체와 결합하게 된다면 결합도가 낮아져 유연하게 되는 것이다.

이처럼  의존성을 외부로 부터 전달 받았다는 것을 의존성을 주입받았다 즉, **의존성 주입 ( Dependency Injection )**이라고 말한다.

![ Gg.jpg](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pv3gwe59j20u00yv0ut.jpg)

첫 번째 그림은 의존 방향이 한 쪽으로 쭉 이어지지만 두 번째 그림의 경우 의존 방향이 역전되는 것을 볼 수 있다. 

이를 **Dependency Inversion** 이라고 하며 SOLID 원칙 중 **DIP**에 해당한다. 

의존을 하게 된다면 구상체에 의존하지말고 추상체를 통해 의존하는 것이 더 유연한 프로그램이 될 것이다. 

## default Method

Java 8 이상부터 기능 개선이 있었다. 인터페이스가 **구현체**를 가질 수 있게 된 것이다. 

우리는 앞서 interface는 추상메서드로만 이뤄진 클래스라고 했다. 하지만 Java 8부터는 그저 메서드만 선언하는 것이 아닌 구현부분을 가질 수 있게 된 것이다.  즉, 무조건적으로 override해서 사용하지 않아도 된다는 것이다. ( 물론, override해서 사용할 수 있다.)

이를 **default Method** 라고 한다.

```java
interface MyInterface {
    void method1(); // 구현 X : 추상 메서드

    default void sayHello() {   // 구현 O
        System.out.println("Hello World");
    }
}

public class Main implements MyInterface {
    public static void main(String[] args) {
        new Main().sayHello();
    }

    @Override
    public void method1() {
        throw new RuntimeException();
    }
}
```

### 1. Adapter 역할

```java
public interface MyInterface {
    void method1();

    void method2();
}

public class Main {
    public static void main(String[] args) {
        new Service().method1();
    }
}

class Service implements MyInterface {

    @Override
    public void method1() {
        System.out.println("Hello World");
    }

    @Override
    public void method2() {
        // nothing
    }
}
```

`method1` 메서드만 사용하지만 필요없는 `method2` 메서드도 `override` 해서 사용해야 한다.

이를 피하기 위해서 **Adapter**를 사용해야 한다.

```java
public class MyInterfaceAdapter implements MyInterface {
    @Override
    public void method1() {
    }

    @Override
    public void method2() {
    }
}

class Service extends MyInterfaceAdapter {
    @Override
    public void method1() {
        System.out.println("Hello World");
    }
}
```

`MyInterfaceAdapter`에 `MyInterface`를 구현하고 `Service`에는 `MyInterfaceAdapter`를 상속하는 방식이 Apdater이다.

Apdater를 통해 `Interface`내의 수많은 메서드들 중 사용해야 할 메서드 몇 개만 `override`하여 사용할 수 있게 된다. 

 

하지만 Java의 경우 상속을 하나만 받을 수 있기에 다른 클래스를 상속받고 있다면 Apdater를 사용하지 못하는 문제점이 발생하게 된다. 그렇게 된다면 결국 첫번째와 마찬가지로 `MyInterface`를 구현하여 사용해야 하니 코드가 깔끔해질 수 없다.

```java
class Service extends SuperClass implements MyInterface {
    @Override
    public void method1() {
        System.out.println("Hello World");
    }

    @Override
    public void method2() {
        // nothing
    }
} 
```

위의 문제를 우리는 default 메서드를 통해 해결할 수 있다.

```java
public interface MyInterface {
    default void method1() {
    }

    default void method2() {
    }
}

class Service extends SuperClass implements MyInterface {
    @Override
    public void method1() {
        System.out.println("Hello World");
    }
}
```

default 메서드 덕분에 훨씬 깔끔한 코드를 만들 수 있게 되었다.

### 2. 인터페이스 추가만으로 기능 확장

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

interface Walkable {
    void walk();
}

class Duck implements Swimmable, Walkable {

    @Override
    public void swim() {
        System.out.println("swim");
    }

    @Override
    public void walk() {
        System.out.println("walk");
    }
}

class Swan implements Flyable, Walkable {

    @Override
    public void fly() {
        System.out.println("fly");
    }

    @Override
    public void walk() {
        System.out.println("walk");
    }
}

public class Main {
    public static void main(String[] args) {
        new Duck().swim();
        new Duck().walk();
        new Swan().fly();
    }
}
```

`Duck`과 `Swan` 클래스에서 메서드들이 중복된다는 것을 확인할 수 있다. 이를 default 메서드를 통해 더욱 깔끔하게 바꿀 수 있다. 

```java
interface Flyable {
    default void fly() {
        System.out.println("fly");
    }
}

interface Swimmable {
    default void swim() {
        System.out.println("swim");
    }
}

interface Walkable {
    default void walk() {
        System.out.println("walk");
    }
}

class Duck implements Swimmable, Walkable {
}

class Swan implements Flyable, Walkable {
}

public class Main {
    public static void main(String[] args) {
        new Duck().swim();
        new Duck().walk();
        new Swan().fly();
    }
}
```

 default 메서드 덕분에 `Duck`과 `Swan`쪽에서 구현을 하지 않더라도 손 쉽게 사용할 수 있다. 

```java
class Swan implements Flyable, Walkable, Swimmable {
}

public class Main {
    public static void main(String[] args) {
        new Duck().swim();
        new Duck().walk();
        new Swan().fly();
        new Swan().swim();
    }
}
```

또한, `Swan`에 수영 기능을 추가하고 싶다면 그저 Swimmable을 넣어주기만 하면 된다.

## static 메서드

```java
public interface Ability {
		// 함수 하나 
    static void sayHello() {
        System.out.println("Hello");
    }
}

public class Main {
    public static void main(String[] args) {
        Ability.sayHello();
    }
}
```

인터페이스에 static 메서드가 사용가능해짐에 따라 인터페이스는 함수 제공자가 될 수 있다.

## Functional Interface

```java
@FunctionalInterface
public interface MyRunnable {
    void run();
}

@FunctionalInterface
interface Mymap {
    static void sayBye() {
        System.out.println("Bye");
    }

    void map();

    default void sayHello() {
        System.out.println("Hello");
    }
}

// 불가능
@FunctionalInterface
interface MyRunnable2 {
    void run1();

    void run2();
}
```

추상메서드가 하나만 존재하는 인터페이스이다. default 나 static 메소드가 있어도 추상메서드가 하나면 함수형 인터페이스이다.

함수형 인터페이스임을 나타내기 위해 `@FunctionalInterface` 어노테이션을 붙여준다.

`FunctionalInterface`에 있는 추상메소드를 함수라고 부른다.

## 익명 클래스 (인터페이스 임시 생성하기 )

> 익명클래스를 사용해서 인터페이스의 인스턴스를 생성하고 구현을 바로 정의한다.
> 

```java
@FunctionalInterface
public interface MyRunnable {
    void run();
}

class SayHello implements MyRunnable {
    @Override
    public void run() {
        System.out.println("Hello World");
    }
}

public class Main {
    public static void main(String[] args) {
        new SayHello().run();
    }
}
```

인터페이스는 객체를 생성할 수 없으니 클래스에 구현하여 해당 클래스를 통해 사용할 수 밖에 없다.

위의 예제에서는 `MyRunnable`의 `run`을 사용하려면  `SayHello` 클래스에 구현하여 `SayHello`를 통해 사용할 수 있다는 것이다. 

이를 해결할 수 있는 방법이 **익명 클래스**이다.

```java
new class XXX implements MySupply {
    @Override
    public String supply() {
        return "Hello World";
    }
}.supply();

------>

//이름없는 클래스를 생성한다.
new MySupply() {
    @Override
    public String supply() {
        return "Hello World";
    }
}.supply();

```

`MySupply`를 구현할 수 있는 클래스를 만들어 `supply`메서드를 사용할 수 있으며 해당 클래스를 **익명 클래스**라고 한다.

```java
@FunctionalInterface
public interface MySupply {
    String supply();
}

@FunctionalInterface
public interface MyRunnable {
    void run();
}

public class Main {
    public static void main(String[] args) {
        MyRunnable r = new MyRunnable() {
            @Override
            public void run() {
                MySupply s = new MySupply() {
                    @Override
                    public String supply() {
                        return "Hello Hello";
                    }
                };
                System.out.println(s.supply());
            }
        };
        r.run();  // Hello Hello 
    }
}
```

## Lambda 표현식

> 익명 메소드를 사용해서 간결한 인터페이스 인스턴스 생성 방법
> 

```java
public class Main2 {
    public static void main(String[] args) {
        MyRunnable r1 = new MyRunnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };
        
        // ------------>

        // 익명 메서드를 사용해서 표현하는 방법 : 람다 표현식
        MyRunnable r2 = () -> System.out.println("Hello");

        r1.run(); // Hello
        r2.run(); // Hello

        MySupply s1 = () -> "Hello World";

        MyRunnable r3 = () -> {
            MySupply s2 = () -> "Hello Hello Hello";
            System.out.println(s2.supply());
        };
        r3.run(); // Hello Hello Hello

    }
}
```

람다 표현식은 간결하게 표현이 가능하도록 만들어주며 `FunctionalInterface` 에서만 가능하다.

만약 `FunctionalInterface`가 아니라면 익명 메서드로 어떤 메서드를 사용할 지 모르니 불가능하다는 것이다.

```java
@FunctionalInterface
public interface MySupplier {
    String supply();
}

@FunctionalInterface
public interface MyMapper {
    int map(String s);
}

@FunctionalInterface
public interface MyConsumer {
    void consume(int i);
}

@FunctionalInterface
public interface MyRunnable {
    void run();
}

public class Main {
    public static void main(String[] args) {
        MySupplier s = () -> "Hello World";

        // MyMapper m = (str) -> str.length();
        MyMapper m = String::length;

        // MyConsumer c = i -> System.out.println(i);
        MyConsumer c = System.out::println;

        MyRunnable r = () -> c.consume(m.map(s.supply()));

        r.run(); // 11
    }
}
```

### 메소드 래퍼런스

```java
MyMapper m = (str) -> str.length();   
--->
MyMapper m = String::length;

MyConsumer c = i -> System.out.println(i);
--->
MyConsumer c = System.out::println;
```

람다 표현식에서 **입력되는 값을 변경없이 바로 사용하는 경우** 최종으로 적용될 메소드이 레퍼런스를 지정해 주는 표현 방식이다. 만약 입력되는 값이 변경이 된다면 사용할 수 없으니 **입력값을 변경하지 말라는 표현방식**이기도 하다.

즉, 개발자의 개입을 차단함으로써 안정성을 얻을 수 있다.

### 제너릭

```java
@FunctionalInterface
public interface MySupplier<T> {
    T supply();
}

@FunctionalInterface
public interface MyMapper<T, V> {
    V map(T s);
}

@FunctionalInterface
public interface MyConsumer<T> {
    void consume(T i);
}

public class Main {
    public static void main(String[] args) {
        MySupplier<String> s = () -> "Hello World";

        MyMapper<String, Integer> m = String::length;
        MyMapper<Integer, Integer> m2 = i -> i * i;
        MyMapper<Integer, String> m3 = Integer::toHexString;

        MyConsumer<String> c = System.out::println;

        MyRunnable r = () -> c.consume(
            m3.map(
                m2.map(
                    m.map(
                        s.supply()
                    )
                ) 
            )
        );

        r.run();
    }
}
```

제너릭을 통해 더욱 확장하여 사용할 수 있다. 

```java
public class Main {
    public static void main(String[] args) {
        new Main().loop(10, System.out::println);  // 호스트 코드

        new Main().filterNumbers(30,
            i -> i % 2 == 0,
            System.out::println);
    }

    void filterNumbers(int max, Predicate<Integer> p, Consumer<Integer> c) {
        for (int i = 0; i < max; i++) {
            if (p.test(i)) {
                c.accept(i);
            }
        }
    }

    void loop(int n, MyConsumer<Integer> consumer) {
        for (int i = 0; i < n; i++) {
            // 뭔가를 해라  ->  i를 주고 뭔가 해라
            // 입력은 있고, 출력은 따로 없어도 된다.
            // Consumer
            consumer.consume(i);
        }
    }
}
```

`filterNumbers`, `loop` 메서드의 경우 그저 반복만 하고 무엇을 할지는 호스트코드에서 정한다.

- java.util.function
- [https://docs.oracle.com/javase/8/docs/api/](https://docs.oracle.com/javase/8/docs/api/)