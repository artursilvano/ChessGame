package pt.isec.pa.chess.model.data.pieces;
import pt.isec.pa.chess.model.Constants;
import pt.isec.pa.chess.model.data.board.Board;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import static pt.isec.pa.chess.model.data.pieces.PieceType.*;
import static pt.isec.pa.chess.model.data.pieces.Team.*;

public abstract class Piece implements Constants, Serializable {
    @Serial private static final long serialVersionUID = 1L;

    Board board;
    private String id;
    private int row, column;
    private final PieceType type;
    private boolean first;
    private final Team team;
    private ArrayList<Piece> DeathPieces= new ArrayList<>();

    Piece(Board b,PieceType t, int r, int c,Team team) {
        this.board = b;
        this.row = r;
        this.column = c;
        this.team = team;
        this.first = true;
        this.type = t;
        if (!type.equals(PieceType.ENPASSANT)) {
            this.id = switch (t) {
                case PAWN -> "p";
                case KNIGHT -> "n";
                case BISHOP -> "b";
                case ROOK -> "r";
                case QUEEN -> "q";
                case KING -> "k";
                default -> null;
            };
            if (team.equals(WHITE))
                id = id.toUpperCase();
        }
    }

    public String getId() { return id + Character.toLowerCase(xAxis[column]) + (TAM - row); }

    public int getRow() { return row; }

    public int getColumn() { return column; }

    public PieceType getType() { return type; }

    public boolean isFirst() { return first; }


    public Team getTeam() { return team; }

    public List getPossibilities() {
        List<Integer[]> pM = new ArrayList<Integer[]>();

        for (int l = 0; l < TAM; l++)
            for (int c = 0; c < TAM; c++)
                if(this.onRange(l,c)) {
                    pM.add(new Integer[]{l,c});
                }

        return pM;
    }

    public void isNotFirst() { this.first = false; }

    public void setFirst(boolean originalFirstMove) { this.first = originalFirstMove; }

    public void setRow(int r) { this.row = r; }

    public void setColumn(int c) { this.column = c; }

    public void setId(String id) {this.id= id;}

    public int[] myKingPosition() {
        int[] kPos = new int[2];
        for(int i = 0; i < TAM; ++i)
            for(int j = 0; j < TAM; ++j)
                if (this.board.getPiece(j, i) != null && this.board.getPiece(j, i).getTeam().equals(this.team) && this.board.getPiece(j, i).getType().equals(KING)) {
                    kPos[0] = j;
                    kPos[1] = i;
                    return kPos;
                }
        return null;
    }

    public boolean posIsSafe(Board b, int x, int y) {
        for(int i = 0; i < TAM; ++i)
            for(int j = 0; j < TAM; ++j)
                if (b.getPiece(j, i) != null && !b.getPiece(j, i).getTeam().equals(this.team))  // Se for uma peca de outra cor
                    if (b.getPiece(j, i).onRange(x, y))                                         // Verifica se a peca inimiga pode se mover para a posicao
                        return false;                                                           // Se puder, a posicao nao e segura

        return true;
    }

    public boolean myKingIsSafe(Board b) {
        int xKing;
        int yKing;
        if (this.getType().equals(KING)) {
            xKing = this.row;
            yKing = this.column;
        } else {
            int[] kPos = this.myKingPosition();
            xKing = kPos[0];
            yKing = kPos[1];
        }
        return this.posIsSafe(b, xKing, yKing);
    }

    public boolean myKingWillBeSafe(int r, int c) {

        Board auxBoard = new Board(this.board);                                             // Cria board por copia
        auxBoard.movePiece(auxBoard.getPiece(this.getRow(), this.getColumn()), r, c);       // Realiza suposto movimento
        return this.myKingIsSafe(auxBoard);                         // Verifica se movimento colocara seu proprio Rei em risco

    }

    public abstract boolean onRange(int row, int column);

    public boolean execMove(int l, int c) {                 // Se conseguir chegar na casa...
        if (this.board.getPiece(l, c) == null || !this.board.getPiece(l, c).getTeam().equals(this.team)) {   // Se nao tiver uma peca de mesma cor ou estiver vazio...
            if (this.board.getPiece(l, c) != null && !this.board.getPiece(l, c).getTeam().equals(this.team)){
                this.board.removePiece(l, c);
                this.board.addPieceDeathPieces(l, c);
            }
                                                  // Se tiver uma peca de outra cor, mata! e se move
            this.board.movePiece(this, l, c);                           // Se estiver vazio, apenas se move
            this.row = l;
            this.column = c;
            this.first = false;
            return true;
        }
        return false;
    }
    public abstract boolean especialMove(int l,int c);

    public boolean move(int l, int c) {                         // Se o movimento for possivel, executa o movimento
        if (onRange(l, c)) {
            if(especialMove(l,c)) return true;
            return execMove(l, c);
        }
        return false;
    }

    public String toString() { return getId() + (((type.equals(KING) || type.equals(ROOK)) || type.equals(PAWN) && this.isFirst()) ? "*" : " "); }
}