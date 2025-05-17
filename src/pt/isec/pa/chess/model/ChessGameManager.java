package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.pieces.Team;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.chess.model.ChessGameSerialization.*;

public class ChessGameManager implements Constants{


    private ChessGame ChessGame;

    private final PropertyChangeSupport pcs;

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


    public List<Integer[]> GMgetPiecePossibilities() { return ChessGame.getPiecePossibilities(); }
    public boolean GMselectPiece(int row, int col) {
        if (ChessGame.selectPiece(row, col)) {
            pcs.firePropertyChange(PROP.select,null,null);
            return true;
        }
        return false;
    }
    public boolean GMmakeAMove(int row, int col) {
        String piece = GMgetSelectedPiece();
         if (ChessGame.makeAMove(row, col)) {
             pcs.firePropertyChange(PROP.newRound,null,null);
             ModelLog.getInstance().addLog(piece + " moveu para " + Character.toLowerCase(xAxis[col]) + (8 - row));
             return true;
         }
        return false;
    }

    public int GMgetRound() {
        return ChessGame.getRound();
    }
    public Team GMgetTeam() {
        return ChessGame.currentPlayer();
    }

    public boolean GMisOver() { return ChessGame.isOver(); }




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


}
