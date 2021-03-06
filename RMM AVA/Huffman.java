import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

//Jean Antunes e João Pedro Bretanha

public class Huffman {

    static Map<Integer, No> sortByComparator(Map<Integer, No> unsortMap, final boolean order) {
        List<Entry<Integer, No>> list = new LinkedList<Entry<Integer, No>>(unsortMap.entrySet());

        /*ordena pela frequencia*/
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
        /*ordena pela chave*/
        Collections.sort(list, new Comparator<Entry<Integer, No>>(){
            public int compare(Entry<Integer, No> o1, Entry<Integer, No> o2) {
                if (order){
                    return o1.getValue().getKey().compareTo(o2.getValue().getKey());

                }
                else {
                    return o2.getValue().getKey().compareTo(o1.getValue().getKey());
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

    public Map<Integer,String> tableCodes(No root, ArrayList<Integer> pilha, Map<Integer, String> table) {
        if(root.getKey() > 255) {
            pilha.add(1);
            tableCodes(root.getNoLeft(), pilha, table);
            pilha.remove(pilha.size()-1);

            pilha.add(0);
            tableCodes(root.getNoRight(), pilha, table);

            pilha.remove(pilha.size()-1);
        }
        else {

           StringBuilder aux = new StringBuilder();
           /*add pilha na tabela*/
           for(int i=0; i < pilha.size(); i++)
                aux.append(pilha.get(i));

           table.put(root.getKey(),aux.toString());
        }
        return table;
    }

    public String encode(byte [] b) {
        Huffman huf = new Huffman();
        HuffmanTree tree = new HuffmanTree();
        Decompress d = new Decompress();
        Map<Integer, No> tableFrequencies = huf.countFrequencies(b);

        huf.saveTableFrequencies(tableFrequencies);
        No root = tree.createTree(tableFrequencies);

        Map<Integer, String> codeMap = huf.tableCodes(root, new ArrayList<>(), new HashMap<>());

        StringBuilder data = new StringBuilder();
        for(char caracter:new String(b).toCharArray()) {
            data.append(codeMap.get((int)caracter));
        }

        return data.toString();
    }

    public void saveTableFrequencies(Map<Integer, No> table) {
        Map<Integer, Integer> tableInt = new HashMap<>();
        for (Map.Entry<Integer, No> entry : table.entrySet()) {
            Integer key = entry.getKey();
            Integer freq = entry.getValue().getValue();
            tableInt.put(key,freq);
        }

        try {
            PrintWriter writer = new PrintWriter("Files/tableFrequencies.txt", "UTF-8");
            writer.println(tableInt.toString().replace("{","").replace("}","").replace(",", "").replace(" ", "\n"));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void geraSaida(String arquivo, String encoded) {

        BitSet bitSet = new BitSet(encoded.length());
        int bitcounter = 0;
        for (Character c : encoded.toCharArray()) {
            if (c.equals('1')) {
                bitSet.set(bitcounter);
            }
            bitcounter++;
        }

        byte[] toByteArray = bitSet.toByteArray();

        try (PrintStream ps = new PrintStream(new File(arquivo))) {
            ps.write(toByteArray, 0, toByteArray.length);
            ps.close();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(IOobject.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        if(args.length == 0){
            System.out.println("Para rodar a compressao, digite: java Huffman <endereco do arquivo .txt>");
        }else {
            Huffman huf = new Huffman();
            HuffmanTree tree = new HuffmanTree();

            FileInputStream file = new FileInputStream(args[0]);
            BufferedInputStream buf = new BufferedInputStream(file);
            DataInputStream data = new DataInputStream(buf);
            byte[] b = new byte[file.available()];
            data.read(b);

            String cod = huf.encode(b);
            Decompress d = new Decompress();

            String normal = huf.readBits(args[0]);

            huf.geraSaida("Files/normal.bin", normal);
            huf.geraSaida("Files/compress.bin", cod);

            System.out.println("Arquivo Comprimido e arquivo normal gerados!\n");
            System.out.println("Os arquivos se encontram na pasta Files.\nO arquivo compress.bin eh onde estah o codigo gerado comprimido. \nO normal.bin eh o arquivo sem compressao");
        }
    }
}
