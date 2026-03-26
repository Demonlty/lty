package practice;

public class Test_20260322 {

    //洗衣机问题 leetCode --> Packing Machine
    public static int MinOps(int[] arr){
        if (arr == null || arr.length == 0){
            return 0;
        }
        int size = arr.length;
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        if (sum % size != 0){
            return - 1;
        }
        int avg = sum / size;
        int leftSum = 0;
        int res = 0;
        for (int i = 0; i < size; i++) {
            int leftRest = leftSum - i * avg; //i位置左边需要的衣服数量
            int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg; // i位置右边需要的衣服数量
            if (leftRest < 0 && rightRest < 0){
                //左右都负，即都需要i位置来提供衣服
                res = Math.max(res, Math.abs(leftRest) + Math.abs(rightRest));
            }else {
                //左右都正
                //左右一正一负
                res = Math.max(res, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
            }
            leftSum += arr[i];
        }
        return res;
    }
}
