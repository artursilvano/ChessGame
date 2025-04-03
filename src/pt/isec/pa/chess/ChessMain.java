package pt.isec.pa.chess;

import pt.isec.pa.chess.model.data.board.Board;

public class ChessMain {
    public static void main(String[] args) {

        Board b = new Board();
        System.out.println(b);
        System.out.println(b.possibleMoves(b.getPiece("Ra1")));
        b.getPiece("Ra1").move(6, 0);

        System.out.println(b.possibleMoves(b.getPiece("Ra2")));



    }
}
