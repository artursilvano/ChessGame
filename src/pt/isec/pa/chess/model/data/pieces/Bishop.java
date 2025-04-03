package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;


public class Bishop extends Piece {
    public Bishop(Board b, int r, int c,Team team) {
        super(b,PieceType.BISHOP,r,c,team);
    }

    public boolean onRange(int l, int c) {
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        if (Math.abs(this.getRow() - l) != Math.abs(this.getColumn() - c)) {
            return false;
        } else {
            int i = 0;
            int j = 0;

            while(l != this.getRow() + i && c != this.getColumn() + j) {
                if (i != 0 && this.board.getPiece(this.getRow() + i, this.getColumn() + j) != null) {
                    return false;
                }

                if (this.getRow() > l) --i;
                else ++i;
                if (this.getColumn() > c) --j;
                else ++j;
            }

            return true;
        }

    }
}



