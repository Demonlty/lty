package sorts;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class HashTest {

    //哈希函数
    //均匀性，离散型

    //设置RandomPool结构
    //insert(key) 不重复加入
    //delete(key) 移除
    //getRandom() 等概率随机返回
    //时间复杂度都是o(1)
    class Pool<K> {
        private HashMap<K,Integer> keyIndexMap;
        private HashMap<Integer,K> indexKeyMap;
        private int size;

        public Pool() {
            this.keyIndexMap = new HashMap<>();
            this.indexKeyMap = new HashMap<>();
            this.size = 0;
        }

        public void insert(K key){
            if (!keyIndexMap.containsKey(key)){
                keyIndexMap.put(key,this.size);
                indexKeyMap.put(this.size++,key);
            }
        }

        public void delete(K key){
            if (!keyIndexMap.containsKey(key)){
                int lastIndex = --this.size;
                K k = indexKeyMap.get(lastIndex);
                Integer i = keyIndexMap.get(key);
                this.keyIndexMap.put(k,i);
                this.indexKeyMap.put(i,k);
                this.keyIndexMap.remove(key);
                this.indexKeyMap.remove(lastIndex);
            }
        }

        public K getRandom(){
            if (this.size == 0){
                return null;
            }
            int randomIndex = (int) (Math.random() * this.size); //0 ~ size-1
            return this.indexKeyMap.get(randomIndex);
        }
    }

    //布隆过滤器 省空间，但是有失误率   只跟样本量和失误率有关，和单个样本的大小无关
    //位图
    public static void bitArr(){
        //使用基础类型表示为类型
        int[] arr = new int[]{};
        //arr[0] 表示 0 ~ 31
        //arr[1] 表示 32 ~ 63
        //arr[2] 表示 64 ~ 95

        //178位的信息
        int numIndex = 178 / 32;
        int bitIndex = 178 % 32;
        int s = (arr[numIndex] | (1 << bitIndex)); //178位的状态
        int i = 178;
        arr[numIndex] = arr[numIndex] | (1 << bitIndex); //178位的状态设置为1
        arr[numIndex] = arr[numIndex] & (~(1 << bitIndex)); //178位的状态设置为0
        int bit = (arr[i/32] >> (i % 32)) & 1; //拿出178位的状态
    }

    //一致性哈希原理  分布式数据库底层原理
    //1、哈希环 解决增删需全量改动问题  比如：在环上分 m1,m2,m3,m4,4个点
    //2、在哈希环上 虚拟分配（形成按比例分配）解决两个问题：数量少怎么均分环；增删后怎么均分环；
    //比如：m1(a0~a1000)，m2(a0~a1000)，m3(a0~a1000)，利用哈希的均匀性，m1,m2,m3是可以均分环的；
    //3、按不同比例分可以控制负载的大小，比如m1(a0~a500)，m2(a0~a2000)，m3(a0~a8000)，通过负载大小的规则按上述m1，m2，m3比例分配来控制负载

    //岛问题 上下左右找相连的1，找到后置为2  O(N*M) 优化：并行算法（多CPU同时计算）：记录边界信息，使用并查集合并计算
    public static int countIslands(int[][] m){
        if (m == null || m[0] == null){
            return 0;
        }
        int N = m.length; //行
        int M = m[0].length; //列
        int res = 0; //岛的数量
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (m[i][j] == 1){
                    res++;
                    infect(m,i,j,N,M);
                }
            }
        }
        return res;
    }
    public static void infect(int[][] m, int i, int j, int N, int M){
        if (i < 0 || i >= N || j < 0 || j >= M || m[i][j] != 1){
            return;
        }
        m[i][j] = 2; //合法后，值改成2
        infect(m,i-1,j,N,M);
        infect(m,i+1,j,N,M);
        infect(m,i,j-1,N,M);
        infect(m,i,j+1,N,M);
    }

    public static class Element<V>{
        private V value;

        public Element(V value) {
            this.value = value;
        }
    }
    //并查集 平均时间复杂度 O（1）
    public static class UnionFindSet<V>{
        public HashMap<V, Element<V>> elementMap;
        public HashMap<Element<V>, Element<V>> fatherMap;
        public HashMap<Element<V>, Integer> sizeMap;

        public UnionFindSet(List<V> list) {
            elementMap = new HashMap<>();
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
            for (V v : list){
                Element<V> element = new Element<>(v);
                elementMap.put(v,element);
                fatherMap.put(element,element);
                sizeMap.put(element,1);
            }
        }
        //寻找顶元素，寻找时更新新顶元素
        public Element<V> findHead(Element<V> element){
            Stack<Element<V>> path = new Stack<>();
            while (element != fatherMap.get(element)){
                path.push(element);
                element = fatherMap.get(element);
            }
            while(!path.isEmpty()){
                fatherMap.put(path.pop(),element);
            }
            return element;
        }

        public boolean isSameSet(V a, V b){
            if (elementMap.containsKey(a) && elementMap.containsKey(b)){
                return findHead(elementMap.get(a)) == findHead(elementMap.get(b));
            }
            return false;
        }

        public void union(V a, V b){
            if (elementMap.containsKey(a) && elementMap.containsKey(b)){
                Element<V> af = findHead(elementMap.get(a));
                Element<V> bf = findHead(elementMap.get(b));
                if (af != bf){
                    Element<V> big = sizeMap.get(af) >=  sizeMap.get(bf) ? af : bf;
                    Element<V> small = big == af ? bf : af;
                    fatherMap.put(small, big);
                    sizeMap.put(big, sizeMap.get(big) + sizeMap.get(small));
                    sizeMap.remove(small);
                }
            }
        }
    }

    //位图解决某一范围上数字的出现情况，并可以节省大量空间
    //用分区间统计解决，分段统计


    //有一个包含100亿个URL的大文件，假设每个URL占用64B，情景找出其中所有重复的URL
    //某搜索公司一天的用户搜索词汇是海量的（百亿数据量），请设计一种求出每天热门Top100词汇的可行办法
    //1、哈希分流，分出各个分堆（大根堆），再由各个分堆的堆顶组成总堆（大根堆），总堆依次弹出堆顶，弹出后维持分堆和总堆的关系，总堆依次弹出的堆顶即为结果
    //小排名合成大排名，使用堆结构来解决


    //32位无符号整数的范围是0~4294967295，现在右40亿个无符号整数，可以使用最多1GB的内存，找出所有出现了两次的数
    //1、哈希分流
    //2、使用位图，两位来记录状态
    // 00 出现0次
    // 01 出现1次
    // 10 出现2次
    // 11 出现2次以上


    //可以使用最多10MB的内存，怎么找到这40亿个整数的中位数
    //使用内存大小计算出区间大小（数组大小），4294967295/区间大小 得出多少区间
    //区间一次递加找出20亿所在的区间，再次划分，最后能找到这个中位数


    //位运算
    //有符号32位整数a和b，返回较大的
    //参数n，不是1就是0
    //1-》0；0-》1
    public static int flip(int n){
        return n ^ 1;
    }
    //n是非负数，返回1
    //n是负数，返回0
    public static  int sign(int n){
        return flip((n >> 31) & 1);
    }
    //互斥可以代替if
    public static int getMax1(int a, int b){
        int c = a-b; //可能会溢出，导致代码出错
        int scA = sign(c);
        int scB = flip(scA);
        return a * scA + b * scB;
    }
    //互斥相加可以代替if
    public static int getMax2(int a, int b){
        int c = a - b;
        int sa = sign(a);
        int sb = sign(b);
        int sc = sign(c);
        int difSab = sa ^ sb;
        int sameSab = flip(difSab);
        //返回A的条件：1、a b符号相同 && a - b = c >= 0(即为 sc == 1)
        //           2、a b符号不同 && a >= 0(即为 sa == 1)
        int returnA = difSab * sa + sameSab * sc;
        int returnB = flip(returnA);
        return a * returnA + b * returnB;
    }

    //判断一个32位正数是不是2的幂或者4的幂
    //2的幂:二进制只有一个1  --> 拿出最右边的1 和 原数据 相同
    //4的幂:二进制只有一个1 && n & 0x55555555(01010101010101010101010101010101) != 0
    public static boolean is2Power(int n){
        return (n & (n - 1)) == 0;
    }
    public static boolean is4Power(int n){
        return ((n & (n - 1)) == 0 && (n & 0x55555555) != 0);
    }

    //两个有符号32位整数a和b，实现a和b的加减乘除运算  ^（异或代表无进位相加）
    // a+b = a^b + a&b << 1（进位信息）  直至 a&b << 1 == 0 停止
    public static int add(int a, int b){ //要求 a + b 不溢出
        int sum = a;
        while (b != 0){
            sum = a ^ b; //无进位相加
            b = (a & b) << 1; //进位信息
            a = sum;
        }
        return sum;
    }
    //n的相反数
    public static int negNum(int n){
        return add(~n,1);
    }
    //减法：等同于a加上b的相反数
    public static int minus(int a, int b){
        return add(a, negNum(b));
    }
    //乘法
    public static int multi(int a, int b){
        int res = 0;
        while (b != 0){
            if ((b & 1) != 0){
                res = add(res,a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }
    public static boolean isNeg(int n){
        return n < 0;
    }
    //除法
    public static int div(int a, int b){
        int x = isNeg(a) ? negNum(a) : a;
        int y = isNeg(b) ? negNum(b) : b;
        int res = 0;
        for (int i = 31; i > -1 ; i = minus(i,1)) {
            if ((x >> i) >= y){
                res |= (1 << i);
                x = minus(x,y << i);
            }
        }
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }
    //除法
    public static int divide(int a, int b){
        if (b == 0){
            throw new RuntimeException("divisor is 0");
        }
        if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE){
            return 1;
        } else if (b == Integer.MIN_VALUE) {
            return 0;
        } else if (a == Integer.MIN_VALUE) {
            int res = div(add(a, 1), b);
            //a ÷ b = (a + 1) ÷ b   +   (a -  [(a + 1) ÷ b] × b) ÷ b
            return add(res, div(minus(a, multi(res, b)), b));
        } else {
            return div(a, b);
        }
    }

    public static void main(String[] args) {
        System.out.println(Integer.MIN_VALUE);
    }

}







































