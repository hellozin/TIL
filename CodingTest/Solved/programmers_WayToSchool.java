class Solution {
    public int solution(int m, int n, int[][] puddles) {
        int map[][] = new int[n][m];
        
        map[0][0] = 1;
        
        for(int i = 0; i < puddles.length; i++) {
            map[puddles[i][1]-1][puddles[i][0]-1] = -1;
        }
        
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(map[i][j] == -1) {
                    map[i][j] = 0;
                    continue;
                }
                
                if(i == 0 && j == 0) {
                    continue;
                }
                
                int sum = 0;
                if(i != 0) {
                    sum += map[i-1][j];
                }
                if(j != 0) {
                    sum += map[i][j-1];
                }
                
                map[i][j] = sum % 1000000007;
            }
        }
        
        int answer = map[n-1][m-1];
        return answer;
    }
}

/*
정확성  테스트
테스트 1 〉	통과 (1.02ms, 45.1MB)
테스트 2 〉	통과 (0.96ms, 47.6MB)
테스트 3 〉	통과 (1.03ms, 44.5MB)
테스트 4 〉	통과 (1.10ms, 44.6MB)
테스트 5 〉	통과 (0.98ms, 48.4MB)
테스트 6 〉	통과 (0.94ms, 47.4MB)
테스트 7 〉	통과 (0.95ms, 47.9MB)
테스트 8 〉	통과 (1.03ms, 44.3MB)
테스트 9 〉	통과 (1.09ms, 45.2MB)
테스트 10 〉	통과 (1.02ms, 44.5MB)
효율성  테스트
테스트 1 〉	통과 (1.68ms, 48.1MB)
테스트 2 〉	통과 (1.22ms, 48.3MB)
테스트 3 〉	통과 (1.37ms, 47.7MB)
테스트 4 〉	통과 (1.51ms, 44.3MB)
테스트 5 〉	통과 (1.52ms, 45MB)
테스트 6 〉	통과 (1.77ms, 45MB)
테스트 7 〉	통과 (1.29ms, 47.7MB)
테스트 8 〉	통과 (1.52ms, 48.3MB)
테스트 9 〉	통과 (1.58ms, 48.2MB)
테스트 10 〉	통과 (1.61ms, 44.8MB)
채점 결과
정확성: 50.0
효율성: 50.0
합계: 100.0 / 100.0
*/