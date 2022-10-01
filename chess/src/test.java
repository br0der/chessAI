import java.util.*;

public class test {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int iterations = scan.nextInt();
        scan.nextLine();
        for (int j = 0; j < iterations; j++) {
            String input = scan.nextLine();
            String str = "a";
            for (int i = 0; i < input.length()-(str.length()-1); i++) {
                if(input.substring(i, i+str.length()).equals(str)){
                    break;
                }
            }
            char[] chars = str.toCharArray();

//        for (int i = chars.length-1; i >= 0; i--) {
            int i = chars.length-1;
            while(true){
                if((int)(chars[i])-97==25){
                    chars[i]='a';
                    if(i==0){
                        str = 'a' + String.valueOf(chars);
                        break;
                    }
                }
                else{
                    chars[i] = (char) (((int) chars[i]) + 1);
                    str = String.valueOf(chars);
                    break;
                }
                i--;
            }

            System.out.println(str);
        }
    }
}
