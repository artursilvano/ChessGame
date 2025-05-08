package pt.isec.pa.chess.ui.elements;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;

public class FullBoard extends BorderPane{

    private final ChessGameManager gameManager;
    private final RootPane root;
    private final int WidthHeight;

    private ChessBoard board;

    public FullBoard(ChessGameManager gameManager, RootPane root) {
        this.gameManager = gameManager;
        this.root = root;
        this.setMaxWidth(700);
        this.setMaxHeight(700);
        WidthHeight = (int) (this.getMaxWidth() * 0.9);

        createViews();
        update();
    }

    private void createViews(){
        board = new ChessBoard(this.gameManager, WidthHeight, root);
        setCenter(board);

        setBottom( new BoardColumnCoords(gameManager, WidthHeight) );
        setLeft( new BoardRowCoords(gameManager, WidthHeight) );
    }


    public void update() {
        board.update();
    }
}
