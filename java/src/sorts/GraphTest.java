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
    //kruska算法
    public static void K(Graph graph){
        StringBuilder builder = new StringBuilder("最小生成树顺序：");
        if (graph == null){
            builder.append("图为空");
            System.out.println(builder);
            return;
        }
        HashSet<Node> set = new HashSet<>();
        //图的所有边进行堆排序
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o1.weight;
            }
        });
        for (Edge cur : graph.edges){
            minHeap.offer(cur);
        }

    }

    //最小生成树
    //prim算法

    //Dijikstra算法 适用范围：没有累加和为负数的环
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

