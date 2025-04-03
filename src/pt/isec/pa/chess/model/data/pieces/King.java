package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

import static java.lang.Math.abs;

public class King extends Piece {
    public King(Board b, int r, int c,Team team) {
        super(b,PieceType.KING,r,c,team);
    }

    public boolean onRange(int l, int c) {
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        else if (abs(this.getRow() - l) <= 1 && abs(this.getColumn() - c) <= 1)
            return posIsSafe(this.board, l, c);
        return false;
    }
}
