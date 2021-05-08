package IO;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleCompressorOutputStream extends OutputStream {
    OutputStream out;

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write((byte)b);
    }

    /**
     * @param b byte array of a maze
     * @throws IOException
     * compressing a byte array by counting the number of continues ones and zeros
     */
    @Override
    public void write(byte[] b) throws IOException {
        int index=0;
        byte counter1=-127,counter0=-127;
        while (b[index] != -2) {
            write(b[index]);
            index++;
        }
        write(b[index]);
        index++;
        while (index < b.length) {
            while(b[index]==0) {
                if(counter0==127) {
                    break;
                }
                counter0++;
                index++;
                if (index >= b.length) break;
            }
            write(counter0);
            counter0=-127;
            if (index >= b.length) break;
            while(b[index]==1) {
                if (counter1 == 127) {
                    break;
                }
                counter1++;
                index++;
                if (index >= b.length) break;
            }
            write(counter1);
            counter1=-127;
        }
    }
}