package pt.isec.pa.chess.ui.elements;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.command.PROP;
import pt.isec.pa.chess.model.data.pieces.PieceType;
import pt.isec.pa.chess.model.data.pieces.Team;
import pt.isec.pa.chess.ui.res.ImageManager;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.beans.PropertyChangeSupport;

import static pt.isec.pa.chess.model.Constants.TAM;
import static pt.isec.pa.chess.model.Constants.xAxis;

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


        gameManager.addPropertyChangeListener(PROP.newGame,event -> update());
        gameManager.addPropertyChangeListener(PROP.newRound, event -> update());
        gameManager.addPropertyChangeListener(PROP.select, event -> markSelected());
        gameManager.addPropertyChangeListener(PROP.promWindow, event -> openPromotion());

        if (this.gameManager.gameExists())
            handleSizeChange();

    }

    public void handleSizeChange() {
        wd = fb.getMinSize() * 0.90;

        this.setWidth(wd);      // Define tamanho
        this.setHeight(wd);
        size = wd / gameManager.getBoardSize();    // Especifica tamanho das casas

        update();
    }


    public void registerHandlers() {
        if(gameManager.gameExists()){
            this.setOnMouseClicked(event -> {
                if (!gameManager.GMisPromotion()) {

                    double x = event.getX();
                    double y = event.getY();
                    int row = (int) (y / size);
                    int column = (int) (x / size);


                    if (gameManager.GMgetSelectedPiece() != null) {
                        String selectedPiece = gameManager.GMgetSelectedPiece();

                        if (gameManager.GMmakeAMove(row, column)) {
                            //sounds
                            if (SoundCheckBox.isChecked()) {
                                if (Character.isUpperCase(selectedPiece.charAt(0))) {
                                    SoundManager.play("white.mp3");
                                } else {
                                    SoundManager.play("black.mp3");
                                }//Black or White

                                StringBuilder type = new StringBuilder();

                                switch (Character.toUpperCase(selectedPiece.charAt(0))) {
                                    case 'P':
                                        type.append("pawn");
                                        break;
                                    case 'N':
                                        type.append("knight");
                                        break;
                                    case 'B':
                                        type.append("bishop");
                                        break;
                                    case 'R':
                                        type.append("rook");
                                        break;
                                    case 'Q':
                                        type.append("queen");
                                        break;
                                    case 'K':
                                        type.append("king");
                                        break;
                                }

                                type.append(".mp3");
                                SoundManager.play(type.toString());//Piece Type

                                SoundManager.play((Character.toLowerCase(selectedPiece.charAt(1))) + ".mp3");
                                SoundManager.play((Character.toLowerCase(selectedPiece.charAt(2))) + ".mp3");//Inicial position

                                SoundManager.play(xAxis[column] + ".mp3");
                                SoundManager.play(TAM - row + ".mp3");//Final position

                                if (gameManager.isChecked()) {
                                    SoundManager.play("check.mp3");
                                }

                                if (gameManager.GMkilled()) {
                                    SoundManager.play("captured.mp3");
                                }


                            }


                            return;
                        }

                    }


                    gameManager.GMselectPiece(row, column);
                }
            });

        }
    }



    public void update() {
        marked = false;
        GraphicsContext gc = this.getGraphicsContext2D();

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setGlobalAlpha(1);

        drawBoard();
        drawPieces();

        if (gameManager.GMgetSelectedPiece() != null)
            markSelected();

        if (gameManager.GMisOver())
            endGame();




        registerHandlers();
    }








    public void drawBoard() {
        GraphicsContext gc = this.getGraphicsContext2D();
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

    public void drawPieces() {
        GraphicsContext gc = this.getGraphicsContext2D();
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

    public void markSelected() {
        GraphicsContext gc = this.getGraphicsContext2D();
        if (!marked && gameManager.GMgetSelectedPiece() != null) {
            Integer[] pieceCoords = gameManager.GMgetPieceCoords(gameManager.GMgetSelectedPiece());
            gc.setFill(Color.GREEN);
            gc.setGlobalAlpha(0.2);
            gc.fillRect(size * pieceCoords[1], size * pieceCoords[0], size, size);
            selectedPiece=gameManager.GMgetSelectedPiece();

            if (gameManager.getShowPossibleMoves()) {
                gc.setFill(Color.RED);
                for (Integer[] pos : gameManager.GMgetPiecePossibilities())
                    gc.fillOval(size * pos[1], size * pos[0], size, size);
            }

            marked = true;
        } else {
            marked = false;
            update();
        }

    }

    public void endGame() {
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

    public void openPromotion() {
        if (gameManager.GMisPromotion()) {
            Stage janela = new Stage();
            janela.setResizable(false);
            janela.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                }
            });
            janela.setTitle("Promotion");

            Button knight = new Button();
            Button bishop = new Button();
            Button rook = new Button();
            Button queen = new Button();

            knight.setPrefSize(size, size);
            ImageView view = new ImageView(ImageManager.getImage("pieces/knightW.png"));
            view.setFitHeight(size);
            view.setPreserveRatio(true);
            knight.setGraphic(view);

            bishop.setPrefSize(size, size);
            view = new ImageView(ImageManager.getImage("pieces/bishopW.png"));
            view.setFitHeight(size);
            view.setPreserveRatio(true);
            bishop.setGraphic(view);

            rook.setPrefSize(size, size);
            view = new ImageView(ImageManager.getImage("pieces/rookW.png"));
            view.setFitHeight(size);
            view.setPreserveRatio(true);
            rook.setGraphic(view);

            queen.setPrefSize(size, size);
            view = new ImageView(ImageManager.getImage("pieces/queenW.png"));
            view.setFitHeight(size);
            view.setPreserveRatio(true);
            queen.setGraphic(view);

            knight.setOnAction(event -> {
                gameManager.GMpromote(PieceType.KNIGHT);
                janela.close();
            });
            bishop.setOnAction(event -> {
                gameManager.GMpromote(PieceType.BISHOP);
                janela.close();
            });
            rook.setOnAction(event -> {
                gameManager.GMpromote(PieceType.ROOK);
                janela.close();
            });
            queen.setOnAction(event -> {
                gameManager.GMpromote(PieceType.QUEEN);
                janela.close();
            });


            HBox window = new HBox(knight, bishop, rook, queen);
            window.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Scene scene = new Scene(window, size * 5, size * 1.5);
            janela.setScene(scene);

            janela.showAndWait();
        }


    }



}

