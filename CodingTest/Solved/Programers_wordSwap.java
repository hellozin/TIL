import java.util.*;

/***
 *
 * 프로그래머스 - 단어 변환
 * https://programmers.co.kr/learn/courses/30/lessons/43163?language=java
 *
 */
class Programers_wordSwap {

    public int solution(String begin, String target, String[] words) {
        Queue<String> queue = new LinkedList<>();
        queue.offer(begin);
        queue.offer(null);
        
        int depth = 0;
        int[] visit = new int[words.length];
        while(!queue.isEmpty()) {
            String src = queue.poll();
            
            if(src == null) {
                if(queue.peek() == null) {
                    /* Nothing in next step */
                    return 0;
                }
                depth++;
                
                /* End of sibling */
                queue.offer(null);
            } else if(src.equals(target)) {
                return depth;
            }

            /* Add next steps */
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
        int diffCnt = 0;
        for (int i = 0; i < src.length(); i++) {
            if(src.charAt(i) != dst.charAt(i)) {
                diffCnt++;
                if(diffCnt > 1) {
                    return false;
                }
            }
        }
        /* 
         * if diff one char 
         * return true 
         */
        return diffCnt == 1;
    }
}