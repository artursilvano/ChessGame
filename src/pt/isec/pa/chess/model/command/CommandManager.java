package pt.isec.pa.chess.model.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private Deque<ICommand> history;
    private Deque<ICommand> redoCmds;
    //private Stack<ICommand> history;
    //private Stack<ICommand> redoCmds;

    public CommandManager() {
        history = new ArrayDeque<>();
        redoCmds = new ArrayDeque<>();
        //history = new Stack<>();
        //redoCmds = new Stack<>();
    }

    public boolean invokeCommand(ICommand cmd) {
        redoCmds.clear();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }
        return false;
    }

    public boolean undo() {
        if (history.isEmpty())
            return false;
        ICommand cmd = history.pop();
        if (cmd.undo()) {
            redoCmds.push(cmd);
            return true;
        }

        history.push(cmd);
        return false;
    }

    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }

        return false;
    }

    public boolean hasUndo() { return !history.isEmpty(); }
    public boolean hasRedo() { return !redoCmds.isEmpty(); }
}
