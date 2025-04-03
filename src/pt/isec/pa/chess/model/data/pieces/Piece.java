package pt.isec.pa.chess.model.data.pieces;
import pt.isec.pa.chess.model.data.board.Board;

public abstract class Piece {
    Board board;
    private final Character [] xAxis = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private String id;
    private int row,column;
    private PieceType type;
    private boolean first;
    private final Team t;

    Piece(Board b,PieceType t, int r, int c,Team team) {
        this.board = b;
        this.row = r;
        this.column = c;
        this.t = team;
        this.first=true;
        this.type=t;
        this.id = (t.equals(PieceType.QUEEN) ? "q"
                : t.equals(PieceType.KING) ? "k"
                : t.equals(PieceType.BISHOP) ? "b"
                : t.equals(PieceType.ROOK) ? "r"
                : t.equals(PieceType.KNIGHT) ? "n"
                : t.equals(PieceType.PAWN) ? "p" : " ");

    }

    public String getId() {
        if (t.equals(Team.WHITE)) {
            id=id.toUpperCase();
        }
        return id+xAxis[column]+(8-row);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isFirst() {
        return first;
    }
    public Team getTeam() {
        return t;
    }

    public void setRow(int r) {
         this.row = r;
    }

    public void setColumn(int c) {
        this.column = c;
    }

    public int[] myKingPosition() {
        int[] kPos = new int[2];

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                if (this.board.getPiece(j, i) != null && this.board.getPiece(j, i).getTeam().equals(this.getTeam()) && this.board.getPiece(j, i).getType().equals(PieceType.KING)) {
                    kPos[0] = j;
                    kPos[1] = i;
                    break;
                }
            }
        }

        return kPos;
    }

    public boolean posIsSafe(Board b, int x, int y) {
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                if (b.getPiece(j, i) != null && !b.getPiece(j, i).getTeam().equals(this.t) && b.getPiece(j, i).onRange(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean myKingIsSafe(Board b) {
        int xKing;
        int yKing;
        if (this.getType().equals(PieceType.KING)) {
            xKing = this.row;
            yKing = this.column;
        } else {
            int[] kPos = this.myKingPosition();
            xKing = kPos[0];
            yKing = kPos[1];
        }

        return this.posIsSafe(b, xKing, yKing);
    }

    public abstract boolean onRange(int var1, int var2);

    public boolean execMove(int l, int c) {                 // Se conseguir chegar na casa...
        if (this.board.getPiece(l, c) == null || !this.board.getPiece(l, c).getTeam().equals(this.getTeam())) {   // Se nao tiver uma peca de mesma cor ou estiver vazio...
            if (this.board.getPiece(l, c) != null && !this.board.getPiece(l, c).getTeam().equals(this.getTeam()))
                this.board.removePiece(l, c);                                  // Se tiver uma peca de outra cor, mata! e se move
            this.board.movePiece(this, l, c);                           // Se estiver vazio, apenas se move
            this.row = l;
            this.column = c;
            first = false;
            return true;
        }
        return false;
    }

    public boolean move(int l, int c) {                         // Se o movimento for possivel, executa o movimento
        if (l < 0 || l >= 8 || c < 0 || c >= 8) return false;

        if (onRange(l, c)) {
            return execMove(l, c);
        }

        return false;
    }


    public String toString() {
            return getId() + (((type.equals(PieceType.KING) || type.equals(PieceType.ROOK)) && this.isFirst()) ? "*" : " ");
    }

}
