package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;

    /**
     * this class is a decorator to the outputstream class. It will take the instance of the outputstream object
     * and decorate it during run time
     * @param outputStream - object to decorate
     */
    public MyCompressorOutputStream(OutputStream outputStream) {
        out = outputStream;
    }


    @Override
    public void write(int b) throws IOException {
        // Use the output stream write function
        out.write(b);
    }

    /**
     * decorate: compress the data from the outputstream.
     * write the compressed data to the output object
     * @param maze - byte array to compress
     * @throws IOException - error writing to the file
     */
    @Override
    public void write(byte[] maze) throws IOException {
        int[] dims = getColumnsfromBytes(maze);
        maze= Compress(maze,dims[0],dims[1]);
        out.write(maze);
    }
    public void writeCompressedMaze(byte[] maze) throws  IOException{
        int[] dims = getColumnsfromBytes(maze);
        maze= Compress(maze,dims[0],dims[1]);
        ((ObjectOutputStream)out).writeObject(maze);
        out.flush();
    }
    /**
     * used to compress the data given by the byte array.
     * The idea is to take every 8 bytes (from the maze byte array) and for each row of the maze make a binary number from it.
     * i.e 1,1,1,1,1,1,1,1 will be -1 (signed) or 255 (unassigned).
     * We will do it for each row and if the columns number is bigger than 8 the function will make a binary for each
     * 8 bytes until the end of the row, e.g.: 1,1,1,1,1,1,1,1,1 (9 ones) will become -1,1
     * @param b - byte array to compress
     * @return - new compressed byte array
     * @throws IOException - IO device failure
     */
    public byte[] Compress(byte[] b,int cols,int index) throws IOException {

        // The variable that will make the 8 bit binary number
        // We will convert it to int with the Integer.parseInt method.
        String val = "";

        ByteArrayOutputStream res = new ByteArrayOutputStream();
        int counter = 0;
        // Start the index from the maze and skip the meta data (size and start,goal)
        int i = index;
        // Rows
        i = getNextIndex(b,i);
        // Start position
        i = getNextIndex(b,i);
        i = getNextIndex(b,i);
        // Goal position
        i = getNextIndex(b,i);
        i = getNextIndex(b,i);

        // Add the meta data first
        for (int j = 0; j < i; j++)
            res.write(b[j]);

        while (i<b.length){
            /* If the columns are bigger than 8 use a special function
             * else if, just parse the column and add 1 and 0 until you finish the row and move to the next.
             * The else is just to know when we finished parsing the row when its <= 8.*/
            if(cols > 8)
                i = biggerThanOneByte(b,res,cols,i);

            else if(counter < cols) {
                val+=String.valueOf((int)b[i]);
                counter++;
                i++;
            }

            else {
                res.write((byte) Integer.parseInt(val, 2));
                counter = 0;
                val = "";
            }
        }
        /* Use to take of the last row in the case the columns are smaller than 8.
         * If they are bigger than 8 the "biggerThanOneByte()" function will take
         * care of the last row. As a result val will be "" */
        if(!val.equals(""))
            res.write((byte) Integer.parseInt(val, 2));

        return res.toByteArray();
    }

    /**
     * This function will get the columns dimension by value and return it.
     * The data int array will conatin the columns value and the index to the next meta data (rows).
     * @param b - the maze byte array
     * @return int[] containing the columns value and the index to the next data to compress.
     */
    private int[] getColumnsfromBytes(byte[] b){
        int columns = 0;
        int data[] = new int[2];
        // Check the unassigned value

        /* if the dimensions are smaller than 255 rows and columns than the first 2 bytes
        will be the rows and columns number.
        Else the first 4 bytes and higher will be the dimensions.
        */
        // Get the cols
        int index = getNextIndex(b,0);
        for (int i = 0; i < index; i++) {
            columns +=  b[i] & 0xFF;
        }

        data[0] = columns;
        data[1] = index;
        return data;
    }
    /**
     * Use to get the index to the next meta data
     * @param index - Where to start the search for the next meta data
     * @return index of the next meta data (dim,start,goal,maze)
     */
    private int getNextIndex(byte[] b,int index)
    {
        int check = b[index] & 0xFF;
        if(check < 255)
            index++;
        else {
            while(check >= 255){
                index++;
                check = b[index] & 0xFF;;
            }
            index++;
        }
        return index;
    }

    /**
     * Use to take care of the case when the columns number is bigger than 8
     * @param in The byte array to compress
     * @param out The byte array after compression
     * @param cols the number of columns - Bigger than 8 int
     * @param index - the index to start the loop over the in array.
     *              The function will update the index to the last visited spot on the in array
     *              and sent it back to the caller function so we can know where we last visited
     *              in the array.
     * @return byte array with 1 more compress row
     */
    public static int biggerThanOneByte(byte[] in,ByteArrayOutputStream out, int cols, int index){
        /* We will loop over the array and every 8 steps we will stop and add the binary number that was
        formed and start over again from 0, continuing accumulating numbers until we reach another 8 bit number.
        If the cols will be smaller than 8 we will know that we don't need to loop in steps of 8 and just take
        the left over bits.
        * */
        int limit = 0;
        String val = "";
        while(cols > 8){
            // If the limit reach 8 we will stop and add the output of the binary to the array.
            if(limit == 8){
                // The Integer.parseInt(val,2) will take the binary string and cast it to an int.
                out.write((byte)Integer.parseInt(val,2));
                limit = 0;
                val = "";
                cols-=8;
            }
            else {
                val += String.valueOf((int) in[index]);
                index++;
                limit++;
            }
        }
        /*We check if there are left overs. For example if columns number is 9
         * There will be a left over of 1 byte after the while loop. */
        val = "";
        while(cols > 0){
            val+=String.valueOf((int)in[index]);
            cols--;
            index++;
        }
        out.write((byte)Integer.parseInt(val,2));
        return index;
    }
}
