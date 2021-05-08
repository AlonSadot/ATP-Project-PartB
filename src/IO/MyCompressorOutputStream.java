package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.Math.*;
public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write((byte)b);
    }

    /**
     * @param b byte array of a maze
     * @throws IOException
     * compresses a byte array by converting binary numbers to decimals
     */
    @Override
    public void write(byte[] b) throws IOException {
        int index = 0;
        while (b[index] != -2) {
            write(b[index]);
            index++;
        }
        write(b[index]);
        index++;
        int num = 0;

        while (index < b.length) {
            for (int i = 0; i < 7 ; i++){
                num += b[index] * Math.pow(2, i);
                index++;
                if (index >= b.length) break;
            }
            write(num);
            num=0;
        }
    }
}