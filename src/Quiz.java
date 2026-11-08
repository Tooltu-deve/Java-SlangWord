import java.util.*;

public class Quiz {
    private final String question;
    private final List<String> options;
    private final int answerIndex;

    private Quiz(String question, List<String> options, int answerIndex){
        this.question = question;
        this.options = options;
        this.answerIndex = answerIndex;
    }

    // chức năng 9: cho slang, chọn 1 trong 4 nghĩa
    public static Quiz fromSlang(SlangDictionary dict, Random random){
        List<String> all = dict.randomSlangs(dict.size(), random);
        if(all.isEmpty()){
            throw new IllegalStateException("Từ điển trống");
        }

        String slang = all.get(0);
        List<String> defs = dict.findBySlang(slang);
        String correct = defs.get(random.nextInt(defs.size()));

        List<String> options = new ArrayList<>();
        options.add(correct);
        
        for(int i = 1; i < all.size() && options.size() < 4; i++){
            String falseSlang = all.get(i);
            List<String> falseDef = dict.findBySlang(falseSlang);
            String d = falseDef.get(random.nextInt(falseDef.size()));

            if(!defs.contains(d) && !options.contains(d)){
                options.add(d);
            }
        }
        if(options.size() < 4){
            throw new IllegalStateException("Không đủ dữ liệu tạo câu đố!");
        }

        Collections.shuffle(options, random);
        int answer = options.indexOf(correct);
        return new Quiz("Slang \"" + slang + "\" " + "có nghĩa là gì?", options, answer);
    }

    // chức năng 10: cho nghĩa, chọn 1 trong 4 slang
    public static Quiz fromDefinition(SlangDictionary dict, Random random){
        List<String> all = dict.randomSlangs(dict.size(), random);
        if(all.isEmpty()){
            throw new IllegalStateException("Từ điển trống");
        }

        String slang = all.get(0);
        List<String> defs = dict.findBySlang(slang);
        String def = defs.get(random.nextInt(defs.size()));
        List<String> options = new ArrayList<>();
        options.add(slang);

        for(int i = 1; i < all.size() && options.size() < 4; i++){
            String choiceSlang = all.get(i);
            List<String> choiceDefs = dict.findBySlang(choiceSlang);
            if(!choiceDefs.contains(def) && !options.contains(choiceSlang)){
                options.add(choiceSlang);
            }
        }
        if(options.size() < 4){
            throw new IllegalStateException("Không đủ dữ liệu tạo câu đố!");
        }

        Collections.shuffle(options, random);
        int answer = options.indexOf(slang);
        return new Quiz("Nghĩa \"" + def + "\" " + "thuộc về slang nào?", options, answer);
    }

    public String getQuestion(){
        return question;
    }

    public List<String> getOptions(){
        List<String> res = new ArrayList<>(options);
        return res;
    }

    public int getAnswerIndex(){
        return answerIndex;
    }
}
