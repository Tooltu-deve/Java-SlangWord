import javax.swing.*;

public class RandomPanel extends JPanel implements Refreshable {
    private MainFrame app;

    public RandomPanel(MainFrame app){
        this.app = app;
        add(new JLabel("On this day - đang làm"));
    }

    @Override
    public void refresh(){

    }
}
