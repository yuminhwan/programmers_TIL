## **객체지향 프로그래밍**

쉽게 말하자면 프로그램을 객체로 구성하는 것이다.

**프로그램이 점차 거대화해지면서 어떻게 큰 프로그램을 만들까라고 고민**한 결과 나온 해답이 프로그램의 동작을 객체들에게 나눠서 수행하는 것이다. ( 작게 나눠서 만들고 합쳐!! )

### 객체

- 객체는 작은 기능을 수행한다.
- 객체와 객체는 서로 협력한다.
    - 일을 잘게 쪼개어 객체에게 위임하고, 서로 협력하게 만든 것이다.
- 객체는 서로 구분할 필요가 있다.
    - 객체끼리는 type 즉, 형으로 구분한다.
    - `String str = “Hello World”`

### 타입 만들기 ( class 만들기 )

```java
package com.programmers;

import java.lang.*;

class MyObject extends Object implements Runnable {
    //필드영역
    private int a = 0;
    //메소드영역
    public void run() {
        a += 1;
    }
}

MyObject obj = new MyObject();
```

## **객체지향의 특성**

### 1. 캡슐화

- 완성도가 있다.
    - 기능을 수행하는 단위로써 완전함을 갖는다.
    - 이 기능을 수행하기 위해 다른 외부의 값, 객체들과 협력이 필수적으로 필요하다면 외부에 **의존**한다고 한다.
        - **의존이 없는 것이 좋고 있더라도 그 관계를 느슨하게 만들어 주는 것이 좋다.**
- 정보가 은닉되어 있다.
    - 객체의 정보가 밖에서 접근하거나, 밖에서 객체 내의 정보에 접근하지 못하게 한다.
    - 객체는 스스로 동작할 수 있는 환경을 갖고 있어야 한다. 즉, 외부에 의존하거나, 외부의 침략을 제한하여야 한다.

```java
public class Human {
    private Heart heart;
    private Blood blood;
    protected Gene gene;

    Blood donation() {
        return null;
    }  
}
```

위의 코드를 본다면 Blood는 private로 접근이 제한되어 있으니 다른 객체가 접근을 하지 못한다. 

또한, 이를 donation()이라는 메서드를 통해서만 접근할 수 있도록 하여 정보를 은닉하고 속성과 행위를 하나로 묶는 캡슐화가 된 것이다 .

위에서 볼 수 있듯이 우리는 접근지정자를 통해 캡슐화를 조절할 수 있다.

![Untitled](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pv1mlswbj20no09cq3k.jpg)

### 2. **상속**

- 상위, 부모, super, [추상]
- 하위, 자식, (this), [구체]
- 그렇다고 **공통된 기능을 여러 객체에게 전달하고 싶을 때 사용하면 안된다.**
    - 추상과 구체의 관계에서만 상속이 가능하다.
        - `원자 > 물질 > 생물 > 동물 > 포유류 > 사람 > 남자 > 짱구`

### 3. **추상화**

- 추상화된 객체 : 추상체
- 구체적인 객체 : 구상체

객체간의 관계에서 상위에 있는 것이 항상 하위보다 추상적이여 한다. 

1. **의미적 추상화** 
   
    ```java
    class Login {
        void login() {}
    }
    
    class KakaoLogin extends Login {
        void login() {}
    }
    ```
    
1. **추상 클래스**
   
    ```java
    // 추상기능을 가진 객체
    abstract class Login {
        abstract void login();
    }
    
    class KakaoLogin extends Login {
        void kakao() {}
    @Override void login() {}
    }
    ```
    
1. 인터페이스 
   
    ```java
    // 객체 자체가 추상적
    interface Login {
        void login();
    }
    
    class KakaoLogin implements Login {
        void kakao() {}
    @Override void login() {}
    }
    ```
    

### 4. **다형성**

- 형(type)을 여러가지로 표현할 수 있다.

```java
class KakaoLogin implements Login {
    void kakao() {}
@Override void login() {}
}

interface Portal {
    void portal();
}

class NaverLogin implements Login, Portal {
    void naver() {}
@Override void login() {}
@Override void portal() {}
}

KakaoLogin k = new KakaoLogin();
Login k = new KakaoLogin();
```

위의 코드처럼 `KakaoLogin`는 `Login` 타입으로 표현할 수 있다.

```java
Login login = new Login();
Login login = new KakaoLogin();
Login login = new NaverLogin();
login.login();

Portal portal = new NaverLogin();
potal.portal();
```

- 누가 접근하냐에 따라 필터링된(접근한 객체가 권한을 가진) 데이터만 접근할 수 있다.
    - 만약 Login이 NaverLogin에 접근한다면 login메서드만 접근 가능하다.
    - 만약 Portal이 NaverLogin에 접근한다면 portal메서드만 접근 가능하다.
    - 이렇게 해야지 캡슐화가 깨지지 않는다. ( 외부에서 함부로 접근하지 못한다. )

## **객체지향 설계**

### **UML**

객체지향 프로그래밍은 기능을 객체에게 나눠서 수행시킨다.

즉, 객체를 어떻게 구분했고 객체간의 어떤 연관관계를 가지고 있는 지 설명할 수 있어야 한다.

그래서 설명하기 위한 도구 UML이 있다. 

**종류**

- Usecase Diagram
- Sequence Diagram
- Package Diagram
- **Class Diagram ( 가장 많이 씀 )**

![Untitled](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pv1olgz4j20e808udgq.jpg)

출처)[https://m.blog.naver.com/sule47/220857776925](https://m.blog.naver.com/sule47/220857776925)

![Untitled](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pv1q405bj20iz0d5751.jpg)

출처)[https://www.nextree.co.kr/p6753/](https://www.nextree.co.kr/p6753/)

- 이름, 필드 , 클래스로 객체를 표현한다.
- 관계
    - **Generalization (일반화)**
        - 상속 (extends)
        - 화살표 끝에 있는 객체가 추상체  ( 부모 )
        - 화살표 시작에 있는 객체가 구상체 ( 자식 )
    - Realiztion (실제화)
        - 구현 (implements)
    - Dependency ( 의존 )
        - A(화살표 시작)가 동작할 때는 B(화살표 끝)가 있어야 한다.
    - 연관
        - 무슨 연관인지 모르겠다 있긴 하다
    - 직접 연관
        - 1번(화살표 시작)이 2번(화살표 끝)을 사용한다
        - 1번이 2번에 직접적인 연관이 있다.
    - 집합, 집합연관
        - 1번(사각형)이 2번을 소유하는 경우
        - 2번이 없더라도 1번 스스로 존재하는 데 지장이 없을 경우
    - 합성, 복합연관
        - 1번(사각형)이 2번을 소유하는 경우
        - 하지만 2번이 없더라면 1번 스스로 존재할 수 없는 경우
        - 1번에게 2번이 필수 요소일 경우
        
         
        

## **어떻게 하면 객체를 잘 나누고 연관지을 수 있느냐?**

객체지향 설계를 하는 5가지 원칙이 있다. 일명 **SOLID** 원칙

1. 단일 책임 원칙 (Single Responsibility Principle : SRP ) 
    - 클래스는 단 한 개의 책임을 가져야 한다.
    - 클래스 설계를 먼저 하기보단, 어떤 기능(메서드)가 필요한지 생각해본 후 그 메서드들을 누가 실행할지 생각하자.
2. 개방 - 폐쇠 원칙 ( Open-Closed Principle : OCP ) 
    - 기능을 변경하거나 확장할 수 있으면서, 그 기능을 사용하는 코드는 수정하지 않는다.
3. 리스코프 치환 원칙 (Liskov Substitution Principle : LSP ) 
    - 상위 타입의 객체를 하위 타입의 객체로 치환해도 상위 타입을 사용하는 프로그램은 정상적으로 작동해야 한다.
        - 명확한 추상과 구상 관계라면 자연스럽게 지켜진다.
        - 하위 타입이 상위 타입의 명세에서 벗어난 기능을 수행하는 지 잘 체크
4. 인터페이스 분리 원칙 (Interface Segregation Principle : ISP) 
    - 인터페이스는 그 인터페이스를 사용하는 클라이언트를 기준으로 분리해야 한다.
        - 자신이 사용하는 메서드에만 의존할 수 있도록 코드를 설계하자.
5. 의존 역전 원칙 (Dependency Inversion Principle : DIP) 
    - 고수준 모듈은 저수준 모듈의 구현에 의존해서는 안된다.
    - 저수준 모듈이 고수준 모듈에서 정의한 추상 타입에 의존해야 한다.

해당 원칙을 따라하다보니 공통점이 보였고 그것을 모은 것이 **디자인 패턴**이다.

23가지 디자인 패턴이 있다.

- [https://refactoring.guru/](https://refactoring.guru/)