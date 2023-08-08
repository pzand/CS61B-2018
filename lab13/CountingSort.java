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
        // 定义最小值 和 最大值，用于counting创建数组
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        min = (min < 0 ? -min : -1);    // 数组中没有负数，则不创建负数数组
        max += 1;
        min += 1;
        int[] positiveCounting = new int[max];
        int[] negativeCounting = new int[min];

        // 统计数字的个数
        for (int num : arr) {
            if (num >= 0) {
                positiveCounting[num]++;
            } else {
                negativeCounting[-num]++;
            }
        }

        // 确定每个数字排序后的位置
        int[] positiveState = new int[max];
        int[] negativeState = new int[min];
        int pos = 0;    // 通过一个变量pos，保持优雅。
        // positiveState[i] = positiveState[i - 1] + positiveCounting[i], ps[0] = c[0]。对于第一项需要判断
        // 负数从后往前 正数从前往后
        for (int i = negativeCounting.length - 1; i >= 0; i--) {
            negativeState[i] = pos;
            pos += negativeCounting[i];
        }
        for (int i = 0; i < positiveCounting.length; i++) {
            positiveState[i] = pos;
            pos += positiveCounting[i];
        }

        // 根据原数组 和 数字的实际位置，合成一个排序后的数组
        int[] sorted = new int[arr.length];
        for (int num : arr) {
            if (num >= 0) {
                sorted[positiveState[num]] = num;
                positiveState[num]++;
            } else {
                sorted[negativeState[-num]] = num;
                negativeState[-num]++;
            }
        }
        return sorted;
    }
}
