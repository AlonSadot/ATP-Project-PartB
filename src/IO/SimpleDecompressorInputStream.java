package IO;

import javax.lang.model.element.NestingKind;
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

    /**
     * @param b compressed byte array
     * @return
     * @throws IOException
     */
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
        int num , iterator = index;
        while (index < list.length){
            num = list[index] + 127;
            for (int k = 0;k<num;k++){
                b[iterator] = 0;
                iterator++;
            }
            index++;
            if (index >= list.length) break;
            num = list[index] + 127;
            for (int k = 0;k<num;k++){
                b[iterator] = 1;
                iterator++;
            }
            index++;
        }
        return 0;
    }
}