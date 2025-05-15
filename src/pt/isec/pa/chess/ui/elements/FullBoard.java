package pt.isec.pa.chess.ui.elements;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;

import static java.lang.Math.min;

public class FullBoard extends BorderPane{

    private final ChessGameManager gameManager;
    private final RootPane root;
    private double WidthHeight;

    private ChessBoard board;
    private BoardRowCoords brt, brb;
    private BoardColumnCoords bcl, bcr;

    public FullBoard(ChessGameManager gameManager, RootPane root) {
        this.gameManager = gameManager;
        this.root = root;
        this.setMinHeight(300);
        this.setMinWidth(300);

        createViews();
        update();
    }

    private void createViews(){
        board = new ChessBoard(this.gameManager, this);
        setCenter(board);

        brt = new BoardRowCoords(gameManager, this);
        brb = new BoardRowCoords(gameManager, this);
        bcl = new BoardColumnCoords(gameManager, this);
        bcr = new BoardColumnCoords(gameManager, this);
        setLeft(brt);
        setRight(brb);
        setTop(bcl);
        setBottom(bcr);
    }


    public void update() {
        WidthHeight = root.getMinSize() * 0.85;
        this.setMaxHeight(WidthHeight);
        this.setMaxWidth(WidthHeight);

        board.handleSizeChange();
        brt.handleSizeChange();
        brb.handleSizeChange();
        bcl.handleSizeChange();
        bcr.handleSizeChange();
    }

    public double getMinSize() {
        return WidthHeight;
    }
}
