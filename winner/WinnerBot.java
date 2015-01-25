package winner;

import java.util.List;
import java.util.Random;

import fi.zem.aiarch.game.hierarchy.Engine;
import fi.zem.aiarch.game.hierarchy.Move;
import fi.zem.aiarch.game.hierarchy.Player;
import fi.zem.aiarch.game.hierarchy.Side;
import fi.zem.aiarch.game.hierarchy.Situation;

public class WinnerBot implements Player {
	
	private Random rnd;
	private Engine engine;
	
	public WinnerBot(Random rnd) {
		this.rnd = rnd;
	}
	
	public void start(Engine engine, Side side) {
		this.engine = engine;
	}
	
	public Move move(Situation situation, int timeLeft) {
		List<Move> moves = situation.legal();
		if (moves.isEmpty()) return situation.makePass();
		
		for (Move m : moves){
			if (situation.canAttack(m.getFrom(), m.getTo())){
				return m;
			}
		}
		return moves.get(rnd.nextInt(moves.size()));
	}
}
