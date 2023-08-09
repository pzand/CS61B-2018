/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            int place = starts[item];
            sorted2[place] = item;
            starts[item] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        // TODO make counting sort work with arrays containing negative numbers.
        /* 实际实验表示该方法不行，因为数组过大*/
//         由于补码负数的特性，-2147483648的绝对值仍为自身，正数的最大值为2147483647
//         则最大可以初始化2147483647大小的数组，无法存放2147483647，-2147483648，-2147483647
//         即把2147483647，-2147483648两个临界值单独存储，-2147483647存放到负数数组的零位置。
//         通过对负数加上2147483647，达到负数转正数效果

        // 定义最小值 和 最大值，用于counting创建数组
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        // 如果随意开数组会导致内存溢出，根据题意知数值范围不会超过2百万
        // 因此让第一位存储min，最高位存储max
        int[] counting = new int[max - min + 1];
        for (int num : arr) {
            counting[num + (-min)]++;
        }

        // 根据统计数字，得出数字实际排序位置
        int[] starts = new int[max - min + 1];
        int pos = 0;
        for (int i = 0;i < counting.length;i++) {
            starts[i] = pos;
            pos += counting[i];
        }

        // 得到实际的数字排序
        int[] sorted = new int[arr.length];
        for (int num : arr) {
            sorted[starts[num + (-min)]] = num;
            starts[num + (-min)]++;
        }
        return sorted;
    }

    public static void main(String[] args) {
          int[] arr = {-1, -2, -2, -3, -1, 10, -19, 100, 100};
//        for (int num : naiveCountingSort(arr)) {
//            System.out.println(num);
//        }

        for (int num : betterCountingSort(arr)) {
            System.out.println(num);
        }
    }
}
