SHOW databases;
use prod;
SHOW tables;

SELECT * FROM 
prod.session;

-- 위 아래 같음 / 위에서 데이터베이스를 지정 했으니

SELECT * FROM 
session;

SELECT id, user_id, channel_id
FROM prod.session;

SELECT * 
FROM prod.session
LIMIT 10;

-- 유일한 채널 ID를 알고 싶을 때
SELECT DISTINCT channel_id 
FROM prod.session;

-- 채널 ID별로 카운트를 하려면 어떻게 할까?  --> GROUP BY, COUNT
SELECT channel_id, COUNT(1)
FROM prod.session
GROUP BY 1; -- GROUP BY channel_id랑 같은 뜻이다.

-- 테이블의 모든 레코드 수 카운트, COUNT(*)와 같음
SELECT COUNT(1)
FROM prod.session;

-- channel 테이블의 모든 레코드들을 표시
SELECT *                          
FROM prod.channel;

-- channel이 Facebook경우만 레코드수 카운트
SELECT COUNT(1)
FROM prod.session
WHERE channel_id = 5;  -- WHERE channel_id in (5)와 같은 뜻

-- CASE WHEN
SELECT channel_id, 
  CASE
    WHEN channel_id in (1, 5, 6) THEN 'Social-Media'
    WHEN channel_id in (2, 4) THEN 'Search-Engine'
    ELSE 'Something-Else'
  END channel_type
FROM prod.session;

SELECT DISTINCT channel_id, 
  CASE
    WHEN channel_id in (1, 5, 6) THEN 'Social-Media'
    WHEN channel_id in (2, 4) THEN 'Search-Engine'
    ELSE 'Something-Else'
  END channel_type
FROM prod.session;

-- COUNT 제대로 이해하기
SELECT * FROM prod.count_test;
SELECT COUNT(1) FROM prod.count_test;
SELECT COUNT(0) FROM prod.count_test;
SELECT COUNT(NULL) FROM prod.count_test;
SELECT COUNT(value) FROM prod.count_test;
SELECT COUNT(DISTINCT value)  FROM prod.count_test;

-- WHERE 조건

SELECT COUNT(1)
FROM prod.session
WHERE channel_id IN (4, 5);

SELECT COUNT(1)
FROM prod.channel
WHERE channel LIKE '%G%';

SELECT COUNT(1)
FROM prod.channel
WHERE channel LIKE 'G%';

SELECT DISTINCT channel
FROM prod.channel
WHERE channel LIKE '%o%';

SELECT DISTINCT channel
FROM prod.channel
WHERE channel NOT LIKE '%o%';

-- String 함수들 써보기
SELECT
    LENGTH(channel),     
    UPPER(channel),
    LOWER(channel), 
    LEFT(channel, 4),
    RPAD(channel, 15, '-')
FROM prod.channel;

-- ORDER BY and NULL
SELECT value
FROM prod.count_test
ORDER BY value DESC;

SELECT value
FROM prod.count_test
ORDER BY 1 DESC;

SELECT value
FROM prod.count_test
ORDER BY value ASC;


-- DATE 관련 함수들 써보기
SELECT
  created, CONVERT_TZ(created, 'GMT', 'Asia/Seoul') seoul_time,
  YEAR(created) y, QUARTER(created) q, MONTH(created) m, MONTHNAME(created) mnn, 
  DATE(created) d, HOUR(created) h, MINUTE(created) m, SECOND(created) s
FROM prod.session
LIMIT 10;

SELECT created,
    DATEDIFF(now(), created) gap_in_days,
    DATE_ADD(created,  INTERVAL 10 DAY) ten_days_after_created
FROM prod.session
LIMIT 10;


SELECT STR_TO_DATE('01,5,2013','%d,%m,%Y');

-- Type casting
SELECT cast('100.0' as float), convert('100.0', float);

SELECT channel_id, COUNT(1) AS session_count, COUNT(DISTINCT user_id) AS user_count
FROM prod.session
GROUP BY 1
ORDER BY 2 DESC;

-- 월별 세션수를 계산하는 SQL
SELECT
    LEFT(created, 7) AS mon,
    COUNT(1) AS session_count
FROM prod.session
GROUP BY 1  -- GROUP BY mon, GROUP BY LEFT(created, 7)
ORDER BY 1;


-- 가장 많이 사용된 채널은 무엇인가?
SELECT
    channel_id,
    COUNT(1) AS session_count,
    COUNT(DISTINCT user_id) AS user_count
FROM prod.session 
GROUP BY 1                         -- GROUP BY channel_id
ORDER BY 2 DESC;              -- ORDER BY session_count DESC


-- 가장 많은 세션을 만들어낸 사용자 ID는 무엇인가?
SELECT
    user_id,
    COUNT(1) AS count
FROM prod.session
GROUP BY 1                         -- GROUP BY user_id
ORDER BY 2 DESC              -- ORDER BY count DESC
LIMIT 1;

-- 월별 유니크한 사용자 수
SELECT
    LEFT(created, 7) AS mon,
    COUNT(DISTINCT user_id) AS user_count
FROM prod.session
GROUP BY 1  -- GROUP BY mon, GROUP BY LEFT(created, 7)
ORDER BY 1;

-- 월별 채널별 유니크한 사용자 수
SELECT s.id, s.user_id, s.created, s.channel_id, c.channel
FROM prod.session s
JOIN channel c ON c.id = s.channel_id;

-- 
SELECT 
  LEFT(s.created, 7) AS mon,
  c.channel,
  COUNT(DISTINCT user_id) AS mau
 FROM prod.session s
 JOIN prod.channel c ON c.id = s.channel_id
GROUP BY 1, 2
ORDER BY 1 DESC, 2;

