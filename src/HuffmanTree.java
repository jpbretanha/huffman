/**
 * Created by João Pedro Bretanha on 09/05/2017.
 */


import java.util.*;

public class HuffmanTree {


     static No createTree(Map<Integer, No> list) {

        No parent = null;
        No parentAll = null;
        Integer count = 1;
        Huffman huffman = new Huffman();

        while (list.size() > 1) {
            /*pega os 2 caracters com menor frequencia da lista*/
            Integer k1 =  (Integer) list.keySet().toArray()[0];
            Integer k2 =  (Integer) list.keySet().toArray()[1];

            No node1 = list.get(k1);
            No node2 = list.get(k2);

            list.remove(k1);
            list.remove(k2);

            parent = new No(255+count, node1.getValue() + node2.getValue());

            parent.setNoLeft(node1);
            parent.setNoRight(node2);

            list.put(255+count,parent);

            count++;


            list = huffman.sortByComparator(list, true);

        }
         //parentAll = new No(255+count,parent.getNoLeft().getValue() + parent.getNoRight().getValue());

         // parentAll.setNoLeft(parent.getNoLeft());
         //parentAll.setNoRight(parent.getNoRight());

         

        return parent;
    }



}
