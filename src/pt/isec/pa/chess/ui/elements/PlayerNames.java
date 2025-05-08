package pt.isec.pa.chess.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.Team;

public class PlayerNames extends VBox {
    private Label lblPlayer1;
    private Label lblPlayer2;
    private Label lblCurrentPlayer;
    private ChessGameManager gameManager;

    public PlayerNames(ChessGameManager gameManager) {
        this.gameManager = gameManager;
        lblPlayer1 = new Label();
        lblPlayer2 = new Label();
        lblCurrentPlayer = new Label();

        this.setSpacing(10);
        this.getChildren().addAll(lblPlayer1, lblPlayer2, lblCurrentPlayer);
    }

    public void update() {
        lblPlayer1.setText("Player 1: " + gameManager.getWhitePlayer());
        lblPlayer2.setText("Player 2: " + gameManager.getBlackPlayer());
        String current = (gameManager.GMgetTeam() == Team.WHITE
                ? gameManager.getWhitePlayer()
                : gameManager.getBlackPlayer());
        lblCurrentPlayer.setText("Current Player: " + gameManager.GMgetTeam().toString() + " - " + current);
    }



}
