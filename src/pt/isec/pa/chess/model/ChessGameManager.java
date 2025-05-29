package pt.isec.pa.chess.model;


import pt.isec.pa.chess.model.command.CommandManager;
import pt.isec.pa.chess.model.command.ICommand;
import pt.isec.pa.chess.model.command.MovePieceCommand;
import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.PieceType;
import pt.isec.pa.chess.model.data.pieces.Team;
import pt.isec.pa.chess.model.command.PROP;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.chess.model.ChessGameSerialization.*;
import static pt.isec.pa.chess.model.data.pieces.PieceType.PAWN;

public class ChessGameManager implements Constants{


    private ChessGame ChessGame;

    private final PropertyChangeSupport pcs;
    private final CommandManager cmdMgr = new CommandManager();

    private Boolean showPossibleMoves = false;

    public ChessGameManager() {
        ChessGame = null;
        pcs = new PropertyChangeSupport(this);
    }

    public boolean gameExists() {
        return ChessGame != null;
    }
    public void createGame() {
        boolean gameExists = gameExists();
        ChessGame = new ChessGame();

        if (!gameExists)    pcs.firePropertyChange(PROP.initGame, null, null);
        else                pcs.firePropertyChange(PROP.newGame, null, null);

    }

    public String getWhitePlayer() { return ChessGame.getpWhite(); }
    public String getBlackPlayer() {
        return ChessGame.getpBlack();
    }
    public void setWhitePlayer(String pWhite) {
        ChessGame.setpWhite(pWhite);
        pcs.firePropertyChange(PROP.setName, null, null);
    }
    public void setBlackPlayer(String pBlack) {
        ChessGame.setpBlack(pBlack);
        pcs.firePropertyChange(PROP.setName, null, null);
    }


    public int getBoardSize() { return TAM; }
    public Character getXAxis(int i) { return xAxis[i]; }

    public String GMgetSelectedPiece() { return ChessGame.getSelectedPiece(); }
    public Integer[] GMgetPieceCoords(String pieceId) { return ChessGame.getPieceCoords(pieceId); }
    public ArrayList<String> GMgetPieces() { return ChessGame.getPieces(); }

    public boolean changeShowPossibleMoves(boolean show) {
        showPossibleMoves = show;
        pcs.firePropertyChange(PROP.showMoves,null,null);
        pcs.firePropertyChange(PROP.select,null,null);
        return showPossibleMoves;
    }
    public boolean getShowPossibleMoves() { return showPossibleMoves; }

    public List<Integer[]> GMgetPiecePossibilities() { return ChessGame.getPiecePossibilities(); }
    public boolean GMselectPiece(int row, int col) {
        if (ChessGame.selectPiece(row, col)) {
            pcs.firePropertyChange(PROP.select,null,null);
            return true;
        }
        return false;
    }

    /*
    public boolean GMmakeAMove(int row, int col) {
        String piece = GMgetSelectedPiece();
        if (ChessGame.makeAMove(row, col)) {
            pcs.firePropertyChange(PROP.newRound,null,null);
            ModelLog.getInstance().addLog(piece + " moveu para " + Character.toLowerCase(xAxis[col]) + (8 - row));
            return true;
        }
        return false;
    }
     */

    public boolean GMmakeAMove(int row, int col){
        String piece = GMgetSelectedPiece();
        if (piece == null) return false;

        Integer [] orig = GMgetPieceCoords(piece);
        if (orig == null) return false;

        Piece selected = getBoard().getPiece(orig[0], orig[1]);
        if (selected == null || selected.getTeam() != GMgetTeam()) return false;


        ICommand cmd = new MovePieceCommand(this, orig[0], orig[1], row, col);
        if (cmdMgr.invokeCommand(cmd)) {
            if (selected.getType().equals(PAWN) && (row == 0 || row == 7)) {
                ChessGame.setPromotion(true);
                ChessGame.setPieceToPromote(selected);
                pcs.firePropertyChange(PROP.promWindow, null, null);
            } else {
                ChessGame.setPromotion(false);
            }
            pcs.firePropertyChange(PROP.newRound,null,null);
            ModelLog.getInstance().addLog(piece + " moveu para " + Character.toLowerCase(xAxis[col]) + (8 - row));

            return true;
        }
        return false;
    }

    public boolean GMisPromotion() { return ChessGame.isPromotion(); }

    public void GMpromote(PieceType type) {
        if (GMisPromotion()) {
            String pid = ChessGame.getPieceToPromote();
            if (ChessGame.promote(type)) {
                ModelLog.getInstance().addLog(pid + " foi promovido para " + type.toString().toLowerCase());
                pcs.firePropertyChange(PROP.newRound,null,null);
            }
        }
    }

    public int GMgetRound() {
        return ChessGame.getRound();
    }
    public Team GMgetTeam() {
        return ChessGame.currentPlayer();
    }

    public boolean GMisOver() { return ChessGame.isOver(); }

    public boolean isChecked(){return ChessGame.isCheck();}

    public boolean GMkilled(){
        return ChessGame.Killed();
    }

    public void importThis(String filename) throws IOException {
        if (!gameExists())
            createGame();

        ChessGame.importGame(filename);
        pcs.firePropertyChange(PROP.newGame, null, null);
        ModelLog.getInstance().addLog("Jogo importado");
    }

    public void exportThis(String filename) throws IOException {
        if(ChessGame != null) {
            ChessGame.exportGame(filename);
        }
        ModelLog.getInstance().addLog("Jogo exportado");

    }

    public void openThis(String filename) throws IOException, ClassNotFoundException {
        boolean gameExists = gameExists();
        ChessGame = deserialize(filename);

        if (!gameExists)    pcs.firePropertyChange(PROP.initGame, null, null);
        else                pcs.firePropertyChange(PROP.newGame, null, null);

        ModelLog.getInstance().addLog("Jogo aberto");
    }

    public void saveThis(String filename) throws IOException {
        if(ChessGame != null) {
            serialize(filename, ChessGame);
        }
        ModelLog.getInstance().addLog("Jogo gravado");
    }



    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(prop, listener);
    }

    public boolean undo() {
        boolean ok = cmdMgr.undo();
        if (ok) { pcs.firePropertyChange(PROP.newRound, null, null);}
        return ok;
    }

    public boolean redo() {
        boolean ok = cmdMgr.redo();
        if (ok) { pcs.firePropertyChange(PROP.newRound, null, null);}
        return ok;
    }
    public boolean hasRedo() {return cmdMgr.hasRedo();}
    public boolean hasUndo() {return cmdMgr.hasUndo();}


    public Board getBoard() {
        return ChessGame.getBoard();
    }

    public void nextRound() {
        ChessGame.nextRound();
    }

    public void retreatRound() {
        ChessGame.retreatTurn();
        ChessGame.setTeam();
    }


    public boolean makeMoveFromCommand(int fromRow, int fromCol, int toRow, int toCol) {
        Piece selected = ChessGame.getBoard().getPiece(fromRow, fromCol);
        if (selected == null || selected.getTeam() != GMgetTeam())
            return false;

        if (selected.move(toRow, toCol)) {
            ChessGame.unselectPiece();
            return true;
        }

        return false;
    }









}
