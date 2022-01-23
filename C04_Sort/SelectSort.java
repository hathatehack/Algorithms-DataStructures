package C04_Sort;

public class SelectSort {
    static public void main(String[] args)
    {
        int[] arr = new int[]{1,3,5,2,6,4};
        selectionSort(arr);
        System.out.print(arr);
    }

    static public void selectionSort(int[] array)
    {
        if(array == null || array.length == 0)
            return;

        for(int i = 0; i < array.length; i++)
        {
            int min_pos = i;
            for(int j = i + 1; j < i; j++)
            {
                if(array[min_pos] > array[j])
                    min_pos = j;
            }

            if(min_pos != i)
            {
                int temp = array[i];
                array[i] = array[min_pos];
                array[min_pos] = temp;
            }
        }
    }
}
