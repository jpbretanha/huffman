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

    static Map<Integer, No> sortByComparator(Map<Integer, No> unsortMap, final boolean order) {
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
            }
            else {
                No s = countChars.get((int) caracter);
                countChars.put((int) caracter, new No((int) caracter, s.getValue()+1));
            }
        }
        return sortByComparator(countChars, true);
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
        HuffmanTree arvore = new HuffmanTree();

        FileInputStream file = new FileInputStream("Files/file.txt");
        BufferedInputStream buf = new BufferedInputStream(file);
        DataInputStream data = new DataInputStream(buf);
        byte[] b = new byte[file.available()];
        data.read(b);

        Map<Integer, No> a = huf.countFrequencies(b);

        No teste = arvore.createTree(a);
        huf.prefixado(teste);
    }
}
