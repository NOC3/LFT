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

    void prog(){
        stat();
        match(Tag.EOF);
    }

    void statlist(){
        stat();
        statlistp();
    }

    void statlistp(){
        //gurado i first stat =={"("}
        if(look.tag == '('){
            stat();
            statlistp();
        }else{

        }
    }

    void stat(){
        match('(');
        statp();
        match(')');
    }

    void statp(){
        switch(look.tag){
            case '=':
                match('=');
                match(Tag.ID);
                expr();
            break;
            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat();
                elseopt();
            break;
            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat();
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist();
            break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
            break;
            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
            break;
            default:
                error("Syntax error in statp");
        }
    }

    void elseopt(){
        //guardo i first di elseopt == {"("}
        if(look.tag=='('){
            match('(');
            match(Tag.ELSE);
            stat();
            match(')');
        }else{

        }
        
    }

    void bexpr(){
        match('(');
        bexprp();
        match(')');
    }

    void bexprp(){
        match(Tag.RELOP);
        expr();
        expr();
    }

    void expr(){
        switch(look.tag){
            case Tag.NUM:
                match(Tag.NUM);
            break;
            case Tag.ID:
                match(Tag.ID);
            break;
            case '(':
                match('(');
                exprp();
                match(')');
            break;
            default:
                error("Syntax error in expr");
        }
    }

    void exprp(){
        switch(look.tag){
            case '+':
                match('+');
                exprlist();
            break;
            case '-':
                match('-');
                expr();
                expr();
            break;
            case '*':
                match('*');
                exprlist();
            break;
            case '/':
                match('/');
                expr();
                expr();
            break;
            default:
                error("Syntax error in exprp");
        }
    }
    
    void exprlist(){
        expr();
        exprlistp();
    }

    void exprlistp(){
        switch(look.tag){
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                exprlistp();
            break;
            default:
            break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = ""; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}