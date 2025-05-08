package pt.isec.pa.chess.ui.elements;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.Team;
import pt.isec.pa.chess.ui.res.ImageManager;


public class ChessBoard extends Canvas {

    private final ChessGameManager gameManager;
    private final RootPane root;


    private final double size;

    public ChessBoard(ChessGameManager gameManager, int wh, RootPane root) {
        this.gameManager = gameManager;
        this.root = root;

        this.setHeight(wh);
        this.setWidth(wh);

        size = this.getWidth() / gameManager.getBoardSize();

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());


        update();
        registerHandlers();
    }


    public void registerHandlers() {
        if(gameManager.gameExists()){
        this.setOnMouseClicked(event -> {

            double x = event.getX();
            double y = event.getY();
            int row = (int) (y / size);
            int column = (int) (x / size);

            if (gameManager.GMgetSelectedPiece() == null) {
                if (gameManager.GMselectPiece(row, column)) {
                    GraphicsContext gc = this.getGraphicsContext2D();
                    gc.setFill(Color.GREEN);
                    gc.setGlobalAlpha(0.2);
                    gc.fillRect(size * column, size * row, size, size);


                    /* Se ligar "Show Possible Moves", aparece isso

                    gc.setFill(Color.RED);
                    for (Integer[] pos : gameManager.GMgetPiecePossibilities()) {
                        System.out.println(pos[0] + " " + pos[1]);
                        gc.fillOval(size * pos[1], size * pos[0], size, size);
                    }
                     */
                }
            } else {
                if (gameManager.GMmakeAMove(row, column)) {
                    root.update();
                }
            }

        });
        }
    }

    public void update() {

        GraphicsContext gc = this.getGraphicsContext2D();

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setGlobalAlpha(1);

        for (int i = 0; i < gameManager.getBoardSize(); i++) {
            for (int j = 0; j < gameManager.getBoardSize(); j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.valueOf("#C8A776"));
                } else {
                    gc.setFill(Color.valueOf("#805531"));
                }
                gc.fillRect(size * j, size * i, size * (j + 1), size * (i + 1));
            }
        }
        StringBuilder type = new StringBuilder();
        int row, column;

        for(String p : gameManager.GMgetPieces()) {
            type.append("pieces/");
            switch(Character.toUpperCase(p.charAt(0))) {
                case 'P': type.append("pawn"); break;
                case 'N': type.append("knight"); break;
                case 'B': type.append("bishop"); break;
                case 'R': type.append("rook"); break;
                case 'Q': type.append("queen"); break;
                case 'K': type.append("king"); break;
            }
            if (Character.isUpperCase(p.charAt(0))) type.append("W");
            else type.append("B");
            type.append(".png");

            Image image = ImageManager.getImage(type.toString());

            row = gameManager.getBoardSize() - Character.getNumericValue(p.charAt(2));
            column = gameManager.columnToNum(Character.toUpperCase(p.charAt(1)));

            gc.drawImage(image, size * column, size * row, size, size);
            type.delete(0, type.length());
        }

        if (gameManager.GMisOver()) {
            Stage janela = new Stage();
            janela.setTitle("FIM DE JOGO");
            janela.setResizable(false);

            Label lbl = new Label((gameManager.GMgetTeam().equals(Team.WHITE) ? gameManager.getBlackPlayer() : gameManager.getWhitePlayer()) + " Venceu o jogo!");

            VBox layout = new VBox(10, lbl);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

            Scene scene = new Scene(layout, 250, 150);
            janela.setScene(scene);
            janela.showAndWait();
        }

    }
}
