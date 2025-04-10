package pt.isec.pa.chess.model.data.board;
import pt.isec.pa.chess.model.Constants;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

import static pt.isec.pa.chess.model.data.pieces.PieceType.*;
import static pt.isec.pa.chess.model.data.pieces.Team.*;

public class Board implements Constants, Serializable {
    private static final long serialVersionUID = 1L;
    Piece[][] board;

    public Board() {                        // Cria tabuleiro inicial
        board = new Piece[TAM][TAM];
        for (int c = 0; c < TAM; c++) {
            if (c == 0 || c == TAM - 1) {
                board[0][c] =       ROOK.createPiece(this, 0, c, BLACK);
                board[TAM - 1][c] = ROOK.createPiece(this, TAM - 1, c, WHITE);
            }
            if (c == 1 || c == TAM - 2) {
                board[0][c] =       KNIGHT.createPiece(this, 0, c, BLACK);
                board[TAM - 1][c] = KNIGHT.createPiece(this, TAM - 1, c, WHITE);
            }
            if (c == 2 || c == TAM - 3) {
                board[0][c] =       BISHOP.createPiece(this, 0, c, BLACK);
                board[TAM - 1][c] = BISHOP.createPiece(this, TAM - 1, c, WHITE);
            }
            if (c == 3) {
                board[0][c] =       QUEEN.createPiece(this, 0, c, BLACK);
                board[TAM - 1][c] = QUEEN.createPiece(this, TAM - 1, c, WHITE);
            }
            if (c == 4) {
                board[0][c] =       KING.createPiece(this, 0, c, BLACK);
                board[TAM - 1][c] = KING.createPiece(this, TAM - 1, c, WHITE);
            }
            board[1][c] =           PAWN.createPiece(this, 1, c, BLACK);
            board[TAM - 2][c] =     PAWN.createPiece(this, TAM - 2, c, WHITE);
        }
    }

    public Board(Board bAux) {
        board = new Piece[TAM][TAM];
        Piece piece;
        for (int r = 0; r < TAM; r++) {
            for (int c = 0; c < TAM; c++) {
                if(bAux.getPiece(r, c) != null) {
                    piece = bAux.getPiece(r, c);
                    board[r][c] = piece.getType().createPiece(this, piece.getRow(), piece.getColumn(), piece.getTeam());
                }
            }
        }
    }

    public Board(String line) throws FileNotFoundException {      // Cria board a partir de uma string
        this.board = new Piece[TAM][TAM];
        Piece piece;
        String[] pieces = line.split(",");

        for (String p : pieces) {
            piece = createPieceByText(p);
            this.board[piece.getRow()][piece.getColumn()] = piece;
        }
    }


    public Piece createPieceByText(String id) {
        if (id.isEmpty()) return null;
        Piece piece = null;
        PieceType type;
        type = switch (Character.toUpperCase(id.charAt(0))) {
            case 'P' -> PAWN;
            case 'N' -> KNIGHT;
            case 'B' -> BISHOP;
            case 'R' -> ROOK;
            case 'Q' -> QUEEN;
            case 'K' -> KING;
            default -> null;
        };
        if (type != null) {
            piece = type.createPiece(this,  (TAM - Character.getNumericValue(id.charAt(2))), columnToNum(Character.toUpperCase(id.charAt(1))), Character.isUpperCase(id.charAt(0)) ? WHITE : BLACK);   // Cria peca
            if (id.length() == 3) piece.isNotFirst();
        }
        return piece;
    }

    public Piece getPiece(int x, int y) { return board[x][y] == null ? null : board[x][y]; }

    public Piece getPiece(String id) {
        for(int c = 0; c < TAM; c++)
            for(int l = 0; l < TAM; l++)
                if (this.board[l][c] != null && this.board[l][c].getId().equals(id)) {
                    return this.board[l][c];
                }

        return null;
    }

    public void removePiece(int l, int c) {
        board[l][c] = null;
        /* Adicionar a lista de pecas removidas*/
    }

    public void addPiece(Piece piece, int l, int c) {
        board[l][c] = piece;
    }

    public void movePiece(Piece piece, int x, int y) {
        for (int i = 0; i < TAM; i++)
            for (int j = 0; j < TAM; j++)
                if (board[i][j] != null && board[i][j] == piece) {
                    board[i][j] = null;
                }

        addPiece(piece, x, y);
    }
















    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int l = 0; l< TAM; l++) {
            sb.append("\n\t _________________________________________________________\n").append(Math.abs(l-8) ).append("\t");

            for (int c = 0; c < TAM; c++){
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

        for (int l = 0; l< TAM; l++) {
            sb.append("\n\t _________________________________________________________\n").append(Math.abs(l-8) ).append("\t");

            for (int c = 0; c < TAM; c++){
                sb.append(" | ");
                sb.append(piece.onRange(l,c) ? "\u001b[41m" : "\u001b[0m").append(this.board[l][c] == null ? "    " : this.board[l][c].toString()).append("\u001b[0m");
            }

            sb.append(" |\t\t");
        }

        sb.append("\n\t _________________________________________________________\n").append("\t    A      B      C      D      E      F      G      H");
        return sb.toString();
    }





}

