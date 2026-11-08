import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class RandomPanel extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MainFrame app;
    private JLabel title = new JLabel(" ");
    private JLabel slangLabel = new JLabel(" ");
    private JTextArea defArea = new JTextArea();
    private Random random = new Random();   // cho nút "Slang khác", không seed

    public RandomPanel(MainFrame app){
        this.app = app;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        slangLabel.setFont(slangLabel.getFont().deriveFont(Font.BOLD, 36f));

        defArea.setEditable(false);
        defArea.setLineWrap(true);
        defArea.setWrapStyleWord(true);
        defArea.setFont(defArea.getFont().deriveFont(16f));

        JButton other = new JButton("Slang khác");
        other.addActionListener(e -> showSlang(pick(random)));

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.CENTER);
        header.add(other, BorderLayout.EAST);

        // dùng BorderLayout thay GridLayout để header chỉ cao bằng nút, không bị kéo giãn theo slangLabel
        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.add(header, BorderLayout.NORTH);
        top.add(slangLabel, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(defArea), BorderLayout.CENTER);

    }

    private void showToday(){
        LocalDate today = LocalDate.now();
        title.setText("On this day - " + today.format(DATE_FORMAT));
        showSlang(pick(new Random(today.toEpochDay())));
    }

    // chọn 1 slang từ danh sách đã sort -> cùng seed + cùng dữ liệu luôn ra cùng kết quả
    private String pick(Random r){
        List<String> all = app.getDict().allSlangs();
        if(all.isEmpty()) return null;
        return all.get(r.nextInt(all.size()));
    }

    private void showSlang(String slang){
        if(slang == null){
            slangLabel.setText("Từ điển trống");
            defArea.setText("");
            return;
        }
        slangLabel.setText(slang);
        List<String> defs = app.getDict().findBySlang(slang);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < defs.size(); i++){
            sb.append("• ").append(defs.get(i)).append("\n");
        }
        defArea.setText(sb.toString());
        defArea.setCaretPosition(0);
    }

    @Override
    public void refresh(){
        showToday();
    }
}
