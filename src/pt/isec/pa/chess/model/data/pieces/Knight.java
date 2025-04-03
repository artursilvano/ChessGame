package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public class Knight extends Piece {
    public Knight(Board b, int r, int c,Team team) {
        super(b,PieceType.KNIGHT,r,c,team);
    }

    public boolean onRange(int l, int c) {
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        return Math.abs(this.getRow() - l) <= 2 && Math.abs(this.getColumn() - c) <= 2 && Math.abs(this.getRow() - l) > 0 && Math.abs(this.getColumn() - c) > 0 && Math.abs(this.getRow() - l) != Math.abs(this.getColumn() - c);

    }
}
