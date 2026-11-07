import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;


public class HistoryPanel extends JPanel implements Refreshable {
    private MainFrame app;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JLabel title = new JLabel();

    public HistoryPanel(MainFrame app){
        this.app = app;
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        add(title, BorderLayout.NORTH);

        JList<String> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        refresh();
    }


    @Override
    public void refresh(){
        List<String> history = app.getHistory();
        Collections.reverse(history);
        model.clear();
        model.addAll(history);
        title.setText("Lịch sử tìm kiếm (" + model.size() + " mục)");
    }
}
