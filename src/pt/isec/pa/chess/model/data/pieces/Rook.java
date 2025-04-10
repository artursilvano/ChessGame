package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public class Rook extends Piece {
    public Rook(Board b, int r, int c,Team team) {
        super(b, PieceType.ROOK, r, c, team);
    }

    public boolean onRange(int l, int c) {
        if (l < 0 || l >= TAM || c < 0 || c >= TAM) return false;
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        if (this.getRow() == l && this.getColumn() != c || this.getRow() != l && this.getColumn() == c) {
            if (this.getColumn() != c) {
                int i = this.getColumn();

                while(i != c) {
                    if (i != this.getColumn() && this.board.getPiece(l, i) != null) { return false; }

                    if (this.getColumn() > c) --i;
                    else ++i;
                }
            } else {
                int i = this.getRow();

                while(i != l) {
                    if (i != this.getRow() && this.board.getPiece(i, c) != null) { return false; }

                    if (this.getRow() > l) --i;
                    else ++i;
                }
            }
            return myKingWillBeSafe(l, c);
        }

        return false;
    }

    public boolean especialMove(int l, int c) {return false;}
}
