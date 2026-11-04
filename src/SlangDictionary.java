import java.io.Serializable;
import java.util.*;

public class SlangDictionary implements Serializable{
    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> slangMap = new HashMap<>();
    private Map<String, Set<String>> wordIndex = new HashMap<>();

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
            List<String> token = tokenize(definition);
            for(String word : token){
                wordIndex.computeIfAbsent(word, k -> new HashSet<>()).add(slang);
            }
        }
    }

    private static List<String> tokenize(String text){
        List<String> res = new ArrayList<>();
        for(String word : text.toLowerCase().split("\\W+")){
            if(!word.isEmpty()){
                res.add(word);
            }
        }
        return res;
    }

    // slang truyền vào phải đã normalize
    private void index(String slang){
        List<String> defs = slangMap.get(slang);
        if(defs == null){
            return;
        }
        for(String def : defs){
            List<String> token = tokenize(def);
            for(String word : token){
                wordIndex.computeIfAbsent(word, k -> new HashSet<>()).add(slang);
            }
        }
    }

    // slang truyền vào phải đã normalize; gọi TRƯỚC khi sửa/xoá nghĩa
    private void unindex(String slang){
        List<String> defs = slangMap.get(slang);
        if(defs == null){
            return;
        }
        for(String def : defs){
            List<String> token = tokenize(def);
            for(String word : token){
                Set<String> slangs = wordIndex.get(word);
                if(slangs != null){
                    slangs.remove(slang);
                    if(slangs.isEmpty()){
                        wordIndex.remove(word);
                    }
                }
            }
        }
    }

    public List<String> findByDefinition(String query){
        List<String> words = tokenize(query);
        if(words.isEmpty()){
            return new ArrayList<>();
        }
        Set<String> first = wordIndex.get(words.get(0));
        if(first == null){
            return new ArrayList<>();
        }
        Set<String> res = new HashSet<>(first);
        for(int i = 1; i < words.size(); i++){
            Set<String> slangs = wordIndex.get(words.get(i));
            if(slangs == null){
                return new ArrayList<>();
            }
            res.retainAll(slangs);
        }
        List<String> list = new ArrayList<>(res);
        Collections.sort(list);
        return list;
    }

    public boolean editDefinition(String slang, int pos, String newDefinition){
        slang = normalize(slang);
        List<String> definition = slangMap.get(slang);
        if(definition == null || pos < 0 || pos >= definition.size()){
            return false;
        }
        unindex(slang);
        definition.set(pos, newDefinition.trim());
        index(slang);
        return true;
    }

    public void overwrite(String slang, String definition){
        slang = normalize(slang);
        if(slangMap.containsKey(slang)){
            List<String> def = slangMap.get(slang);
            unindex(slang);
            def.clear();
            def.add(definition.trim());
            index(slang);
        }else{
            addDefinition(slang, definition);
        }
    }

    public boolean delete(String slang){
        slang = normalize(slang);
        if(!slangMap.containsKey(slang)) return false;
        unindex(slang);
        slangMap.remove(slang);
        return true;
    }

    public List<String> randomSlangs(int n, Random random){
        List<String> keys = new ArrayList<>(slangMap.keySet());
        if(n > keys.size()){
            n = keys.size();
        }
        if(n <= 0){
            return new ArrayList<>();
        }
        Collections.shuffle(keys, random);
        return new ArrayList<>(keys.subList(0, n));
    }

    public List<String> allSlangs(){
        List<String> keys = new ArrayList<>(slangMap.keySet());
        Collections.sort(keys);
        return keys;
    }
}
