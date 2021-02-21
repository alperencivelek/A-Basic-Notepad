import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alperen Civelek
 */
public class TextEditor extends Frame implements ActionListener, WindowListener, TextListener{
    JTextArea textBox=new JTextArea("",25,80);
    private String filename="New Text";
    private boolean isChanged=false; //Dosyanin kayda ihtiyaci var mi?
    UndoManager um=new UndoManager();
    UndoCommand geriAl=new UndoCommand(this);//Command Pattern nesnesi
    //Menu ayarlama islemleri
    public TextEditor(String items[]) {
        textBox.getDocument().addUndoableEditListener(new UndoableEditListener(){
            @Override
            public void undoableEditHappened(UndoableEditEvent e){
                um.addEdit(e.getEdit());
            }
        });
        MenuBar menubar=new MenuBar();
        setMenuBar(menubar);
        
        Menu fileBar=new Menu("File");
        menubar.add(fileBar);
        MenuItem menuitem;
        
        menuitem=new MenuItem("New");
        menuitem.addActionListener(this);
        fileBar.add(menuitem);
        
        menuitem=new MenuItem("Open");
        menuitem.addActionListener(this);
        fileBar.add(menuitem);
        
        menuitem=new MenuItem("Save");
        menuitem.addActionListener(this);
        fileBar.add(menuitem);
        
        menuitem=new MenuItem("Save as");
        menuitem.addActionListener(this);
        fileBar.add(menuitem);
        
        menuitem=new MenuItem("Exit");
        menuitem.addActionListener(this);
        fileBar.add(menuitem);
        
        Menu EditBar=new Menu("Edit");
        menubar.add(EditBar);
        
        menuitem=new MenuItem("Undo");
        menuitem.addActionListener(this);
        EditBar.add(menuitem);
        
        menuitem=new MenuItem("Redo");
        menuitem.addActionListener(this);
        EditBar.add(menuitem);
        
        Menu FindBar=new Menu("Find");
        menubar.add(FindBar);
        
        menuitem=new MenuItem("Find Word");
        menuitem.addActionListener(this);
        FindBar.add(menuitem);
        
        menuitem=new MenuItem("Find and Replace");
        menuitem.addActionListener(this);
        FindBar.add(menuitem);
        
        Menu CheckBar=new Menu("Check");
        menubar.add(CheckBar);
        
        menuitem=new MenuItem("SpellChecker");
        menuitem.addActionListener(this);
        CheckBar.add(menuitem);
        
        
        
        setLayout(new BorderLayout());
        add("Center", textBox);

        addWindowListener(this);

        if (items.length>0){
            loadFile(items[0]);
        }
        else{
            loadFile("Notepad");
        }
        pack();
        show();
    }
    private void saveChanges() {
        if (isChanged) {
        saveFile(false);
        }
    }
    private void loadFile(String newfile) {
        saveChanges();
        textBox.setText("");
        rename(newfile);
        if (!filename.equals("Notepad")) {
            try {
            InputStream FileToRead=new FileInputStream(filename);
            int n;
            byte[] a=new byte[4096];
            while ((n=FileToRead.read(a))>0)
            textBox.append(new String(a, 0, n));
            FileToRead.close();
            }
        catch (FileNotFoundException x) {
        }
        catch (IOException x) {}
        }
        isChanged=false;
    }
    private void saveFile(boolean isExist) {
        if (filename.equals("Notepad") || isExist) {
        FileDialog FileToSave=new FileDialog(this, "", FileDialog.SAVE);
        FileToSave.show();
        if (FileToSave.getFile()!=null)
            rename(FileToSave.getDirectory()+FileToSave.getFile());
        }
        try {
        Writer save=new FileWriter(filename);
        save.write(textBox.getText());
        save.close();
        isChanged=false;
        }
        catch (IOException x) {
        }
    }
    private void exit(Window parent) {
        saveChanges();
        dispose();
        System.exit(0);
    }
    private void rename(String newname) {
        filename=newname;
        setTitle("Alperen's Editor "+filename);
    }
    private void SearchWord(){
        String SearchedWord=JOptionPane.showInputDialog(null,"Word:","Find",JOptionPane.INFORMATION_MESSAGE);
        String text= textBox.getText();
        text=text.replace("\n", "");
        int index=text.indexOf(SearchedWord);
        if(index==-1){
            JOptionPane.showMessageDialog(null, "Not Found!");
        }
        else{
            textBox.setCaretPosition(index);
        }
    }
    private void FindNReplace(){
        JPanel p = new JPanel();
        JTextField ReplaceWord = new JTextField(10);
        JTextField ReplacedWord = new JTextField(10);
        p.add(new JLabel("Replaced Word:"));
        p.add(ReplaceWord);
        p.add(new JLabel("Replace Word: "));
        p.add(ReplacedWord);
        JOptionPane.showConfirmDialog(null, p,"Find and Replace", JOptionPane.OK_CANCEL_OPTION);
        String Replace_Word=ReplaceWord.getText();
        String Replaced_Word=ReplacedWord.getText();
        String text=textBox.getText();
        int index=text.indexOf(Replace_Word);
        if(index==-1){
            JOptionPane.showMessageDialog(null, "Not Found!");
        }
        else{
            text=text.replace(Replace_Word, Replaced_Word);
            textBox.setText(text);
        }
    }
    public static boolean SpellChecker(String word){
        try{
            BufferedReader dict=new BufferedReader(new FileReader("words.txt"));
            String str;
            while((str=dict.readLine())!=null){
                if(str.indexOf(word)!=-1){
                    return true;
                }
            }
            dict.close();
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "Program couldn't find the Dictionary");
        }
        return false;
    }

    public void WordChecks(){
        String wrong_words="";
        SpellCheck sc=new SpellCheck(textBox);
        for(Iterator itr = sc.iterator();itr.hasNext();){
            String word=(String)itr.next();
            if(SpellChecker(word)==false){
                wrong_words=wrong_words+word+" ";
            }
        }
        if(wrong_words!=""){
            JOptionPane.showMessageDialog(null, "Wrong Word(s):"+wrong_words);
        }
        else{
            JOptionPane.showMessageDialog(null, "Nothing Wrong");
        }
    }
    
    @Override
    public void windowActivated(WindowEvent e){
    }
    @Override
    public void windowClosed(WindowEvent e){
    }
    @Override
    public void windowDeactivated(WindowEvent e){
    }
    @Override
    public void windowDeiconified(WindowEvent e){
    }
    @Override
    public void windowIconified(WindowEvent e){
    }
    @Override
    public void windowOpened(WindowEvent e){
    }
    @Override
    public void windowClosing(WindowEvent e) {
        exit(e.getWindow());
    }
    @Override
    public void textValueChanged(TextEvent e) {
        isChanged=true;
    }
    @Override
    public void actionPerformed(ActionEvent input) {
        String Komut=input.getActionCommand();
        if (Komut.equals("New")){
            loadFile("Notepad");
        }
        else if (Komut.equals("Open")) {
            FileDialog d=new FileDialog(this, "", FileDialog.LOAD);
            d.show();
            if (d.getFile()!=null){
                loadFile(d.getDirectory()+d.getFile());
            }   
        }
        else if (Komut.equals("Save")){
            saveFile(false);
        }
        else if (Komut.equals("Save as")){
            saveFile(true);
        }
        else if (Komut.equals("Exit")){
            exit(this);
        }
        else if(Komut.equals("Find Word")){
            SearchWord();
        }
        else if(Komut.equals("Find and Replace")){
            FindNReplace();
        }
        else if(Komut.equals("Undo")){
            geriAl.execute();
        }
        else if(Komut.equals("Redo")){
            geriAl.unexecute();
        }
        else if(Komut.equals("SpellChecker")){
            WordChecks();
        }
    }
}