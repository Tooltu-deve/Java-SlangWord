import java.awt.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class QuizPanel extends JPanel implements Refreshable {
    private MainFrame app;
    private Random random = new Random();
    private Quiz quiz;
    private int score = 0;
    private int total = 0;

    private JRadioButton bySlang = new JRadioButton("Slang -> nghĩa", true);
    private JRadioButton byDefinition = new JRadioButton("Nghĩa -> slang");
    private JTextArea question = new JTextArea();
    private JButton[] answers = new JButton[4];
    private JLabel result = new JLabel(" ");
    private JLabel scoreLabel = new JLabel("Điểm: 0/0");

    public QuizPanel(MainFrame app){
        this.app = app;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ButtonGroup group = new ButtonGroup();
        group.add(bySlang);
        group.add(byDefinition);
        bySlang.addActionListener(e -> changeMode());
        byDefinition.addActionListener(e -> changeMode());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel type = new JLabel("Kiểu: ");
        top.add(type);
        top.add(bySlang);
        top.add(byDefinition);
        // khoảng trống 40px giữa radio và điểm
        top.add(Box.createHorizontalStrut(40));
        top.add(scoreLabel);

        question.setEditable(false);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(question.getFont().deriveFont(18f));

        JPanel grid = new JPanel(new GridLayout(4, 1, 0, 8));
        for(int i = 0; i < 4; i++){
            answers[i] = new JButton(" ");
            final int idx = i;
            answers[i].addActionListener(e -> choose(idx));
            grid.add(answers[i]);
        }

        JButton next = new JButton("Câu tiếp theo");
        next.addActionListener(e -> nextQuestion());
        
        JPanel center = new JPanel(new BorderLayout());
        center.add(question, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(result, BorderLayout.CENTER);
        bottom.add(next, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void nextQuestion(){
        try {
            quiz = bySlang.isSelected() ? Quiz.fromSlang(app.getDict(), random) : Quiz.fromDefinition(app.getDict(), random);
        } catch(IllegalStateException e){ 
            question.setText(e.getMessage());
            for(int i = 0; i < 4; i++){
                answers[i].setEnabled(false);
            }
            return;
        }
        question.setText(quiz.getQuestion());
        List<String> options = quiz.getOptions();
        for(int i = 0; i < 4; i++){
            answers[i].setText((char)('A' + i) + ". " + options.get(i));
            answers[i].setEnabled(true);
        }
        result.setText(" ");
    }

    private void choose(int idx){
        total++;
        for(JButton j : answers){
            j.setEnabled(false);
        }
        int ans = quiz.getAnswerIndex();
        // macOS không hiện màu nền JButton nên đánh dấu bằng ký hiệu
        answers[ans].setText("✔ " + answers[ans].getText());
        if(idx == ans){
            result.setText("Chính xác!");
            score++;
        }else{
            answers[idx].setText("✘ " + answers[idx].getText());
            result.setText("Sai! Đáp án đúng là: " + quiz.getOptions().get(ans));
        }
        scoreLabel.setText("Điểm: " + score + "/" + total);
    }

    // đổi kiểu đố -> tính điểm lại từ đầu
    private void changeMode(){
        score = 0;
        total = 0;
        scoreLabel.setText("Điểm: 0/0");
        nextQuestion();
    }

    @Override
    public void refresh(){
        nextQuestion();
    }
}
