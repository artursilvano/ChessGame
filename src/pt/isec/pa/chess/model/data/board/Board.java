package pt.isec.pa.chess.model.data.board;
import pt.isec.pa.chess.model.data.pieces.*;

public class Board {
    Piece[][] board;
    private final int tam = 8;

    public Board() {
        board = new Piece[tam][tam];
        for (int c = 0; c < tam; c++) {
            if (c == 0 || c == tam - 1) {
                board[0][c] = new Rook(this, 0, c, Team.BLACK);
                board[tam - 1][c] = new Rook(this, tam - 1, c, Team.WHITE);
            }
            if (c == 1 || c == tam - 2) {
                board[0][c] = new Knight(this, 0, c, Team.BLACK);
                board[tam - 1][c] = new Knight(this, tam - 1, c, Team.WHITE);
            }
            if (c == 2 || c == tam - 3) {
                board[0][c] = new Bishop(this, 0, c, Team.BLACK);
                board[tam - 1][c] = new Bishop(this, tam - 1, c, Team.WHITE);
            }
            if (c == 3) {
                board[0][c] = new Queen(this, 0, c, Team.BLACK);
                board[tam - 1][c] = new Queen(this, tam - 1, c, Team.WHITE);
            }
            if (c == 4) {
                board[0][c] = new King(this, 0, c, Team.BLACK);
                board[tam - 1][c] = new King(this, tam - 1, c, Team.WHITE);
            }
            //board[1][c] = new Pawn(this, 1, c, Team.Black);
            //board[tam - 2][c] = new Pawn(this, tam - 2, c, Team.White);
        }

    }

    public Piece getPiece(int x, int y) {
        return board[x][y] == null ? null : board[x][y];
    }

    public Piece getPiece(String id) {
        for(int c = 0; c < 8; c++) {
            for(int l = 0; l < 8; l++) {
                if (this.board[l][c] != null && this.board[l][c].getId().equals(id)) {
                    return this.board[l][c];
                }
            }
        }
        return null;
    }

    public void removePiece(int l, int c) {
        board[l][c] = null;
    }

    public void addPiece(Piece piece, int l, int c) {
        board[l][c] = piece;
        piece.setRow(l);
        piece.setColumn(c);
    }

    public void movePiece(Piece piece, int x, int y) {
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (board[i][j] != null && board[i][j] == piece) {
                    removePiece(i, j);
                }
            }
        }
        addPiece(piece, x, y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int l=0;l<tam;l++) {
            sb.append("\n\t _________________________________________________________\n").append(Math.abs(l-8) ).append("\t");

            for (int c = 0; c < tam; c++){
                sb.append(" | ");
                sb.append(this.board[l][c] == null ? "    " : this.board[l][c].toString());
            }

            sb.append(" |\t\t");
        }

        sb.append("\n\t _________________________________________________________\n").append("\t    A      B      C      D      E      F      G      H");
        return sb.toString();
    }

    public String possibleMoves(Piece piece) {
        if (piece == null) return "Invalid Piece";
        StringBuilder sb = new StringBuilder();

        for (int l=0;l<tam;l++) {
            sb.append("\n\t _________________________________________________________\n").append(Math.abs(l-8) ).append("\t");

            for (int c = 0; c < tam; c++){
                sb.append(" | ");
                sb.append(piece.onRange(l,c) ? "\u001b[41m" : "\u001b[0m").append(this.board[l][c] == null ? "    " : this.board[l][c].toString()).append("\u001b[0m");
            }

            sb.append(" |\t\t");
        }

        sb.append("\n\t _________________________________________________________\n").append("\t    A      B      C      D      E      F      G      H");
        return sb.toString();
    }
}

