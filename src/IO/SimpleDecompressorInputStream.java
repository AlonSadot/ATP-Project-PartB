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
        int index=0,index2=0,counter0=127,counter1=127,countRow=0,countCol=0;
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
        index++;

        int num;

        while (index<list.length){
            for (countRow = 0;countRow<row;countRow++){
                while(countCol<column){
                    num = list[index] + 127;
                    for (int i = 0;i<num;i++){
                        b[index] = 0;
                        index++;
                        countCol++;
                    }
                    index++;
                    num = list[index] + 127;
                    for (int i = 0;i<num;i++){
                        b[index] = 1;
                        index++;
                        countCol++;
                    }

                }
            }
        }
        return 0;
    }

}
