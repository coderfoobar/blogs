
# 在无序数组中找到第K个最大值

> Create Time ： 2017年8月3日 Ref : http://blog.csdn.net/hzh_csdn/article/details/53446211

1.排序

```Java
public static int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
}
```

时间复杂度为O(nlog(n))。

2.通过堆

```Java
public static int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> q = new PriorityQueue<Integer>(k);
    for (int i : nums) {
        q.offer(i);    // add
        if (q.size() > k) {
            q.poll();  // remove
        }
    }
    return q.peek();   // element
}
```

时间复杂度为O(nlog(k))，空间复杂度为 O(k) 。

3.快排

```Java
ublic static int findKthLargest(int[] nums, int k) {
        if (k < 1 || nums == null) {
            return 0;
        }

        return getKth(nums.length - k + 1, nums, 0, nums.length - 1);
    }

    public static int getKth(int k, int[] nums, int start, int end) {

        int pivot = nums[end];

        int left = start;
        int right = end;

        while (true) {

            while (nums[left] < pivot && left < right) {
                left++;
            }

            while (nums[right] >= pivot && right > left) {
                right--;
            }

            if (left == right) {
                break;
            }

            swap(nums, left, right);
        }

        swap(nums, left, end);

        if (k == left + 1) {
            return pivot;
        } else if (k < left + 1) {
            return getKth(k, nums, start, left - 1);
        } else {
            return getKth(k, nums, left + 1, end);
        }
    }

    public static void swap(int[] nums, int n1, int n2) {
        int tmp = nums[n1];
        nums[n1] = nums[n2];
        nums[n2] = tmp;
    }
```

平均时间复杂度为O(n)，最坏情况下为O(n^2)。
