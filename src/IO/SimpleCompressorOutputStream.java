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
//        while (b[index] != -2) {
//            if (b[index] == -1){
//                index++;
//                continue;
//            }
//            write(b[index]);
//            index++;
//        }

        int index2 = 0;
        String x = "";
        String y = "";
        while(b[index2] != -1){
            x += b[index2];
            index2++;
        }
        index2++;
        while(b[index2] != -1){
            y += b[index2];
            index2++;
        }
        int row = Integer.parseInt(x), column = Integer.parseInt(y);

        while (b[index] != -2) {
            write(b[index]);
            index++;
        }
        write(b[index]);
        index++;
        for (int i=0; i< row; i++){
            //for (int j=0 ; j < column ; j++){
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
            //}
            write(counter1);
            counter1=-127;
        }
    }
}
