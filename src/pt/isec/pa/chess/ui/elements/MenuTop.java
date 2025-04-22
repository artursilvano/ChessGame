package pt.isec.pa.chess.ui.elements;


import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

import java.io.File;
import java.io.IOException;

public class MenuTop extends MenuBar {

    private ChessGameManager gameManager;

    private Menu Game,Learning,Mode;
    private MenuItem miNew,miOpen,miSave,miImport,miExport,miQuit;
    private MenuItem miNormal,miSPM,miUndo,miRedo;

    public MenuTop(ChessGameManager gameManager) {
        this.gameManager = gameManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        Game = new Menu("Game");
        miNew = new MenuItem("New");
        miOpen = new MenuItem("Open");
        miSave = new MenuItem("Save");
        miImport = new MenuItem("Import");
        miExport = new MenuItem("Export");
        miQuit = new MenuItem("Quit");
        Game.getItems().addAll(miNew,miOpen,miSave,miImport, miExport, new SeparatorMenuItem(),miQuit);

        Learning = new Menu("Learning");
        miSPM = new MenuItem("Show Possible Moves");
        miUndo = new MenuItem("Undo");
        miRedo = new MenuItem("Redo");
        Learning.getItems().addAll(miSPM,miUndo,miRedo);

        Mode = new Menu("Mode");
        miNormal = new MenuItem("Normal");
        Mode.getItems().addAll(miNormal,Learning);

        this.getMenus().add(Game);
        this.getMenus().add(Mode);
    }

    private void registerHandlers() {
        miNew.setOnAction(event -> {
            Stage janela = new Stage();
            janela.setTitle("Novo Jogo - Inserir Nomes");

            Label lbl1 = new Label("Nome jogador 1:");
            TextField txtNome1 = new TextField();
            txtNome1.setPromptText("Nome do jogador");

            Label lbl2 = new Label("Nome jogador 2:");
            TextField txtNome2 = new TextField();
            txtNome1.setPromptText("Nome do jogador");

            Button btnOk = new Button("OK");

            btnOk.setOnAction(e -> {
                String nome1, nome2;

                nome1 = txtNome1.getText().trim();
                nome2 = txtNome2.getText().trim();

                if (!nome1.isEmpty() && !nome2.isEmpty()) {

                    gameManager.createGame();

                    gameManager.setWhitePlayer(nome1);
                    System.out.println("Jogador 1: " + gameManager.getWhitePlayer());

                    gameManager.setBlackPlayer(nome2);
                    System.out.println("Jogador 2: " + gameManager.getBlackPlayer());

                    janela.close();
                }
            });

            VBox layout = new VBox(10, lbl1, txtNome1, lbl2, txtNome2, btnOk);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

            Scene scene = new Scene(layout, 250, 150);
            janela.setScene(scene);
            janela.showAndWait();
        });

        miOpen.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir jogo de xadrez");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ficheiros de Jogo", "*.dat"),
                    new FileChooser.ExtensionFilter("Todos os ficheiros", "*.*")
            );

            File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());

            if (selectedFile != null) {
                try {
                    gameManager.openThis(selectedFile.getAbsolutePath());
                    System.out.println("Jogo carregado: " + selectedFile.getName());
                    // Aqui poderias também atualizar a UI, se necessário
                    System.out.println("Jogador 1: " + gameManager.getWhitePlayer());
                    System.out.println("Jogador 2: " + gameManager.getBlackPlayer());

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao abrir o jogo:\n" + e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        miSave.setOnAction(event -> {
            if(gameManager.gameExists()){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar jogo de xadrez");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Ficheiros de Jogo", "*.dat"),
                        new FileChooser.ExtensionFilter("Todos os ficheiros", "*.*")
                );

                File fileToSave = fileChooser.showSaveDialog(this.getScene().getWindow());

                if (fileToSave != null) {
                    try {
                        gameManager.saveThis(fileToSave.getAbsolutePath());
                        System.out.println("Jogo guardado em: " + fileToSave.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao guardar o jogo:\n" + e.getMessage());
                        alert.showAndWait();
                    }
                }
            } else {

                        Alert alert = new Alert(Alert.AlertType.ERROR, "Jogo não Criado\n");
                        alert.showAndWait();
            }
        });

        miImport.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importar jogo parcial");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ficheiros de Texto ou CSV", "*.txt", "*.csv"),
                    new FileChooser.ExtensionFilter("Todos os ficheiros", "*.*")
            );

            File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());

            if (selectedFile != null) {
                try {
                    // Lê e processa o conteúdo do ficheiro
                    gameManager.importThis(selectedFile.getAbsolutePath()); // ← Criar este método no gameManager
                    System.out.println("Jogo parcial importado com sucesso.");
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao importar o jogo:\n" + e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        miExport.setOnAction(event -> {
            if(gameManager.gameExists()){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar jogo parcial");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ficheiros CSV", "*.csv"),
                    new FileChooser.ExtensionFilter("Ficheiros de Texto", "*.txt")
            );

            File fileToSave = fileChooser.showSaveDialog(this.getScene().getWindow());

            if (fileToSave != null) {
                try {
                    gameManager.exportThis(fileToSave.getAbsolutePath()); // Criar este método
                    System.out.println("Jogo exportado com sucesso para: " + fileToSave.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao exportar o jogo:\n" + e.getMessage());
                    alert.showAndWait();
                }
            }}
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Jogo não Criado\n");
                alert.showAndWait();
            }
        });

        miQuit.setOnAction(actionEvent -> {
            Platform.exit();
        });

    }
    private void update() {
        miUndo.setDisable(true);
        miRedo.setDisable(true);
    }
}
