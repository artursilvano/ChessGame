package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelLog {
    private static ModelLog _instance = null;
    private final List<String> log;
    private final PropertyChangeSupport pcs;

    public ModelLog() {
        log = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }

    public static ModelLog getInstance() {
        if (_instance == null)
            _instance = new ModelLog();
        return _instance;
    }

    public void addLog(String msg) {
        log.add(msg);
        pcs.firePropertyChange("logAdded", null, msg);
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(log);
    }

    public void clearLogs() {
        log.clear();
        pcs.firePropertyChange("logsCleared", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
}
