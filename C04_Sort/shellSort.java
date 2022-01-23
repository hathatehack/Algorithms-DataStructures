package C04_Sort;

public class shellSort {
    static public void main(String[] args)
    {
        int[] arr = new int[]{1,3,5,2,6,4};
        shellSort(arr);
        System.out.print(arr);
    }

    static public void shellSort(int[] array)
    {
        int knuth = 1;
        for (; knuth <= array.length / 3; knuth = 3*knuth + 1){}

//        int gap = array.length/2;
        int gap = knuth;
        for (; gap > 0; gap/=2)
            insertSort(array, gap);
    }

    static public void insertSort(int[] array, int gap)
    {
        for(int i = gap; i < array.length; i++)
        {
            int ele = array[i];
            int j = i - gap;
            for (; j >= 0; j-=gap)
            {
                if(ele < array[j])
                    array[j + gap] = array[j];
                else
                    break;
            }
            array[j + gap] = ele;
        }
    }
}
