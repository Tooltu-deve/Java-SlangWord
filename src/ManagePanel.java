import javax.swing.*;

public class ManagePanel extends JPanel implements Refreshable {
    private MainFrame app;

    public ManagePanel(MainFrame app){
        this.app = app;
        add(new JLabel("Quản lý slang - đang làm"));
    }

    @Override
    public void refresh(){

    }
}
