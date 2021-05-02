package IO;

import javax.lang.model.element.NestingKind;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MyDecompressorInputStream extends InputStream {
    InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException{
        byte[] list = in.readAllBytes();
        int index=0;
        while (list[index] != -2) {
            b[index] = list[index];
            index++;
        }
        b[index] = list[index];
        index++;
        int iterator = index , smallCounter =0;
        String binaryNum;
        while (index < list.length){
            binaryNum = String.format("%" + 7 + "s", Integer.toBinaryString(list[index])).replaceAll(" ", "0");
            StringBuilder rev = new StringBuilder();
            rev.append(binaryNum);
            rev.reverse();
            binaryNum = rev.toString();
            smallCounter=0;
            for (int i=0 ; i < binaryNum.length() ; i++){
                if (iterator >= b.length) break;
                b[iterator] = (byte)(binaryNum.charAt(i) - '0');
                iterator++;
            }
            index ++;
        }
        return 0;
    }
}