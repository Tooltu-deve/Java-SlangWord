import java.io.Serializable;
import java.util.*;

public class SlangDictionary implements Serializable{
    private static final long serialVersionUID = 1L;

    private HashMap<String, List<String>> slangMap = new HashMap<>();

    private static String normalize(String slang){
        String res = slang.trim();
        res = res.toUpperCase();
        return res;
    }

    public int size(){
        return slangMap.size();
    }

    public boolean contains(String slang){
        return slangMap.containsKey(normalize(slang));
    }

    public List<String> findBySlang(String slang){
        List<String> l = slangMap.get(normalize(slang));
        if(l == null){
            return null;
        }
        return new ArrayList<>(l);
    }

    public void addDefinition(String slang, String definition){
        slang = normalize(slang);
        definition = definition.trim();
        List<String> l = slangMap.computeIfAbsent(slang, k -> new ArrayList<>());
        if(!l.contains(definition)){
            l.add(definition);
        }
    }
}
