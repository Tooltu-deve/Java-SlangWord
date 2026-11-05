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

    }

    public static SlangDictionary loadData(){
        return null;
    }

    public static List<String> loadHistory(){
        return new ArrayList<>();
    }

    public static void appendHistory(String entry){
       
    }

    public static void clearHistory(){
        
    }
}
