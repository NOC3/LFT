
public class Esercizio1_2{
    public static boolean scan(String s){
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);
		System.out.println((int)ch);
		if(ch!=' '){		    
		   switch (state) {
		    case 0:
			if ( 48<=(int)ch && (int)ch<=57 )//il ch è un numero
			    state = 10;
			else if (ch == '_')
			    state = 1;
			else if(65<=(int)ch && (int)ch <= 122)
			    state = 2;
			else
			    state = -1;
			break;

		    case 10:
			if ((65<=(int)ch && (int)ch <= 122) ||(48<=(int)ch && (int)ch<=57) || ch=='_')
			    state = 10;
			else
			    state = -1;
			break;

		    case 1:
			if (ch == '_')
			    state = 1;
			else if ((65<=(int)ch && (int)ch <= 122) ||(48<=(int)ch && (int)ch<=57) )//il ch è una lettera o un numero
			    state = 2;
			else
			    state = -1;
			break;

		    case 2:
			if ((65<=(int)ch && (int)ch <= 122) ||(48<=(int)ch && (int)ch<=57) || ch=='_')
			    state = 2;
			else
			    state = -1;
			break;
		    }
		}																											
	}
	return state == 2;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
