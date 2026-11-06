import java.io.IOException;
import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SlangDictionary dict = FileManager.loadData();
        if(dict == null){
            try{
                dict = FileManager.readOriginal();
                FileManager.saveData(dict);
            } catch(IOException e){
                JOptionPane.showMessageDialog(null, "Không đọc được " + FileManager.ORIGINAL_FILE + ":\n" + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        final SlangDictionary loaded = dict;
        SwingUtilities.invokeLater(() -> new MainFrame(loaded).setVisible(true));
    }
}
