## Spring Frameworks을 활용한 데이터 레이어(RDB) 접근 방법

### JDBC 

자바 어플레키에션은 JDBC API를 이용하여 데이터계층과 통신을 한다.

```java
@Slf4j
@SpringBootTest
public class JDBCTest {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASS = "";

    static final String DROP_TABLE_SQL = "DROP TABLE customer IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customer(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";
    static final String INSERT_SQL = "INSERT INTO customer(id, first_name, last_name) VALUES(1, 'minhwan','yu')";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @DisplayName("JDBC 테스트")
    @Test
    void jdbcTest() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            log.info("GET CONNECTION");

            Statement statement = connection.createStatement();
            statement.executeUpdate(DROP_TABLE_SQL);
            statement.executeUpdate(CREATE_TABLE_SQL);
            log.info("CREATED TABLE");

            statement.executeUpdate(INSERT_SQL);
            log.info("INSERTED CUSTOMER INFORMATION");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER WHERE id = 1");

            while (resultSet.next()) {
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                log.info("CUSTOMER FULL NAME : {}", fullName);
                assertThat(fullName).isEqualTo("minhwan yu");
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

- Connection 획득 
- Statement 를 이용한 질의 
- ResultSet 을 이용한 질의결과 사용 
- Statement , Connection 반납 

과 같은 과정을 거치게 된다. 

현재 코드를 보면 매우 복잡하고 Exception 처리도 복잡하다.

이런 과정을 스프링은 JdbcTemplate을 통해 해결한다. 



#### 참고 : Try - With - Resources 

Connection과 Statement 둘다 AutoCloseable을 구현하고 있어 try-with-resources 사용이 가능하다.

```java
@DisplayName("JDBC 테스트 : Try_With_Resources 사용(Java 7)")
@Test
void jdbcTest_Try_With_Resources_Seven() {
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement statement = connection.createStatement()) {

        log.info("GET CONNECTION");

        statement.executeUpdate(DROP_TABLE_SQL);
        statement.executeUpdate(CREATE_TABLE_SQL);
        log.info("CREATED TABLE");

        statement.executeUpdate(INSERT_SQL);
        log.info("INSERTED CUSTOMER INFORMATION");

        ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER WHERE id = 1");

        while (resultSet.next()) {
            String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
            log.info("CUSTOMER FULL NAME : {}", fullName);
            assertThat(fullName).isEqualTo("minhwan yu");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

@DisplayName("JDBC 테스트 : Try_With_Resources 사용(Java 9)")
@Test
void jdbcTest_Try_With_Resources_Nine() throws SQLException {
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    Statement statement = connection.createStatement();

    try (connection; statement) {

        log.info("GET CONNECTION");

        statement.executeUpdate(DROP_TABLE_SQL);
        statement.executeUpdate(CREATE_TABLE_SQL);
        log.info("CREATED TABLE");

        statement.executeUpdate(INSERT_SQL);
        log.info("INSERTED CUSTOMER INFORMATION");

        ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER WHERE id = 1");

        while (resultSet.next()) {
            String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
            log.info("CUSTOMER FULL NAME : {}", fullName);
            assertThat(fullName).isEqualTo("minhwan yu");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

자바 9부터는 try문 밖에서 생성하고 정의할 수 있다.



### JDBC Tempalte 

JDBC Template를 이용해서, 데이터 계층에 접근이 가능하다.

기존 JDBC를 이용했을때의 반복적인 작업을 JDBC Template이 대신 수행해준다.

```java
@DisplayName("JdbcTemplate 테스트")
@Test
void jdbcTemplateTest() {
    jdbcTemplate.update(DROP_TABLE_SQL);
    jdbcTemplate.update(CREATE_TABLE_SQL);
    log.info("CREATED TABLE USING JDBC TEMPLATE");

    jdbcTemplate.update(INSERT_SQL);
    log.info("INSERTED CUSTOMER INFORMATION USING JDBC TEMPLATE");

    String fullName = jdbcTemplate.queryForObject(
        "SELECT * FROM customer WHERE id = 1",
        (resultSet, i) -> resultSet.getString("first_name") + " " + resultSet.getString("last_name")
    );
    log.info("FULL_NAME : {}", fullName);
    assertThat(fullName).isEqualTo("minhwan yu");
}
```

yaml 파일에 DB설정정보를 입력하면 AutoConfiguration에 의해 자동으로 빈을 생성해준다. (Conntection, Statement 생성, 반납 등 )

확실히 코드량도 적어지고 간단해졌지만 자바 코드상에 SQL구문은 피할 수 없다. 코드와 쿼리가 뒤섞이며 유지보수가 힘들어진다. 

이를 해결하고자 QueryMapper가 나오게 되었다. 



### Mybatis(QueryMapper)

JDBC 의 반복적인 작업을 쿼리매퍼인 Mybatis가 대신 수행해준다.

자바 코드와 쿼리를 분리하여 쿼리 수정으로 자바 코드 수정 이나 컴파일 하는 작업을 하지 않아도 된다.

어노테이션 방식과 XML 방식이 있다. 

```yaml
mybatis:
  type-aliases-package: com.prgrms.springbootjpa.repository.domain
  configuration:
    map-underscore-to-camel-case: true
    default-fetch-size: 100
    default-statement-timeout: 30
  mapper-locations: classpath:mapper/*.xml
```

- `type-aliases-package` : 객체에다 resultSet 결과를 매핑

- `map-underscore-to-camel-case` : 테이블의 컬럼이름의 언더바 형식을 카멜 케이스 형태로 매핑

- `default-fetch-size` : 결과를 최대 100개 가져오겠다.

- `default-statement-timeout` : statement이 통신할 때 timeout 설정

- `mapper-locations` : xml 파일 위치



```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.prgrms.springbootjpa.repository.CustomerMapper">
    <insert id="save">
        INSERT INTO customer (id, first_name, last_name)
        VALUES (#{id}, #{firstName}, #{lastName})
    </insert>

    <update id="update">
        UPDATE customer
        SET first_name=#{firstName},
            last_name=#{lastName}
        WHERE id = #{id}
    </update>

    <select id="findById" resultType="customer">
        SELECT *
        FROM customer
        WHERE id = #{id}
    </select>

    <select id="findAll" resultType="customer">
        SELECT *
        FROM customer
    </select>
</mapper>
```

XML파일에서 id로 지정한 것이 mapper 인터페이스의 메서드와 연결되게 된다.

```java
@Mapper
public interface CustomerMapper {
    void save(Customer customer);

    Customer findById(long id);
}
```



```java
// @Alias("customers")
public class Customer {
    private long id;
    private String firstName;
    private String lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getter, setter ..
}
```

`@Alias` : mybatis에서 지원하는 어노테이션으로 TypeAlias, 즉 별칭을 지정할 때 사용한다.

기본적으로 getter, setter 로 동작한다.



```java
@Slf4j
@SpringBootTest
public class MybatisTest {

    static final String DROP_TABLE_SQL = "DROP TABLE customer IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customer(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CustomerMapper customerMapper;

    @DisplayName("고객을 저장한다.")
    @Test
    void save() {
        jdbcTemplate.update(DROP_TABLE_SQL);
        jdbcTemplate.update(CREATE_TABLE_SQL);

        customerMapper.save(new Customer(1L, "minhwan", "yu"));
        Customer customer = customerMapper.findById(1L);

        log.info("fullName : {} {}", customer.getFirstName(), customer.getLastName());
        assertThat(customer.getFirstName()).isEqualTo("minhwan");
        assertThat(customer.getLastName()).isEqualTo("yu");
    }
}
```

자바 코드와 쿼리를 분리함으로써 유지보수가 쉬워진다. 

하지만 쿼리 매퍼와 JdbcTemplate의 한계인 RDB와 자바 객체의 패러다임 불일치가 생기게 된다.

이를 JPA로 극복 가능하다.



### JPA 

#### **JPA를 사용해야하는 이유**

- 생산성 증진
	- SQL에 의존적인 개발에서 탈피하여, 객체중심으로 생산적인 개발이 가능하다.
- 객체와 관계형테이블의 패러다임 불일치
	- 객체지향 프로그래밍은 추상화, 캡슐화, 상속, 다형성 등을 제공한다.
	- 관계형 데이터베이스 데이터 중심으로 구조화 되어있으며, OOP의 특징을 지원하지 않는다.



### 설정

```yaml
jpa:
    generate-ddl: true
    open-in-view: false
    show-sql: true
```

Spring은 EntityScan을 통해 @Entity 애노테이션이 명시한 클래스를 찾는다. 이때, `spring.jpa.generate-ddl=true` 옵션을 `true`로 설정되어 있다면 해당 데이터를 근거로 서버 시작 시점에 DDL문을 생성하여 DB에 적용한다.



### @Entity

```java
@Entity
@Table(name = "customer")
public class CustomerEntity {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private int age;

    // getter, setter...
}
```

- `@Entity` :  객체가 RBD 테이블과 매핑됨을 알려줌

- `@Table` :  어떤 테이블과 매핑될 지 명시,
	-  명시 하지 않는다면 클래스명을 따라감

`@Id` : PK값 명시

JPA는 기본생성자와 리플랙션을 통해 동작되어 진다.



#### JPA에서 기본 생성자가 필요한 이유

> Java Reflection API를 사용하면 해당 클래스의 매서드, 맴버 변수, 변수 타입 등을 알 수 있다.
>
> 하지만 생성자의 인자 정보들은 가져올 수 없기 때문에 기본 생성자가 없다면 Java Reflection은 해당 객체를 생성할 수 없게 된다.
>
> 추가적으로 하이버네이트 같은 구현체들은 조금 더 유연하게 바이트코드를 조작하는 라이브러리 등을 통하여 기본생성자가 없더라도 동작한다.
>
> 그렇더라도 JPA 스펙에서 가이드한 것 처럼 기본생성자를 꼭 사용하는 것이 좋다!
>
> 참고자료 : https://www.inflearn.com/questions/105043



### JpaRepostiory

```java
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
```

JPA는 Entity와 매핑될 Repostiory 인터페이스가 필요하다. 

Entity 객체와 id로 설정한 객체의 타입을 준다. 

CustomerEntity라는 객체가 RDB에 customer 테이블과 매핑이 되고 그것을 repository 레이어에 접근할 수 있는 계층이 만들어 진 것이다.



```java
@Slf4j
@SpringBootTest
public class JPATest {

    @Autowired
    CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @DisplayName("고객을 저장한다.")
    @Test
    void insert() {
        // given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("minhwan");
        customer.setLastName("yu");

        // when
        customerRepository.save(customer);

        // then
        CustomerEntity entity = customerRepository.findById(1L).get();
        log.info("{} {}", entity.getFirstName(), entity.getLastName());

        assertThat(entity.getId()).isEqualTo(customer.getId());
    }

    @DisplayName("고객정보를 수정한다.")
    @Test
    @Transactional
    void update() {
        // given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("minhwan");
        customer.setLastName("yu");
        customerRepository.save(customer);

        // when
        CustomerEntity entity = customerRepository.findById(1L).get();
        entity.setFirstName("hwani");
        entity.setLastName("you");

        // then
        CustomerEntity updated = customerRepository.findById(1L).get();
        log.info("{} {}", entity.getFirstName(), entity.getLastName());
        assertThat(updated.getFirstName()).isEqualTo("hwani");
        assertThat(updated.getLastName()).isEqualTo("you");
    }

}
```

영속성 컨텍스트에서 관리되어 진다. → `@Transactional`

따로 저장하지 않아도 더티 체킹이 진행중이여서 만약 엔티티에 변경이 일어나고 트랜잭션에 의해 커밋이 되게 되면 변경이 일어났구나 라며 자동적으로 업데이트 쿼리가 진행된다.

자바와 객체와 RDB 테이블이 매핑됨으로써 객체가 변경시 RDB 테이블도 변경된다. 테이블을 마치 자바의 객체처럼 다룰 수 있다. 

만약 요구사항에 나이정보가 추가되었더라도 단순히 Entity 객체에 필드만 추가만 해주면 된다! 컬럼 추가, 변경을 손쉽게 할 수 있다. 

Mybatis의 경우 쿼리문 수정을 피할 수 없다.

JPA는 테이블이 클래스로 관리되어 지기 때문에 동일성을 유지할 수 있다.



## JPA 프로젝트 설정

Jpa는 하나의 인터페이스이다. 여러 구현체가 있는 데 그 중 hibernate를 사용한다. (압도적으로 많이 사용) 

```yaml
spring:
  jpa:
    generate-ddl: true
    hibernate:
      ddl-auto: create-drop
    database: h2
    show-sql: true
    open-in-view: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
        query:
          in_clause_parameter_padding: true
```

- `ddl-auto`
	- **none** : 아무것도 실행하지 않는다 (대부분의 DB에서 기본값이다)
	- **create-drop** : SessionFactory가 시작될 때 drop및 생성을 실행하고, SessionFactory가 종료될 때 drop을 실행한다 (in-memory DB의 경우 기본값이다)
	- **create** : SessionFactory가 시작될 때 데이터베이스 drop을 실행하고 생성된 DDL을 실행한다
	- **update** : 변경된 스키마를 적용한다
	- **validate** : 변경된 스키마가 있다면 변경점을 출력하고 애플리케이션을 종료한다
	- **enable_lazy_load_no_trans** : transaction이 아닌 환경에서도 lazy load가 가능하게 설정한다.



- `dialect` : 데이터베이스의 방언
	- JPA는 특정 데이터베이스에 종속되지 않는다. 따라서, 애플리케이션의 구성에 맞게 데이터베이스를 설정할 수 있다. 
	- 하지만 각각의 데이터베이스가 제공하는 SQL문법과 함수는 조금씩 다르다. 
	- 즉, 방언은 SQL표준을 지키지 않는 특정 데이터베이스만의 고유한 기능이다.
	- 하이버네이트는 40가지 이상의 데이터베이스 방언을 지원한다.



- `in_clause_parameter_padding`
	- SQL의 IN 쿼리를 효과적으로 재사용 하기 위해서 사용하는 옵션으로, 반드시 true로 설정해야 한다.
	- false로 설정하고 IN 쿼리가 늘어나는 경우에 Hibernate의 Execution Plan Cache가 효과적으로 재사용 되지 않기 때문에 OOM 증상이 발생할 수 있음.



JPA는 다음과 같은 설정을 거치게 된다.

어떤 구현체를 쓸지 결정하는 것이 `JpaVendorAdapter`이다. 

```java
@Bean
public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties) {
    AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setShowSql(jpaProperties.isShowSql());
    adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
    adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
    return adapter;
}
```

파리미터의 JpaProperties는 yaml에 정의한 속성들이 자동으로 빈으로 주입되어 만들어진다.



```java
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter,
                                                                   JpaProperties jpaProperties) {
    LocalContainerEntityManagerFactoryBean em
        = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("com.kdt.lecture.domain");
    em.setJpaVendorAdapter(jpaVendorAdapter);

    Properties properties = new Properties();
    properties.putAll(jpaProperties.getProperties());
    em.setJpaProperties(properties);

    return em;
}
```

Jpa에서 테이블과 매핑되는 Entity를 관리해주는 것이 EntityManager이다.

이를 EntityManagerFactoryBean이 만들어준다.

테이블과 매핑될 Entity를 어느 패키지에서 관리할지 `packagesToScan`메서드를 통해 지정할 수 있다.

JpaProperties설정도 여기서 진행된다.



```java
@Bean
public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

    return transactionManager;
}
```

TransactionManager는 트랜잭션 어노테이션이 사용되어 진 곳을 관리하게 된다.







 