import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ManagePanel extends JPanel implements Refreshable {
    private static final String[] COLUMNS = {"Slang", "STT", "Definition"};

    private MainFrame app;
    private JTextField filter = new JTextField(15);
    private JLabel status = new JLabel(" ");
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public ManagePanel(MainFrame app){
        this.app = app;
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        model = new DefaultTableModel(COLUMNS, 0){
            @Override public boolean isCellEditable(int row, int col) {return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel f = new JLabel("Lọc:");
        JButton addBtn = new JButton("Thêm");
        JButton editBtn = new JButton("Sửa");
        JButton delBtn = new JButton("Xoá");
        JButton resetBtn = new JButton("Reset");

        top.add(f);
        top.add(filter);
        top.add(addBtn);
        top.add(editBtn);
        top.add(delBtn);
        top.add(resetBtn);

        filter.addActionListener(e -> applyFilter());
        addBtn.addActionListener(e -> addSlang());
        editBtn.addActionListener(e -> editSlang());
        delBtn.addActionListener(e -> deleteSlang());
        resetBtn.addActionListener(e -> resetDict());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    private void reload(){
        SlangDictionary dict = app.getDict();
        List<Object[]> rows = new ArrayList<>();
        for(String slang : dict.allSlangs()){
            List<String> defs = dict.findBySlang(slang);
            for(int i = 0; i < defs.size(); i++){
                rows.add(new Object[]{slang, i + 1, defs.get(i)});
            }
        }
        // nạp 1 lần, không addRow từng dòng
        model.setDataVector(rows.toArray(new Object[0][]), COLUMNS);
        // setDataVector tạo lại cột nên phải chỉnh độ rộng sau
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        status.setText(dict.size() + " slang, " + rows.size() + " nghĩa");
    }


    private void applyFilter(){
        String text = filter.getText().trim();
        if(text.isEmpty()){
            sorter.setRowFilter(null);
        }else{
            // quote để ký tự như ( [ * không bị hiểu là regex
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        }
    }

    // trả về chỉ số dòng trong MODEL, -1 nếu chưa chọn
    private int selectedRow(){
        int view = table.getSelectedRow();
        if(view == -1){
            status.setText("Hãy chọn 1 dòng");
            return -1;
        }

        return table.convertRowIndexToModel(view);
    }

    private void addSlang(){
        JTextField slangField = new JTextField(15);
        JTextField defField = new JTextField(30);
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 4));
        form.add(new JLabel("Slang:"));
        form.add(slangField);
        form.add(new JLabel("Definition:"));
        form.add(defField);

        int ok = JOptionPane.showConfirmDialog(this, form, "Thêm slang", JOptionPane.OK_CANCEL_OPTION);
        if(ok != JOptionPane.OK_OPTION) return;

        String slang = slangField.getText().trim();
        String def = defField.getText().trim();

        if(slang.isEmpty() || def.isEmpty()){
            status.setText("Vui lòng điền đầy đủ thông tin");
            return;
        }
        SlangDictionary dict = app.getDict();
        if(!dict.contains(slang)){
            dict.addDefinition(slang, def);
        }else{
            String[] options = {"Overwrite", "Duplicate", "Huỷ"};
            int choice = JOptionPane.showOptionDialog(this, "Slang \"" + slang + "\" đã tồn tại. Bạn muốn làm gì?", "Slang trùng",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(choice == 0){
                dict.overwrite(slang, def);
            }else if(choice == 1){
                dict.addDefinition(slang, def);
            }else{
                return;
            }
        }
        app.saveDict();
        reload();
        status.setText("Đã thêm slang!");
    }

    private void editSlang(){
        int row = selectedRow();
        if(row == -1) return;
        String slang = (String) model.getValueAt(row, 0);
        int pos = (Integer) model.getValueAt(row, 1) - 1;
        String old = (String) model.getValueAt(row, 2);

        String newDef = JOptionPane.showInputDialog(this, "Nghĩa mới của " + slang + ": ", old);
        if(newDef == null) return;
        if(newDef.trim().isEmpty()){
            status.setText("Lỗi ký tự rỗng");
            return;
        }
        if(!app.getDict().editDefinition(slang, pos, newDef)){
            status.setText("Không sửa được \"" + slang + "\"");
            return;
        }
        app.saveDict();
        reload();
        status.setText("Sửa thành công!");
    }

    private void deleteSlang(){
        int row = selectedRow();
        if(row == -1) return;
        String slang = (String) model.getValueAt(row, 0);

        int ok = JOptionPane.showConfirmDialog(this, "Xoá slang \"" + slang + "\" (toàn bộ nghĩa)?", "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION);
        if(ok != JOptionPane.YES_OPTION) return;

        app.getDict().delete(slang);
        app.saveDict();
        reload();
        status.setText("Đã xoá \"" + slang + "\"");
    }

    private void resetDict(){
        int ok = JOptionPane.showConfirmDialog(this, "Khôi phục từ điển gốc? Mọi thay đổi sẽ mất.", "Reset",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(ok != JOptionPane.YES_OPTION) return;

        try{
            app.setDict(FileManager.readOriginal());
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Không đọc được " + FileManager.ORIGINAL_FILE + ":\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        app.saveDict();
        reload();
        status.setText("Đã khôi phục từ điển gốc");
    }

    @Override
    public void refresh(){
        reload();
    }
}
