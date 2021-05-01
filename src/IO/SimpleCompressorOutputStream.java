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

    @Override
    public void write(byte[] b) throws IOException {
        int index=0;
        byte counter1=-127,counter0=-127;
        while (b[index] != -2) {
            if (b[index] == -1){
                index++;
                continue;
            }
            write(b[index]);
            index++;
        }

        index++;
        while(index<b.length){ //start from 0's
            while(b[index] != -2){
                while(b[index]==0) {
                    if(counter0==127) {
                        index++;
                        break;
                    }
                    counter0++;
                    index++;
                }
                write(counter0);
                counter0=-127;

                while(b[index]==1) {
                    if (counter1 == 127) {
                        index++;
                        break;
                    }
                    counter1++;
                    index++;
                }
            }
            write(counter1);
            counter1=-127;
        }
    }
}
