package practice;

public class Test_20260330 {

    //K进制（0~K）
    //K伪进制（1~K）
    //从低到高，依次分一个。不能分下一个高位的时候，从这个位置到低位继续分
    public static String KPseudoBase(int n, int k){

        int p = 1;
        int i = 0;
        while (n - p >= 0){
            n -= p;
            p *= k;
            i++;
        }
        int[] arr = new int[i];
        for (int j = 0; j < arr.length; j++) {
            arr[j] = 1;
        }
        StringBuilder sb = new StringBuilder();
        int s = (int) Math.pow(k, arr.length - 1);
        for (int j = arr.length - 1; j >= 0; j--) {
            while (n - s >= 0){
                n -= s;
                arr[j]++;
            }
            s /= k;
            sb.append((char) ('A'+arr[j]-1));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(KPseudoBase(18279, 26));
    }

}
