
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alperen Civelek
 */
public class SpellCheck implements Container {
    String text;
    String[] word_array;
    public SpellCheck(JTextArea textBox){
        text=textBox.getText();
        word_array=text.split(" ");
    }
    @Override
    public Iterator iterator() {
        return new WordIterator();
    }
    private class WordIterator implements Iterator{
        int index;
        @Override
        public boolean hasNext() {
           if(index<word_array.length){
               return true;
           }
           else{
               return false;
           }
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return word_array[index++];
            }
            else{
                return null;
            }
        }
        
    }
}
