package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

import static java.lang.Math.abs;

public class King extends Piece {
    public King(Board b, int r, int c,Team team) {
        super(b, PieceType.KING, r, c, team);
    }

    public boolean onRange(int l, int c) {
        if (l < 0 || l >= TAM || c < 0 || c >= TAM) return false;
        if (isRoque(l,c)) { return true; }
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        else if (abs(this.getRow() - l) <= 1 && abs(this.getColumn() - c) <= 1)
            return myKingWillBeSafe(l, c);
        return false;
    }
    public boolean isRoque(int x, int y) {         // Castling
        if (this.board.getPiece(x, y) != null && this.board.getPiece(x, y).getType().equals(PieceType.ROOK) && this.board.getPiece(x, y).getTeam().equals(getTeam())  && isFirst() && this.board.getPiece(x, y).isFirst() && myKingIsSafe(this.board)) {
            for (int i = 0; getColumn() + i != y; ) {

                if ((i != 0 && this.board.getPiece(getRow(), getColumn()+i) != null) || !posIsSafe(this.board, getRow(), getColumn())) return false;

                if (getColumn() > y) i--;
                else i++;
            }
            return myKingWillBeSafe(x, y);
        }
        return false;
    }

    public boolean especialMove(int l, int c) {
        if (isRoque(l,c)) {
            board.getPiece(l,c).execMove( l, getColumn() > c ? c + 3 : c - 2);
            this.execMove( l,getColumn() > c ? getColumn() - 2 : getColumn() + 2);
            return true;
        }
        return false;
    }
}
