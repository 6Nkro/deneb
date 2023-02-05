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
* F/W, Library : vuetify, axios, vuex-persistedstate, sweetalert2, vue3-youtube, vuedraggable, vue-final-modal

## 개발하며 겪은 어려웠던 점 / 과제
* 순서 정보에 대한 처리
<img src="https://user-images.githubusercontent.com/114794711/216782041-72f875e7-587d-479b-8992-ebee43e9c0f7.gif" />
<img src="https://user-images.githubusercontent.com/114794711/216785707-310ab598-b5ae-49ae-9b1c-6ae19dcd1589.png" />

페이지, 폴더, 북마크로 구분되는 각각의 오브젝트는 사용자가 임의로 순서를 변경할 수 있어야 하고 변경된 순서 정보를 저장해야 했는데, 순서 정보는 각 오브젝트의 상위(계정 > 페이지, 페이지 > 폴더, ...) 테이블에서 직렬화 된 배열 형태로 관리하도록 했습니다.
데이터가 insert, delete, drag & drop으로 인한 순서 이동으로 변경 될 때마다 상위 테이블의 order 컬럼을 추가로 update 해줘야 하는점이 있어 옳은 설계였는지 아직 의문이 남습니다.

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
