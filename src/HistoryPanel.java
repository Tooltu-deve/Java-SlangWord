import javax.swing.*;

public class HistoryPanel extends JPanel implements Refreshable {
    private MainFrame app;

    public HistoryPanel(MainFrame app){
        this.app = app;
        add(new JLabel("Lịch sử - đang làm"));
    }

    @Override
    public void refresh(){

    }
}
