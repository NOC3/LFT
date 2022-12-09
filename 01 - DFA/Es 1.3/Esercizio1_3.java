
public class Esercizio1_3{
    public static boolean scan(String s){
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);
			System.out.println((int)ch);			 
			int ascii=(int)ch;		    
			switch (state) {
				case 0:
				if ( 48<=ascii && ascii<=57 && ((ascii%2)==0) )// numero pari			
					state = 2;
				else if (48<=ascii && ascii<=57 && ((ascii%2)==1)) //numero dispari
					state = 1;
				else if(65<=(int)ch && (int)ch <= 122)
					state = 10;
				else
					state = -1;
				break;

				case 10://pozzo
				if ((65<=ascii && ascii <= 122) ||(48<=ascii && ascii<=57))
					state = 10;
				else
					state = -1;
				break;

				case 1://dispari
				if ((76<=ascii && ascii<=90) || (108<=ascii && ascii<=122))
					state = 3;//L-Z ok
				else if ((65<=ascii && ascii<=75)||(97<=ascii && ascii<=107))
					state = 10;//A-K no
				else if(48<=ascii && ascii<=57 && ((ascii%2)==0))	
					state=2;//altro numero pari	
				else if(48<=ascii && ascii<=57 && ((ascii%2)==1))	
					state=1;//altro numero dispari		
				else
					state = -1;
				break;

				case 2://pari
				if(48<=ascii && ascii<=57 && (ascii%2!=0))//numero dispari
						state=1;
				else if(48<=ascii && ascii<=57 && (ascii%2==0))//num pari
					state=2;
				else if((65<=ascii && ascii<=75)||(97<=ascii && ascii<=107))
					state=3;//A-K || a-k ok
				else if((76<=ascii && ascii<=90)||(108<=ascii && ascii<=122))
					state=10;//L-Z || l-z no
				else
					state=-1;			
				break;
				
				case 3:
				if ((65<=ascii && ascii<=122))
					state = 3;//letteraa-k ok
				else if((48<=ascii && ascii<=57))
					state = 10;//numero no
				else
					state = -1;	
				break;
				}
																														
		}
		return state == 3;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
