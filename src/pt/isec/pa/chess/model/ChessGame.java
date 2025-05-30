package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;

import static pt.isec.pa.chess.model.data.pieces.PieceType.*;
import static pt.isec.pa.chess.model.data.pieces.Team.*;

/**
 * ChessGame é a classe responsável por realizar as ações e movimentos do jogo, aplica as regras e lógica do jogo
 * @author Artur Capelossi, Diogo Beja e Nikolay Grachev
 *
 */
public class ChessGame implements Constants, Serializable {
    @Serial private static final long serialVersionUID = 1L;// FACADE

    private Board board;

    private Team roundTeam;
    private int round;

    private String pWhite;
    private String pBlack;

    private int lastDeathCount;
    private Piece selectedPiece = null;

    private boolean promotion = false;
    private Piece pieceToPromote = null;

    private Team winner = null;


    /**
     * Default Constructor
     * <p>
     * Inicializara a Board com as posições iniciais padrões das peças
     *
     */
    public ChessGame() {
        board = new Board();
        round = 0;
        roundTeam = WHITE;
    }


    /**
     * Constructor por linha de texto
     * <p>
     * Cria a Board baseado num arquivo de texto
     * @param filename Local do arquivo de texto que será usado para a criação da Board
     */
    public ChessGame (String filename) {   // Inicia um jogo de xadrez ja iniciado
        importGame(filename);
    }

    /**
     * Obtém nome do jogador da equipa WHITE
     * @return pWhite (nome do jogador da equipa WHITE)
     */
    public String getpWhite() {
        return pWhite;
    }
    /**
     * Define nome do jogador da equipa WHITE
     * @param pWhite (novo nome do jogador da equipa WHITE)
     */
    public void setpWhite(String pWhite) {
        this.pWhite = pWhite;
    }


    /**
     * Obtém do nome do jogador da equipa BLACK
     * @return pBlack (nome do jogador da equipa BLACK)
     */
    public String getpBlack() {
        return pBlack;
    }
    /**
     * Define nome do jogador da equipa BLACK
     * @param pBlack (novo nome do jogador da equipa BLACK)
     */
    public void setpBlack(String pBlack) {
        this.pBlack = pBlack;
    }

    /**
     * Obtém peça selecionada
     * @return ID da peça selecionada (selectedPiece)
     */
    public String getSelectedPiece() { return selectedPiece == null ? null : selectedPiece.getId(); }

    /**
     * Busca peças na Board e cria array com os ‘IDs’ delas. Os ‘IDs’ representam o tipo, equipa e posição na Board
     * @return Array com ‘IDs’ das peças
     */
    public ArrayList<String> getPieces() {
        ArrayList<String> pieces = new ArrayList<>();
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (board.getPiece(i,j) != null && !board.getPiece(i,j).getType().equals(ENPASSANT))
                    pieces.add(board.getPiece(i, j).getId());
            }
        }
        return pieces;
    }

    /**
     * Caso haja alguma peça selecionada (selectedPiece), o método retorna uma array com todas posições em que ela pode se mover
     * @return Array de posições em que a selectedPiece pode se mover
     */
    public List<Integer[]> getPiecePossibilities() {
        if (selectedPiece != null)
            return selectedPiece.getPossibilities();
        return null;
    }


    /**
     * Se possível, dentro das regras do jogo, seleciona peça na posição (row, column)
     * @param row linha em que a peça está
     * @param col coluna em que a peça está
     * @return true se uma peça foi selecionada com sucesso, caso contrario, false
     */
    public boolean selectPiece(int row, int col) {                                    // Seleciona peca
        Piece sP = this.board.getPiece(row, col);
        if (sP == null) return false;
        //if (this.selectedPiece != null) return false;                                 // Se selecionou uma peca, nao pode selecionar outra


        if (sP.getTeam().equals(this.roundTeam) && !sP.getPossibilities().isEmpty()) {
            this.selectedPiece = sP;
            return true;
        }

        return false;
    }

    /**
     * Define valor de selectedPiece para null
     */
    public void unselectPiece() {
        this.selectedPiece = null;
    }

    /**
     * A partir do ‘ID’ de uma peça, busca a peça na Board e devolve a posição dela
     * @param pieceId ‘ID’ da peça que quer encontrar a posição
     * @return Posição da peça com ‘ID’ igual a pieceID
     */
    public Integer[] getPieceCoords(String pieceId) {
        Integer[] coords = new Integer[2];
        if (board.getPiece(pieceId) != null) {
            coords[0] = board.getPiece(pieceId).getRow();
            coords[1] = board.getPiece(pieceId).getColumn();
            return coords;
        }
        return null;
    }

    /**
     * Realiza, se possível dentro das regras do jogo, o movimento da selectedPiece para a posição (row, col)
     * @param row linha para onde se mover
     * @param col coluna para onde se mover
     * @return true se movimento foi realizado com sucesso, false caso contrário
     */
    public boolean makeAMove(int row, int col) {
        if (this.selectedPiece == null) return false;
        if (this.selectedPiece.move(row, col)) {
            if (this.selectedPiece.getType().equals(PAWN) && (row == 0 || row == 7)) {
                this.promotion = true;
                this.pieceToPromote = selectedPiece;

                System.out.println("to Promote: " + pieceToPromote.getId());
            } else
                this.promotion = false;
            this.selectedPiece = null;
            this.nextRound();
            return true;
        }
        return false;
    }

    /**
     * Define variável booleana promotion, que diz se um movimento acarretará numa Promoção
     * @param promotion variável booleana que definirá promotion
     */
    public void setPromotion(boolean promotion) { this.promotion = promotion; }

    /**
     * Obtém valor atual de promotion, que representa a necessidade de alguma peça do jogo ser promovida
     * @return valor de promotion
     */
    public boolean isPromotion() { return this.promotion; }

    /**
     * Define qual é a peça que deverá ser promovida, colocada na variável pieceToPromote
     * @param p Piece que deve ser promovida
     */
    public void setPieceToPromote(Piece p) { this.pieceToPromote = p; }

    /**
     * Obtém 'ID' da peça que deve ser promovida
     * @return ‘ID’ da peça em pieceToPromote
     */
    public String getPieceToPromote() {
        if (this.pieceToPromote == null) return null;
        else return this.pieceToPromote.getId();
    }

    /**
     * Realiza ação de promover peça em pieceToPromote para o tipo type
     * @param type tipo de peça para o qual o Pawn será promovido
     * @return true se a promoção foi realizada com sucesso, false caso contrário
     */
    public boolean promote(PieceType type) {
        if (pieceToPromote == null) return false;

        Piece promoted = type.createPiece(this.getBoard(), pieceToPromote.getRow(), pieceToPromote.getColumn(), pieceToPromote.getTeam());
        this.getBoard().movePiece(promoted, promoted.getRow(), promoted.getColumn());

        this.pieceToPromote = null;
        this.promotion = false;

        return true;
    }

    /**
     * Obtém número de rounds
     * @return número de rounds jogados
     */
    public int getRound() { return this.round; }

    /**
     * Obtém qual equipe deve jogar o round atual
     * @return Team que deve jogar o round
     */
    public Team currentPlayer() { return this.roundTeam; }

    /**
     * Passa o jogo para o próximo round, soma número de rounds jogados, alterna quem deve jogar e limpa peças temporárias para realizar o En Passant
     */
    public void nextRound() {
        ++round;
        roundTeam = (roundTeam == BLACK) ? WHITE : BLACK;
        cleanEnPassant(roundTeam);
    }

    /**
     * Verifica se o jogo ainda pode ser jogado, ou se já não há jogadas possíveis. Definindo no fim se há um vencedor e quem é.
     * @return true se o jogo acabou, false se o jogo deve continuar
     */
    public Boolean isOver() {
        for (int l = 0; l < TAM; l++)        // Verifica se alguma das pecas do time que vai jogar tem movimentos possiveis (Se nenhuma peca do time tiver movimentos possiveis, o jogo acaba)
            for (int c = 0; c < TAM; c++)
                if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.roundTeam)) {
                    if (!this.board.getPiece(l, c).getPossibilities().isEmpty()) return false;
                }

        // Verificar o ganhador ou empate
        this.winner = this.roundTeam == WHITE ? BLACK : WHITE;
        return true;

    }

    /**
     * Apaga peças temporárias EnPassant da equipa t
     * @param t equipa das peças EnPassant que devem ser apagadas
     */
    public void cleanEnPassant(Team t){
        for (int l = 0; l < TAM; l++) {
            for (int c = 0; c < TAM; c++) {

                if (this.board.getEnPassant(l, c) != null && this.board.getEnPassant(l, c).getType().equals(ENPASSANT)) {
                    if (this.board.getEnPassant(l, c).getTeam().equals(t)) {

                        this.board.removePiece(l, c);
                    }
                }
            }
        }
    }

    /**
     * Cria arquivo de texto com a equipa que deve jogar o round e os ‘IDs’ das peças que estão na Board
     * @param filename Local/nome em que o arquivo de texto criado deve ser salvo
     * @return true se o processo for realizado com sucesso, false caso contrário
     */
    public boolean exportGame(String filename) {
        try (FileWriter fw = new FileWriter(filename)){
            cleanEnPassant(BLACK);
            cleanEnPassant(WHITE);
            fw.write(this.roundTeam.toString() + ",\n");
            for (int i = 0; i < TAM; i++)
                for (int j = 0; j < TAM; j++)
                    if (this.board.getPiece(i, j) != null) {
                        fw.write(this.board.getPiece(i, j).toString().replaceAll("\\s","") + ",");
                    }

            fw.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Cria Board a partir de um arquivo de texto que contém a equipa que deve jogar e os ‘IDs’ das peças
     * @param filename Local/nome do arquivo de texto que deve ser lido
     * @return true se o processo for realizado com sucesso, false caso contrário
     */
    public boolean importGame(String filename) {
        try (FileReader fr = new FileReader(filename); Scanner sc = new Scanner(fr)){

            StringBuilder fileContent = new StringBuilder();

            while (sc.hasNext())
                fileContent.append(sc.nextLine());

            String[] pieces = fileContent.toString()
                    .replaceAll("\\s", "")  // remove espacos
                    .split(",");                        // separa por virgula

            this.roundTeam = pieces[0].equals("WHITE") ? WHITE : BLACK;

            pieces = Arrays.copyOfRange(pieces, 1, pieces.length);

            this.board = new Board(pieces);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Obtém Board do jogo
     * @return Board do jogo
     */
    public Board getBoard() { return this.board; }

    /**
     * Volta um round
     */
    public void retreatTurn(){
        round--;
    }

    /**
     * Define próxima equipa a jogar
     */
    public void setTeam(){
        if (this.roundTeam == WHITE)
            roundTeam = BLACK;
        else
            roundTeam = WHITE;
    }

    /**
     * Verifica se o rei está em Check
     * @return true se o rei estiver em Check
     */
    public Boolean isCheck() {
        for (int l = 0; l < TAM; l++)
            for (int c = 0; c < TAM; c++)
                if (this.board.getPiece(l, c) != null && !this.board.getPiece(l, c).myKingIsSafe(this.board)) { return true; }
        return false;
    }

    /**
     * Verifica se alguma peça foi capturada desde a última verificação
     * @return true se uma nova peça foi capturada
     */
    public boolean Killed(){
        int currentDeaths = this.board.getDeathPieces().size();
        boolean captured = currentDeaths > lastDeathCount;
        lastDeathCount = currentDeaths;
        return captured;
    }

}
