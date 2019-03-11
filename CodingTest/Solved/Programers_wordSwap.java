import java.util.*;

class Programers_wordSwap {

    public int solution(String begin, String target, String[] words) {
        int times = 0;

        int[] visit = new int[words.length];
        Stack<String> stack = new Stack<>();
        stack.push(begin);

        while(!stack.empty()) {
            String src = stack.pop();
            if(src.equals(target)) {
                return times;
            }
            times++;

            for (int i = 0; i < words.length; i++) {
                if (visit[i] == 0 && changeable(src, words[i])) {
                    stack.push(words[i]);
                    visit[i] = 1;
                }
            }
        }
        return 0;
    }

    private boolean changeable(String src, String dst) {
        int count = 0;
        for (int i = 0; i < src.length(); i++) {
            if(src.charAt(i) != dst.charAt(i)) {
                count++;
                if(count > 1) {
                    return false;
                }
            }
        }
        return count == 0 ? false : true;
    }
}