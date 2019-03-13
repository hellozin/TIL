import java.util.*;

/***
 *
 * 프로그래머스 - 단어 변환
 * https://programmers.co.kr/learn/courses/30/lessons/43163?language=java
 *
 */
class Programers_wordSwap {

    public int solution(String begin, String target, String[] words) {
        int[] visit = new int[words.length];
        Queue<String> queue = new LinkedList<>();
        queue.offer(begin);
        queue.offer(null);

        int times = 0;
        while(!queue.isEmpty()) {
            String src = queue.poll();
            if(src == null) {
                if(queue.peek() == null) {
                    /* Nothing in next step */
                    return 0;
                }
                times++;
                queue.offer(null);
            } else if(src.equals(target)) {
                return times;
            }

            for (int i = 0; src != null && i < words.length; i++) {
                if (visit[i] == 0 && changeable(src, words[i])) {
                    queue.offer(words[i]);
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