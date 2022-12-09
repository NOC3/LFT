import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
            if (look.tag != Tag.EOF) 
                move();
        } else error("syntax error: tag <" + look.tag + "> != <"+t+">");
    }

    public void prog() {
        if(look.tag == '('){
            int lnext_prog = code.newLabel();
            stat(lnext_prog); //inizio
            code.emitLabel(lnext_prog); 
            match(Tag.EOF); // fine 
            try {
                code.toJasmin(); //traduzione
            } catch (java.io.IOException e) {
                System.out.println("IO error\n");
            }
        }else{
            error("Error in prog");
        }
    }

    public void stat(int lnext_prog){
        match('(');
        statp(lnext_prog);
        match(')');
    }

    public void statlist(int lnext_prog) {
        int slnext = code.newLabel();
        stat(slnext);
        code.emitLabel(slnext);
        statlistp(lnext_prog);
    }

    void statlistp(int lnext_prog){
        System.out.println(look.toString());
        switch (look.tag) {
            case '(': 
                int s_next = code.newLabel();
                stat(s_next);
                code.emitLabel(s_next);
                statlistp(lnext_prog);
                break;
            case ')': 
                break;
            default:
                error("Error in statlistp");
        }
    }

    
    public void statp(int lnext) {
        int label_t,label_f;
        switch(look.tag) {

            case Tag.READ:
                match(Tag.READ);
                if (look.tag==Tag.ID) {
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                if (read_id_addr==-1) {
                    read_id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
                code.emit(OpCode.invokestatic,0);
                code.emit(OpCode.istore,read_id_addr);
                }else
                    error("Error in grammar (stat) after read with " + look);
            break;
        
            case Tag.PRINT :
                match(Tag.PRINT);
                exprlist(0);
            break;

            case '=':
                match('=');
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if(id_addr==-1){
                    id_addr=count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                expr();
                code.emit(OpCode.istore, id_addr);
            break;

            case Tag.COND:
                match(Tag.COND);
                label_t = code.newLabel();
                label_f = code.newLabel();
                bexpr(label_t,label_f);
                code.emitLabel(label_t);
                stat(lnext);
                //elseopt(lnext);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(label_f);
                elseopt(lnext);
            break;

            case Tag.WHILE:
                match(Tag.WHILE);
                int start = code.newLabel();
                code.emitLabel(start);
                label_f = lnext;
                label_t = code.newLabel();
                bexpr(label_t,label_f);
                code.emitLabel(label_t);
                stat(start);
                code.emit(OpCode.GOto, start);
                break;

            case Tag.DO:
                match(Tag.DO);
                statlist(lnext);
            break;

            default:
                error("Syntax error in statp");
        }
    }


    void elseopt(int lnext){
        switch (look.tag) { 
            case '(':
                match('(');
                match(Tag.ELSE);
                stat(lnext);
                match(')');
                break;
            case ')': // eps transizione
                break;
            default:
                error("Error in elseopt");
        }
    }

    void bexpr(int label_t,int label_f){
        match('(');
        bexprLogic(label_t, label_f);
        match(')');
    }

    void bexprLogic(int label_t,int label_f){
        switch(look.tag){
            case Tag.RELOP://caso come in precedenza
                bexprp(label_t,label_f);
            break;
                //casi con connettivi
            case 33: //'!'
                 match('!');
                match('(');
                bexprLogic(label_f, label_t);
                match(')');
            break;
            case Tag.AND:
                match(Tag.AND);
                match('(');//inizio primo operando
                int new_l = code.newLabel();
                bexprLogic(new_l, label_f);
                match(')');//fine primo operando
                code.emitLabel(new_l);//valuta seconda espressione
                match('(');//inizio secondo operando
                bexprLogic(label_t, label_f);
                match(')');//fine secondo operando
            break;
            case Tag.OR:
                match(Tag.OR);
                match('(');
                new_l = code.newLabel();
                bexprLogic(label_t, new_l);
                match(')');
                code.emitLabel(new_l);//valuta seconda espressione
                match('(');
                bexprLogic(label_t, label_f);
                match(')');
            break;
            default:
                error("Error in bexpr tag <" + look.tag + ">");
            break; 
        }
    }

    void bexprp(int label_t,int label_f){
        switch(((Word)look).lexeme){
            case "==":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpeq, label_t);
                code.emit(OpCode.GOto, label_f);
                break;
            case ">":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpgt, label_t);
                code.emit(OpCode.GOto, label_f);
                break;
            case "<":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmplt, label_t);
                code.emit(OpCode.GOto, label_f);
                break;
            case ">=":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpge, label_t);
                code.emit(OpCode.GOto, label_f);
                break;
            case "<=":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmple, label_t);
                code.emit(OpCode.GOto, label_f);
                break;
            case "<>": //!=
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpne, label_t);
                code.emit(OpCode.GOto, label_f);
                break;
            default:
                error("Error in bexprp");
        }
    }

    //0 -> print
    //1 -> iadd
    //2 -> imul 
    private void exprlist(int opNum){
        expr();
        if(opNum==0){ code.emit(OpCode.invokestatic, 1);}
        exprlistp(opNum);
    }


    void expr(){
        switch(look.tag){
            case Tag.NUM: //carico costante
                code.emit(OpCode.ldc, Integer.parseInt(((NumberTok)look).lexeme));
                match(Tag.NUM);

            break;
            case Tag.ID: //carico variabile
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    error("Error in expr, undeclared variable " + ((Word) look).lexeme);
                }
                match(Tag.ID);
                code.emit(OpCode.iload, id_addr );
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
                exprlist(1);
            break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
            break;
            case '*':
                match('*');
                exprlist(2);
            break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
            break;
            default:
                error("Syntax error in exprp");
        }
    }

    void exprlistp(int opNum){
        switch(look.tag){
            case Tag.NUM:
            case Tag.ID:
            case '('://riconosco first di expr()
                expr();
                switch(opNum){
                    case 0:
                        code.emit(OpCode.invokestatic, 1);
                    break;
                    case 1:
                        code.emit(OpCode.iadd);
                    break;
                    case 2:
                        code.emit(OpCode.imul);
                    break;
                    default:
                        error("opNum unknown (exprlistp)");
                }
                exprlistp(opNum);
            break;

            case ')': // eps transizione -> expr + ... + expr + eps
            break;  
            
            default:
                error("Error in exprlistp");
            break;
        }
    }




public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "ProvaLogici.lft"; //inserire il path del file
    try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Translator tr = new Translator(lex, br);
        tr.prog();
        System.out.println("\nFile Output.j generato!");
        System.out.println(
                "Digita 'java -jar jasmin.jar Output.j' per il file Output.class e 'java Output' per eseguirlo.\n");
        br.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

//*1 per la print, col fatto che expr non è NULL non posso printare una stringa vuota,
//  se volessi dovrei cambiare nella prodizione di print nel metodo statp() exprlistPrint con exprlistpPrint
