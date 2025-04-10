package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public class enPassant extends Piece {
    String idPawn;

    public enPassant(Board b, int x, int y,Team c, String pId) {
        super(b, PieceType.ENPASSANT, x, y, c);
        setId("enP");
        setFirst_False();
        idPawn = pId; // Id do peao correspondente
    }

    public boolean onRange(int x, int y) {
        return false;
    }
    public boolean especialMove(int l, int c) {return true;}
    @Override
    public String getId() {
        return " . ";
    }
}
