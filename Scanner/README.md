# Formal Languages

CSE4031 Formal Languages, Undergraduate School

Home works and Lab. Materials

1.  추가 키워드: char, double, for, do, goto, switch, case, break, default

2.  추가 연산자: ':'

3.  추가 인식 리터럴

1)  character literal

2)  string literal

3)  double literal ( .123, 123. 과 같은 숏폼 인식)

4.  문서화 주석

1)  documented(/\*_ ~ _/) comments

2)  single line documented(///) comments

5.  추가 토큰 속성값 출력: file name, line number, column number

- 각 리터럴은 4장 슬라이드의 오토마타를 기준으로 인식할 것.

- 예제 코드 실험시, 구현한 스캐너가 인식할 수 있는 토큰 구조에 해당하지 않는 경우에는 에러 메시지 출력 후 다음 토큰 인식 시도.

- 출력 형식은 다음과 같음.

Token -----> int (token number, token value, file name, line number, column number)

Token -----> main (token number, token value, file name, line number, column number)

...

Documented Comments ------> comment contents

...
