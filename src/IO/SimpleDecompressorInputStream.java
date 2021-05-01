package IO;

import java.io.IOException;
import java.io.InputStream;

public class SimpleDecompressorInputStream extends InputStream {
    InputStream in;

    public SimpleDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException{
        byte[] list = in.readAllBytes();
        int index=0,index2=0;
        String x = "";
        String y = "";
        while(list[index2] != -1){
            x += list[index2];
            index2++;
        }
        index2++;
        while(list[index2] != -1){
            y += list[index2];
            index2++;
        }
        int row = Integer.parseInt(x), column = Integer.parseInt(y);
        while (b[index] != -2) {
            b[index] = list[index];
            index++;
        }
        return 0;
    }

}
