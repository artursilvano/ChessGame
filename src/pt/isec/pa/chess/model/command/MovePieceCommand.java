package pt.isec.pa.chess.model.command;

import pt.isec.pa.chess.model.ChessGame;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.King;
import pt.isec.pa.chess.model.data.pieces.Pawn;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.PieceType;

public class MovePieceCommand implements ICommand {
    private final ChessGameManager manager;
    private final int fromRow, fromCol, toRow, toCol;
    private Piece movedPiece;
    private Piece capturedPiece;
    private boolean originalFirstMove;

    // En Passant
    private Piece capturedEnPassantPawn;
    private int capturedEnPassantRow, capturedEnPassantCol;
    private Piece enPassantPieceCreated;
    private int enPassantRow, enPassantCol;

    // Rook
    private boolean wasCastling;
    private Piece rookMoved;
    private int rookFromRow, rookFromCol;
    private int rookToRow, rookToCol;
    private boolean rookOriginalFirstMove;

    public MovePieceCommand(ChessGameManager game, int fromRow, int fromCol, int toRow, int toCol) {
        this.manager = game;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    @Override
    public boolean execute() {
        movedPiece = manager.getBoard().getPiece(fromRow, fromCol);
        capturedPiece = manager.getBoard().getPiece(toRow, toCol);

        if (movedPiece == null)
            return false;

        originalFirstMove = movedPiece.isFirst();

        //enPassant
        if (movedPiece instanceof Pawn) {
            Piece ep = manager.getBoard().getEnPassant(toRow, toCol);
            if (ep != null) {
                capturedEnPassantRow = ep.getRow();
                capturedEnPassantCol = ep.getColumn();
                capturedEnPassantPawn = manager.getBoard().getPiece(capturedEnPassantRow, capturedEnPassantCol);
            }

            if (Math.abs(toRow - fromRow) == 2) {
                enPassantRow = (fromRow + toRow) / 2;
                enPassantCol = fromCol;
                enPassantPieceCreated = manager.getBoard().getEnPassant(enPassantRow, enPassantCol);
            }
        }

        //Castling
        if (movedPiece instanceof King king) {
            if (king.isRoque(toRow, toCol)) {
                wasCastling = true;

                rookMoved = manager.getBoard().getPiece(toRow, toCol);
                if (rookMoved != null && rookMoved.getType() == PieceType.ROOK) {
                    rookFromRow = rookMoved.getRow();
                    rookFromCol = rookMoved.getColumn();

                    if (fromCol > toCol) { // queenside castling (long castling)
                        rookToCol = fromCol - 1;
                    } else { // kingside castling (short castling)
                        rookToCol = fromCol + 1;
                    }
                    rookToRow = fromRow;

                    rookOriginalFirstMove = rookMoved.isFirst();
                }
            }
        }

        if (manager.makeMoveFromCommand(fromRow, fromCol, toRow, toCol)) {
            manager.nextRound();
            return true;
        }
        return false;

    }

    @Override
    public boolean undo() {
        manager.getBoard().removePiece(toRow, toCol);
        manager.getBoard().addPiece(movedPiece, fromRow, fromCol);
        movedPiece.setColumn(fromCol);
        movedPiece.setRow(fromRow);

        movedPiece.setFirst(originalFirstMove);

        if (capturedPiece != null) {
            manager.getBoard().addPiece(capturedPiece, toRow, toCol);
        }

        //enPassant
        if (capturedEnPassantPawn != null) {
            manager.getBoard().addPiece(capturedEnPassantPawn, capturedEnPassantRow, capturedEnPassantCol);
        }
        if (enPassantPieceCreated != null) {
            manager.getBoard().removePiece(enPassantRow, enPassantCol);
        }

        // Castling
        if (wasCastling && rookMoved != null) {
            manager.getBoard().removePiece(rookToRow, rookToCol);
            manager.getBoard().addPiece(rookMoved, rookFromRow, rookFromCol);
            rookMoved.setRow(rookFromRow);
            rookMoved.setColumn(rookFromCol);
            rookMoved.setFirst(rookOriginalFirstMove);
        }

        manager.retreatRound();
        return true;
    }
}
