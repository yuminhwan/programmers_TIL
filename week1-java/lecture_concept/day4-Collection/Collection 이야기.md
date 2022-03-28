# Colllection 이야기

## Collection

여러 데이터의 묶음을 **Collection**이라고 한다. 

Collection 자체는 추상체이다. 또한, List, Set과 같은 구상체를 가진다.

### 종류

- List
    - LinkedList
    - ArrayList
    - Vector
    - Stack
- Set
    - HashSet

등등

### 예제  1

**MyCollection.java**

```java
public class MyCollection<T> {
    private final List<T> list;

    public MyCollection(List<T> list) {
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public MyCollection<T> filter(Predicate<T> predicate) {
        List<T> newList = new ArrayList<>();
        foreach(d -> {
            if (predicate.test(d))
                newList.add(d);
        });
        return new MyCollection<>(newList);
    }

    public <U> MyCollection<U> map(Function<T, U> function) {
        List<U> newList = new ArrayList<>();
        foreach(d -> newList.add(function.apply(d)));
        return new MyCollection<>(newList);
    }

    public void foreach(Consumer<T> consumer) {
        for (int i = 0; i < list.size(); i++) {
            // 여기서 무엇인가 해야함 ~
            T data = list.get(i);
            consumer.accept(data);
            // data 관련 로직
            // 리턴 없음 -> Consumer
        }
    }
}
```

**Main.java**

```java
public class Main {
    public static void main(String[] args) {
        MyCollection<String> c1 = new MyCollection<>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"));
        MyCollection<Integer> c2 = c1.map(String::length);
        MyCollection<Integer> c3 = c2.filter(i -> i % 2 == 0);
        c3.foreach(System.out::println);

				---------------------------->

        //메서드 체이닝
        new MyCollection<>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"))
            .map(String::length)
            .filter(i -> i % 2 == 1)
            .foreach(System.out::println);

        int s = new MyCollection<>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"))
            .map(String::length)
            .filter(i -> i % 2 == 1)
            .size();

        System.out.println(s); // 3
    }
}
```

메소드 체이닝을 통해 더욱 깔끔하게 코드를 작성할 수 있다. ( 메서드가 MyCollection을 반환하기 떼문에 )

### 예제 2

```java
public class User {
    private final int age;
    private final String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

public class Main2 {
    public static void main(String[] args) {
        new MyCollection<User>(
            Arrays.asList(
                new User(15, "AAA"),
                new User(16, "BBB"),
                new User(17, "CCC"),
                new User(18, "DDD"),
                new User(19, "EEE"),
                new User(20, "FFF"),
                new User(21, "GGG"),
                new User(22, "HHH"),
                new User(23, "III")
            )
        )
            .filter(user -> user.getAge() >= 19)
            .map(User::getName)
            .foreach(System.out::println);
    }
}
```

해당 코드는 User 객체를 가지는 Collection에서 User 필드의 age의 값이 19 이상인 객체들만 골라 User 필드의 이름으로 출력하고 있다. 

여기서 User 객체의 필드를 get 메서드를 통해 꺼내서 확인하고 있는 데 이러한 방식은 지양되는 방식이다.

그렇게 된다면 User 객체는 그저 데이터만 가진 객체가 될 뿐이니 말이다. 

몇몇의 경우 get을 통해 필드를 반환받아야 겠지만 그 경우가 아니라면 **객체에 메시지를 보내는 방식**을 쓰는 것이 좋다.

### 객체에 메시지를 보내라!

상태 데이터를 가지는 객체에서 데이터를 꺼내려(get)하지 말고 객체에 메시지를 보내보자.

```java
user.getAge() >= 19
```

 

위 코드를 

```java
public boolean isOverAndSameAge(int age) {
    return this.age >= age;
}
```

User 객체의 메서드로 리팩토링 하자는 것이다. 추가적으로 비교하는 age를 고정하지 않고 매개변수로 받아 테스트에도 용이하고 유지보수도 쉽게 바꿔줄 수 있다. 

### toString()

```java
@Override
public String toString() {
    return name + " (" + age + ")";
}
```

User 클래스에 toString을 override하여 자신이 원하는 값으로 출력해줄 수 있다.

### 결과

```java
public class User {
    private final int age;
    private final String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public boolean isOverAndSameAge(int age) {
        return this.age >= age;
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}

public class Main2 {
    public static void main(String[] args) {
        new MyCollection<User>(
            Arrays.asList(
                new User(15, "AAA"),
                new User(16, "BBB"),
                new User(17, "CCC"),
                new User(18, "DDD"),
                new User(19, "EEE"),
                new User(20, "FFF"),
                new User(21, "GGG"),
                new User(22, "HHH"),
                new User(23, "III")
            )
        )
            .filter(user -> user.isOverAndSameAge(19))
            .foreach(System.out::println);
    }
}
```

## Iterator

여러 데이터의 묶음 ( Collection )을 풀어서 하나씩 처리할 수 있는 수단이다.

### next(), hasNext()

```java
public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA");
        Iterator<String> iter = list.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
```

next()를 통해서 다음 데이터를 조회할 수 있다. 하지만 역으로 즉, 이전 데이터를 조회할 수 는 없다.

만약 다음 데이터가 없는 데 next를 호출하게 되면 Exception이 발생된다. 그래서 hasNext 메서드를 통해 다음 데이터가 있는 지를 확인한 뒤 호출하는 것이 안전하다.

```java
// MyCollection.java
public MyIterator<T> iterator() {
    return new MyIterator<T>() {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public T next() {
            return list.get(index++);
        }
    };
}

//Main.java
public class Main {
    public static void main(String[] args) {
        MyIterator<String> iter =
            new MyCollection<String>(Arrays.asList("A", "BC", "CAS", "DASD", "EAAAA"))
                .iterator();

        while (iter.hasNext()) {
            String s = iter.next();
            int len = s.length();
            if (len % 2 == 0) {
                continue;
            }

            System.out.println(s);
        }
    }
}
```

MyCollection 클래스에 MyIterator를 구현하였다. 

하지만 Iterator에서는 앞써 사용한 filter나 map 등의 고차 함수를 사용하지 못하고 하나씩 꺼내어 확인해봐야 한다.

그렇다면 filter나 map 등의 고차 함수를 사용하는 방법은 없을까? 아래에서 계속 알아보자 

## Stream

```java
public class Main {
    public static void main(String[] args) {
        Arrays.asList("A", "AB", "ABC", "ABCD", "ABCDE")
            .stream()
            .map(String::length)
            .filter(i -> i % 2 == 1)
            .forEach(System.out::println);
    }
}
```

간단히 말해 데이터의 연속이다.

우리가 자주 사용하는 `System.in`, `System.out`도 모두 스트림이다. 

Java 8 이상부터 사용이 가능하며 `Collections.stream()` 으로 제공해준다.

위에서 사용해보았던 `filter, map, forEach`와 같은 고차함수 즉, 함수를 인자로 받는 함수를 제공한다. 

Collection에서 고차 함수가 모든 데이터에 로직을 다 수행한 뒤 하나의 Collection으로 결과가 만들어졌다면 Stream에서는 Itreator처럼 데이터를 하나하나 떨어뜨려 수행하며 필요없다면 처리하지 않으며 필요한 시점까지 미루었다가 실시하는 `lazy valueration` 이 구현되어있기때문에 매우 효율적으로 동작한다.

또한, 어떤 경우에는 동적 환경에 따라 각 스트림의 동작이 멀티 스레드로 동작하거나 `parallel`하게 동작하면서 좋은 성능을 보여주기도 한다.

```java
public class Main2 {
    public static void main(String[] args) {
        Stream<Integer> s = Arrays.asList(1, 2, 3).stream(); 

        IntStream s2 = Arrays.stream(new int[] {1, 2, 3}); 
    }
}
```

List의 경우 stream()이라는 메서드가 있어 호출하게 되면 Stream을 반환하게 되고 배열의 경우 Arrays.stream()을 통해 Stream을 반환한다. 이때, 만약 primitive type이라면 각 타입을 위한 Stream이 반환되어 진다. 

### boxed()

```java
Arrays.stream(new int[] {1, 2, 3}).map(i -> Integer.valueOf(i));

-------->

Arrays.stream(new int[] {1, 2, 3}).boxed()
```

primitive type을 위한 Stream을 일반적인 Stream으로 만들어 주기 위해 `map` 을 사용할 수 있지만 `boxed()` 라는 메서드를 사용해서 반환해줄 수 있다. ( `오토 박싱` 개념을 생각하면 된다. )

### collect(), toArray()

```java
public class Main2 {
    public static void main(String[] args) {
        Arrays.stream(new int[] {1, 2, 3}).boxed().collect(Collectors.toList()); // List 타입
        
        Arrays.stream(new int[] {1, 2, 3}).boxed().toArray(); // Object[] 타입
        Arrays.stream(new int[] {1, 2, 3}).boxed().toArray(Integer[]::new); // Integer[] 타입
    }
}
```

원하는 타입으로 반환해준다. 

`toArray`의 경우 `boxed` 했을 경우 인자로 타입을 지정해주지 않으면 `Obejct[]` 타입으로 반환하게 된다.

### generate. iterate

```java
public class Main2 {
    public static void main(String[] args) {
        Random r = new Random();
        Stream.generate(r::nextInt)
            .limit(10)
            .forEach(System.out::println);  // 랜덤 값 10개 출력

        Stream.iterate(0, (i) -> i + 2) // 초기값 , 다음 값에 어떤 로직을 수행할 지
            .limit(10)
            .forEach(System.out::println); // 0 2 4 6 .... 16 18
    }
}
```

Stream을 만들때는 `Stream.generate` 와  `Stream.iterate` 으로 만들 수 있다.

`generate`는 인자로 Supplier를 전달받아 Stream을 반환한다.

`iterate` 는 인자로 초기값과 다음 값에 수행되어질 로직(*`UnaryOperator` : Function의 구현체)*을 전달받아 Stream을 반환한다.

### 예제

```java
public class Main3 {
    public static void main(String[] args) {
        //주사위를 100번 던져서 6이 나올 확률을 구하시오.
        Random r = new Random();
        long count = Stream.generate(() -> r.nextInt(6) + 1)
            .limit(100)
            .filter(n -> n == 6)
            .count();

        System.out.println(count);
    }
}
```

```java
public class Main3 {
    public static void main(String[] args) {
        // 1 ~ 9 사이 값 중에서 겹치지 않게 3개를 출력하라.
        Random r = new Random();
        int[] arr = Stream.generate(() -> r.nextInt(9) + 1)
            .distinct() // 중복 제거
            .limit(3)
            .mapToInt(i -> i)
            .toArray();

        System.out.println(Arrays.toString(arr)); // [10, 9, 6]
    }
}
```

```java
public class Main3 {
    public static void main(String[] args) {
        // 0 ~ 200 사이의 값 중에서 랜덤값 5개를 뽑아 큰 순서대로 표시하시오.
        Random r = new Random();
        int[] arr = Stream.generate(() -> r.nextInt(200) + 1)
            .limit(5)
            .sorted(Comparator.reverseOrder()) // 역순
            .mapToInt(i -> i)
            .toArray();

        System.out.println(Arrays.toString(arr));  // [91, 59, 50, 35, 5]
    }
}
```

### 결론

**스트림을 사용하면 연속된 데이터에 대해서 풍부한 고차함수들을 사용하여 강력한 기능을 간결하게 표현할 수 있다.**

## Optional

`NPE(Null Pointer Exception)`은 가장 만힝 발생하는 에러 중의 하나이다.

우리가 사용하는 자바에서는 거의 모든 것이 래퍼런스이다. 즉, 거의 모든 것이 `null`이 될 수 있다는 말이다.

그래서 우리는 항상 `null`을 확인할 필요가 있으니 `null`을 사용하지 않는 방법을 생각하게 되었다.

### null을 사용하지 않는 방법?

1. **EMPTY 객체**
    
    ```java
    public class User {
        public static User EMPTY = new User(0, "");  // EMPTY 객체 추가 
    
        public boolean isOverAndSameAge(int age) {
            if (this == EMPTY) {   // 이런식으로 활용 가능
                return false;
            }
            return this.age >= age;
        }
    }
    
    public class Main {
        public static void main(String[] args) {
            User user = User.EMPTY;
    
            User user2 = getUser();
    
            //if(user2 == null) 이 방법을 아래 방법처럼
            if (user2 == User.EMPTY)
    
                System.out.println(user);
        }
    
        private static User getUser() {
            return User.EMPTY;
        }
    }
    ```
    

1. **Optional**
    
    위의 EMPTY 객체는 아는 사람들만이 통용 가능하기 때문에 null을 체크할 수 있게 표현자체를 나타내는 것이 `Optinal`이다.
    

### Optional

객체(`null` 포함)를 포장해주는 래퍼 클래스( Wrapper class ) 이다. 

```java
public class Main2 {
    public static void main(String[] args) {
        Optional<User> optionalUser = Optional.empty();  // null
        optionalUser = Optional.of(new User(1, "2")); // 데이터
    }
}
```

null 데이터를 나타내주기 위해 `Optinal.empty()`,  *데이터가 있는 경우*  `Optional.of({DATA})`를 사용한다.

`null`임을 확인하는 방법은 `isEmpty()`, `isPresent()`, `ifPresent()`, `ifPresentOrElse()`가 있다.

### **null 값 확인**

```java
if (optionalUser.isPresent()) {
    // do 1
} else {
    // do 2
}

if (optionalUser.isEmpty()) {
    // do 2
} else {
    // do 1
}

optionalUser.ifPresent(user -> {
    // do 1
});

optionalUser.ifPresentOrElse(user -> {
    // do 1
}, () -> {
    // do2
});
```

- isEmpty()
    - 값이 없으면 즉, null이면 true
- isPresent()
    - 값이 있으면 true
- ifPresent()
- ifPresentOrElse()

### 결론

만약 반환값이 `User`라면 `null`을 체크해야하는 지 아닌지가 고민되지만 `Optional<User>`을 반환해주면 `present`를 확인해야하는 것을 명확히 알 수 있다.