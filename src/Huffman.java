import com.sun.xml.internal.fastinfoset.util.CharArray;
import org.w3c.dom.*;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by jean.antunes on 05/05/2017.
 */
public class Huffman {

    private static Map<Integer, No> sortByComparator(Map<Integer, No> unsortMap, final boolean order) {
        List<Entry<Integer, No>> list = new LinkedList<Entry<Integer, No>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Entry<Integer, No>>() {
            public int compare(Entry<Integer, No> o1, Entry<Integer, No> o2) {
                if (order){
                    return o1.getValue().getValue().compareTo(o2.getValue().getValue());
                }
                else {
                    return o2.getValue().getValue().compareTo(o1.getValue().getValue());
                }
            }
        });

        Map<Integer, No> sortedMap = new LinkedHashMap<Integer, No>();
        for (Entry<Integer, No> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private Map<Integer, No> countFrequencies(byte[] b) {
        Map<Integer, No> countChars = new HashMap<>();
        for(char caracter:new String(b).toCharArray()) {
            if(!countChars.containsKey((int) caracter)){
                countChars.put((int) caracter, new No((int) caracter, 1));
            }
            else {
                No s = countChars.get((int) caracter);
                countChars.put((int) caracter, new No((int) caracter, s.getValue()+1));
            }
        }
        return sortByComparator(countChars, true);
    }


    private No createTree(Map<Integer, No> list) {

        No parent = null;
        No parentAll;
        Integer count = 0;

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

            list = sortByComparator(list, true);

        }

        parentAll = new No(255+count,parent.getNoLeft().getValue() + parent.getNoRight().getValue());

        parentAll.setNoLeft(parent.getNoLeft());
        parentAll.setNoRight(parent.getNoRight());

        return parentAll;
    }

    public void prefixado(No no) {
        if(no != null){
            System.out.print(no.getValue()+ " ");
            System.out.println("\n '0' ");
            prefixado(no.getNoLeft());
            System.out.println("\n '1' ");
            prefixado(no.getNoRight());

        }
    }

    public static void main(String[] args) throws IOException {
        Huffman huf = new Huffman();

        FileInputStream file = new FileInputStream("Files/file.txt");
        BufferedInputStream buf = new BufferedInputStream(file);
        DataInputStream data = new DataInputStream(buf);
        byte[] b = new byte[file.available()];
        data.read(b);

        Map<Integer, No> a = huf.countFrequencies(b);

        No teste = huf.createTree(a);
        huf.prefixado(teste);
    }
}
