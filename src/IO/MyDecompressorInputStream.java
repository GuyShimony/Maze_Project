package IO;

import java.io.*;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    /**
     * this class is a decorator to the inputstream class. It will take the instance of the inputstream object
     * and decorate it during run time
     * @param in - object to decorate
     */
    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        // Use the input stream read function
        return in.read();
    }

    /**
     * decorate: decompress the data from the input stream.
     * read the compressed data from the input object and write back to the byte array
     * @param b - byte array to decompress
     * @throws IOException - error writing to the file
     */
    @Override
    public int read(byte[] b) throws IOException {

        // in order to fix bug of array not updating out of the scope
        byte[] temp;
        temp = inflate();
        int size_maze = temp.length;
        for (int i=0;i<size_maze;i++){
            b[i] = temp[i];
        }
        return size_maze;
    }

    /**
     * used to decompress the data given by the byte array.
     * Steps:
     * 1) Get the number of columns from the compressed file (the first byte if the number is 255 or more
     * if the number is bigger than 255.
     * 2) Create an array for the new compress array and read the data from the file using the input stream.
     * 3) Create a new buffer and for each byte of the compress file get the binary string value of it.
     * each char of the binary string is a byte in the original maze.

     * @return - new decompressed byte array
     * @throws IOException - IO device failure
     */
    public byte[] inflate() throws IOException {
        byte[] getBytes = {};
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        getBytes = new byte[in.available()];
        in.read(getBytes);

        /* First need to copy all the metadata to the new array.
         * Second, get from the meta data the maze dimension.
         * Third start looping over the compressed maze and decompress it.*/
        int current_index = getMetaDataFromFile(getBytes,res);
        int cols = getNextIndexAndValue(getBytes,0)[0];
        int[] index = new int[1];
        // Loop over the data and get the maze matrix
        for (int i = current_index; i < getBytes.length; i++) {
            // This method is used to get the 8 bit binary string representing of the number.
            /* There are 3 possible states:
             * 1. The number of cols is < 8 -> take the first n bits of the byte.
             * 2. The number of cols is 8 -> the for loop will handle it.
             * 3. The number of cols is bigger than 8 -> the seconds if will handle it by combining bytes together
             *   from the file to make the n (n bigger than 8) bit number.
             *   The end of */
            String binary_string = String.format("%8s", Integer.toBinaryString(getBytes[i] & 0xFF)).replace(' ', '0');
            if (cols < 8) {
                binary_string = binary_string.substring(binary_string.length() - cols);
            }
            else if(cols > 8){
                binary_string = getBinaryStringOfMoreThanByte(getBytes,i,cols,index);
                /* After we looped over the array to calculate
                 the number we need, we need to update the index as well*/
                i = index[0];
            }

            for (int j = 0; j < binary_string.length(); j++) {
                /* For each char of the binary string we cast it to a byte and add it to the buffer*/
                String bit_in_string = String.valueOf(binary_string.charAt(j));
                res.write(Integer.parseInt(bit_in_string));
            }
        }
        return res.toByteArray();
    }

    /**
     * We will loop over the columns and convert each number to 8 bit binary string.
     * We will use this function if the columns number is bigger than 8 so we can assume that a row for an 9 columns
     * maze will look like 255,1.
     * Convert the 255 and 1 to a string:  111111111
     * @param getBytes - The compressed byte array of the maze
     * @param i - the index pointing to the current position in the compressed file we want to start from.
     * @param columns - the number of columns in the maze
     * @param index - the last index in the original compressed array we checked.
     * @return BinaryString of type 1010101...
     */
    private String getBinaryStringOfMoreThanByte(byte[] getBytes, int i, int columns,int[] index) {
        String long_bit_number = "";
        String remain = "";
        int temp = columns;

        while(temp > 8){
            long_bit_number += String.format("%8s", Integer.toBinaryString(getBytes[i] & 0xFF)).replace(' ', '0');
            i++;
            temp-=8;
        }
        /* Get the remaining from the original number by getting the substring of the 8 bit binary
         * (We can only get a 8 bit number each time even if its smaller) by using the substring method.
         * We will start the substring from 8 - (columns % 8). For example:
         * If columns is 10, than 8 - (10 % 8) = 6 and we indeed need only the last two bits of the 8 bit number.
         * special case is when columns is a multiply of 8 than the formula will not provide 0. */
        remain=String.format("%8s", Integer.toBinaryString(getBytes[i] & 0xFF)).replace(' ', '0');
        int substring_index = 0;
        if(columns % 8 != 0)
            substring_index = 8 - (columns % 8);

        remain = remain.substring(substring_index);
        long_bit_number += remain;

        index[0] = i;
        return long_bit_number;
    }


    /**
     * Use to decompress al the meta data of the maze: columns numbers, rows numbers, start point, gaol point
     * (In that order).
     * The function will loop over the compress byte array and will get each data mention above and put it in the
     * new Array.
     * @param getBytes - the data from the compress file
     * @param b - the new array to copy the data to
     * @return the index which the maze matrix will start in the compressed file
     */
    private int getMetaDataFromFile(byte[] getBytes, ByteArrayOutputStream b) {
        int current_index = 0;
        int last_index = 0;
        // Get the columns
        current_index = getNextIndexAndValue(getBytes,0)[1];
        copyData(b,getBytes,0,current_index);

        // Get the rows
        last_index = current_index;
        current_index = getNextIndexAndValue(getBytes,last_index)[1];
        copyData(b,getBytes,last_index,current_index);

        // Get start Position
        last_index = current_index;
        current_index = getNextIndexAndValue(getBytes,last_index)[1];
        copyData(b,getBytes,last_index,current_index);
        last_index = current_index;
        current_index = getNextIndexAndValue(getBytes,last_index)[1];
        copyData(b,getBytes,last_index,current_index);

        // Get the goal position
        last_index = current_index;
        current_index = getNextIndexAndValue(getBytes,last_index)[1];
        copyData(b,getBytes,last_index,current_index);
        last_index = current_index;
        current_index = getNextIndexAndValue(getBytes,last_index)[1];
        copyData(b,getBytes,last_index,current_index);

        return current_index;
    }

    /**
     * This function will get an index to start from and a byte array of bytes.
     * It will loop over the array starting from the index looking for a number smaller than 255 / -1 ( A set byte).
     * On the way to finding that number each set byte (all 1's) will be added to the total value.
     * Finally when that number is found it will add it to the total found until then and return the value + the index
     * of the next number to search for.
     * It can be considered as an Iterator over the special byte array
     * @param b - The array with data
     * @param index - the index to start the search from
     * @return - the index of the start of the next number
     */
    private int[] getNextIndexAndValue(byte[] b,int index)
    {
        int[] result = new int[2];
        int value = 0;
        int check = b[index] & 0xFF;
        if(check < 255) {
            value = check;
            index++;
        }
        else {
            while(check >= 255){
                index++;
                check = b[index] & 0xFF;;
                value += 255;
            }
            index++;
            value += check;
        }
        result[0] = value;
        result[1] = index;
        return result;
    }

    private void copyData(ByteArrayOutputStream to, byte[] from, int start, int finish){
        for (int i = start; i < finish; i++)
            to.write(from[i]);
    }

}
