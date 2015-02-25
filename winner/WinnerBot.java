package winner;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fi.zem.aiarch.game.hierarchy.Board;
import fi.zem.aiarch.game.hierarchy.Board.Square;
import fi.zem.aiarch.game.hierarchy.Engine;
import fi.zem.aiarch.game.hierarchy.Move;
import fi.zem.aiarch.game.hierarchy.MoveType;
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
		System.gc();
		this.engine = engine;
		this.mySide = side;
	}
	
	public Move move(Situation situation, int timeLeft) {
		List<Move> moves = situation.legal();
		if (moves.isEmpty()) return situation.makePass();
		if (situation.mustFinishAttack()){
			return moves.get(0);
		}
		
		List<Move> legalMoves = situation.legal();
		int maxScore = Integer.MIN_VALUE;
		Move maxMove = null;
		
		for (Move move : legalMoves){
			int score = minimax(situation.copyApply(move), 6, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
			if (maxScore < score){
				maxScore = score;
				maxMove = move;
				
			}
		}
		
		return maxMove;
	}
	
	private int minimax(Situation situation, int depth, int a, int b, int side){
		
		List<Move> legalMoves = situation.legal();
		
		if (depth == 0 || situation.isFinished()){
			return side * score(situation, legalMoves);
		}

		int maxScore = Integer.MIN_VALUE;
		
		for (Move move : legalMoves){
			int score = -minimax(situation.copyApply(move), depth -1, -b, -a, -side);
			maxScore = Math.max(maxScore, score);
			a = Math.max(a, score);
			if (a >= b){
				break;
			}
		}
		
		return maxScore;
	}

	private int score(Situation situation, List<Move> legalMoves) {
		
//		if (!situation.getTurn().equals(mySide)) x = -x;
		int maxValue = 0;
		
		Board board = situation.getBoard();
		
//		for(Move m : legalMoves){
//			maxValue += x * m.getPiece().getValue();
//		} DD4hAQChPjzoz6bFes5kR4yWS4btiqRzrwAkCSFAjpsmDTJDwwItSnyo0LOv
		
		for (Board.Square s : board.pieces(situation.getTurn())){
			maxValue += 100;
		}
		
		for (Board.Square s : board.pieces(situation.getTurn().opposite())){
			maxValue -= 100 * s.getPiece().getValue();
		}

		
		return maxValue;

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
		
		public void scoreNeg(){
			score = -score;
		}

		

	}
	
}
