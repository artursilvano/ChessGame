package pt.isec.pa.chess.ui.elements;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.Team;
import pt.isec.pa.chess.ui.res.ImageManager;

import java.beans.PropertyChangeSupport;

public class ChessBoard extends Canvas {

    private final ChessGameManager gameManager;
    private final FullBoard fb;

    private double size;
    private double wd;

    private boolean marked = false;
    private boolean isOver = false;
    private String selectedPiece;
    PropertyChangeSupport pcs;

    public ChessBoard(ChessGameManager gameManager, FullBoard fb) {
        this.gameManager = gameManager;
        this.fb = fb;
        pcs = new PropertyChangeSupport(this);


        gameManager.addPropertyChangeListener("newGame", event -> update());
        gameManager.addPropertyChangeListener("newRound", event -> update());
        gameManager.addPropertyChangeListener("select", event -> markSelected(this.getGraphicsContext2D()));

        if (this.gameManager.gameExists())
            handleSizeChange();

    }

    public void handleSizeChange() {
        wd = fb.getMinSize() * 0.90;

        this.setWidth(wd);      // Define tamanho
        this.setHeight(wd);
        size = wd / gameManager.getBoardSize();    // Especifica tamanho das casas

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
                    gameManager.GMselectPiece(row, column);
                } else {
                    gameManager.GMmakeAMove(row, column);
                }
            });

        }
    }


    public void update() {
        marked = false;
        GraphicsContext gc = this.getGraphicsContext2D();

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setGlobalAlpha(1);

        drawBoard(gc);
        drawPieces(gc);

        if (gameManager.GMgetSelectedPiece() != null)
            markSelected(gc);

        if (gameManager.GMisOver())
            endGame(gc);

    }














    public void drawBoard(GraphicsContext gc) {
        for (int i = 0; i < gameManager.getBoardSize(); i++) {              // Cria Tabuleiro
            for (int j = 0; j < gameManager.getBoardSize(); j++) {
                if ((i + j) % 2 == 0)
                    gc.setFill(Color.valueOf("#C8A776"));
                else
                    gc.setFill(Color.valueOf("#805531"));

                gc.fillRect(size * j, size * i, size * (j + 1), size * (i + 1));
            }
        }
    }

    public void drawPieces(GraphicsContext gc) {
        StringBuilder type = new StringBuilder();                        // Poe pecas no tabuleiro
        Integer[] rc;
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

            rc = gameManager.GMgetPieceCoords(p);

            gc.drawImage(image, size * rc[1], size * rc[0], size, size);
            type.delete(0, type.length());
        }
    }

    public void markSelected(GraphicsContext gc) {
        if (!marked) {
            Integer[] pieceCoords = gameManager.GMgetPieceCoords(gameManager.GMgetSelectedPiece());
            gc.setFill(Color.GREEN);
            gc.setGlobalAlpha(0.2);
            gc.fillRect(size * pieceCoords[1], size * pieceCoords[0], size, size);
            selectedPiece=gameManager.GMgetSelectedPiece();
            /*
            gc.setFill(Color.RED);
            for (Integer[] pos : gameManager.GMgetPiecePossibilities())
                gc.fillOval(size * pos[1], size * pos[0], size, size);
            */
            marked = true;
        }

    }

    public void endGame(GraphicsContext gc) {
        if (!isOver) {
            isOver = true;
            Stage janela = new Stage();
            Button btnOk = new Button("OK");
            janela.setTitle("FIM DE JOGO");
            janela.setResizable(false);

            Label lbl = new Label((gameManager.GMgetTeam().equals(Team.WHITE) ? gameManager.getBlackPlayer() : gameManager.getWhitePlayer()) + " Venceu o jogo!");

            btnOk.setOnAction(_ -> {
                janela.close();
            });

            VBox layout = new VBox(10, lbl, btnOk);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

            Scene scene = new Scene(layout, 250, 150);
            janela.setScene(scene);
            janela.showAndWait();
        }
    }

}
