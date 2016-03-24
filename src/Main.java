import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static private Map<Character, Integer> idx;
    static char[] alphabet;
    static int[] key;

    public static void main(String[] args) throws IOException {
        // calcIndex();
        //encryptText();
        //decryptText();
        indexForShifts();

    }

    public static double calcIndexOfCoincidence(String a, String b) {
        int cnt = 0;
        int n = Math.min(a.length(), b.length());
        for (int i = 0; i < n; i++) {
            cnt += (a.charAt(i) == b.charAt(i)) ? 1 : 0;
        }
        return (double) cnt / (double) n;
    }


    public static void indexForShifts() throws IOException {
        System.out.println("Текст:");
        String text = readFile(scanner.next());
        for (int i = 1; i <= 15; i++) {
            System.out.println(100*calcIndexOfCoincidence(text, shift(text, i)));
        }
    }

    public static String shift(String s, int l) {
        return s.substring(l) + s.substring(0, l);
    }

    private static void calcIndex() throws IOException {
        System.out.println("Первая строка");
        String fileA = scanner.next();
        System.out.println("Вторая строка");
        String fileB = scanner.next();
        String a = readFile(fileA);
        String b = readFile(fileB);
        double averageIndexOfCoincidence = calcAverageIndexOfCoincidence(a, b);
        System.out.println("Индекс : " + averageIndexOfCoincidence);
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(new File(path).toPath()));
    }

    public static double calcAverageIndexOfCoincidence(String a, String b) {
        char[][] s = new char[2][];
        s[0] = a.toCharArray();
        s[1] = b.toCharArray();
        int[][] cnt = new int[2][(1 << 16)];
        for (int i = 0; i < 2; i++) {
            for (char c : s[i]) {
                cnt[i][c]++;
            }
        }
        long result = 0;
        for (int i = 0; i < (1 << 16); i++) {
            result += cnt[0][i] * cnt[1][i];
        }
        return (double) result / ((double) a.length() * (double) b.length());
    }

    private static void encryptText() throws IOException {
        System.out.println("открытый текст:");
        String messageFile = scanner.next();
        String message = readFile(messageFile);
        System.out.println("алфавит:");
        String alphabetFile = scanner.next();
        alphabet = readFile(alphabetFile).toCharArray();
        System.out.println("ключ:");
        String keyFile = scanner.next();
        key = readKey(keyFile);
        idx = new HashMap<>();
        for (int i = 0; i < alphabet.length; i++) {
            idx.put(alphabet[i], i);
        }
        String cryptogram = encrypt(message);

        String cryptogramFile = "encrypt.txt";
        writeFile(cryptogramFile, cryptogram);
    }

    private static void decryptText() throws IOException {
        System.out.println("текст:");
        String messageFile = scanner.next();
        String message = readFile(messageFile);
        System.out.println("лфавит:");
        String alphabetFile = scanner.next();
        alphabet = readFile(alphabetFile).toCharArray();
        System.out.println("ключ:");
        String keyFile = scanner.next();
        key = readKey(keyFile);
        idx = new HashMap<>();
        for (int i = 0; i < alphabet.length; i++) {
            idx.put(alphabet[i], i);
        }
        String cryptogram = decrypt(message);

        String cryptogramFile = "decrypt.txt";
        writeFile(cryptogramFile, cryptogram);
    }

    static private void writeFile(String path, String s) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(s);
        writer.flush();
        writer.close();
    }

    static private String removeSymbols(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (idx.containsKey(s.charAt(i))) {
                result.append(s.charAt(i));
            }
        }
        return result.toString();
    }

    static public String encrypt(String message) {
        String copy = removeSymbols(message);
        char[] buf = copy.toCharArray();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = rotate(buf[i], key[i % key.length]);
        }
        return new String(buf);
    }

    public static String decrypt(String cryptogram) {
        String copy = removeSymbols(cryptogram);
        char[] buf = copy.toCharArray();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = rotate(buf[i], -key[i % key.length]);
        }
        return new String(buf);
    }


    static private char rotate(char c, int val) {
        return alphabet[((idx.get(c) + val) % alphabet.length + alphabet.length) % alphabet.length];
    }

    static private int[] readKey(String file) throws IOException {
        String keyString = readFile(file);
        StringTokenizer keyTokenizer = new StringTokenizer(keyString, " ");
        int key[] = new int[keyTokenizer.countTokens()];
        for (int i = 0; i < key.length; i++) {
            key[i] = Integer.parseInt(keyTokenizer.nextToken());
        }
        return key;
    }

}
