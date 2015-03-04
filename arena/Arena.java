package arena;

import java.io.File;
import java.util.List;
import java.util.Random;

import randombot.RandomBot;
import winner.WinnerBot;
import fi.zem.aiarch.engine.hierarchy.Bot;
import fi.zem.aiarch.engine.hierarchy.BotLoader;
import fi.zem.aiarch.game.hierarchy.Player;
import fi.zem.aiarch.game.hierarchy.Runner;
import fi.zem.aiarch.gui.hierarchy.Launcher;
public class Arena {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		
		
		Launcher launcher = new Launcher();
		BotLoader jes = new BotLoader();
		List<Bot> jee = BotLoader.findBots(new File("../"));

		Player p1 = new WinnerBot(new Random() );
		
		Player p2 = BotLoader.createBot(jee.get(5));

		System.out.println(Runner.game(p1, p2));
	}
}
