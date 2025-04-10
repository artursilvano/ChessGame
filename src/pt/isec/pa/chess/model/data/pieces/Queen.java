package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public class Queen extends Piece {
    public Queen(Board b, int r, int c,Team team) {
        super(b, PieceType.QUEEN, r, c, team);
    }

    public boolean onRange(int l, int c) {
        if (l < 0 || l >= TAM || c < 0 || c >= TAM) return false;
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        if ((this.getRow() != l || this.getColumn() == c) && (this.getRow() == l || this.getColumn() != c) && Math.abs(this.getRow() - l) != Math.abs(this.getColumn() - c)) {
            return false;
        } else {
            if (this.getRow() == l && this.getColumn() != c) {
                int i = this.getColumn();

                while(i != c) {
                    if (i != this.getColumn() && this.board.getPiece(l, i) != null) return false;

                    if (this.getColumn() > c)--i;
                    else ++i;
                }
            } else if (this.getRow() != l && this.getColumn() == c) {
                int i = this.getRow();

                while(i != l) {
                    if (i != this.getRow() && this.board.getPiece(i, c) != null) return false;

                    if (this.getRow() > l) --i;
                    else ++i;
                }
            } else if (Math.abs(this.getRow() - l) == Math.abs(this.getColumn() - c)) {
                int i = 0;
                int j = 0;

                while(l != this.getRow() + i && c != this.getColumn() + j) {
                    if (i != 0 && this.board.getPiece(this.getRow() + i, this.getColumn() + j) != null) return false;

                    if (this.getRow() > l) --i;
                    else ++i;
                    if (this.getColumn() > c) --j;
                    else ++j;
                }
            }

            return myKingWillBeSafe(l,c);
        }
    }
    public boolean especialMove(int l, int c) {return false;}
}


