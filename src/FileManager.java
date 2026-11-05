import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileManager {
    public static final String ORIGINAL_FILE = "data/slang.txt";
    public static final String DATA_FILE = "data/dictionary.dat";
    public static final String HISTORY_FILE = "data/history.txt";

    private FileManager(){}

    public static SlangDictionary readOriginal() throws IOException {
        SlangDictionary dict = new SlangDictionary();
        String lastSlang = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ORIGINAL_FILE), StandardCharsets.UTF_8))) {
            br.readLine(); // Bỏ qua dòng tiêu đề
            String line;
            while((line = br.readLine()) != null){
                int pos = line.indexOf("`");
                if(pos < 0){
                    if(lastSlang != null){
                        addDefinitions(dict, lastSlang, line);
                    }
                }else{
                    String slang = line.substring(0, pos).trim();
                    String defs = line.substring(pos + 1).trim();
                    if(slang.isEmpty()) continue;
                    addDefinitions(dict, slang, defs);
                    lastSlang = slang;
                }
            }
        } 
        return dict;
    }

    private static void addDefinitions(SlangDictionary dict, String slang, String defs){
        String[] definitions = defs.split("\\|");
        for(String str : definitions){
            str = str.trim();
            if(!str.isEmpty()){
                dict.addDefinition(slang, str);
            }
        }
    }

    public static void saveData(SlangDictionary dict) throws IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(DATA_FILE)))){
            out.writeObject(dict);
        }
    }

    public static SlangDictionary loadData(){
        if(!new File(DATA_FILE).exists()) return null;
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(DATA_FILE)))){
            Object obj = in.readObject();
            if(obj instanceof SlangDictionary){
                return (SlangDictionary) obj;
            }
            return null;
        } catch(IOException | ClassNotFoundException e){
            System.err.println("Không đọc được " + DATA_FILE + ": " + e);
            return null;
        }
    }

    public static List<String> loadHistory(){
        List<String> res = new ArrayList<>();
        if(!new File(HISTORY_FILE).exists()) return res;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(HISTORY_FILE), StandardCharsets.UTF_8))){
            String line;
            while((line = br.readLine()) != null){
                res.add(line);
            }
        } catch(IOException e){
            System.err.println("Không đọc được " + HISTORY_FILE + ": " + e);
        }
        return res;
    }

    public static void appendHistory(String entry){
        entry = entry.replace('\n', ' ').replace('\r', ' ');
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HISTORY_FILE, true), StandardCharsets.UTF_8))){
            bw.write(entry);
            bw.newLine();
        } catch(IOException e){
            System.err.println("Không ghi được " + HISTORY_FILE + ": " + e);
        }
    }

    public static void clearHistory(){
        new File(HISTORY_FILE).delete();
    }
}
