#include <cstdio>
#include <cstring>

int main(void)
{
	FILE* file = fopen("sample_input_flag.txt", "r");
	
	int numTestCase = 0;
	fscanf(file, "%d", &numTestCase);
	int* W = new int[50];
	int* B = new int[50];
	int* R = new int[50];

	for (int testCount = 1; testCount <= numTestCase; testCount++) {
		memset(W, 0, sizeof(int) * 50);
		memset(B, 0, sizeof(int) * 50);
		memset(R, 0, sizeof(int) * 50);

		int row = 0; int col = 0;
		fscanf(file, "%d %d", &row, &col);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				char item = 0;
				fscanf(file, "%c", &item);

				switch (item) {
				case 'W': W[i]++; break;
				case 'B': B[i]++; break;
				case 'R': R[i]++; break;
				default:  j--; // 공백처리
				}
			}
		}

		/*
		*	제출환경에서는 _CRT_INT_MAX 호출불가
		*	row * col 로 대체
		*/
		int minChange = _CRT_INT_MAX;
		int wc, bc, rc;
		wc = bc = rc = 0;

		for (int w = 0; w < row - 2; w++) {
			wc += col - W[w];
			for (int b = w + 1; b < row - 1; b++) {
				bc += col - B[b];
				for (int r = b + 1; r < row; r++) {
					rc += col - R[r];
				}
				if (wc + bc + rc < minChange)
					minChange = wc + bc + rc;
				rc = 0;
			}
			bc = 0;
		}
		printf("#%d %d\n", testCount, minChange);
	}
	fclose(file);
	return 0;
}