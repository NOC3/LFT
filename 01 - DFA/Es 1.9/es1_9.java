//soprannome "Noce/noce"
public class es1_9{
	public static boolean scan(String s){

	int state = 1;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);
			//System.out.println((int)ch);			 		    
					
			switch (state) {
				case 1:
					if(ch=='n'||ch=='N'){
						state=2;
					}else{
						state=12;
					}
				break;
				case 2:
					if(ch=='o'){
						state=3;
					}else{
						state=23;
					}
				break;
				case 3:
					if(ch=='c'){
						state=4;
					}else{
						state=34;
					}
				break;
				case 4:
					state=5;
				break;
				case 5:
					if((int)ch>=0 && (int)ch<=255 ){
						state=10;
					}else{	
						state=-1;
					}
				break;
				case 12:
					if(ch=='o'){
						state=13;
					}else{	
						state=10;
					}
				break;
				case 13:
					if(ch=='c'){
						state=14;
					}else{	
						state=10;
					}
				break;
				case 14:
					if(ch=='e'){
						state=5;
					}else{	
						state=10;
					}
				break;
				case 23:
					if(ch=='c'){
						state=24;
					}else{	
						state=10;
					}
				break;
				case 24:
					if(ch=='e'){
						state=5;
					}else{	
						state=10;
					}
				break;
				case 10:
					state=10;
				break;
				case 34:
					if(ch=='e'){
						state=5;
					}else{	
						state=10;
					}
				break;

				}
																														
		}
		return state == 5;
	    }

	    public static void main(String[] args)
	    {
	    	System.out.println("(per lo svolgimento ho usato il mio soprannome 'Noce'/'noce')");
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	    }
	}
