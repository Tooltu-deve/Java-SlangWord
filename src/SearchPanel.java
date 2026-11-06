import javax.swing.*;

public class SearchPanel extends JPanel implements Refreshable {
    private MainFrame app;

    public SearchPanel(MainFrame app){
        this.app = app;
        add(new JLabel("Tìm kiếm - đang làm"));
    }

    @Override
    public void refresh(){

    }
}
