## SELECT 살펴보기 

```sql
SHOW DATABASES;
USE prod;
SHOW TABLES;
```

먼저 사용할 데이터베이스를 설정해줘야 한다. 



### SELECT

```sql
SELECT 필드이름1, 필드이름2, ...
FROM 테이블이름
WHERE 선택조건
GROUP BY 필드이름1, 필드이름2, ...
ORDER BY 필드이름 [ASC|DESC] -- 필드 이름 대신에 숫자 사용 가능 LIMIT N;
```

테이블(들)에서 레코드들(혹은 레코드수)을 **읽어오는데** 사용한다. 

WHERE를 사용한다면 조건을 만족하는 레코드들을 읽어온다. 

```sql
SELECT * -- *는 모든 필드를 지칭하는 표현
FROM prod.session; -- 앞서 USE prod;를 수행했다면 FROM session도 사용 가능

SELECT id, user_id, channel_id
FROM prod.session;

SELECT *
FROM prod.session 
LIMIT 10;

SELECT DISTINCT channel_id  -- 유일한 채널 ID를 알고 싶은 경우 
FROM prod.session;

SELECT channel_id, COUNT(1) -- 채널 ID별 카운트를 하려면 GROUP BY/COUNT
FROM prod.session
GROUP BY 1;

SELECT COUNT(1)  -- 테이블의 모든 레코드 수 카운트. COUNT(*)와 같음
FROM prod.session;

SELECT COUNT(1)   -- channel이 Facebook경우만 레코드수 카운트
FROM prod.session 
WHERE channel_id = 5;
```

- LIMIT 
	- 레코드 수 제한
- DISTINCT
	- 중복 제거 

- GROUP BY 
	- `GROUP BY 1`에서 `1`은 SELECT에 쓰인 첫번째 필드를 뜻한다. 
	- 위의 예제에선 `channel_id`를 뜻한다.



### CASE WHEN

필드 값의 변환을 위해 사용 한다. 

`CASE WHEN 조건 THEN 참일때 값 ELSE 거짓일 때 값 END 필드이름` 형식이다.

```sql
CASE
	WHEN 조건1 THEN 값1 
	WHEN 조건2 THEN 값2 
	ELSE 값3
END 필드이름

SELECT channel_id, CASE
	WHEN channel_id in (1, 5, 6) THEN 'Social-Media' 
	WHEN channel_id in (2, 4) THEN 'Search-Engine' 
	ELSE 'Something-Else'
END channel_type 
FROM prod.session;
```

여기서 in은 `channel_id = 1 or 5 or 6` 과 같은 뜻이다.



### NULL

값이 존재하지 않음을 나타내는 상수이며 테이블 정의시 디폴트 값으로 지정 가능하다.

NULL 판단의 경우 특수한 문법인 `IS NULL, IS NOT NULL`을 사용한다.



### COUNT 

SELECT된 레코드들을 count한다. 하지만 함수의 인자가 NULL인 경우 count를 하지 않는다.

| value |
| :---: |
| NULL  |
|   1   |
|   1   |
|   0   |
|   0   |
|   4   |
|   3   |

```SQL
SELECT COUNT(1) FROM prod.count_test -> 7
SELECT COUNT(0) FROM prod.count_test -> 7
SELECT COUNT(NULL) FROM prod.count_test -> 0  -- 항상 NULL이니 count가 안됨.
SELECT COUNT(value) FROM prod.count_test -> 6
SELECT COUNT(DISTINCT value) FROM prod.count_test -> 4
```



### WHERE 

- IN, NOT IN 
	- `WHERE channel_id in (3,4)`
- LIKE , NOT LIKE
	- 대소문자 구별 없이 문자열 매칭 기능 
	- `WHERE channel LIKE 'G%'` -> 'G*' 
		- 채널 이름이 G로 시작함
	- `WHERE channel LIKE '%o%'` -> '\*o\*`
		- 채널 이름에 o가 들어감



### STRING 함수 

- LEFT(str, N)
	- 처음부터 N개의 문자열 반환
- REPLACE(str, exp1, exp2)
- UPPER(str)
- LOWER(str)
-  LENGTH(str)
-  LPAD, RPAD
	- 문자열을 패딩시킨다. 

- SUBSTRING
	- 어떤 위치든 원하는 문자열을 꺼낼 수 있다.

- CONCAT



### ORDER BY 

- 디폴트 순서는 오름차순이다. 
- 내림차순을 원하면 `DESC`
- 여러 개의 필드를 사용해서 정렬하려면 콤마로 구분해서 사용한다.
- NULL 순서? 
	- 오름차순일 경우, 처음에 위치한다. 
	- 내림차순일 경우, 마지막에 위치한다.



### 타입 변환 

#### DATE Conversion

```sql
SELECT
	created, CONVERT_TZ(created, 'GMT', 'Asia/Seoul') seoul_time,
	YEAR(created) y, QUARTER(created) q, MONTH(created) m, MONTHNAME(created) mnn, 		DATE(created) d, HOUR(created) h, MINUTE(created) m, SECOND(created) s
FROM session 
LIMIT 10; 
```

- NOW

	- 타임존 관련 변환 : `CONVERT_TZ(now(), 'GMT', 'Asia/Seoul')`

	- DATE(년 월 일), WEEK(주 번호), MONTH, YEAR, HOUR, MINUTE, SECOND, QUARTER(분기), MONTHNAME(월 영어이름)
	- DATEDIFF (두 시간 간의 차이)
	- DATE_ADD

● STR_TO_DATE, DATE_FORMAT



#### Type Casting 

```sql
SELECT cast('100.0' as float), convert('100.0', float);
```

- cast(category as float)

- convert(expression, float)

	


## GROUP BY & Aggregate 함수

테이블의 레코드들을 그룹핑하여 그룹별로 다양한 정보를 계산한다. 

이는 두 단계로 이루어진다. 

- 먼저 그룹핑을 할 필드를 결정한다. (하나 이상의 필드)
	- GROUP BY로 지정(필드 이름이나 일련번호로 사용)
- 그룹별로 계산할 내용 결정 
	- Aggregate 함수 사용 
	- COUNT, SUM, AVG, MIN, MAX, GROUP_CONCAT 
	- 보통 필드 이름을 지정하는 것이 일반적이다. (alias)

```sql
SELECT
	LEFT(created, 7) AS mon,  -- 년도-월 (4 1 2)
	COUNT(1) AS session_count
FROM prod.session
GROUP BY 1 -- GROUP BY mon, GROUP BY LEFT(created, 7) 이렇게도 가능
ORDER BY 1;
-- 월별 세션수를 계산


SELECT
	channel_id,
	COUNT(1) AS session_count, 
	COUNT(DISTINCT user_id) AS user_count
FROM prod.session
GROUP BY 1 
ORDER BY 2 DESC;  
-- 가장 많이 사용된 채널 


SELECT 
	user_id,
	COUNT(1) AS count 
FROM prod.session 
GROUP BY 1
ORDER BY 2 DESC 
LIMIT 1;
-- 가장 많은 세션을 만들어낸 사용자 ID

SELECT
	LEFT(created, 7) AS mon, 
	COUNT(DISTINCT user_id) AS user_count
FROM prod.session
GROUP BY 1 
ORDER BY 1;
-- 월별 유니크한 사용자 수 
```



### 월별 채널별 유니크한 사용자 수 

필요한 정보 - 시간 정보, 사용자 정보, 채널 정보 

먼저 어느 테이블을 사용해야 하는지 생각한다. 

여기서는 channel 이름을 사용할 거라 session과 channel 두 테이블의 조인이 필요하다.

```sql
SELECT s.id, s.user_id, s.created, s.channel_id, c.channel 
FROM session s
JOIN channel c ON c.id = s.channel_id;
-- session, channel join

SELECT
	LEFT(s.created, 7) AS mon, 
	c.channel,
	COUNT(DISTINCT user_id) AS mau
FROM session s
JOIN channel c ON c.id = s.channel_id
GROUP BY 1, 2 
ORDER BY 1 DESC, 2;
```