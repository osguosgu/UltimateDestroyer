package arena;

import java.util.Random;

import randombot.RandomBot;
import winner.WinnerBot;
import fi.zem.aiarch.game.hierarchy.Player;
import fi.zem.aiarch.game.hierarchy.Runner;

public class Arena {
	
	public static void main(String[] args) {
		
		Player p1 = new WinnerBot(new Random() );
		Player p2 = new RandomBot(new Random() );
		
		System.out.println(Runner.game(p1, p2));
	}
}
