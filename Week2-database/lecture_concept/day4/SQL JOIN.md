## MySQL에서 지원하는 컬럼 타입 

- Numeric Type 
	- INTEGER, INT, SMALLINT, TINYINT, MEDIUMINT, BIGINT 
	- DECIMAL, NUMERIC 
		- 소수점 단위 언제까지는 신경 안 쓸때 유용하다.
	- FLOAT, DOUBLE, BIT

- Data and Time Type 
	- DATE, DATETIME, TIMESTAMP,TIME, YEAR
- String Type 
	- CHAR, VARCHAR, BINARY, VARBINARY, BLOB, TEXT, ENUM, SET
- JSON Type 
	- 다양한 JSON 조작함수를 제공한다. 
- Spatial Type 
	- 위도와 경도를 중심으로한 위치 관련 타입 
	- 매우 유용할 때가 있다.



## INSERT 

먼저 테이블 2개를 생성해준다. 

```sql
CREATE TABLE prod.vital ( 
    user_id int not null, 
    vital_id int primary key, 
    date timestamp not null, 
    weight int not null
);

CREATE TABLE prod.alert ( 
    alert_id int primary key, 
    vital_id int,
	alert_type varchar(32), 
    date timestamp,
	user_id int 
);
```



이후 데이터를 추가해보자!

```sql
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(100, 1, '2020-01-01', 75); 
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(100, 3, '2020-01-02', 78); 
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(101, 2, '2020-01-01', 90); 
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(101, 4, '2020-01-02', 95); 
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(999, 5, '2020-01-02', -1); 
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(999, 5, '2020-01-02', 10);  -- PRIMARY KEY가 중복되었으니 추가가 안되고 실패한다.
```

```sql
INSERT INTO prod.alert VALUES(1, 4, 'WeightIncrease', '2020-01-02', 101); 
INSERT INTO prod.alert VALUES(2, NULL, 'MissingVital', '2020-01-04', 100); 
INSERT INTO prod.alert VALUES(3, NULL, 'MissingVital', '2020-01-04', 101);
```

아래 방식처럼 필드 순서대로 INSERT를 하면 필드 리스트를 생략할 수 있다.



## DELETE 

조건을 기반으로 테이블에서 레코드 삭제, 모든 레코드 삭제가 가능하다. 

그래도 **테이블은 존재하게 된다.** 

### VS TRUNCATE 

TRUNCATE는 조건없이 **모든 레코드를 삭제하여 속도가 빠르지만 트랜잭션 사용시 롤백이 불가능하다.** 

이것도 테이블은 존재한다.



```sql
DELETE FROM prod.vital WHERE weight <= 0; -- 워크벤치에서 PRIMARY KEY로 삭제하지 않을 경우 기본적으로 막아놓았다..
DELETE FROM prod.vital WHERE vital_id = 5;
DELETE FROM prod.vital;
```





## UPDATE 

조건을 기반으로 테이블에서 특정 레코드(들)의 필드 값을 수정할 수 있다. 

```sql
UPDATE prod.vital 
SET weight = 92 
WHERE vital_id = 4;
-- vital_id가 4인 레코드의 weigh를 92로 변경
```



## JOIN

두 개 이상의 테이블들을 공통 필드를 가지고 통합하는 것이다. 스타 스키마로 구성된 테이블들로 분산되어 있던 정보를 통합하는 데 사용한다.



### 문법

![img](https://tva1.sinaimg.cn/large/e6c9d24egy1h0tesz9k4nj20tn0ec77j.jpg)

[출처](https://theartofpostgresql.com/blog/2019-09-sql-joins/)



```sql
SELECT A.*, B.*
FROM raw_data.table1 A
( 종류 ) JOIN raw_data.table2 B ON A.key1 = B.key1 and A.key2 = B.key2 
WHERE A.ts >= '2019-01-01';
```

괄호 종류안에 INNER, LEFT, RIGHT, CROSS가 들어갈 수 있다. 하지만 MySQL은 FULL 조인을 지원하지 않아 FULL은 사용할 수 없고 SELF의 경우 키워드가 없고 하나의 테이블을 alias로 구분하여 조인한다.



### JOIN시 고려해야할 점 

먼저 중복 레코드가 없고 PRIMARY KEY의 uniqueness가 보장됨을 봐야한다.

조인하는 테이블들 간의 관계를 명확하게 정의해야 한다.

- One to one 
- One to many / Many to One 
	- 주문과 주문한 아이템의 관계를 생각하면 된다. 
	- 중복이 된다면 증폭되어 커지게 된다.
- Many to many 
	- one to one이나 ont to many로 바꿀 수 있다면 변환하여 조인하는 것이 덜 위험하다.

어느 테이블을 베이스로 잡을지 즉, From에 사용할지 결정해야한다. From뒤에 사용한 테이블이 왼쪽으로 기준이 된다. 

### JOIN의 종류 

- INNER JOIN 

	```sql
	SELECT * FROM prod.vital v
	JOIN prod.alert a ON v.vital_id = a.vital_id; 
	```

	- 양쪽 테이블에서 **매치가 되는 레코드들만** 리턴 
	- 양쪽 테이블의 필드가 **모두 채워진 상태**로 리턴 
	- 기본 조인으로 INNER 생략 가능

- LEFT JOIN 

	```sql
	SELECT * FROM prod.vital v
	LEFT JOIN prod.alert a ON v.vital_id = a.vital_id;
	```

	- 왼쪽 테이블의 모든 레코드들을 리턴 
	- 오른쪽 테이블의 필드는 매칭되는 경우에만 채워진 상태로 리턴

- FULL JOIN

	```sql
	SELECT * FROM prod.vital v
	LEFT JOIN prod.alert a ON v.vital_id = a.vital_id
	UNION -- vs. UNION ALL
	SELECT * FROM prod.vital v
	RIGHT JOIN prod.alert a ON v.vital_id = a.vital_id;
	```

	- 왼쪽, 오른쪽 테이블의 모든 레코드들을 리턴
	- 매칭되는 경우에만 양쪽 테이블들의 모든 필드들이 채워진 상태로 리턴
	- MySQL에서는 지원하지 않아 LEFT JOIN과 RIGHT JOIN을 UNION하여 사용한다.
	- UNION은 중복을 제거하고 UNION ALL은 중복 제거를 하지 않는다.

- CROSS JOIN

	```sql
	SELECT * FROM prod.vital v 
	CROSS JOIN prod.alert a;
	```

	- 왼쪽, 오른쪽 테이블의 모든 레코드들의 **조합**을 리턴
	- ON 조건이 필요없다.

- SELF JOIN 

	```sql
	SELECT * FROM prod.vital v1
	JOIN prod.vital v2 ON v1.vital_id = v2.vital_id;
	```

	- 동일한 테이블을 alias를 통해 구분하여 자기 자신과 조인한다. 
	- 자기 자신이니 같은 필드가 2번 반복된다.