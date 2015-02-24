package winner;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fi.zem.aiarch.game.hierarchy.Board;
import fi.zem.aiarch.game.hierarchy.Board.Square;
import fi.zem.aiarch.game.hierarchy.Engine;
import fi.zem.aiarch.game.hierarchy.Move;
import fi.zem.aiarch.game.hierarchy.Player;
import fi.zem.aiarch.game.hierarchy.Side;
import fi.zem.aiarch.game.hierarchy.Situation;

public class WinnerBot implements Player {
	
	private int NEUTRAL = 0;
	private int LOW = 1;
	private int NORMAL = 2;
	private int HIGH = 3;
	
	
	private Random rnd;
	private Engine engine;
	private Side mySide;
	
	public WinnerBot(Random rnd) {
		this.rnd = rnd;
	}
	
	public void start(Engine engine, Side side) {
		this.engine = engine;
		this.mySide = side;
	}
	
	public Move move(Situation situation, int timeLeft) {
		List<Move> moves = situation.legal();
		if (moves.isEmpty()) return situation.makePass();
		
		List<Move> legalMoves = situation.legal();
		Node maxNode = new Node(null, Integer.MIN_VALUE);
		
		for (Move move : legalMoves){
			int score = negaMax(situation.copyApply(move), 3);
			if (maxNode.getScore() < score){
				maxNode.
			}
		}
		
		Situation desiredSituation = negaMax(situation, 2).getSituation();
		
		return desiredSituation.getPreviousMove();
	}
	
	private Node negaMax(Situation situation, int depth){
		
		int maxScore = Integer.MIN_VALUE;
		
		if (depth == 0 || situation.isFinished()){
			return new Node(situation, score(situation));
		}
		
		List<Move> legalMoves = situation.legal();

		for (Move move : legalMoves){
			Node node = negaMax(situation.copyApply(move), depth -1);
			if (maxNode.getScore() < node.getScore()){
				maxNode = node;
			}
		}
		
		if (situation.getTurn().equals(mySide)) maxNode.turnScoreNeg();
		
		return maxNode;
	}

	private int score(Situation situation) {
		
		Board currentBoard = situation.getBoard();
		Iterator<Square> pieces = currentBoard.pieces(situation.getTurn()).iterator();
		int totalValue = 0;
		while(pieces.hasNext()){
			totalValue += pieces.next().getPiece().getValue();
		}
		return totalValue;
	}
	
	private class Node{
		
		private Situation situation;
		private int score;
		
		public Node(Situation situation, int score){
			this.situation = situation;
			this.score = score;
		}
		
		public Situation getSituation() {
			return situation;
		}

		public void setSituation(Situation situation) {
			this.situation = situation;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
		
		public void turnScoreNeg(){
			score = -score;
		}

		

	}
	
}
