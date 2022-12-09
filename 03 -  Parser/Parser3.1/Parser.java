import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
	} else error("syntax error");
    }

    public void start() {
        System.out.println("start");
	    do{
            expr();
        }while(look.tag!=Tag.EOF);
	    match(Tag.EOF);
    }

    private void expr() {
        System.out.println("expr");
        term();
        exprp();
    }

    private void exprp() {
        System.out.println("exprP");
	switch (look.tag) {
        case '+':
            move();
            term();
            exprp();
        break;
        case '-':
            move();
            term();
            exprp();
        break;            
        }

    }

    private void term() {
        System.out.println("term");
        fact();
        termp();
    }

    private void termp() {
        System.out.println("termP");
        switch(look.tag){
            case '*':
                move();
                fact();
                termp();
            break;
            case '/':
                move();
                fact();
                termp();
            break;
        }
    }

    private void fact() {
        System.out.println("fact");
            switch(look.tag){
               case '(':
                    move();
                    expr();
                    if(look.tag==')'){
                        move();
                    }else{
                        error("syntax error");
                    }
                break;
                case 256:
                    move();
                break;
                default:
                    error("Syntax error in fact");
                break;
            }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "./Testo.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}