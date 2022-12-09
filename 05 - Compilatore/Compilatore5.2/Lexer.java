import java.io.*;
import java.util.*;

public class Lexer {

	public static int line = 1;
	private char peek = ' ';

	private void readch(BufferedReader br) {
		try {
			peek = (char) br.read();
		} catch (IOException exc) {
			peek = (char) -1; // ERROR
		}
	}

	public Token lexical_scan(BufferedReader br) {
		while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r' || peek == '/') {
			if (peek == '\n') {
				line++;
			}
			boolean fineCommento;
			if (peek == '/') {
				readch(br);
				if (peek == '*') {
					fineCommento = false;
					while (!fineCommento) {
						readch(br);
						if (peek == '*') {
							readch(br);
							if (peek == '/') {
								fineCommento = true;
							}
						}
						if (peek == (char) -1) {
							return new Token(Tag.EOF);
						}

					}
				} else if (peek == '/') {
					while (peek != '\n') {
						readch(br);
					}
				} else {
					return Token.div;
				}
			}
			readch(br);
		}

		switch (peek) {
			case '!':
				peek = ' ';
				return Token.not;

			case '(':
				peek = ' ';
				return Token.lpt;

			case ')':
				peek = ' ';
				return Token.rpt;

			case '{':
				peek = ' ';
				return Token.lpg;

			case '}':
				peek = ' ';
				return Token.rpg;

			case '+':
				peek = ' ';
				return Token.plus;

			case '-':
				peek = ' ';
				return Token.minus;

			case '*':
				peek = ' ';
				return Token.mult;

			/*
			 * case '/': //questo case lo gestisco nel while iniziale per distinguere un
			 * Token.div da un commento peek = ' '; return Token.div ;
			 */
			case ';':
				peek = ' ';
				return Token.semicolon;

			case '&':
				readch(br);
				if (peek == '&') {
					peek = ' ';
					return Word.and;
				} else {
					System.err.println("Erroneous character" + " after & : " + peek);
					return null;
				}

			case '|':
				readch(br);
				if (peek == '|') {
					peek = ' ';
					return Word.or;
				} else {
					System.err.println("Erroneous character" + " after | : " + peek);
					return null;
				}

			case '<':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.le;
				} else if (peek == '>') {
					peek = ' ';
					return Word.ne;
				} else {
					return Word.lt;
				}

			case '>':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.le;
				} else {
					return Word.gt;
				}
			case '=':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.eq;
				} else {
					return Token.assign;
				}

			case (char) -1:
				return new Token(Tag.EOF);

			default:
				if (Character.isLetter(peek) || peek == '_') {

					String parola = "";
					// accumulo char dentro parola fino a quando non trovo ' '(accetto anche underscore)
					while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
						parola = parola + peek;
						readch(br);
					}
					switch (parola) {
						// casi riconosciuti
						case "cond":
							return Word.cond;
						case "when":
							return Word.when;
						case "then":
							return Word.then;
						case "else":
							return Word.elsetok;
						case "while":
							return Word.whiletok;
						case "do":
							return Word.dotok;
						case "seq":
							return Word.seq;
						case "print":
							return Word.print;
						case "read":
							return Word.read;
						case "_":
							return null;
						// casi non riconosciuti==variabili
						default:
							return new Word(Tag.ID, parola);
					}

				} else if (Character.isDigit(peek)) {

					String num = "";
					while (Character.isDigit(peek) || Character.isLetter(peek)) {
						num = num + peek;
						readch(br);
					}
					if (num.charAt(0) == '0' && num.length() > 1) {
						System.err.println("numero che comincia per zero non consentito");
						return null;
					}
					for (int i = 0; i < num.length(); i++) {
						if (!(num.charAt(i) >= 48 && num.charAt(i) <= 57)) {// 48='0',57='9'
							System.err.println("errore");
							return null;
						}
					}
					return new NumberTok(Tag.NUM, num);

				} else {
					System.err.println("Erroneous character: " + peek);
					return null;
				}

		}
	}

	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "./Testo.txt"; // il percorso del file da leggere
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Token tok;
			do {
				tok = lex.lexical_scan(br);
				System.out.println("Scan: " + tok);
			} while (tok.tag != Tag.EOF);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
