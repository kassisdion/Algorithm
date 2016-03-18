/*
** merge_sort.c
** Made by faisant florian, 50151252
*/

#include <stdio.h> /*printf(), fopen(), fscanf()*/
#include <stdlib.h> /*exit_success, atoi()*/
#include <time.h> //clock()

static void print_int_array(int *array, int array_size) {
	for (int i = 0; i < array_size; i++) {
		printf("%d\n", array[i]);
	}
}

static int* parse_input(char *file_name, int n) {
	int *array = malloc(sizeof(int) * n);
	FILE *file = fopen(file_name, "r");

    int i;
	for (i = 0; i < n; i++) {
		fscanf(file, "%d", &array[i]);
	}

	return (array);
}

/* Function to merge the two subarray [l...m] and [m+1...r]*/
void merge(int arr[], int l, int m, int r)
{
    int i, j, k;
    int n1 = m - l + 1;
    int n2 =  r - m;
 
    /* create and populate left subarray */
    int L[n1];
    for (i = 0; i < n1; i++) {
        L[i] = arr[l + i];
    }

    /* create and populate right subarray */
    int R[n2];
    for (j = 0; j < n2; j++) {
    	R[j] = arr[m + 1 + j];
    }
 
    /* merge the 2 subarray back into [l..r]*/
    i = 0;
    j = 0;
    k = l;
    while (i < n1 && j < n2) {
    	if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        }
        else {
        	arr[k] = R[j];
        	j++;
        }
        k++;
    }
 
    /* Copy the remaining elements of L[], if there are any */
    while (i < n1) {
        arr[k] = L[i];
        i++;
        k++;
    }
 
    /* Copy the remaining elements of R[], if there are any */
    while (j < n2) {
        arr[k] = R[j];
        j++;
        k++;
    }
}

/* 
** l is for left index
** r is right index
*/
static void merge_sort(int *a, int l, int r) {
	if (l < r) {
		int mid = (l + r) / 2;//shoudl use l+(r-l)/2 into avoid overflow
		
		merge_sort(a, l, mid);
		merge_sort(a, mid + 1, r);
		
		merge(a, l, mid, r);
	}
}

int main(int argc, char **argv)
{
	if (argc != 3) {
		printf("Usage : <your_program> <input_file> <N>\n");
	}

    int arr_size = atoi(argv[2]);
	int *arr = parse_input(argv[1], arr_size);

    clock_t start = clock();

    merge_sort(arr, 0, arr_size - 1);
 
    clock_t stop = clock();
    double elapsed = (double)(stop - start) * 1000.0 / CLOCKS_PER_SEC;

    print_int_array(arr, arr_size);
    printf("Running time = %f ms\n", elapsed);

    return (EXIT_SUCCESS);
}
