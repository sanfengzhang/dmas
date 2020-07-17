package com.han.dams.sort;



/**
 * @author: Hanl
 * @date :2020/2/20
 * @desc:
 */
public class MyList<T> {

    Node head;

    int size=0;

    public MyList() {
        head = new Node();
    }

    public void add(T value) {
        Node first = head;
        Node addNode = new Node(value);
        while (null != first.next) {
            first = first.next;
        }
        first.next = addNode;
        size++;
    }

    public void forEach() {
        Node first = head;
        while (null != first.next) {
            System.out.println(first.next.value);
            first = first.next;
        }
    }

    public Node get(int index) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index=" + index);
        }
        Node first = head;
        while (index >= 0) {
            first = first.next;
            index--;
        }
        return first;
    }

    ///  1-->2-->3-->4-->5-->6
    ///1-->5-->6
    ///指定交换两个位置的节点,通过这个可以递归实现链表的反转或者部分节点反转
    public void reversTwo(int start, int end) throws IllegalArgumentException {
        if (end > start && start >= 0) {
            Node startNode = get(start);//获取起始节点的前驱节点
            Node endNode = get(end);//获取最后一个节点的后继节点
            Node startPreNode = get(start - 1);//获取起始节点的前驱节点
            Node endPreNode = get(end - 1);//获取最后一个节点的后继节点

            startPreNode.next = endNode;//让起始节点的前驱节点指向结束节点1-->5
            endPreNode.next = startNode;    //4-->2
            Node startNext = startNode.next;//这里先将起始节点的后继指针保存起来！！！
            startNode.next = endNode.next;
            endNode.next = startNext;
            reversTwo(start+1,end-1);
        }

    }

    ///  1-->2-->3-->4-->5-->6-->7-->8-->9
    ///删除链表倒数第N个节点，一趟扫描排序实现。思路可以有两个指针一个指针以1向前走，另一个指针先走到N的位置
    ///
    public void deleteBottomIndex(int index) {

    }

    ///合并两个有序链表
    public void mergeSortList(MyList other) {

    }

    public boolean isEmpty() {

        return size == 0 ? true : false;
    }

    public static void main(String[] args) {
        MyList<Integer> myList = new MyList<>();
        myList.add(1);
        myList.add(2);
        myList.add(3);
        myList.add(4);
        myList.add(5);
        myList.add(6);
        myList.add(7);
        myList.add(8);

        myList.reversTwo(1, 7);
      //  myList.reversTwo(2, 3);
        myList.forEach();
    }
}
