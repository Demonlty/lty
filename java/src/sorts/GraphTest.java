package sorts;

import java.util.*;
import java.util.LinkedList;

public class GraphTest {

    static class Graph{
        public HashMap<Integer, Node> nodes; //key value --》 <点的编号，点>
        public HashSet<Edge> edges;

        public Graph() {
            nodes = new HashMap<>();
            edges = new HashSet<>();
        }
    }

    static class Node{
        public int value;
        public int in; //入度
        public int out; //出度
        public ArrayList<Node> nexts;
        public ArrayList<Edge> edges;

        public Node(int value) {
            this.value = value;
            in = 0;
            out = 0;
            nexts = new ArrayList<>();
            edges = new ArrayList<>();
        }
    }

    static class Edge{
        public int weight;
        public Node from;
        public Node to;

        public Edge(int weight, Node from, Node to) {
            this.weight = weight;
            this.from = from;
            this.to = to;
        }
    }

    //宽度优先遍历 BFS
    public static void breadthFirstSearch(Node node){
        //队列 + Set检查
        if (node == null){
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        HashSet<Node> set = new HashSet<>();
        queue.offer(node);
        set.add(node);
//        System.out.print(node.value + " ");
        StringBuilder path = new StringBuilder("宽度优先遍历：" + node.value);
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            for (Node curNode : cur.nexts){
//                if (!set.contains(curNode)){
//                    set.add(curNode);
//                    System.out.print(curNode.value + " ");
//                    queue.offer(curNode);
//                }
                if (set.add(curNode)){
                    path.append(" -> ").append(curNode.value);
                    queue.offer(curNode);
                }
            }
        }
        System.out.println(path);
    }

    //深度优先遍历 Depth-First Search DFS
    public static void DFS(Node start){
        StringBuilder builder = new StringBuilder("深度优先遍历：");
        if (start == null){
            builder.append("图为空");
            System.out.println(builder);
            return;
        }
        Stack<Node> stack = new Stack<>();
        HashSet<Node> set = new HashSet<>();
        stack.push(start);
        set.add(start);
        builder.append(start.value);
        while (!stack.isEmpty()){
            Node pop = stack.pop();
            for (Node cur : pop.nexts){
                if (set.add(cur)){
                    builder.append(" -> ").append(cur.value);
                    stack.push(pop);
                    stack.push(cur);
                    break;
                }
            }
        }
        System.out.println(builder);
    }

    //拓扑排序算法（Topological sorting algorithm） 适用于 无环有向图
    //通过寻找入度为0来实现
    public static void TSA(Graph graph){
        StringBuilder builder = new StringBuilder("拓扑排序：");
        if (graph == null){
            builder.append("图为空");
            System.out.println(builder);
            return;
        }
        //记录节点剩余入度的map
        HashMap<Node,Integer> map = new HashMap();
        Queue<Node> queue = new LinkedList<>();

        for (Node node : graph.nodes.values()){
            if (node.in == 0){
                queue.offer(node);
            }
            map.put(node,node.in);
        }
        int count = 0;
        while (!queue.isEmpty()){
            Node poll = queue.poll();
            if (count > 1){
                builder.append(" -> ");
            }
            builder.append(poll.value);
            for (Node cur : poll.nexts){
                Integer i = map.get(cur) - 1;
                map.put(cur,i);
                if (i == 0){
                    queue.offer(cur);
                }
            }
        }
        if (graph.nodes.size() != count){
            builder = new StringBuilder("拓扑排序：失败（图中存在环）");
        }
        System.out.println(builder);
    }

    //最小生成树
    //Kruskal算法
    //1、将图中所有边按权重从小到大排序。
    //2、依次取出每条边，若该边连接的两个顶点当前不属于同一个连通分量（即加入后不会形成环）（并查集），则选择这条边加入最小生成树。
    //3、重复直到选够 V-1 条边（V 为顶点数）。
    public static void K(Graph graph){
        StringBuilder builder = new StringBuilder("最小生成树顺序：");
        if (graph == null){
            builder.append("图为空");
            System.out.println(builder);
            return;
        }
        //图的所有边进行堆排序
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        });
        for (Edge cur : graph.edges){
            minHeap.offer(cur);
        }
        List<Node> nodeList = new ArrayList<>();
        for (Integer node : graph.nodes.keySet()){
            nodeList.add(graph.nodes.get(node));
        }
        HashTest.UnionFindSet unionFindSet = new HashTest.UnionFindSet(nodeList);

        List<Edge> res = new ArrayList<>();
        while (!minHeap.isEmpty()){
            Edge curEdge = minHeap.poll();
            boolean same = unionFindSet.isSameSet(curEdge.from, curEdge.to);
            if (!same){
                unionFindSet.union(curEdge.from, curEdge.to);
                res.add(curEdge);
                builder.append(curEdge.from + "-->");
                if (res.size() == nodeList.size() - 1){ //节点的总数 == n ，已得到 n-1 条边，生成树完成
                    builder.append(curEdge.to);
                    break;
                }
            }
        }
        if (res.size() != nodeList.size() - 1){
            System.out.println(new StringBuilder("最小生成树顺序：原图不连通，不存在最小生成树"));
            return;
        }
        System.out.println(builder);
    }

    //最小生成树
    //prim算法
    //任选一个起始顶点（例如 0），将其加入“已访问集合”。
    //维护一个最小堆（优先队列），存储所有从已访问集合指向未访问顶点的边，按权重排序。
    //每次从堆中取出权重最小的边 (u, v, w)，如果 v 尚未被访问，则选择该边加入 MST，并标记 v 为已访问。
    //将新访问的顶点 v 的所有邻接边中，指向未访问顶点的边加入堆。
    //重复直到所有顶点都被访问，或堆为空（图不连通）。
    public static List<Node> prim(Graph graph){
        if (graph == null){
            return null;
        }
        //图的所有边进行堆排序
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        });
        List<Node> res = new ArrayList<>();
        //由点找边
        Node node = graph.nodes.get(0);
        res.add(node);
        for (Edge cur : node.edges){
            minHeap.offer(cur);
        }
        int n = graph.nodes.size();
        boolean[] visited = new boolean[n];

        while (!minHeap.isEmpty()){
            //有最小的边找新的点
            Edge curEdge = minHeap.poll();
            if (!visited[curEdge.to.value]){
                res.add(curEdge.to);
                visited[curEdge.to.value] = true;
                for (Edge cur : curEdge.to.edges){
                    if (!visited[cur.to.value]){
                        minHeap.offer(cur);
                    }
                }
            }
        }
        if (res.size() != n){
            return null;
        }
        return res;
    }


    //Dijkstra算法 适用范围：没有累加和为负数的环
    public static HashMap<Node,Integer> Dijikstra(Node start){
        HashMap<Node,Integer> distanceMap = new HashMap<>();
        distanceMap.put(start,0);
        HashSet<Node> selectedNodes = new HashSet<>();
        Node minNode = getMinNode(distanceMap, selectedNodes);
        while (minNode != null){
            Integer distance = distanceMap.get(minNode);
            for (Edge edge : minNode.edges){
                Node toNode = edge.to;
                if (!distanceMap.containsKey(toNode)){
                    distanceMap.put(toNode,edge.weight + distance);
                }else {
                    distanceMap.put(toNode,Math.min(distanceMap.get(toNode),distance+edge.weight));
                }
            }
            selectedNodes.add(minNode);
            minNode = getMinNode(distanceMap, selectedNodes);
        }
        return distanceMap;
    }
    public static Node getMinNode(HashMap<Node,Integer> distanceMap, HashSet<Node> selectedNodes){
        Node minNode = null;
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distanceMap.entrySet()){
            Node node = entry.getKey();
            Integer distance = entry.getValue();
            if (distance < minDistance && !selectedNodes.contains(node)){
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        node1.nexts.add(node2);
        node1.nexts.add(node3);
        node1.nexts.add(node4);
        node2.nexts.add(node1);
        node2.nexts.add(node3);
        node2.nexts.add(node5);
        node3.nexts.add(node1);
        node3.nexts.add(node4);
//        node3.nexts.add(node5);
        node4.nexts.add(node1);
//        node4.nexts.add(node5);
        node5.nexts.add(node2);
        node5.nexts.add(node3);
        node5.nexts.add(node4);
        breadthFirstSearch(node1);
        DFS(node1);
//        TSA(node1);
    }
}

