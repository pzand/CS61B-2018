import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int maxLength = 0;
        for (String str : asciis) {
            maxLength = Math.max(maxLength, str.length());
        }

        String[] sorted = asciis.clone();
        for (int i = maxLength - 1; i >= 0; i--) {
            sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     *
     * @param asciis Input array of Strings
     * @param index  The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        // 统计index位置的字符个数
        int[] counting = new int[256 + 1];
        for (String str : asciis) {
            counting[charAtHelper(str, index)]++;
        }

        // 确定该位置字符在排序后的位置
        int[] states = new int[256 + 1];
        int pos = 0;
        // 为该位置空缺的字符，优先处理(最高优先级)
        states[256] = pos;
        pos += counting[256];
        for (int i = 0; i < 256; i++) {
            states[i] = pos;
            pos += counting[i];
        }

        // 根据计算的位置，排列得到排序后的数组
        String[] sorted = new String[asciis.length];
        for (String str : asciis) {
            int charNum = charAtHelper(str, index);
            sorted[states[charNum]] = str;
            states[charNum]++;
        }
        // 复制数组达到破坏原来数组的效果
        System.arraycopy(sorted, 0, asciis, 0, sorted.length);
    }

    // 如果越界表示该位置没有字符，为最高优先级256。正常范围是0~255
    private static int charAtHelper(String str, int index) {
        if (index >= str.length()) {
            return 256;
        }
        return str.charAt(index);
    }

    // 实现MSD基数排序
    public static String[] MSDsort(String[] asciis) {
        String[] sorted = asciis.clone();
        sortHelperMSD(sorted, 0, sorted.length, 0);
        return sorted;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort

        // 停止递归，如果分区少于1个
        if (end - start <= 1) {
            return;
        }

        // 统计数值
        int[] counting = new int[256 + 1];
        for (int i = start; i < end;i++) {
            counting[charAtHelper(asciis[i], index)]++;
        }

        // 确定排序后的位置。位置从零开始，因此实际需要加上start
        int[] starts = new int[256 + 1];
        int pos = 0;
        // 优先级最高的，即该位置没有字符的
        starts[256] = pos;
        pos += counting[256];
        for (int i = 0;i < 256;i++) {
            starts[i] = pos;
            pos += counting[i];
        }

        // 复制一个开始和结束的数组，用于递归
        int[] starts1 = Arrays.copyOf(starts, starts.length);

        //
        String[] sorted = new String[end - start];
        for (int i = start;i < end;i++) {
            int charNum = charAtHelper(asciis[i], index);
            sorted[starts[charNum]] = asciis[i];
            starts[charNum]++;
        }

        // 复制排序后的字符到原来的数组实现破坏性
        System.arraycopy(sorted, 0, asciis, start, sorted.length);

        // 对于最后一个分区 手动递归，避免for循环越界
        sortHelperMSD(asciis, start + starts1[255], end, index + 1);
        sortHelperMSD(asciis, start, start + starts1[0], index + 1);
        // 递归执行分区
        for (int i = 0;i < 256 - 1;i++){
            int s = start + starts1[i], e = start + starts1[i + 1];
            // 避免因为分区的元素是相同的，导致无限递归。原因可能是递归条件没有判断index的问题
            if (start == s && e == end) {
                continue;
            }
            sortHelperMSD(asciis,s ,e, index + 1);
        }
    }


    public static void main(String[] args) {
        String[] str = {"aaa", "bbb", "ccc", "ddd", "aaa", "abc", "bca", "da", "a", "aaa", "abc"};
        for (String s : MSDsort(str)) {
            System.out.println(s);
        }
        for (String s : sort(str)) {
            System.out.println(s);
        }
    }
}
