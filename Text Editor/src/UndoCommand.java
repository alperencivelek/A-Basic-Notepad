import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alperen Civelek
 */
public class UndoCommand implements Command{
    TextEditor textedit;
    public UndoCommand(TextEditor textedit){
        this.textedit=textedit;
    }

    @Override
    public void execute() {
        try{
            textedit.um.undo();
        }
        catch(CannotUndoException x){
            JOptionPane.showMessageDialog(null, "Nothing to Undo!");
        }
    }

    @Override
    public void unexecute() {
        try{
            textedit.um.redo();
        }
        catch(CannotRedoException x){
            JOptionPane.showMessageDialog(null, "Nothing to Redo!");
        }
    }
}
