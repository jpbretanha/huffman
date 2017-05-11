import com.sun.xml.internal.fastinfoset.util.CharArray;
import org.w3c.dom.*;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


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
                countChars.put((int) caracter, new No((int) caracter, 1));
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
            prefixado(no.getNoLeft());

//           System.out.print(no.getKey()+ " ");
            prefixado(no.getNoRight());



        }
    }

    public Map<Integer,ArrayList> tableCodes(No root, ArrayList<Integer> pilha, Map<Integer, ArrayList> table) {
        if(root.getKey() > 255) {
            pilha.add(1);
            tableCodes(root.getNoLeft(), pilha, table);
            pilha.remove(pilha.size()-1);
            pilha.add(0);
            tableCodes(root.getNoRight(), pilha, table);
            pilha.remove(pilha.size()-1);
        }
        else {
            //System.out.println(root.getKey());
           //System.out.println("pilha completa: " + pilha.toString());
           ArrayList<Integer> aux = new ArrayList<>();
           /*add pilha na tabela*/
           for(int i=0;i<pilha.size();i++)
               aux.add(i, pilha.get(i));

           table.put(root.getKey(),aux);
        }
        return table;
    }

    public String readBits(String arquivo) {
        Path path = Paths.get(arquivo);
        byte[] data;
        try {
            data = Files.readAllBytes(path);
            BitSet set = BitSet.valueOf(data);

            String binaryString = "";
            for (int i = 0; i <= set.length(); i++) {
                if (set.get(i)) {
                    binaryString += "1";
                } else {
                    binaryString += "0";
                }
            }

            return binaryString;
        } catch (IOException ex) {
            //Logger.getLogger(IOobject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0";
    }

    public static void main(String[] args) throws IOException {
        Huffman huf = new Huffman();
        HuffmanTree arvore = new HuffmanTree();

        FileInputStream file = new FileInputStream("Files/file.txt");
        BufferedInputStream buf = new BufferedInputStream(file);
        DataInputStream data = new DataInputStream(buf);
        byte[] b = new byte[file.available()];
        data.read(b);
        String tes = huf.readBits("Files/file.txt");

        //System.out.println(tes.length());
        Map<Integer, No> a = huf.countFrequencies(b);
        No teste = arvore.createTree(a);

        Map<Integer, ArrayList> table = huf.tableCodes(teste, new ArrayList<>(), new HashMap<>());

        System.out.println(table.toString());

    }
}
