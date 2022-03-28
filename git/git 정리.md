## 분산 버전 관리 시스템

버전 관리란 게임에서의 세이브포인트 같은 느낌이다. 

버전 관리 없이 개발한다는 것은 세이브 없이 게임을 하는 것과 마찬가지일 것이다. 

만약 두 명의 개발자가 하나의 서비스를 만드는 데 버전 관리가 없다면 이메일로 보내서 합친다거나 백업본을 만들어둬야 한다 거나 매우 귀찮게 될 것이다.  


## 버전 관리 시스템 : Git  

개발을 할 때 원하는 시점마다 깃발을 꽂고(버전을 만들고) 이들 간에 자유롭게 돌아다닐 수 있다. 

내가 만든 버전 뿐만 아니라 동료가 만든 버전으로 이동할 수 있고, 동료와 내 버전을 비교해서 최신본으로 코드를 업데이트를 할 수 있다. 

Git은 CLI, CUI 두가지 방법으로 사용 가능하다.



## Git, Git Bash

![image-20220328222437622](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pwz2ufgaj20nt0hhq52.jpg)

터미널에서 git을 입력시에 위와 같은 커맨드가 나와야 한다. 나오지 않는 다면 git을 설치해줘야 한다.

### git init

```bash
git init
```

Git으로 버전관리를 하고 싶다면 git init을 통해 `.git` 폴더 즉, 로컬저장소를 만드는 것부터 시작한다.

`.git`폴더는 로컬 저장소에 내가 만든 버전 정보, 원격 저장소 주소 등이 저장된다. 

원격 저장소에서 내 컴퓨터로 코드를 받아오면 로컬 저장소가 자동으로 생기게 된다. 

![image-20220328223400575](https://tva1.sinaimg.cn/large/e6c9d24egy1h0px8ulfcaj20nt0hh0vu.jpg)

Boxiting-cat이라는 폴더를 만들고 그 폴더안에서 `git init`을 통해  로컬저장소를 만들어 준다. 

 `.git`이라는 폴더가 생성된 것을 확인할 수 있다. (폴더가 보이지는 않는다.)

### 첫번째 버전 만들기 (git commit)

#### Commit이란?

하나의 버전이라고 생각하면 된다.

만약 페이지1,2,3을 커밋했다면 페이지 1,2,3 하나의 덩어리인 버전이 만들어 지는 것이다.  **(버전1)**

여기서 유의해야 할점이 위의 버전에서 페이지2를 수정하고 커밋했다고 페이지2만 덩어리로 만들어지는 것이 아닌 페이지 1,2(수정본),3 모두가 덩어리로 버전이 만들어 진다. **(버전2)**

만약 버전2가 이상하다면 버전1로 이동하여 다시 시작할 수 있다. 

즉, 커밋이란 변경 사항의 모음이 아닌 하나의 최종 코드 모음이라고 생각하면 된다. 

#### 커밋으로 만들길 원하는 파일만 선택 : add 

만약 페이지1,2,3을 작성한 뒤 페이지 1,2만 add를 할 경우 페이지 1,2라는 버전이 생성된다. 

아까 생성했던 Boxiting-cat 폴더에 README.md, index.html 파일을 생성했다고 가정하자. 

```bash
git add README.md 
git commit -m "프로젝트 설명 파일 추가"
git log 
```

![image-20220328225040873](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pxq714a7j20nt0hhq4c.jpg)

커밋을 하게 되면 `771e12e`라는 이름으로 커밋이 하나 생성된 것이다. 

로그를 보고 싶다면 `git log`를 통해 볼 수 있다.

![image-20220328225127628](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pxr00x5vj20nt0hhdgi.jpg)

#### 커밋에 대해... 

커밋은 **의미 있는 변동사항**을 묶어서 만들어야 한다. 

만약 버튼 클릭 버그를 고치는 데 5가지 파일을 수정했다면 그 5가지를 묶어서 하나의 커밋으로 만들어줘야 한다.

그래야지 동료 개발자나 미래의 내가 버튼 클릭 버그를 고치는 데 어떤 파일을 수정했는 지 손쉽게 파악이 가능하다. 

또한, 커밋 메시지는 되도록 상세하게 적어줘야 한다. 커밋 메시지 관련은 밑의 글을 참고하면 좋다. 

- [좋은 git 커밋 메시지를 작성하기 위한 8가지 약속](https://djkeh.github.io/articles/How-to-write-a-git-commit-message-kor/)
- [커밋 메시지 가이드](https://github.com/RomuloOliveira/commit-messages-guide/blob/master/README_ko-KR.md)



### Github 연동 (git remote, push)

#### 로컬 저장소와 원격 저장소 

- 로컬 저장소 : 내 컴퓨터 즉, 로컬에 있는 저장소 
- 원격 저장소 : 로컬이 아닌 Github와 같이 원격 저장소 전용 서버이며 여러 사람이 함께 공유하기 위한 저장소이다.



#### Github에서 만들고 커밋 푸시하기 

깃허브에 레파지토리를 하나 생성하자. 생성한 뒤 페이지를 오면 이런 커맨드가 있을 것이다.

```bash
echo "# Boxiting" >> README.md
git init
git add README.md # 앞에서 여기까지 진행
git commit -m "first commit" 
git branch -M main
git remote add origin https://github.com/yuminhwan/Boxiting.git
git push -u origin main
```

여기서 `remote add origin`의 경우 그냥 remote를 origin이라는 이름으로 추가한 것이다. 고로, origin말고도 다른 이름으로 대체가 가능하다. 

remote를 지정해주고 push 하게 되면 내가 만든 커밋이 Github에 올라간 것을 확인할 수 있다.

![image-20220328231127666](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pybzdlrdj20nt0hhmyx.jpg)

![image-20220328231228039](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pycuyei1j20ih0a83z7.jpg)



### 다른 사람이 만든 저장소 받아오기 (git clone, pull)

원격 저장소에 A가 커밋을 올리고 B가 이 저장소를 자신의 컴퓨터에 받아오고 싶어한다면 먼저 `clone`을 해줘야 한다. clone을 하게 되면 원격 저장소의 코드를 내 컴퓨터에 받아올 수 있다. (로컬 저장소 즉, .git폴더도 자동으로 생성된다.)

그러던 중 A가 새로운 버전을 만들어 원격 저장소에 push하였을 때 B가 이 업데이트된 데이터를 가져오고 싶다면 `pull`로 받아올 수 가 있다. 물론 B도 커밋을 만들어 원격 저장소에 push할 수 있다. (원격 저장소에 푸시 권한이 있을 때만 가능)

위의 상황을 재현하기 위해 Boxiting-oct라는 폴더를 하나 만들고 cat폴더는 A, oct폴더는 B라고 생각하자. 

![image-20220328232324211](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pyo9dpj4j20nt0hht9z.jpg)

`clone`을 통해 원격 저장소를 로컬 저장소에 가져온 모습이다. 여기서 `git clone 주소` 이후 `.`을 입력해주지 않으면 새 폴더를 생성하여 clone하게 된다. 입력해주면 현재 폴더에 내려받게 된다.  파일을 확인해보면 cat에서 올렸던 README.md만 있는 것을 확인할 수 있다. 



이후 app.js파일을 생성하고 push를 하게 되면 Github에 올라간 것을 확인할 수 있다.

![image-20220328232850521](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pytwhgp8j20nt0hhjtn.jpg)

![image-20220328233007815](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pyv8h3g4j20i203zmx8.jpg)



#### 원격 저장소의 변경사항 내 컴퓨터에 받아오기 

이제 cat 컴퓨터에서 oct가 올린 커밋을 받아와 보자 

![image-20220328233133385](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pywq2uvqj20nt0hhtar.jpg)





pull을 하면 하기 전에 없었던 app.js을 확인할 수 있다.



## GUI (SourceTree)

버튼 클릭으로 위에서 알아본 Git명령을 실행할 수 있는 도구이다.

![image-20220328233536736](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pz0y6snlj20wa0i9tai.jpg)

아까 만들었던 저장소를 연결해보면 커밋을 한 눈에 볼 수 있다.



커맨드로 살펴보았던 add, commit, pull을 GUI로 해보자 

### GUI에서의 add, commit,pull 

cat폴더의 README.md를 수정한 뒤 GUI를 보면 스테이지에 올라가지 않은 파일이 있을 것이다. 스테이지는 add를 하게 해주면 올라가는 곳이라고 생각하면 된다. 

GUI에서 add는 그저 옆에 박스를 체크해주면 된다.

![image-20220328234452413](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pzal6n95j20wa0i9jt3.jpg)

![image-20220328234500273](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pzaphkcmj20wa0i9dhc.jpg)



이후 커밋해주기위해 커밋버튼을 누르고 커밋을 하게 되면 커밋이 된 것을 확인 할 수 있다.

![image-20220328234609930](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pzbx2upyj20wa0i90u1.jpg)

![image-20220328234622610](https://tva1.sinaimg.cn/large/e6c9d24egy1h0pzc50gmpj20wa0i976b.jpg)

상태 창을 보면 main과 origin/main이 있다. 그냥 main의 경우 로컬저장소의 main이고 origin/main은 원격저장소의 main이라고 생각하면 된다. 

![image-20220329001507959](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q062pf1nj20wa0i9q4z.jpg)

푸쉬하게 되면 main과 origin/main이 같이 있는 것을 확인할 수 있다. (같은 버전이니깐!)

### Branch 

만약 커밋을 한 줄로 쌓으면 어떻게 될 까? A, B 서로의 커밋이 구분이 가지않아 불편하고 충돌이 일어날 수 있다.

이럴때 Branch를 사용하면 된다. branch를 사용하면 충돌이 나더라도 합치는 시점에 명시적으로 충돌을 해결할 수 있다.

우리는 이미 branch를 하나 가지고 있다. 앞써 나왔던 main이다.  이미 main이라는 branch를 가지고 있고 해당 branch에서 여러 작업을 한 것이다. 

```bash
git branch branch명  # 브렌치 추가 
git switch branch명  # 해당 브렌치로 이동
```



![image-20220329005857134](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q1fnp5vyj20nt0hhjsc.jpg)

cat이라는 branch를 만들고 옮김으로써 우리는 HEAD가 cat을 바라보도록 만들어주었다. 만약 cat 브랜치에서 커밋을 추가하게 된다면 main 브랜치는 아직 과거 커밋을, cat 브랜치는 새 커밋을 가르키게 될 것이다.



![image-20220329010454587](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q1lv8waij20gg0c3t91.jpg)

![image-20220329010859361](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q1q3prc5j20wa0i9mzi.jpg)

로컬저장소에 `feat/main-page`를 만들고 푸쉬함으로써 원격저장소에도 `orgin/feat/main-page`가 생성된 것을 확인할 수 있다.



![image-20220329011435355](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q1vxg7d9j20wa0i9dhz.jpg)

이후 oct 폴더에서 확인하면 `orgin/feat/main-page`가 가지가 갈라지듯 갈라지는 것을 확인할 수 있다.  여기에 `feat/comment` 브렌치를 만들고 파일 추가 후 커밋하게 되면 위와 같은 모습으로 생성된다.



### Merge 

main 브랜치의 최신 커밋(base)에 oct 브랜치(compare)의 최신 커밋을 합치려고 한다면 base인 main브랜치로 이동한 후 compare 브랜치인 oct와 merge 시켜주면 된다.

```bash
git merge branch명 
```

그렇게 되면 oct와 main 브랜치 모두 같은 커밋을 가르키게 된다.



![image-20220329012149197](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q23gm9c4j20t30ezq51.jpg)

![image-20220329012350643](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q25jvmfqj20wa0i9tb1.jpg)



### 컨플릭트(conflict)

머지할 때 두 버전이 같은 곳을 수정하게 되면 컨플릭트가 발생한다. 이 경우 수동으로 고쳐줘야 머지할 수 있게 된다.

만약 cat저장소에 feat/love라는 브랜치를 만들고 이 브랜치에는 '스파링 좋아요' 텍스트 추가했다고 하자. 이때 main으로 다시 돌아간 뒤 '스파링 싫어요'라는 텍스트를 추가하고 머지하게 되면 컨플릭트가 발생하게 된다.

![image-20220329013040778](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q2cnvggdj20st08ldgj.jpg)

![image-20220329013203122](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q2e3hwokj20wa0i940m.jpg)

![image-20220329013221718](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q2eexbe4j20v20h1q4c.jpg)



### 저장소 통째로 복제하기 : 포크(Fork)

푸쉬 권한이 없지만 푸쉬 하고 싶다면 ? 포크 기능을 사용하면 된다.

- 브랜치 
  - 하나의 원본저장소에서 분기를 나눈다. 
  - 하나의 원본 저장소에서 코드 커밋 이력을 편하게 볼 수 있다. 
  - 다수의 사용자가 다수의 브랜치를 만들면 관리 하기 힘들다. 
- 포크 
  - 여러 원격저장소를 만들어 분기를 나눈다.
  - 원본저장소에 영향을 미치지 않으므로 마음껏 코드를 수정할 수 있다. 
  - 원본저장소의 이력을 보려면 따로 주소를 추가해줘야 한다.

( 포크의 경우 실습 진행이 불가능..... 아이디가 하나 더 있어야 할 것 같다...)



### 내 코드 머지해줘... : 풀 리퀘스트 

원본 저장소에 코드를 올릴 권한이 없으니 메시지를 보내는 것이다

코드를 함께 작성하는 팀원이 있다면, 최대한 직접 머지하는 것은 피하고 모든 머지를 풀 리퀘스트를 통해서 하는 것이 좋다. (코드 리뷰도 가능)



### Tip. 브랜치 관리하기 

1. 보통 `feat/기능이름`으로 한 사람이 개발하는 기능 브랜치를 만든다. ( 혹은 `fix/버그이름`, `hotfix/급한버그`)

2. 작업이 끝나면 `dev`(혹은 `main`)브랜치로 PR을 보낸다. 
3. `dev`브랜치에서 작업이 모두 머지되면 `release`(혹은 `latest`)브랜치로 머지시키고 이를 실서버에 배포한다. 
4. 직접 커밋은 `feat(혹은 fix, hotfix)`브랜치에만 한다. 
5. `dev나 master, release` 브랜치에는 직접 커밋하지 말고 머지만 한다.



## Git 정복하기 

### amend 

깜빡하고 수정 못한 파일이 있다면 방금 만든 커밋에 살짝 추가해준다.

![image-20220329014718091](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q2tyqxwaj20t70ewmyf.jpg)



### stash 

변경사함을 잠시 킵해두는 것이다. (커밋은 하지 않는다.)

![스크린샷 2022-03-29 오전 1.50.25](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q2xm0gx8j20sp0es3zm.jpg)

![image-20220329015145871](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q2yma210j20wa0m0416.jpg)

stash를 하면 지워졌다가 적용을 다시하면 다시 나타난다.



### reset

![image-20220329015523332](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q33zh8kej20sx0iqtbc.jpg)

옛날 커밋으로 브랜치를 돌릴 수 있다. 



- mixed reset 

  - 복권 사기 전 과거로 돌아갔지만 복권 번호는 기억남 

    ![image-20220329015643837](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q33s62kkj20wa0m0goe.jpg)

- hard reset 

  - 복권 사기 전 과거로 돌아갔는 데 복권 번호 기억까지 싹 다 날라감

    ![image-20220329015759251](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q3535kyoj20wa0m0ju8.jpg)



### revert 

reset보단 더욱 보수적이면서 커밋을 되돌리는 방법이다. 되돌리는 커밋을 만드는 것이다.

![image-20220329020112759](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q38ftgtkj20wa0m0jui.jpg)



### cherry-pick

커밋 하나만 떼서 원하는 브랜치에 붙이는 것이다.

![image-20220329020305478](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q3af3xqij20sz0ipq5m.jpg)

![image-20220329020400134](https://tva1.sinaimg.cn/large/e6c9d24egy1h0q3bcnhywj20wa0m00vq.jpg)