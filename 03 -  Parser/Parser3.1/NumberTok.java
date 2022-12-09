public class NumberTok extends Token {
	public String lexeme="";
	public String toString(){ return "< "+ tag +","+lexeme+" >";}
	public NumberTok(int tag,String s){super(tag);lexeme=s;}
}
