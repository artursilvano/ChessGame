package pt.isec.pa.chess.model;


import pt.isec.pa.chess.model.command.CommandManager;
import pt.isec.pa.chess.model.command.ICommand;
import pt.isec.pa.chess.model.command.MovePieceCommand;
import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.PieceType;
import pt.isec.pa.chess.model.data.pieces.Team;
import pt.isec.pa.chess.model.command.PROP;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.chess.model.ChessGameSerialization.*;
import static pt.isec.pa.chess.model.data.pieces.PieceType.PAWN;

/**
 * ChessGameManager é a Facade do modelo de dados (Model Data), responsável por levar e trazer ações/informações da UI para o modelo de dados
 * @author Artur Capelossi, Diogo Beja e Nikolay Grachev
 */
public class ChessGameManager implements Constants{


    private ChessGame ChessGame;

    private final PropertyChangeSupport pcs;
    private final CommandManager cmdMgr = new CommandManager();

    private Boolean showPossibleMoves = false;

    /**
     * Default Constructor
     */
    public ChessGameManager() {
        ChessGame = null;
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Verifica existência de um ChessGame associado
     * @return true se o ChessGame existir, false caso contrário
     */
    public boolean gameExists() {
        return ChessGame != null;
    }

    /**
     * Cria um jogo e envia o devido sinal para a UI atualizar
     */
    public void createGame() {
        boolean gameExists = gameExists();
        ChessGame = new ChessGame();

        if (!gameExists)    pcs.firePropertyChange(PROP.initGame, null, null);
        else                pcs.firePropertyChange(PROP.newGame, null, null);

    }


    /**
     * Busca nome do jogador da equipa WHITE
     * @return jogador da equipa WHITE
     */
    public String getWhitePlayer() { return ChessGame.getpWhite(); }
    /**
     * Busca nome do jogador da equipa BLACK
     * @return jogador da equipa BLACK
     */
    public String getBlackPlayer() {
        return ChessGame.getpBlack();
    }

    /**
     * Define nome do jogador da equipa WHITE
     * @param pWhite nome do jogador da equipa WHITE
     */
    public void setWhitePlayer(String pWhite) {
        ChessGame.setpWhite(pWhite);
        pcs.firePropertyChange(PROP.setName, null, null);
    }
    /**
     * Define nome do jogador da equipa BLACK
     * @param pBlack nome do jogador da equipa BLACK
     */
    public void setBlackPlayer(String pBlack) {
        ChessGame.setpBlack(pBlack);
        pcs.firePropertyChange(PROP.setName, null, null);
    }

    /**
     * Obtém tamanho (número de células em cada direção) da Board
     * @return tamanho da Board
     */
    public int getBoardSize() { return TAM; }

    /**
     * Obtém letra equivalente à coluna de número i
     * @param i número da coluna
     * @return letra correspondente
     */
    public Character getXAxis(int i) { return xAxis[i]; }

    /**
     * Obtém ‘ID’ da peça seleciona (selectedPiece) no ChessGame
     * @return ‘ID’ da peça selecionada
     */
    public String GMgetSelectedPiece() { return ChessGame.getSelectedPiece(); }

    /**
     * Obtém coordenadas da peça com ‘ID’ igual apieceID
     * @param pieceId ‘ID’ da peça
     * @return coordenada da peça
     */
    public Integer[] GMgetPieceCoords(String pieceId) { return ChessGame.getPieceCoords(pieceId); }

    /**
     * Obtém array de IDs de todas as peças na Board
     * @return array de IDs
     */
    public ArrayList<String> GMgetPieces() { return ChessGame.getPieces(); }

    /**
     * Alterna ativação do botão 'show Possible moves' na UI, e sinaliza possíveis outros Stages
     * @param show valor para o qual showPossibleMoves será alterado
     * @return showPossibleMoves
     */
    public boolean changeShowPossibleMoves(boolean show) {
        showPossibleMoves = show;
        pcs.firePropertyChange(PROP.showMoves,null,null);
        pcs.firePropertyChange(PROP.select,null,null);
        return showPossibleMoves;
    }

    /**
     * Obtém valor da variável booleana showPossibleMoves
     * @return showPossibleMoves
     */
    public boolean getShowPossibleMoves() { return showPossibleMoves; }

    /**
     * Obtém lista de posições em que a peça selecionada no ChessGame pode fazer
     * @return lista de posições possíveis
     */
    public List<Integer[]> GMgetPiecePossibilities() { return ChessGame.getPiecePossibilities(); }

    /**
     * Tenta selecionar peça que está na posição (row, col)
     * @param row linha em que a peça está
     * @param col coluna em que a peça está
     * @return true se a peça foi selecionada com sucesso, false caso contrário
     */
    public boolean GMselectPiece(int row, int col) {
        if (ChessGame.selectPiece(row, col)) {
            pcs.firePropertyChange(PROP.select,null,null);
            return true;
        }
        return false;
    }

    /*
    public boolean GMmakeAMove(int row, int col) {
        String piece = GMgetSelectedPiece();
        if (ChessGame.makeAMove(row, col)) {
            pcs.firePropertyChange(PROP.newRound,null,null);
            ModelLog.getInstance().addLog(piece + " moveu para " + Character.toLowerCase(xAxis[col]) + (8 - row));
            return true;
        }
        return false;
    }
     */

    /**
     * Tenta realizar movimento da peça selecionada no ChessGame
     * @param row linha do movimento
     * @param col coluna do movimento
     * @return true caso o movimento tenha sido realizado com sucesso, false caso contrário
     */
    public boolean GMmakeAMove(int row, int col){
        String piece = GMgetSelectedPiece();
        if (piece == null) return false;

        Integer [] orig = GMgetPieceCoords(piece);
        if (orig == null) return false;

        Piece selected = getBoard().getPiece(orig[0], orig[1]);
        if (selected == null || selected.getTeam() != GMgetTeam()) return false;


        ICommand cmd = new MovePieceCommand(this, orig[0], orig[1], row, col);
        if (cmdMgr.invokeCommand(cmd)) {
            if (selected.getType().equals(PAWN) && (row == 0 || row == 7)) {
                ChessGame.setPromotion(true);
                ChessGame.setPieceToPromote(selected);
                pcs.firePropertyChange(PROP.promWindow, null, null);
            } else {
                ChessGame.setPromotion(false);
            }
            pcs.firePropertyChange(PROP.newRound,null,null);
            ModelLog.getInstance().addLog(piece + " moveu para " + Character.toLowerCase(xAxis[col]) + (8 - row));

            return true;
        }
        return false;
    }

    /**
     * Verifica se alguma peça do jogo deve ser promovida
     * @return true se alguma peça deverá ser promovida, false caso contrário
     */
    public boolean GMisPromotion() { return ChessGame.isPromotion(); }

    /**
     * Tenta realizar promoção da peça que deve ser promovida no ChessGame
     * @param type tipo de peça que a peça deve se tornar
     */
    public void GMpromote(PieceType type) {
        if (GMisPromotion()) {
            String pid = ChessGame.getPieceToPromote();
            if (ChessGame.promote(type)) {
                ModelLog.getInstance().addLog(pid + " foi promovido para " + type.toString().toLowerCase());
                pcs.firePropertyChange(PROP.newRound,null,null);
            }
        }
    }

    /**
     * Obtém número do round
     * @return número do round
     */
    public int GMgetRound() {
        return ChessGame.getRound();
    }

    /**
     * Obtém equipa que deve jogar
     * @return equipa que joga o round atual
     */
    public Team GMgetTeam() {
        return ChessGame.currentPlayer();
    }

    /**
     * Verifica se o jogo acabou
     * @return true se o jogo acabou, false se o jogo deve continuar
     */
    public boolean GMisOver() { return ChessGame.isOver(); }

    /**
     * Verifica se o rei está em Check
     * @return
     */
    public boolean isChecked(){return ChessGame.isCheck();}

    /**
     * Verifica se alguma peça foi capturada
     * @return true se alguma peça foi capturada
     */
    public boolean GMkilled(){
        return ChessGame.Killed();
    }

    /**
     * Lê arquivo de texto para criar um tabuleiro baseado no conteúdo do ficheiro
     * @param filename Local/nome do ficheiro que deve ser lido
     */
    public void importThis(String filename) throws IOException {
        if (!gameExists())
            createGame();

        ChessGame.importGame(filename);
        pcs.firePropertyChange(PROP.newGame, null, null);
        ModelLog.getInstance().addLog("Jogo importado");
    }

    /**
     * Guarda estado atual do tabuleiro e do jogo num ficheiro de texto
     * @param filename Local/nome do ficheiro que deve ser guardado
     */
    public void exportThis(String filename) throws IOException {
        if(ChessGame != null) {
            ChessGame.exportGame(filename);
        }
        ModelLog.getInstance().addLog("Jogo exportado");

    }

    /**
     * Deserializa(abre) arquivo que contém um ChessGame salvo anteriormente
     * @param filename Local/nome do arquivo a ser aberto
     */
    public void openThis(String filename) throws IOException, ClassNotFoundException {
        boolean gameExists = gameExists();
        ChessGame = deserialize(filename);

        if (!gameExists)    pcs.firePropertyChange(PROP.initGame, null, null);
        else                pcs.firePropertyChange(PROP.newGame, null, null);

        ModelLog.getInstance().addLog("Jogo aberto");
    }

    /**
     * Serializa(salva) ChessGame em arquivo que pode ser aberto posteriormente
     * @param filename Local/nome do arquivo a ser salvo
     */
    public void saveThis(String filename) throws IOException {
        if(ChessGame != null) {
            serialize(filename, ChessGame);
        }
        ModelLog.getInstance().addLog("Jogo gravado");
    }


    /**
     * Cria método para classes da UI adicionarem PropertyChangeListeners
     * @param prop nome da propriedade a ser salva e possivelmente sinalizada em algum momento do jogo
     * @param listener método do elemento da UI que será executado quando prop for sinalizado
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(prop, listener);
    }

    /**
     * Retorna jogo para estado anterior
     * @return true se operação foi realizada com sucesso, false caso contrário
     */
    public boolean undo() {
        boolean ok = cmdMgr.undo();
        if (ok) { pcs.firePropertyChange(PROP.newRound, null, null);}
        return ok;
    }

    /**
     * Avança jogo para próximo estado
     * @return true se operação foi realizada com sucesso, false caso contrário
     */
    public boolean redo() {
        boolean ok = cmdMgr.redo();
        if (ok) { pcs.firePropertyChange(PROP.newRound, null, null);}
        return ok;
    }

    /**
     * Verifica se há algum estado de jogo posterior
     * @return true se existe algum estado de jogo posterior
     */
    public boolean hasRedo() {return cmdMgr.hasRedo();}

    /**
     * Verifica se há algum estado de jogo anterior
     * @return true se existe algum estado de jogo anterior
     */
    public boolean hasUndo() {return cmdMgr.hasUndo();}


    /**
     * Obtém Board do jogo
     * @return Board do jogo
     */
    public Board getBoard() {
        return ChessGame.getBoard();
    }

    /**
     * Passa jogo para o próximo round
     */
    public void nextRound() {
        ChessGame.nextRound();
    }

    /**
     * Retorna um round do jogo
     */
    public void retreatRound() {
        ChessGame.retreatTurn();
        ChessGame.setTeam();
    }

    /**
     * Tenta realizar movimento da peça na posição (fromRow, fromCol) para a posição (toRow, toCol)
     * @param fromRow linha da peça a ser movida
     * @param fromCol coluna da peça a ser movida
     * @param toRow linha do movimento
     * @param toCol coluna do movimento
     * @return true se o movimento foi realizado com sucesso, false caso contrário
     */
    public boolean makeMoveFromCommand(int fromRow, int fromCol, int toRow, int toCol) {
        Piece selected = ChessGame.getBoard().getPiece(fromRow, fromCol);
        if (selected == null || selected.getTeam() != GMgetTeam())
            return false;

        if (selected.move(toRow, toCol)) {
            ChessGame.unselectPiece();
            return true;
        }

        return false;
    }









}
