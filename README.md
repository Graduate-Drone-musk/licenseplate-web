# Licenseplate Detect - Get illegal plate
<p>
Using Spring, Get illegal plate to s3
</p>


## 전반적인 프로세스
------------
### 현재 위치를 표시할 수 있는 지도 (Naver map API 사용, 개선 필요 )
  - 화면에 위치를 찍은 포인트를 리스트로 반환
  - 해당 포인트 좌표를 드론이 비행하고 오기위한 용도

------------

### Recognize 이후   
1.  S3의 데이터 가져와서 실제 자동차 데이터인지 확인 (필터링 작업)

2.  해당 url 및 유저 정보 DB에 저장

3.  화면 출력을 위한 API 작성
    ```
    // API 수신 구조
    {
        "startTime" : "20220605", // 시작날짜
        "endTime" : "20220709",   // 검색 마지막 날짜
        "showCount" : "10",       // 보고 싶은 갯수
        "page" : "1",             // FE 페이징 처리 대신 ( 후에 FE가 직접 페이징 처리함 )
        "allPageCount" : "-1"     // Total Page 
    }

    // API 반환 구조
    {
        "status" : 전송 상태,
        "message": 전송 메세지,
        "illegalList" : { // 요일을 받으면 데이터 리스트 전송
            { // user 정보
                "phone": "",
                "userName": "",
                "date": "",
                "licenseplate": "",
                "lpUrl": "",
                "originUrl": ""
            },
        ... 
	}
    ```
    동일한 구조로 user 주민등록번호로 조회 가능 
     - BE에서만 구현, FE구현 필요
------------

### 관리자 결제 기능 - 구현 필요
1. 카카오 API를 통해 불법주차 주인에게 메세지 전송