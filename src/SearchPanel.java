import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchPanel extends JPanel implements Refreshable {
    private static final String[] COLUMNS = {"Slang", "Definition"};
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private MainFrame app;
    private JTextField input = new JTextField(25);
    private JRadioButton bySlang = new JRadioButton("Slang", true);
    private JRadioButton byDefinition = new JRadioButton("Definition");
    private JLabel status = new JLabel(" ");
    private DefaultTableModel model;
    private JButton btn = new JButton("Tìm");

    public SearchPanel(MainFrame app){
        this.app = app;
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonGroup group = new ButtonGroup();
        group.add(bySlang);
        group.add(byDefinition);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Tìm theo:"));
        top.add(bySlang);
        top.add(byDefinition);
        top.add(input);
        top.add(btn);

        model = new DefaultTableModel(COLUMNS, 0){
            @Override public boolean isCellEditable(int row, int col) {return false; }
        };

        JTable table = new JTable(model);

        btn.addActionListener(e -> search());
        input.addActionListener(e -> search());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        
    }

    private void search(){
        String key = input.getText().trim();
        if(key.isEmpty()){
            status.setText("Vui lòng nhập từ khoá!");
            return;
        }
        if(bySlang.isSelected()){
            searchSlang(key);
        }else{
            searchDefinition(key);
        }
    }

    private void searchSlang(String key){
        long start = System.nanoTime();
        List<String> res = app.getDict().findBySlang(key);
        double ms = (System.nanoTime() - start) / 1_000_000.0;
        if(res == null){
            model.setRowCount(0);
            status.setText("Không tìm thấy \"" + key + "\"");
        }else{
            Object[][] data = new Object[res.size()][];
            for(int i = 0; i < res.size(); i++){
                data[i] = new Object[]{key.toUpperCase(), res.get(i)};
            }
            model.setDataVector(data, COLUMNS);
            status.setText(String.format("Tìm thấy %d nghĩa trong %.3f ms", res.size(), ms));
        }

        saveHistory(key, res != null);
    }

    private void searchDefinition(String key){
        long start = System.nanoTime();
        SlangDictionary dict = app.getDict();
        List<String> res = dict.findByDefinition(key);
        double ms = (System.nanoTime() - start) / 1_000_000.0;
        if(res.isEmpty()){
            model.setRowCount(0);
            status.setText("Không tìm thấy \"" + key + "\"");
        }else{
            Object[][] data = new Object[res.size()][];
            for(int i = 0; i < res.size(); i++){
                data[i] = new Object[]{res.get(i), String.join(" | ", dict.findBySlang(res.get(i)))};
            }
            model.setDataVector(data, COLUMNS);
            status.setText(String.format("Tìm thấy %d slang trong %.3f ms", res.size(), ms));
        }

        saveHistory("[Definition] " + key, !res.isEmpty());
    }

    private void saveHistory(String text, boolean found){
        String time = LocalDateTime.now().format(TIME_FORMAT);
        app.addHistory(time + " | " + text.toUpperCase() + (!found ? " (không tìm thấy)" : ""));
    }

    @Override
    public void refresh(){
        model.setRowCount(0);
        status.setText(" ");
        input.requestFocusInWindow();
    }
}
