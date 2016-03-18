/*
** insertion_sort.c
** Made by faisant florian, 50151252
*/

#include <stdio.h> /*printf(), fopen(), fscanf()*/
#include <stdlib.h> /*EXIT_SUCCESS, atoi(), malloc()*/

static void print_int_array(int *array, int array_size) {
	for (int i = 0; i < array_size; i++) {
		printf("%d\n", array[i]);
	}
}

static int* parse_input(char *file_name, int n) {
	int *array = malloc(sizeof(int) * n);
	FILE *file = fopen(file_name, "r");

	for (int i = 0; i < n; i++) {
		fscanf(file, "%d", &array[i]);
	}

	return (array);
}

void insertion_sort(int arr[], int n)
{
   int i, key, j;
   for (i = 1; i < n; i++)
   {
       key = arr[i];
       j = i-1;
  
       /* Move elements of arr[0..i-1], that are
          greater than key, to one position ahead
          of their current position */
       while (j >= 0 && arr[j] > key)
       {
           arr[j+1] = arr[j];
           j = j-1;
       }
       arr[j+1] = key;
   }
}

int main(int argc, char **argv)
{
	if (argc != 3) {
		printf("Usage : <your_program> <input_file> <N>\n");
	}

    int arr_size = atoi(argv[2]);
	int *arr = parse_input(argv[1], arr_size);
 
    printf("Given array is \n");
    print_int_array(arr, arr_size);

    insertion_sort(arr, arr_size);
 
    printf("\nSorted array is \n");
    print_int_array(arr, arr_size);
    
    return (EXIT_SUCCESS);
}
