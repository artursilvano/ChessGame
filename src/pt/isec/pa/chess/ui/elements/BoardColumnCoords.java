package pt.isec.pa.chess.ui.elements;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.chess.model.ChessGameManager;

public class BoardColumnCoords extends HBox {

    private final FullBoard fb;
    private final ChessGameManager gameManager;

    BoardColumnCoords(ChessGameManager gameManager, FullBoard fb) {
        this.fb = fb;
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);

        handleSizeChange();
    }

    public void handleSizeChange() {
        this.getChildren().clear();
        this.setWidth(fb.getMinSize() * 0.9);
        this.setHeight(fb.getMinSize() * 0.05);

        Label l;
        for (int i = 0; i < gameManager.getBoardSize(); i++) {
            l = new Label(Character.toString(gameManager.getXAxis(i)));
            l.setPrefWidth(this.getWidth() / gameManager.getBoardSize());
            l.setAlignment(Pos.CENTER);
            l.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            l.setStyle("-fx-text-fill: #000000;" +
                    "-fx-background-color: #f0d9b5;" +
                    "-fx-font-size: 12px;" +
                    "-fx-padding: 5px;" +
                    "-fx-border-color: #000000;"
            );
            this.getChildren().add(l);
        }
    }
}
