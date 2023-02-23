# deneb
>북마크 관리 및 공유 사이트

## 개요
* 개발기간 : 2022/12/08 ~ 2023/01/05
* 개발인원 : 1명
* 주소 : http://deneb.run?test

## 개발환경
* BE : JDK 11, Spring Boot, MyBatis, Oracle Database
* FE : HTML, CSS, Javascript, Vue.js
* Server : AWS EC2, Oracle Cloud
* F/W, Library : vuetify, axios, vue-router, vuex, vuex-persistedstate, sweetalert2, vue3-youtube, vuedraggable, vue-final-modal

## 개발하며 겪은 어려웠던 점 / 개선할 점
### 순서 정보에 대한 처리
<img src="https://user-images.githubusercontent.com/114794711/216782041-72f875e7-587d-479b-8992-ebee43e9c0f7.gif" />
<img src="https://user-images.githubusercontent.com/114794711/216785707-310ab598-b5ae-49ae-9b1c-6ae19dcd1589.png" />

* 데이터 정규화 및 자료 구조 선택을 올바르게 했는지에 대한 고찰 필요

### 모듈간 결합도 문제
<img src="https://user-images.githubusercontent.com/114794711/220876332-ba6b52d8-c7a1-4a05-92ba-5fc8d981aa7a.png" />

* 모듈 분리의 의의에 대해 다시 생각해보기
* 필드에 @Autowired 어노테이션은 현재 권장하지 않는 추세

### 클린 코드

* 러프한 코드에 대한 리팩토링 필요

### 컴포넌트 분리와 상태관리

* 컴포넌트를 분리하는 기준과 재사용성을 고려한 컴포넌트 분리 방법을 다시 고민해보기
* 컴포넌트 깊이에 따라 복잡해지는 상태관리를 효율적으로 해보기(Vuex 활용)

### 동작 효율성

* 탐색, 정렬, 상태관리에서 불필요한 동작 또는 오버헤드를 만들고 있는지 살펴보기
* 쿼리를 효율적으로 쓰고 있는지, 커넥션에 악영향을 끼치는 부분 생각해보기(batch 활용 고려)

## ERD
<img src="https://user-images.githubusercontent.com/114794711/215716512-21b8e93d-a6fc-485a-9362-e775d6d180e1.png" />

* Account : 사용자 계정
* Bookcase : 북마크 관리 root
* Book : 북마크 폴더
* Bookmark : 북마크
* Sub_* : 공유 북마크 clone
* Share_Reply : 공유 게시물 댓글

## 화면 구성
<img src="https://user-images.githubusercontent.com/114794711/215732173-0f25f353-a105-4049-b9fb-c879d1543bbe.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732170-70c7c19f-c4e0-4762-bbc1-ffce37c5bcda.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732162-77ad1819-170b-4a29-8172-1b6361a09501.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732158-7965e79a-af81-4eb0-96c1-8302fe532aab.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732156-fb91b687-9998-4e17-af78-4ffc8e9b2b23.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732155-53eadcb9-59ca-4a35-95b6-9f1acd9ab266.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732154-14c4f17b-45b0-4f9e-8eae-36e1816107b6.png" />
<img src="https://user-images.githubusercontent.com/114794711/215732145-2e3481b2-46ee-4058-872c-497a27884679.png" />
