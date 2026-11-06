import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {
    private SlangDictionary dict;
    private List<String> history;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
    private JPanel menu = new JPanel(new GridLayout(0, 1, 0, 8));

    public MainFrame(SlangDictionary dict){
        super("Slang Dictionary");
        this.dict = dict;
        this.history = FileManager.loadHistory();

        addPage("Tìm kiếm", "SEARCH", new SearchPanel(this));
        addPage("Lịch sử", "HISTORY", new HistoryPanel(this));
        addPage("Quản lý slang", "MANAGE", new ManagePanel(this));
        addPage("On this day", "RANDOM", new RandomPanel(this));
        addPage("Đố vui", "QUIZ", new QuizPanel(this));

        // bọc menu ở NORTH để GridLayout không kéo giãn nút
        JPanel west = new JPanel(new BorderLayout());
        west.add(menu, BorderLayout.NORTH);
        west.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(west, BorderLayout.WEST);
        add(cards, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void addPage(String title, String name, JPanel panel){
        cards.add(panel, name);
        JButton btn = new JButton(title);
        menu.add(btn);
        btn.addActionListener(e -> {
            cardLayout.show(cards, name);
            if(panel instanceof Refreshable){
                ((Refreshable) panel).refresh();
            }
        });
    }

    public SlangDictionary getDict(){
        return dict;
    }

    public void setDict(SlangDictionary dict){
        this.dict = dict;
    }

    public void saveDict(){
        try{
            FileManager.saveData(dict);
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Không lưu được " + FileManager.DATA_FILE + ":\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<String> getHistory(){
        return new ArrayList<>(history);
    }

    public void addHistory(String entry){
        history.add(entry);
        FileManager.appendHistory(entry);
    }

    public void clearHistory(){
        history.clear();
        FileManager.clearHistory();
    }
}
