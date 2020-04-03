import com.projetProgReseau.client.Client;

public class LancementClient {

	
	public static void main(String[] argu) {
		if (argu.length == 5) {
			
			String s = argu[0];
			int p = Integer.parseInt(argu[1]);
			String modeJeu = argu[2];
			String strategy = argu[3];
			int maxturn = Integer.parseInt(argu[4]);
			
			Client c1 = new Client(s, p, modeJeu, strategy, maxturn);
			Thread t = new Thread(c1);
			t.start();
			
		} else {
			System.out.println("ERREUR : Il manque des options !!");
			System.out.println("Les options :");
			System.out.println("\t 1. nom du serveur");
			System.out.println("\t 2. numero du port du serveur");
			System.out.println("\t 3. mode de jeu ( \"normal\" ou \"solo\" ou \"duo\" )");
			System.out.println("\t 4. strategie des agents ( \"random\" ou \"put_bomb\" ou \"break_wall\" ou \"esquive\" )");
			System.out.println("\t 5. nombre de tour maximum");
		} 
	}


}
