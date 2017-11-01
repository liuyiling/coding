import java.util.*;

import static java.lang.System.out;

/**
 * Created by liuyiling on 2017/9/6
 */
public class WhoIsLuck {

    public static void main(String[] args) {
        List<String> candidates = new ArrayList<>();
        candidates.add("马驰");
        candidates.add("俊达");
        candidates.add("陈祺");
        candidates.add("李坤");
        candidates.add("林炜");
        candidates.add("刘艺凌");

        Random random =new Random();
        Set<String> luckyMans = new HashSet<>();
        while(luckyMans.size() != 2){
            int index = random.nextInt(candidates.size());
            luckyMans.add(candidates.get(index));
        }

        out.println(luckyMans);

    }

}
