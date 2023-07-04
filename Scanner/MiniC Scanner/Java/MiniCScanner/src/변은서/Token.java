
public class Token {

	//ordinal은 enum의 정의된 순서 값(=enum 상수 값, TokenType에서 따로 상수를 정해주지 않고 열거만 해두었기 때문)을 반환하는 메소드
    private static final int KEYWORDS = TokenType.Eof.ordinal();	//키워드 갯수 (모든 키워드는 TokenType에서 Eof 앞에 있어야함)

    private static final String[] reserved = new String[KEYWORDS];	//String 배열 생성 (size = 키워드 갯수)
    private static Token[] token = new Token[KEYWORDS];				//Token 배열 생성 (size = 키워드 갯수)

    public static final Token eofTok = new Token(TokenType.Eof, "<<EOF>>");
    public static final Token constTok = new Token(TokenType.Const, "const");
    public static final Token returnTok = new Token(TokenType.Return, "return");
    public static final Token voidTok = new Token(TokenType.Void, "void");
    public static final Token elseTok = new Token(TokenType.Else, "else");
    public static final Token ifTok = new Token(TokenType.If, "if");
    public static final Token intTok = new Token(TokenType.Int, "int");
    public static final Token whileTok = new Token(TokenType.While, "while");
    public static final Token leftBraceTok = new Token(TokenType.LeftBrace, "{");
    public static final Token rightBraceTok = new Token(TokenType.RightBrace, "}");
    public static final Token leftBracketTok = new Token(TokenType.LeftBracket, "[");
    public static final Token rightBracketTok = new Token(TokenType.RightBracket, "]");
    public static final Token leftParenTok = new Token(TokenType.LeftParen, "(");
    public static final Token rightParenTok = new Token(TokenType.RightParen, ")");
    public static final Token semicolonTok = new Token(TokenType.Semicolon, ";");
    public static final Token commaTok = new Token(TokenType.Comma, ",");
    public static final Token assignTok = new Token(TokenType.Assign, "=");
    public static final Token eqeqTok = new Token(TokenType.Equals, "==");
    public static final Token ltTok = new Token(TokenType.Less, "<");
    public static final Token lteqTok = new Token(TokenType.LessEqual, "<=");
    public static final Token gtTok = new Token(TokenType.Greater, ">");
    public static final Token gteqTok = new Token(TokenType.GreaterEqual, ">=");
    public static final Token notTok = new Token(TokenType.Not, "!");
    public static final Token noteqTok = new Token(TokenType.NotEqual, "!=");
    public static final Token plusTok = new Token(TokenType.Plus, "+");
    public static final Token minusTok = new Token(TokenType.Minus, "-");
    public static final Token multiplyTok = new Token(TokenType.Multiply, "*");
    public static final Token divideTok = new Token(TokenType.Divide, "/");
    public static final Token reminderTok = new Token(TokenType.Reminder, "%");
    public static final Token addAssignTok = new Token(TokenType.AddAssign, "+=");
    public static final Token subAssignTok = new Token(TokenType.SubAssign, "-=");
    public static final Token multAssignTok = new Token(TokenType.MultAssign, "*=");
    public static final Token divAssignTok = new Token(TokenType.DivAssign, "/=");
    public static final Token remAssignTok = new Token(TokenType.RemAssign, "%=");
    public static final Token incrementTok = new Token(TokenType.Increment, "++");
    public static final Token decrementTok = new Token(TokenType.Decrement, "--");
    public static final Token andTok = new Token(TokenType.And, "&&");
    public static final Token orTok = new Token(TokenType.Or, "||");
    
    //1. 추가 키워드
    public static final Token charTok = new Token(TokenType.Char, "char");
    public static final Token doubleTok = new Token(TokenType.Double, "double");
    public static final Token forTok = new Token(TokenType.For, "for");
    public static final Token doTok = new Token(TokenType.Do, "do");
    public static final Token gotoTok = new Token(TokenType.Goto, "goto");
    public static final Token switchTok = new Token(TokenType.Switch, "switch");
    public static final Token caseTok = new Token(TokenType.Case, "case");
    public static final Token breakTok = new Token(TokenType.Break, "break");
    public static final Token defaultTok = new Token(TokenType.Default, "default");
    
    //2. 추가 연산자
    public static final Token colonTok = new Token(TokenType.Colon, ":");
    

    private TokenType type;
    private String value = "";

    private Token (TokenType t, String v) {
        type = t; 	//객체 type 초기화
        value = v;	//객체 value 초기화
        if (t.compareTo(TokenType.Eof) < 0) {
        	/*	
        	 * 
        	 * The compareTo() method returns:
        	 * negativeinteger, if this enum is less than the defined object. t가 eof보다 작을 때 (키워드일 때)
        	 * zero, if this enum is equal to the defined object. t가 eof일 때
        	 * positive integer, if this enum is greater than the defined object. t가 eof보다 클 때
        	 *
        	 */
        	
            int ti = t.ordinal();	//토큰의 정의된 순서(=enum 상수 값)
            reserved[ti] = v;		//reserved의 ti번째에 token string 저장 (키워드 string 저장하는 배열)
            token[ti] = this;		//token의 ti번째에 자기 자신 객체 저장 (키워드 token 저장하는 배열)
        }
    }

    public TokenType type( ) { return type; }

    public String value( ) { return value; }

    public static Token keyword  ( String name ) {
        char ch = name.charAt(0);	//name의 첫 글자
        if (ch >= 'A' && ch <= 'Z') return mkIdentTok(name);	//첫 글자가 A~Z에 해당하면 mkIdentTok 실행후 반환 값 리턴
        for (int i = 0; i < KEYWORDS; i++)	//A~Z에 해당하지 않으면 name이 keyword인지 확인, keyword이면 해당 토큰 반환
           if (name.equals(reserved[i]))  return token[i];	//reserved는 keyword의 string 정보가 담긴 배열
        return mkIdentTok(name); //keyword 아니면 mkIdentTok 실행 후 반환 값 리턴
    }

    public static Token mkIdentTok (String name) {
        return new Token(TokenType.Identifier, name); //인자 값으로 새로운 토큰(Identifier) 생성
    }

    public static Token mkIntLiteral (String name) {
        return new Token(TokenType.IntLiteral, name);	//인자 값으로 새로운 토큰(IntLiteral) 생성
    }
    
    public static Token mkCharacterLiteral (String name) {
        return new Token(TokenType.CharacterLiteral, name);	//인자 값으로 새로운 토큰(CharacterLiteral) 생성
    }
    public static Token mkStringLiteral (String name) {
        return new Token(TokenType.StringLiteral, name);	//인자 값으로 새로운 토큰(StringLiteral) 생성
    }
    public static Token mkDoubleLiteral (String name) {
        return new Token(TokenType.DoubleLiteral, name);	//인자 값으로 새로운 토큰(DoubleLiteral) 생성
    }
    public String toString ( ) {
        if (type.compareTo(TokenType.Identifier) < 0) return value;
        return type + "\t" + value;
    } // toString
    
    public String getValue ( ) {
        if (type.compareTo(TokenType.Identifier) < 0) return "0";
        return value;
    }
    
    public int getTokenNumber(){
    	return type.ordinal();
    }
    
    public static void main (String[] args) {
    	// test
        System.out.println(eofTok);
        System.out.println(whileTok);
    }
} // Token
