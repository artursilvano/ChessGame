package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public class Pawn extends Piece {
    public Pawn(Board b, int r, int c,Team team) {
        super(b, PieceType.PAWN, r, c, team);
    }



    public boolean onRange(int l, int c) {
        if (l < 0 || l >= TAM || c < 0 || c >= TAM) return false;
        if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.getTeam())) return false;

        if (this.getTeam().equals(Team.WHITE)) {
            if ((l == this.getRow() - 1 && c == this.getColumn() && this.board.getPiece(l, c) == null) ||
                (l == this.getRow() - 2 && c == this.getColumn() && this.isFirst() && this.board.getPiece(l, c) == null && this.board.getPiece(l + 1, c) == null) ||

                (((this.board.getPiece(l, c) != null && !this.board.getPiece(l, c).getTeam().equals(this.getTeam())) || (this.board.getEnPassant(l, c) != null && !this.board.getEnPassant(l, c).getTeam().equals(this.getTeam())))
                        && Math.abs(this.getColumn() - c) == 1 && l == this.getRow() - 1)) {

                return myKingWillBeSafe(l, c);
            }
        } else if (this.getTeam().equals(Team.BLACK)) {
            if ((l == this.getRow() + 1 && c == this.getColumn() && this.board.getPiece(l, c) == null) ||
                (l == this.getRow() + 2 && c == this.getColumn() && this.isFirst() && this.board.getPiece(l, c) == null && this.board.getPiece(l - 1, c) == null) ||

                (((this.board.getPiece(l, c) != null && !this.board.getPiece(l, c).getTeam().equals(this.getTeam())) || (this.board.getEnPassant(l, c) != null && !this.board.getEnPassant(l, c).getTeam().equals(this.getTeam())))
                        && Math.abs(this.getColumn() - c) == 1 && l == this.getRow() + 1 )) {

                return myKingWillBeSafe(l, c);
            }
        }

        return false;
    }
    public boolean EnPassant(int l, int c) {   // passar funcoes de mover peca para funcao move

        if ( this.board.getEnPassant(l, c) != null && !this.board.getEnPassant(l, c).getTeam().equals(this.getTeam())) {

            this.board.removePiece(this.board.getEnPassant(l, c).getRow(), this.board.getEnPassant(l,c).getColumn()); // Mata peao correspondente
            this.board.addPieceDeathPieces(l, c);
            execMove(l,c);
            return true;
        }
        return false;
    }

    public boolean duasCasas(int x, int y) {   // passar funcoes de mover peca para funcao move
        if (getTeam().equals(Team.WHITE)) {
            if (x == this.getRow() - 2 && y == this.getColumn() && this.isFirst() && this.board.getPiece(x, y) == null && this.board.getPiece(x + 1, y) == null) {           // Come para a esquerda ou direita
                this.execMove(x, y);
                this.board.addPiece(new enPassant(this.board, x, y, getTeam(), this.getId()), x + 1, y );
                return true;
            }
        } else if (getTeam().equals(Team.BLACK)) {
            if (x == this.getRow() + 2 && y == this.getColumn() && this.isFirst() && this.board.getPiece(x, y) == null && this.board.getPiece(x - 1, y) == null) {           // Duas casas para frente, opc, apenas na primeira jogada
                this.execMove(x, y);

                this.board.addPiece(new enPassant(this.board, x, y, getTeam(), this.getId()), x - 1, y );

                return true;
            }
        }
        return false;
    }

    public boolean especialMove(int l,int c) {
        if (duasCasas(l,c)) return true;
        if (EnPassant(l,c)) return true;
        return false;
    }

}
