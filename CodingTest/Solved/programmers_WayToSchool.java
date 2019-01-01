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
��Ȯ��  �׽�Ʈ
�׽�Ʈ 1 ��	��� (1.02ms, 45.1MB)
�׽�Ʈ 2 ��	��� (0.96ms, 47.6MB)
�׽�Ʈ 3 ��	��� (1.03ms, 44.5MB)
�׽�Ʈ 4 ��	��� (1.10ms, 44.6MB)
�׽�Ʈ 5 ��	��� (0.98ms, 48.4MB)
�׽�Ʈ 6 ��	��� (0.94ms, 47.4MB)
�׽�Ʈ 7 ��	��� (0.95ms, 47.9MB)
�׽�Ʈ 8 ��	��� (1.03ms, 44.3MB)
�׽�Ʈ 9 ��	��� (1.09ms, 45.2MB)
�׽�Ʈ 10 ��	��� (1.02ms, 44.5MB)
ȿ����  �׽�Ʈ
�׽�Ʈ 1 ��	��� (1.68ms, 48.1MB)
�׽�Ʈ 2 ��	��� (1.22ms, 48.3MB)
�׽�Ʈ 3 ��	��� (1.37ms, 47.7MB)
�׽�Ʈ 4 ��	��� (1.51ms, 44.3MB)
�׽�Ʈ 5 ��	��� (1.52ms, 45MB)
�׽�Ʈ 6 ��	��� (1.77ms, 45MB)
�׽�Ʈ 7 ��	��� (1.29ms, 47.7MB)
�׽�Ʈ 8 ��	��� (1.52ms, 48.3MB)
�׽�Ʈ 9 ��	��� (1.58ms, 48.2MB)
�׽�Ʈ 10 ��	��� (1.61ms, 44.8MB)
ä�� ���
��Ȯ��: 50.0
ȿ����: 50.0
�հ�: 100.0 / 100.0
*/