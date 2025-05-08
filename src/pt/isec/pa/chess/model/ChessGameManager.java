package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.pieces.Team;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.chess.model.ChessGameSerialization.*;

public class ChessGameManager implements Constants{


    private ChessGame ChessGame;

    public ChessGameManager() {
        ChessGame = null;
    }

    public void createGame() {
        ChessGame = new ChessGame();
    }
    public boolean gameExists() {
        return ChessGame != null;
    }


    public String getWhitePlayer() { return ChessGame.getpWhite(); }

    public String getBlackPlayer() {
        return ChessGame.getpBlack();
    }

    public void setWhitePlayer(String pWhite) {
        ChessGame.setpWhite(pWhite);
    }
    public void setBlackPlayer(String pBlack) {
        ChessGame.setpBlack(pBlack);
    }


    public int getBoardSize() { return TAM; }
    public Character getXAxis(int i) { return xAxis[i]; }

    public String GMgetSelectedPiece() {return ChessGame.getSelectedPiece(); }
    public ArrayList<String> GMgetPieces() { return ChessGame.getPieces(); }
    public boolean GMselectPiece(int row, int col) {
        return ChessGame.selectPiece(row, col);
    }
    public List<Integer[]> GMgetPiecePossibilities() { return ChessGame.getPiecePossibilities(); }

    public boolean GMmakeAMove(int row, int col) {
        return ChessGame.makeAMove(row, col);
    }
    public int GMgetRound() {
        return ChessGame.getRound();
    }
    public Team GMgetTeam() {
        return ChessGame.currentPlayer();
    }


    public boolean GMisOver() { return ChessGame.isOver(); }













    public void importThis(String filename) throws IOException {
        createGame();
        ChessGame.importGame(filename);
    }

    public void exportThis(String filename) throws IOException {
        if(ChessGame != null) {
            ChessGame.exportGame(filename);
        }

    }

    public void openThis(String filename) throws IOException, ClassNotFoundException {
        ChessGame = deserialize(filename);
    }

    public void saveThis(String filename) throws IOException {
        if(ChessGame != null) {
            serialize(filename, ChessGame);
        }
    }

}
