// ogni occorrenza di /* deve avere un occorrenza di */

public class es1_11{
	public static boolean scan(String s){

	int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);
			//System.out.println((int)ch);			 		    
					
			switch (state) {
				case 0:
					if(ch=='/'){
						state=1;
					}else{
						state=0;
					}
				break;

				case 1:
					if(ch=='*'){
						state=2;
					}else{
						state=0;
					}
				break;

				case 2:
					if(ch=='*'){
						state=3;	
					}else{
						state=2;
					}
				break;

				case 3:
					if(ch=='/'){
						state=0;
					}else{
						state=2;
					}
				break;

				
			
				}
																														
		}
		return state == 0;
	    }

	    public static void main(String[] args)
	    {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	    }
	}
