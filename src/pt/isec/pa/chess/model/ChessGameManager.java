package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.pieces.Team;

import java.io.IOException;

import static pt.isec.pa.chess.model.ChessGameSerialization.*;

public class ChessGameManager {

    private ChessGame ChessGame;

    public ChessGameManager() {
        ChessGame = new ChessGame();
    }

    public void createGame() {
        ChessGame = new ChessGame();
    }
    public boolean gameExists() {
        return ChessGame != null;
    }

    public String getWhitePlayer() {
        return ChessGame.getpWhite();
    }
    public String getBlackPlayer() {
        return ChessGame.getpBlack();
    }

    public void setWhitePlayer(String pWhite) {
        ChessGame.setpWhite(pWhite);
    }
    public void setBlackPlayer(String pBlack) {
        ChessGame.setpBlack(pBlack);
    }




    public boolean GMselectPiece(int row, int col) {
        return ChessGame.selectPiece(row, col);
    }
    public boolean GMmakeAMove(int row, int col) {
        return ChessGame.makeAMove(row, col);
    }
    public int GMgetRound() {
        return ChessGame.getRound();
    }
    public Team GMgetTeam() {
        return ChessGame.currentPlayer();
    }
    public boolean GMisNotOver() {
        return ChessGame.isNotOver();
    }












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
