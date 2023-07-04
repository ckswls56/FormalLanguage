import java.io.*;

public class Scanner {

    private boolean isEof = false;
    private char ch = ' '; 
    private BufferedReader input;	
    private String line = "";		//�ϳ��� ���� ������ ������ ����
    private static int lineno = 0;			//���� number�� ������ ����
    private static int col = 1;			//�ϳ��� ���ο��� �� ��° ���ڱ��� �о����� ������ ����
    private static int startCol = 1;
    private final String letters = "abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";	//���� ����
    private final String digits = "0123456789";	//���� ����
    private final char eolnCh = '\n';	//end of line ���๮��
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
        col++;	//col 1����
        if (col >= line.length()) {	//col�� line�� ���̺��� Ŭ ���
            try {
                line = input.readLine( );//	���� ���� �о��
            } catch (IOException e) {	//IO����ó��
                System.err.println(e);	//���� �޼��� ���
                System.exit(1);			//����
            } // try
            if (line == null) // at end of file (������ ������� ���)
                line = "" + eofCh;//���๮�� �־���
            else {
                // System.out.println(lineno + ":\t" + line);
                lineno++; //���� �ѹ� ++;
                line += eolnCh;	//���� �ڿ� ���� ���� �־���
            } // if line
            col = 0;//�����̶�� col�� 0���� �ٲ���
        } // if col
        return line.charAt(col);
    }
            

    public Token next( ) { // Return next token
    	//��ü ���� �� ch�� �ʱ� ���� �������� �����Ǿ������Ƿ� ó������ ������ nextChar����
    	startCol=col+1;
    	do {
            if (isLetter(ch) || ch == '_') { // ident or keyword
                String spelling = concat(letters + digits + '_');	//����, ����, �����
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
                ch = nextChar();	//����, ��, ĳ���� ����(Ŀ���� �� ������ �Ű���), ���๮�� �� ��� nextChar����
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
                	ch1 = nextChar(); // escape����
                }
                if(nextChar() == '\''){ // '' -> ���ο� ���� �б�
                	c+=ch1;
                	ch = nextChar();
                    return Token.mkCharacterLiteral(c);
                };
                ch = nextChar();
                return Token.mkCharacterLiteral(c);
            
            case '\"':  // string literal
            	String str ="";
            	ch = nextChar();
            	while (ch != '\"') {	// �������� "
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
            
            case '[': ch = nextChar();		//bubble.mc ����� �������� �߰�
            return Token.leftBracketTok;

            case ']': ch = nextChar();		//bubble.mc ����� �������� �߰�
            return Token.rightBracketTok;

            case ';': ch = nextChar();
            return Token.semicolonTok;
            
            case ':': ch = nextChar();		//colon �ν� �߰�
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
