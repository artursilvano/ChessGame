package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public class Pawn extends Piece {
    public Pawn(Board b, int r, int c,Team team) {
        super(b,PieceType.PAWN,r,c,team);
    }

    public boolean onRange(int l, int c) {
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        if (this.getTeam().equals(Team.WHITE)) {
            if ((l == this.getRow() - 1 && c == this.getColumn() && this.board.getPiece(l, c) == null) ||
                (l == this.getRow() - 2 && c == this.getColumn() && this.isFirst() && this.board.getPiece(l, c) == null && this.board.getPiece(l + 1, c) == null) ||
                (this.board.getPiece(l, c) != null && Math.abs(this.getColumn() - c) == 1 && l == this.getRow() - 1 && !this.board.getPiece(l, c).getTeam().equals(this.getTeam()))) {
                return true;
            }
        } else if (this.getTeam().equals(Team.BLACK)) {
            if ((l == this.getRow() + 1 && c == this.getColumn() && this.board.getPiece(l, c) == null) ||
                (l == this.getRow() + 2 && c == this.getColumn() && this.isFirst() && this.board.getPiece(l, c) == null && this.board.getPiece(l - 1, c) == null) ||
                (this.board.getPiece(l, c) != null && Math.abs(this.getColumn() - c) == 1 && l == this.getRow() + 1 && !this.board.getPiece(l, c).getTeam().equals(this.getTeam()))) {
                return true;
            }
        }

        return false;


    }
}
