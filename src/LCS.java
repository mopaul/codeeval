import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class LCS {

	private enum Move{LEFT, UP, DIAG, NOOP};

	private static String lcs(String str1, String str2){
		StringBuilder l = new StringBuilder();

		int M = str1.length();
		int N = str2.length();
		int lcs[][] = new int[M+1][N+1];
		Move action[][] = new Move[M+1][N+1];

		//initialize
		for (int m=0, n=0; n <= N; n++){
			lcs[n][m] = 0;
			action[n][m] = Move.NOOP;

		}
		for (int m=0, n=0; m <= M; m++){
			lcs[n][m] = 0;
			action[n][m] = Move.NOOP;
		}

		//do dyn prog
		for (int m = 1; m <= M; m++){
			for (int n=1; n <= N; n++){
				if (str1.charAt(m-1) == str2.charAt(n-1)){
					lcs[m][n] = 1+lcs[m-1][n-1];
					action[m][n] = Move.DIAG;
				}else{
					if (lcs[m-1][n] > lcs[m][n-1]){
						lcs[m][n] = lcs[m-1][n];
						action[m][n] = Move.UP;
					}else {
						lcs[m][n] = lcs[m][n-1];
						action[m][n] = Move.LEFT;
					}

				}

			}
		}
		
		//System.out.println(lcs[M][N]);
				
		//build lcs.
		for (int m = M, n = N; m > 0 && n > 0; ){
			if (action[m][n].equals(Move.DIAG)){
				assert str1.charAt(m-1) == str2.charAt(n-1);
				l.append(str1.charAt(m-1));
				n--; m--;
			}
			else if (action[m][n].equals(Move.LEFT)) n--;
			else if (action[m][n].equals(Move.UP)) m--;
			else break;
			
		}
		
		return l.reverse().toString();
	}	


public static void main (String[] args) throws IOException {
	File file = new File(args[0]);
	BufferedReader in = new BufferedReader(new FileReader(file));
	String line;
	while ((line = in.readLine()) != null) {
		if (line.length() == 0) continue;
		String toks[] = line.split(";");
		System.out.println(lcs(toks[0], toks[1]));
	}

	in.close();


	System.exit(0);
}
}

