import javax.swing.*;

public class QuizPanel extends JPanel implements Refreshable {
    private MainFrame app;

    public QuizPanel(MainFrame app){
        this.app = app;
        add(new JLabel("Đố vui - đang làm"));
    }

    @Override
    public void refresh(){

    }
}
