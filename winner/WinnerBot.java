package winner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.zem.aiarch.game.hierarchy.Board;
import fi.zem.aiarch.game.hierarchy.Coord;
import fi.zem.aiarch.game.hierarchy.Engine;
import fi.zem.aiarch.game.hierarchy.Move;
import fi.zem.aiarch.game.hierarchy.Player;
import fi.zem.aiarch.game.hierarchy.Side;
import fi.zem.aiarch.game.hierarchy.Situation;

public class WinnerBot implements Player {

	private Random rnd;
	private Engine engine;
	private Side mySide;
	private int height;
	private int width;
	private int maxPieceValue;
	
	private ArrayList<Move> defaultOpening = new ArrayList<Move>();
	
	public WinnerBot(Random rnd) {
		this.rnd = rnd;
	}
	
	public void start(Engine engine, Side side) {
		
		System.gc();
		this.engine = engine;
		this.mySide = side;
		this.height = engine.getBoardHeight();
		this.width = engine.getBoardWidth();
		this.maxPieceValue = engine.getMaxPiece();
		initOpening();
		
	}
	
	private void initOpening() {
		
		Situation initialSituation = this.engine.makeInitial();
		
		int midX = this.maxPieceValue / 2;
		//if max piece is even then x and y for the middle front piece are not the same
		int midY = (this.maxPieceValue & 1) == 0 ? midX - 1: midX;
		
		defaultOpening.add(generateMove(initialSituation, midX, midY, midX, midY + 1));
		defaultOpening.add(generateMove(initialSituation, midX - 1, midY, midX, midY));
		defaultOpening.add(generateMove(initialSituation, midX - 1, midY + 1, midX - 1, midY + 2));
//		defaultOpening.add(generateMove(initialSituation, midX - 1, midY - 1, midX - 1, midY + 1));
		
	}

	private Move generateMove(Situation situation, int startX, int startY, int endX, int endY) {
		if (this.mySide.equals(Side.BLUE))
			return situation.makeMove(startX, startY, endX, endY);
		return situation.makeMove(this.width - startX -1 , this.width - startY - 1, this.height - endX - 1, this.height - endY - 1 );
	}

	public Move move(Situation situation, int timeLeft) {
		List<Move> moves = situation.legal();
		
		if (!defaultOpening.isEmpty()){
			return defaultOpening.remove(0);
		}
		if (moves.isEmpty()) return situation.makePass();
		
		if (situation.mustFinishAttack()){
			return moves.get(0);
		}
		
		List<Move> legalMoves = situation.legal();
		int maxScore = Integer.MIN_VALUE;
		Move maxMove = null;
		
		for (Move move : legalMoves){
			int score = -alphaBeta(situation.copyApply(move), 3, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
			if (maxScore < score){
				maxScore = score;
				maxMove = move;
			}
		}
		System.out.println("toimii");
		return maxMove;
	}
	
	private int alphaBeta(Situation situation, int depth, int a, int b, int side){
		
		List<Move> legalMoves = situation.legal();
		
		if (depth == 0 || situation.isFinished()){
			if ((depth&1) == 1)
				return  side * score(situation, legalMoves);
			return  side * score(situation, legalMoves);
		}

		int maxScore = Integer.MIN_VALUE;
		
		for (Move move : legalMoves){
			int score = -alphaBeta(situation.copyApply(move), depth -1, -b, -a, -side);
			maxScore = Math.max(maxScore, score);
			a = Math.max(a, score);
			if (a >= b){
				break;
			}
		}
		
		return maxScore;
	}

	private int score(Situation situation, List<Move> legalMoves) {
		
		int totalPieceValue = 0;
		int totalFirepower = 0;
		int dangerValue = 0;
		
		Board board = situation.getBoard();

		//tämä osio vielä vähän epäselvä miksi min ja max näin päin??
		if(situation.isFinished()){
			if(situation.getWinner().equals(situation.getTurn()))
				return Integer.MIN_VALUE;
			return Integer.MAX_VALUE;
		}
		Coord target = situation.getTarget(situation.getTurn());
		for (Board.Square s : board.pieces(situation.getTurn())){
			if(target != null ? target.equals(s.getX(), s.getY()) : false){
				if(s.getPiece().getValue() == this.maxPieceValue){
					dangerValue -= 100;
				} else {
					dangerValue -= s.getPiece().getValue();
				}
			}
			totalPieceValue += s.getPiece().getValue() * 10;
			if(s.getOwner().equals(Side.NONE))
				totalFirepower += board.firepower(situation.getTurn(), s.getX(),s.getY());
		}
		target = situation.getTarget(situation.getTurn().opposite());
		for (Board.Square s : board.pieces(situation.getTurn().opposite())){
			if(target != null ? target.equals(s.getX(), s.getY()) : false){
				if(s.getPiece().getValue() == this.maxPieceValue){
//					dangerValue += 100;
				} else {
					dangerValue += s.getPiece().getValue();
				}
			}
			totalPieceValue -= s.getPiece().getValue() * 5;
			if(s.getOwner().equals(Side.NONE))
				totalFirepower -= board.firepower(this.mySide.opposite(), s.getX(),s.getY());
			
		}

//		aika lopussa DkqgSoChHVzob6a7azITJZLVI0TX8aOCoSwY+Ln
		return totalPieceValue + totalFirepower + dangerValue;

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
