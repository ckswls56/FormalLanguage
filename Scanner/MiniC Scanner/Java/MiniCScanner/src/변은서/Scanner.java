import java.io.*;

public class Scanner {

    private boolean isEof = false;
    private char ch = ' '; 
    private BufferedReader input;	
    private String line = "";		//하나의 라인 정보를 저장할 변수
    private static int lineno = 0;			//라인 number을 저장할 변수
    private static int col = 1;			//하나의 라인에서 몇 번째 문자까지 읽었는지 저장할 변수
    private static int startCol = 1;
    private final String letters = "abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";	//문자 정보
    private final String digits = "0123456789";	//숫자 정보
    private final char eolnCh = '\n';	//end of line 개행문자
    private final char eofCh = '\004';	//end of file EOF
    

    public Scanner (String fileName) { // source filename
    	System.out.println("Begin scanning... programs/" + fileName + "\n");
        try {
            input = new BufferedReader (new FileReader(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }

    private char nextChar() { // Return next char
        if (ch == eofCh)
            error("Attempt to read past end of file");
            System.exit(1);
        }
        col++;	//col 1증가
        if (col >= line.length()) {	//col이 line의 길이보다 클 경우
            try {
                line = input.readLine( );//	다음 라인 읽어옴
            } catch (IOException e) {	//IO예외처리
                System.err.println(e);	//에러 메세지 출력
                System.exit(1);			//종료
            } // try
            if (line == null) // at end of file (라인이 비어있을 경우)
                line = "" + eofCh;//개행문자 넣어줌
            else {
                // System.out.println(lineno + ":\t" + line);
                lineno++; //라인 넘버 ++;
                line += eolnCh;	//라인 뒤에 개행 문자 넣어줌
            } // if line
            col = 0;//라인이라면 col을 0으로 바꿔줌
        } // if col
        return line.charAt(col);
    }
            

    public Token next( ) { // Return next token
    	//객체 생성 후 ch의 초기 값은 공백으로 설정되어있으므로 처음에는 무조건 nextChar실행
    	startCol=col+1;
    	do {
            if (isLetter(ch) || ch == '_') { // ident or keyword
                String spelling = concat(letters + digits + '_');	//문자, 숫자, 언더바
                return Token.keyword(spelling);
            } else if (isDigit(ch)) { // int literal
            	String number ="";
            	if(ch=='0'){
            		number+=ch;
            		ch=nextChar();
            		if(ch=='x'||ch=='X'){
            			number+=ch;
            			ch=nextChar();
            			number+=concat(digits+"ABCDEFabcdef");
            		}
            		else{
            			if(isDigit(ch)){
            				number+=concat("01234567");
            			}
            		}
            		return Token.mkIntLiteral(number);
            	}
                number = concat(digits);
                if(ch!='.')
                	return Token.mkIntLiteral(number);
                String number2 = concat(digits);
                if(ch=='e'){
                	String number3="e";
                	ch=nextChar();
                	if(ch=='+'||ch=='-'){
                		number3+=ch;
                		ch=nextChar();
                	}
                	if(isDigit(ch)){
                		number3+=concat(digits);
                		return Token.mkDoubleLiteral(number+number2+number3);
                	}
                	
                }
                return Token.mkDoubleLiteral(number+number2);
            } else switch (ch) {
            case ' ': case '\t': case '\r': case eolnCh:
                ch = nextChar();	//공백, 탭, 캐리지 리턴(커서를 맨 앞으로 옮겨줌), 개행문자 일 경우 nextChar실행
                startCol=col+1;
                break;
            
            case '/':  // divide or divAssign or comment
                ch = nextChar();
                if (ch == '=')  { // divAssign
                	ch = nextChar();
                	return Token.divAssignTok;
                }
                
                // divide
                if (ch != '*' && ch != '/') return Token.divideTok;
                
                // Block line comment
                if (ch == '*') { 
                	ch = nextChar();
                	if(ch == '*'){
                		ch = nextChar();
                	}
                	String str="";
                	do {
						while (ch != '*') {
							str+=ch;
							ch = nextChar();
						}
						ch = nextChar();
						if(ch!='/')
							str+="*";
					} while (ch != '/');
					ch = nextChar();
					System.out.print("Documented Comments -----> ");
					System.out.println(str);
                	
                }
                // single line comment
                else if (ch == '/')  {
                	ch = nextChar();
                	if(ch=='/'){
		                ch=nextChar();
	                }
                	String str="";
                	while (ch != eolnCh) {
                		str += ch;
                		ch = nextChar();
	                } 
                	System.out.print("Single Line Documented Comments -----> ");
                	System.out.println(str);
	                ch = nextChar();
                }
                
                break;
                
            case '\'':  // char literal
                char ch1 = nextChar();
                String c ="";
                
                if(ch1 == '\\'){
                	c+=ch1;
                	ch1 = nextChar(); // escape문자
                }
                if(nextChar() == '\''){ // '' -> 새로운 문자 읽기
                	c+=ch1;
                	ch = nextChar();
                    return Token.mkCharacterLiteral(c);
                };
                ch = nextChar();
                return Token.mkCharacterLiteral(c);
            
            case '\"':  // string literal
            	String str ="";
            	ch = nextChar();
            	while (ch != '\"') {	// 마지막은 "
            		if(ch=='\\'){
            			str+=ch;
            			ch=nextChar();
            			str+=ch;
            			ch=nextChar();
            		}
            		else{
            			str+=ch;
            			ch = nextChar();
            		}
            	}
	            ch = nextChar();
	            return Token.mkStringLiteral(str);
                
            case '.':
            	String number = concat(digits);
            	if(ch=='e'){
                	String number3="e";
                	ch=nextChar();
                	if(ch=='+'||ch=='-'){
                		number3+=ch;
                		ch=nextChar();
                	}
                	if(isDigit(ch)){
                		number3+=concat(digits);
                		return Token.mkDoubleLiteral(number+number3);
                	}
                	
                }
            	return Token.mkDoubleLiteral(number);
                
            case eofCh: return Token.eofTok;
            
            case '+': 
            	ch = nextChar();
	            if (ch == '=')  { // addAssign
	            	ch = nextChar();
	            	return Token.addAssignTok;
	            }
	            else if (ch == '+')  { // increment
	            	ch = nextChar();
	            	return Token.incrementTok;
	            }
                return Token.plusTok;

            case '-': 
            	ch = nextChar();
                if (ch == '=')  { // subAssign
                	ch = nextChar();
                	return Token.subAssignTok;
                }
	            else if (ch == '-')  { // decrement
	            	ch = nextChar();
	            	return Token.decrementTok;
	            }
                return Token.minusTok;

            case '*': 
            	ch = nextChar();
                if (ch == '=')  { // multAssign
                	ch = nextChar();
                	return Token.multAssignTok;
                }
                return Token.multiplyTok;

            case '%': 
            	ch = nextChar();
                if (ch == '=')  { // remAssign
                	ch = nextChar();
                	return Token.remAssignTok;
                }
                return Token.reminderTok;

            case '(': ch = nextChar();
            return Token.leftParenTok;

            case ')': ch = nextChar();
            return Token.rightParenTok;

            case '{': ch = nextChar();
            return Token.leftBraceTok;

            case '}': ch = nextChar();
            return Token.rightBraceTok;
            
            case '[': ch = nextChar();		//bubble.mc 실행시 오류나서 추가
            return Token.leftBracketTok;

            case ']': ch = nextChar();		//bubble.mc 실행시 오류나서 추가
            return Token.rightBracketTok;

            case ';': ch = nextChar();
            return Token.semicolonTok;
            
            case ':': ch = nextChar();		//colon 인식 추가
            return Token.colonTok;

            case ',': ch = nextChar();
            return Token.commaTok;
                
            case '&': check('&'); return Token.andTok;
            case '|': check('|'); return Token.orTok;

            case '=':
                return chkOpt('=', Token.assignTok,
                                   Token.eqeqTok);

            case '<':
                return chkOpt('=', Token.ltTok,
                                   Token.lteqTok);
            case '>': 
                return chkOpt('=', Token.gtTok,
                                   Token.gteqTok);
            case '!':
                return chkOpt('=', Token.notTok,
                                   Token.noteqTok);

            default:  
            	error("Illegal character " + ch);
            	ch=nextChar();
            } // switch
        } while (true);
    } // next


    private boolean isLetter(char c) {
        return (c>='a' && c<='z' || c>='A' && c<='Z');
    }
  
    private boolean isDigit(char c) {
        return (c>='0' && c<='9');
    }

    private void check(char c) {
        ch = nextChar();
        if (ch != c) 
            error("Illegal character, expecting " + c);
        ch = nextChar();
    }

    private Token chkOpt(char c, Token one, Token two) {
        ch = nextChar();
        if (ch != c)
            return one;
        ch = nextChar();
        return two;
    }

    private String concat(String set) {
        String r = "";
        do {
            r += ch;
            ch = nextChar();
        } while (set.indexOf(ch) >= 0);
        return r;
    }

    public void error (String msg) {
        System.err.print(line);
        System.err.println("Error: column " + col + " " + msg);
        //System.exit(1);
    }

    static public void main ( String[] argv ) {	
        Scanner lexer = new Scanner(argv[0]);	
        Token tok = lexer.next( );				
        String result;							
        while (tok != Token.eofTok) {			
        	result = "Token -----> "+ tok.value() + " (" + tok.getTokenNumber() + ", " + tok.getValue() + ", " + argv[0]+", " + lineno + ", " + startCol + ")";
        	System.out.println(result);
            tok = lexer.next( );				
        } 
    }
}
