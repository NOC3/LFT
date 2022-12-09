
public class Esercizio1_5 {
	public static boolean scan(String s) {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);
			System.out.println((int) ch);
			int ascii = (int) ch;
			switch (state) {
				case 0:
					if ((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'k'))
						state = 1;
					else if ((ch >= 'L' && ch <= 'Z') || (ch >= 'l' && ch <= 'z'))
						state = 2;
					else
						state = -1;
					break;

				case 1:
					if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))// leggo tutte le lettere
						state = 1;
					else if (ascii >= 48 && ascii <= 57)// incontro un numero
						if (ascii % 2 == 0)// pari -> ok
							state = 3;
						else // ascii %2 == 1
							state = 11;

					break;

				case 2:
					if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))// leggo tutte le lettere
						state = 2;
					else if (ascii >= 48 && ascii <= 57)// incontro un numero
						if (ascii % 2 == 0)// pari -> !ok
							state = 22;
						else // ascii %2 == 1
							state = 4;
					break;

				case 11:
					if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
						state = -1;
					else if (ascii >= 48 && ascii <= 57)// incontro un numero
						if (ascii % 2 == 0)// pari -> ok
							state = 3;
						else // ascii %2 == 1
							state = 11;
					break;

				case 22:
					if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))// leggo tutte le lettere
						state = -1;
					else if (ascii >= 48 && ascii <= 57)// incontro un numero
						if (ascii % 2 == 0)// pari -> !ok
							state = 22;
						else // ascii %2 == 1
							state = 4;
					break;

				case 3:
					if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
						state = -1;
					else if (ascii >= 48 && ascii <= 57)// incontro un numero
						if (ascii % 2 == 0)// pari -> ok
							state = 3;
						else // ascii %2 == 1
							state = 11;
					break;

				case 4:
					if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))// leggo tutte le lettere
						state = -1;
					else if (ascii >= 48 && ascii <= 57)// incontro un numero
						if (ascii % 2 == 0)// pari -> !ok
							state = 22;
						else // ascii %2 == 1
							state = 4;
					break;

			}
		}
		return state == 3 || state == 4;
	}

	public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}
