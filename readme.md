# 개발 프레임워크
 - SpringBoot
 - Gradle
 
# 문제해결 전략
 #### 기본 전략
  - JPA를 사용하기 위해 만든 Entity를 VO로, Repository를 Service로 사용
  - Restful API를 지향

 #### 1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 개발 
   - ORM(Object Relational Mapping)으로는 JPA를 사용
   - 테스팅환경에 따른 제약을 줄이기 위해 SpringBoot에 내장 가능한 H2 데이터베이스 사용
   - 신용보증 기관 테이블(기관이름, 기관코드)과 보증금액 테이블(연도,월,기관코드,보증금액)로 설계
   - localhost:5000/loadData
 #### 2. 주택금융 공급 금융기관(은행) 목록을 출력하는 API를 개발하세요.
   - JPA의 nativeQuery (SELECT * FROM INSTITUTE) 를 사용하여 저장된 데이터베이스에서 결과를 얻어 JSON형태로 변경하여 출력
   - localhost:5000/banks

 #### 3. 년도별 각 금융기관의 지원금액 합계를 출력하는 API를 개발하세요. 
   - total_amount 와 detail_amount 를 가져오는 쿼리를 따로 작성
   ```
   SELECT 
       YEAR, 
       SUM(AMOUNT) AS TOTAL_AMOUNT 
   FROM 
       BANK 
   GROUP BY 
       YEAR
   ```
    SELECT  
       I.INSTITUTE_NAME AS BANK,
       SUM(B.AMOUNT) AS AMOUNT 
    FROM 
       BANK AS B 
       LEFT OUTER JOIN 
       INSTITUTE AS I 
           ON B.INSTITUTE_CODE = I.INSTITUTE_CODE 
    WHERE 
       YEAR LIKE :year 
    GROUP BY 
       B.YEAR, 
       B.INSTITUTE_CODE
     
   - 각 연도별  total_amount를 저장하고있는 JSON 데이터에 각 금융기관의 detail_amount를 JSON리스트 데이터를 추가
   - localhost:5000/amounts/banks/years

 #### 4. 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발
   - 연도와 기관코드로 group by를 하고 연도별 지원금액순으로 정렬하고 가장 상위 1개만 가져오는 쿼리를 사용
     ```
     SELECT  
        B.YEAR,
        I.INSTITUTE_NAME AS BANK,
        SUM(B.AMOUNT) AS AMOUNT
     FROM 
        BANK AS B
        LEFT OUTER JOIN
        INSTITUTE AS I 
           ON B.INSTITUTE_CODE = I.INSTITUTE_CODE 
     GROUP BY 
        B.YEAR,
        B.INSTITUTE_CODE
     ORDER BY
        SUM(B.AMOUNT) DESC
     LIMIT 0,1
   - localhost:5000/bank/yealymaxamount
   
 #### 5. 전체 년도(2005~2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API 개발 
   - 문제는 외환은행이지만 다른 금융기관을 입력으로 받는 API에서도 사용할수 있는 쿼리로 작성
   - 해당 금융기관의 기관코드를 찾고 해당 기관의 연도별 평균 지원금액의 최대 최소를 각각 따로 구한다.
   ```
   SELECT * FROM INSTITUTE WHERE INSTITUTE_NAME LIKE :bank
  
   ```
    SELECT
       B.YEAR,
       AVG(B.AMOUNT) AS AMOUNT
    FROM 
       BANK AS  B
    WHERE
       B.INSTITUTE_CODE LIKE :bank_code 
    GROUP BY
       B.YEAR 
    ORDER BY
       AVG(B.AMOUNT) DESC 
    LIMIT 0,1
   
   최소를 구하는 쿼리는 DESC만 제거하여 오름차순으로 구한다.
   - localhost:5000/bank/maxminyear
   
 #### 6. (선택문제) 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하는 API 개발
  - 선형회귀 알고리즘 사용 (SimpleRegression 라이브러리)
  - 1차 선형회귀 알고리즘을 사용하였기 때문에 정확도가 조금 떨어진다. 다중 선형회귀 알고리즘을 사용한다면 개선 될것이라고 생각한다.
  - localhost:5000/predict (requestbody에 {"bank" : 기관이름, "month":특정달 } 전달 )

 #### API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받으 토큰을 가지고 호출하세요.
  - 계정생성시  refresh token 을 발행, 데이터베이스에 계정 정보와 같이 저장
  - 로그인시 acess tgoken을 발행
  - interceptor를 사용하여 컨트롤러에 들어요는 요청을 가로채 http header에서 authorization header에 발급된 토큰값이 있는지 확인하여 인증한다.
  - refresh 토큰 재발급을 interceptor에서 authorization header이 Bearer Token으로 입력 요청이 왔는지 확인해서 데이터베이스의 계정 정보에서 refresh token을 갱신하는 작업은 완료했으나 재발급한 refreshtoken을 출력해주는 작업을 완료하지 못하였다.
  
# 빌드 및 실행방법
 - 이클립스 환경에서 개발하여 이클립스환경 기준으로 빌드 및 실행방법 설명드리겠습니다.
 - marketplace에서 Buildship Gradle Integration 3.0 과 Spring Tools 4 설치
 - gradle project로 import
 - 프로젝트에서 refresh gradle Project 실행
 - Spring boot App으로 Run
 - 기본포트는  5000 번으로 설정되어있습니다.
 - localhost:5000/public/loadData 로 csv파일에서 데이터베이스로 데이터 저장을 먼저해야 다른 API 기능 가능
