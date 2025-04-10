package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;

public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING,


    ENPASSANT;

    public Piece createPiece(Board b, int r, int c, Team t) {
        return switch (this) {
            case PAWN ->    new Pawn(b, r, c, t);
            case KNIGHT ->  new Knight(b, r, c, t);
            case BISHOP ->  new Bishop(b, r, c, t);
            case ROOK ->    new Rook(b, r, c, t);
            case QUEEN ->   new Queen(b, r, c, t);
            case KING ->    new King(b, r, c, t);
            default -> null;
        };
    }


}
