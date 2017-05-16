import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jean.antunes on 12/05/2017.
 */
public class Decompress {
    public Map<Integer, No> readTableFrequencies(String name) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(name));
        Map<Integer, No> tableFreq = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            Integer key = Integer.parseInt(line.substring(0,line.indexOf('=')));
            Integer value = Integer.parseInt(line.substring((line.indexOf('=')+1),line.length()));

            tableFreq.put(key,new No(key, value));

        }

        return tableFreq;
    }
    public void prefixado(No no) {
        if(no != null){
            System.out.print(no.getKey() + " ");
            prefixado(no.getNoLeft());
            prefixado(no.getNoRight());
        }
    }
    public String decode(String text) throws FileNotFoundException {
        Decompress dec = new Decompress();
        HuffmanTree tree = new HuffmanTree();
        Map<Integer, No> tableFreq = dec.readTableFrequencies("Files/tableFrequencies.txt");
        /*gera a arvore*/
        No root = tree.createTree(tableFreq);

        No current = root;

        StringBuilder data = new StringBuilder();
        for (char c: text.toCharArray()) {
            if(c == '1') {
                current = current.getNoLeft();
            }
            else {
                current = current.getNoRight();
            }
            if(current.getKey() < 255) {
                data.append((char) ((int) current.getKey()));
                current = root;
            }
        }
        return data.toString();
    }

    public static void main(String[] args) throws IOException {
        Huffman huf = new Huffman();
        Decompress dec = new Decompress();
        String compressFile = huf.readBits("Files/compress.bin");
        //System.out.println(compressFile);
        String result = dec.decode(compressFile);
        System.out.println("Palavra descomprimida: " + result);


    }
}
