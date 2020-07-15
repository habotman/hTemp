## REST API   결제/결제취소/결제정보 조회 서버시스템
### 개발 프레임워크  
### JAVA  
 javaSe-1.8(jre1.8.0_251)   
1.springframework.boot version.2.3.1.RELEASE, spring-boot-maven-plugin  
 kakaoPayTemp project for Spring Boot  
2.DATABASE  
- h2database File (mv.db)  

###  springframework 설정  
spring-boot-devtools - spring boot app st art  
com.h2database - db  
org.mybatis.spring.boot - sql  
org.jasypt - Encryptor  
org.bgee.log4jdbc-log4j2 - log  
com.fasterxml.jackson.core -json   
org.apache.commons -server   

### 개발사항 전략
1. JAVA Spring Boot 사용하여 서비스 개발 구축에 효율성을 갖는다.  
2. H2 DataBase File 모드 사용하여 Runtime Data 저장을 하는 시스템 구조를 갖는다.  
3. RestController Request/ Response 처리를 통하여 REST API 처리 구조를 만들어 빠르게 개발한다.  
4. Json String 부분을 내부 객체화 처리를 하여 개발단순화를 갖는다.  
5. DB SQL 문 처리시 Mybatis 사용 가독성을 확보한다.  
6. H2 File DB 구성으로 Data 검증이 가능하도록 한다.  
7. DB 적재 정보로 정합성체크, 및 업무시스템을 개발하도록 한다.  
8. Api 가정 테스트케이스 전체 사항을 모두 구축처리 테스트하도록하고 검증작업은 DB적재 데이터로 한다.  

### 업무프로세스
#### 결제 처리 서비스 PAYMENT
 * 1. 결제 정보 정합성 체크 -중복 진입, 미처리, 관리사항 체크 DB 조회
 * 2. 입력값 검증 정제 -결제금액, 부가세처리
 * 3. 관리테이블 정보 적재 - 관리번호 채번
 * 4. 욉부 카드사 전송 처리 -H2 DB 적제 성공가정 - 관리정보  관리 상태 코드 : I:입력(내부관리상태), C:성공, F:실패  - 수정처리
 * 5. 결제/취소 정보 테이블 Row 단위 적재 처리.

#### 취소 처리 서비스 CANCEL
 * 1. 결제 정보 정합성 체크 -중복 진입, 미처리, 관리사항 체크 DB 조회
 * 2. 입력값 검증 정제 -거래번호의 결제정보, 결제금액, 부가세처리 대상 취소요건 처리
 * 3. 관리테이블 정보 적재 - 관리번호 채번
 * 4. 욉부 카드사 전송 처리 -H2 DB 적제 성공가정 - 관리정보  관리 상태 코드 : I:입력(내부관리상태), C:성공, F:실패  - 수정처리
 * 5. 결제/취소 정보 테이블 Row 단위 적재 처리.
#### 조회 처리 서비스 - DB에 저장된 데이터를 조회
 * 1. 입력값 검증 - 관리번호 null check
 * 2. 결제정보테이블 대상 조회.
 * 3. out data 정제처리,  과제요건 - 카드정보 복호화처리 포함.
 *  dataFlgcd - 결제취소구분 - PAYMENT/CANCEL
 *  추가정보 필드 : 작성일시

### 테이블설계 사항
![table](./hTemp/kakaoPayTemp/Doc/kpayH2_DA.png)


### TEST CASE  

#### 테스트 방법 - POST 방식  
1. test case : Talend Cloud API Tester - Google 플러그인  
 - http://localhost:8080/payment (결제)  
 - http://localhost:8080/cancel (취소)  
 - http://localhost:8080/select (조회)  
2. Multi Thread  -  Talend Cloud API Tester 2개 접속 필드값 추가 - 프로그램 로직 처리.  
#####  [TestCase_단위_Mulit포함](./hTemp/kakaoPayTemp/Doc/TestCase_단위_Mulit포함.xlsx) < 해당 파일 관련 테스트결과 내용 포함.

### 빌드 및 실행 방법 
##### 스프링부트 및 이클립스 기본설치
##### /kakaoPayTemp/H2DB/hDB.mv.db >> 해당 파일 로컬저장
##### H2 DB 연결 /kakaoPayTemp/src/main/resources/application.properties  해당내용중에 
##### spring.datasource.url=jdbc:log4jdbc:h2:file:C:/hTemp/hDB;AUTO_SERVER=TRUE  
##### ==> C:/hTemp/hDB 경로설정 저장 빌드.
 


