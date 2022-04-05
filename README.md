# thehouseUpdate
프로젝트 수정   
thehouse Project 수정본.

## 수정 전 문제점

1.  로그인 하지 않으면 사진게시판과 질문 게시판 글을 볼 수 없는 에러.  O
2.  사진게시판 무한스크롤 끝나지않고 계속 중복되서 출력 되는 에러. O
3.  질문게시판 검색 후에 페이징처리 안되는 에러. O
4.  무조건 아이디가 출력되고 닉네임은 출력 안되는 상태. O
5.  사진게시판 이미지 수정 불가.  O
6.  프로필 수정시에 이미지 선택 안하면 에러 발생. O
7.  프로필 이미지 선택하면 버튼 사이즈보다 더 크게 나와서 아래까지 침범하는 문제.  O
8.  프로필 이미지 버튼 사이즈가 왼쪽까지 넓게 설정된 문제. O
9.  프로필 이미지 수정은 가능하나 삭제는 안되는 문제.  O
  

## 수정현황

### 7/14

* 질문게시판 제목클릭해서 이동하는걸로 변경하고 로그인하지 않은 상태에서는 글 상세 볼 수 없고 로그인으로 이동하도록 변경.

### 8/5

* 아이디만 표시되던것 닉네임 등록 해두면 닉네임으로. 닉네임이 없으면 아이디로 출력되도록 변경.
* 프로필 설정에서 이미지 선택하면 preview사이즈보다 크게 출력되던거 사이즈 조절.
* 프로필 이미지 버튼 범위 왼쪽으로 넓게 되있던거 이미지 사이즈에 맞춰서 변경.


### 8/10

* 로그인 안한상태에서 글 볼 수 없는것 수정
* view를 새로 만들어서 작성.
* 시큐리티나 비즈니스 로직으로 처리할 수 있는 방법 있을 것 같긴한데 아직  못함..


### 8/11

* 프로필 이미지 삭제 버튼 생성 및 구현.


### 8/12

* 질문게시판 검색 후 페이징 처리 해결.   
* 비회원 게시판 글쓰기 버튼 누르면 로그인화면으로 이동하도록 수정.
* 비회원 질문게시판 상세페이지에서 답글달기 버튼 다시 활성화하고 누르면 로그인화면으로 이동하도록 수정.

### 11/26

* 프로필 이미지 변경하지 않을 시 수정 불가 에러 해결.
* MultipartHttpServletRequest 사이즈 체크 하는 방법으로 해결.
* 좀 더 상세한 해결 방법은 MyPageServiceImpl 내 해당 메서드에 주석으로 작성.

### 12/06
* PhotoBoardUpdate 수정.
* DB 설계는 변경하지 않는 상태에서 코드만 수정해 구현하는것을 목표로 진행.
* PhotoBoardServiceImpl에 주석으로 설명 작성.
* form으로 데이터 전송하는 방법 그대로 유지.
* 삭제 이미지명은 input으로 생성해 넘겨주서 삭제처리할 수 있도록 구현하고 기존 이미지와 새로 등록한 이미지들도 input으로 생성해 넘겨줘서 처리.
* 새로 등록한 이미지 파일 저장 및 DB에 넣어주는것은 기존 코드 유지.
* 사진게시판 무한스크롤 수정 완료.
* 무한스크롤에서는 쿼리문에 LIMIT을 통해 데이터를 가져오도록 구현했었는데 시작점없이 LIMIT 12; 형태로만 되어 있었기 때문에 중복되서 출력되었던 문제.
* 쿼리문에서 LIMIT #{idx}, 12 형태로 바꿔줌으로써 idx값을 넘겨 처리해주도록 구현.
* List 페이지의 컨트롤러에서도 같은 쿼리문을 사용하기 때문에 해당 컨트롤러에서는 idx를 0으로 만들어 넘겨주도록 수정하고
* 스크롤이 내려갈때마다 실행될 때 i라는 값을 하나씩 증가시켜 데이터가 추가되는 시점에 i를 컨트롤러로 넘겨 * 12를 해주는 방법으로 수정해서 구현.
* 이 리펙토링을 마지막으로 기존 담당파트 리펙토링은 마무리.

### 22/04/05
* 계층형 쿼리 재귀쿼리로 수정.
* 재귀쿼리로 변경하면서 DB 컬럼 및 insert, replyInsert 기능 수정.
* askBoardList에서 title 출력부분 수정.
* askBoardList와 askSearchList 메소드 합쳐서 처리하고 view도 askBoardList.html 하나만 사용하도록 수정.
* 비로그인 회원 글 접근시에 askBoardDetailNonMem으로 접근하던것 askBoardDetail로 접근하도록 수정.
* 동일하게 photoBoardDetailNonMem도 photoBoardDetail로 접근하도록 수정.