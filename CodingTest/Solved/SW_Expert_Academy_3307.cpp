#include <cstdio>
#include <cstring>

int* arr;
int mlength;

int main(void)
{
	FILE* file = fopen("C:\\Users\\paul5\\Downloads\\sample_input.txt", "r");

	int numTestCase = 0;
	fscanf(file, "%d", &numTestCase);
	arr = new int[100000];
	int* path = new int[100000];
	for (int testCount = 1; testCount <= numTestCase; testCount++) {
		memset(arr, 0, sizeof(int) * 100000);

		for (int i = 0; i < 100000; i++)
			path[i] = 1;

		mlength = 0;
		fscanf(file, "%d", &mlength);
		
		for (int i = 0; i < mlength; i++)
			fscanf(file, "%d", &arr[i]);

		for (int i = 0; i < mlength-1; i++) {
			for (int j = i+1; j < mlength; j++) {
				if (arr[i] < arr[j] && path[i] == path[j])
					path[j]++;
			}
		}
		int max = 0;
		for (int i = 0; i < mlength; i++)
			if (path[i] > max)
				max = path[i];
		printf("#%d %d\n", testCount, max);

		/*for (int i = 0; i < mlength; i++)
			printf("%d ", arr[i]);
		printf("\n");

		for (int i = 0; i < mlength; i++)
			printf("%d ", path[i]);
		printf("\n");*/
	}
}