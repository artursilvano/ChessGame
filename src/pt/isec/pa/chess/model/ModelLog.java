package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.command.PROP;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe responsável por guardar e gerir logs do jogo
 * @author Artur Capelossi, Diogo Beja e Nikolay Grachev
 */
public class ModelLog {
    private static ModelLog _instance = null;
    private final List<String> log;
    private final PropertyChangeSupport pcs;

    /**
     * Default Constructor
     */
    public ModelLog() {
        log = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Método para garantir que existe apenas uma instancia da classe ModelLog (Singleton)
     * @return se não existir nenhuma instancia, cria uma e a retorna. Se já existir, retorna a instância já existente
     */
    public static ModelLog getInstance() {
        if (_instance == null)
            _instance = new ModelLog();
        return _instance;
    }

    /**
     * Adiciona mensagem a lista de logs
     * @param msg log a ser adicionado a lista
     */
    public void addLog(String msg) {
        log.add(msg);
        pcs.firePropertyChange(PROP.logAdded, null, msg);
    }

    /**
     * Obtém logs guardados
     * @return lista de logs
     */
    public List<String> getLogs() {
        return Collections.unmodifiableList(log);
    }

    /**
     * Apaga todos os logs do jogo
     */
    public void clearLogs() {
        log.clear();
        pcs.firePropertyChange(PROP.logsCleared, null, null);
    }

    /**
     * Método para classe ModelLogStage adicionar um PropertyChangeListeners
     * @param listener método do ModelLogStage que será executado quando for sinalizado
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
}
