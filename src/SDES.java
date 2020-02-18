import java.io.DataInputStream;
import java.io.IOException;

public class SDES {
    private static final int P10[] = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int P8[] = {6, 3, 7, 4, 8, 5, 10, 9};
    private static final int EP[] = {4, 1, 2, 3, 2, 3, 4, 1};
    private static final int S0[][] = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 3, 2}};
    private static final int S1[][] = {{0, 1, 2, 3}, {2, 0, 1, 3}, {3, 0, 1, 0}, {2, 1, 0, 3}};
    private static final int P4[] = {2, 4, 3, 1};
    private static final int IP[] = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int IPI[] = {4, 1, 3, 5, 7, 2, 8, 6};

    public static void main(String[] args) throws IOException {
        DataInputStream inp = new DataInputStream(System.in);
        System.out.println("Enter 10 bit key : ");
        String key = inp.readLine();
        System.out.println("Enter 8 bit message : ");
        String msg = inp.readLine();

        String p10 = applyPermutation(P10, key);
        System.out.println("P10 : " + p10);
        String shift = leftshift(p10);
        System.out.println("LS-1 : " + shift);
        String K1 = applyPermutation(P8, shift);
        System.out.println("K1 : " + K1);
        for (int i = 0; i < 2; i++)
            shift = leftshift(shift);
        System.out.println("LS-2 : " + shift);
        String K2 = applyPermutation(P8, shift);

        String cipher = encrypt(key, msg, K1, K2);
        System.out.println("Cipher Text : " + cipher);

        String originalmsg = decrypt(cipher, key, K1, K2);
        System.out.println("Original message was : " + originalmsg);
    }

    private static String encrypt(String key, String msg, String K1, String K2) {
        System.out.println("K2 : " + K2);
        String ip = applyPermutation(IP, msg);
        System.out.println("IP : " + ip);
        String swap = ip.substring(4, 8).concat(Functionk(K1, ip));
        String b = Functionk(K2, swap).concat(swap.substring(4, 8));
        return applyPermutation(IPI, b);
    }

    private static String Functionk(String key, String ip) {
        String ep = applyPermutation(EP, ip.substring(4, 8));
        System.out.println("E/P : " + ep);
        String xOR = xor(ep, key, 8);
        System.out.println("XOR : " + xOR);
        String sub = substitute(S0, S1, xOR);
        System.out.println("SUB : " + sub);
        String p4 = applyPermutation(P4, sub);
        System.out.println("P4 : " + p4);
        String answer = xor(p4, ip.substring(0, 4), 4);
        System.out.println("Fk : " + answer);
        return answer;
    }

    private static String substitute(int[][] s0, int[][] s1, String xOR) {
        int r1 = Integer.parseInt(String.valueOf(xOR.charAt(0)) + xOR.charAt(3), 2);
        int c1 = Integer.parseInt(String.valueOf(xOR.charAt(1)) + xOR.charAt(2), 2);
        int r2 = Integer.parseInt(String.valueOf(xOR.charAt(4)) + xOR.charAt(7), 2);
        int c2 = Integer.parseInt(String.valueOf(xOR.charAt(5)) + xOR.charAt(6), 2);


        String answer1 = concatZeros(Integer.toBinaryString(s0[r1][c1]), 2);
        String answer2 = concatZeros(Integer.toBinaryString(s1[r2][c2]), 2);

        return answer1.concat(answer2);
    }

    public static String concatZeros(String initialString, int offset) {
        if (initialString.length() < offset) {
            StringBuilder sb = new StringBuilder(initialString);
            for (int i = 0; i < (offset - initialString.length()); i++) {
                sb.insert(0, '0');
            }
            return sb.toString();
        }
        return initialString;
    }

    private static String xor(String ep, String k1, int offset) {
        int EP = Integer.parseInt(ep, 2);
        int K1 = Integer.parseInt(k1, 2);
        int x_or = EP ^ K1;

        return concatZeros(Integer.toBinaryString(x_or), offset);
    }

    public static String applyPermutation(int[] permuation, String key) {
        StringBuilder sb = new StringBuilder();
        for (int index : permuation) {
            sb.append(key.charAt(index - 1));
        }
        return sb.toString();
    }

    public static String leftshift(String p10) {
        StringBuilder sb = new StringBuilder(p10);
        char temp = sb.charAt(0);
        sb.deleteCharAt(0);
        sb.insert(4, temp);

        temp = sb.charAt(5);
        sb.deleteCharAt(5);
        sb.insert(9, temp);
        return sb.toString();
    }

    private static String decrypt(String cipher, String key, String K1, String K2) {
        String ip = applyPermutation(IP, cipher);
        String a = ip.substring(4,8).concat(Functionk(K2, ip));
        String b = Functionk(K1, a).concat(a.substring(4,8));
        String msg = applyPermutation(IPI, b);
        return msg;
    }
}
