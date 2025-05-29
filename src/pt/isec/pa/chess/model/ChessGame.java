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

    public ChessGame() {
        board = new Board();
        round = 0;
        roundTeam = WHITE;
    }

    public ChessGame (String filename) {   // Inicia um jogo de xadrez ja iniciado
        importGame(filename);
    }

    public String getpWhite() {
        return pWhite;
    }

    public void setpWhite(String pWhite) {
        this.pWhite = pWhite;
    }

    public String getpBlack() {
        return pBlack;
    }

    public void setpBlack(String pBlack) {
        this.pBlack = pBlack;
    }


    public String getSelectedPiece() { return selectedPiece == null ? null : selectedPiece.getId(); }

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

    public List<Integer[]> getPiecePossibilities() {
        if (selectedPiece != null)
            return selectedPiece.getPossibilities();
        return null;
    }



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

    public void unselectPiece() {
        this.selectedPiece = null;
    }

    public Integer[] getPieceCoords(String pieceId) {
        Integer[] coords = new Integer[2];
        if (board.getPiece(pieceId) != null) {
            coords[0] = board.getPiece(pieceId).getRow();
            coords[1] = board.getPiece(pieceId).getColumn();
            return coords;
        }
        return null;
    }

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

    public void setPromotion(boolean promotion) { this.promotion = promotion; }

    public boolean isPromotion() { return this.promotion; }

    public void setPieceToPromote(Piece p) { this.pieceToPromote = p; }

    public String getPieceToPromote() {
        if (this.pieceToPromote == null) return null;
        else return this.pieceToPromote.getId();
    }

    public boolean promote(PieceType type) {
        if (pieceToPromote == null) return false;

        Piece promoted = type.createPiece(this.getBoard(), pieceToPromote.getRow(), pieceToPromote.getColumn(), pieceToPromote.getTeam());
        this.getBoard().movePiece(promoted, promoted.getRow(), promoted.getColumn());

        this.pieceToPromote = null;
        this.promotion = false;

        return true;
    }

    public int getRound() { return this.round; }

    public Team currentPlayer() { return this.roundTeam; }

    public void nextRound() {
        ++round;
        roundTeam = (roundTeam == BLACK) ? WHITE : BLACK;
        cleanEnPassant(roundTeam);
    }


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

    public Board getBoard() { return this.board; }

    public void retreatTurn(){
        round--;
    }

    public void setTeam(){
        if (this.roundTeam == WHITE)
            roundTeam = BLACK;
        else
            roundTeam = WHITE;
    }

    public Boolean isCheck() {
        for (int l = 0; l < TAM; l++)
            for (int c = 0; c < TAM; c++)
                if (this.board.getPiece(l, c)!=null && !this.board.getPiece(l, c).myKingIsSafe(this.board)){return true;}
        return false;
    }

    public boolean Killed(){
        int currentDeaths = this.board.getDeathPieces().size();
        boolean captured = currentDeaths > lastDeathCount;
        lastDeathCount = currentDeaths;
        return captured;
    }

}
