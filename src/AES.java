import java.io.DataInputStream;
import java.io.IOException;

public class AES {
    private static final int S[][] = {{9, 4, 10, 11}, {13, 1, 8, 5}, {6, 2, 0, 3}, {10, 14, 15, 7}};

    public static void main(String[] args) throws IOException {
        DataInputStream inp = new DataInputStream(System.in);
        System.out.println("Enter 12 bit key : ");;
        String key = inp.readLine();
        System.out.println("Enter 12 bit message : ");
        String msg = inp.readLine();
        String W0 = key.substring(0,8);
        String W1 = key.substring(8,16);
        System.out.println("W0 : "+W0);
        System.out.println("W1 : "+W1);
        String W2 = calculateWI(W0,"10000000",W1);
        System.out.println("W2 : "+W2);
        String W3 = xor(W1, W2,8);
        System.out.println("W3 : "+W3);
        String W4 = calculateWI(W2,"00110000",W3);
        System.out.println("W4 : "+W4);
        String W5 = xor(W3, W4, 8);
        System.out.println("W5 : "+W5);
        String K1 = W0.concat(W1);
        String K2 = W2.concat(W3);
        String K3 = W4.concat(W5);
        System.out.println("K1 : "+K1);
        System.out.println("K2 : "+K2);
        System.out.println("K3 : "+K3);
        String ark = xor(msg,K1,16);
        System.out.println("ARK : "+ark);
        String subnib = substituteNibble(ark);
        System.out.println("Substite Nibble : "+subnib);
        String sr = shiftrow(subnib);
        System.out.println("Shift row : "+sr    );
        
    }

    private static String shiftrow(String subnib) {
        StringBuilder sb = new StringBuilder(subnib);
        String temp = subnib.substring(4,8);
        sb.delete(4,8);
        sb.insert(4,subnib.substring(12,16));
        sb.delete(12,16);
        sb.insert(12,temp);

        return sb.toString();
    }

    private static String substituteNibble(String ark) {
        return substitute(ark.substring(0,8)).concat(substitute(ark.substring(8,16)));
    }

    private static String calculateWI(String w0,String temp, String w1) {
        return xor(w0,xor(temp,substitute(w1.substring(4,8).concat(w1.substring(0,4))),8),8);
    }

    private static String xor(String ep, String k1, int offset) {
        int EP = Integer.parseInt(ep, 2);
        int K1 = Integer.parseInt(k1, 2);
        int x_or = EP ^ K1;

        return concatZeros(Integer.toBinaryString(x_or), offset);
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

    private static String substitute(String xOR) {
        int r1 = Integer.parseInt(String.valueOf(xOR.charAt(0)) + xOR.charAt(1), 2);
        int c1 = Integer.parseInt(String.valueOf(xOR.charAt(2)) + xOR.charAt(3), 2);
        int r2 = Integer.parseInt(String.valueOf(xOR.charAt(4)) + xOR.charAt(5), 2);
        int c2 = Integer.parseInt(String.valueOf(xOR.charAt(6)) + xOR.charAt(7), 2);


        String answer1 = concatZeros(Integer.toBinaryString(S[r1][c1]), 4);
        String answer2 = concatZeros(Integer.toBinaryString(S[r2][c2]), 4);

        return answer1.concat(answer2);
    }
}
