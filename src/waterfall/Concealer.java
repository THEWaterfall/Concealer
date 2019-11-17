package waterfall;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Concealer {
    private final static String startCode = "AWaterfall";
    private final static byte[] base64StartCode = Base64.getEncoder().encode(startCode.getBytes());

    private final static String separator = ";AW;";

    public void conceal(String filepath, String text) {
        File file = new File(filepath);

        try(FileOutputStream output = new FileOutputStream(file, true)) {
            byte[] base64EncodedText = Base64.getEncoder().encode(text.getBytes());

            if(isFileConcealed(file)) {
                output.write(separator.getBytes());
            } else {
                output.write(base64StartCode);
            }

            output.write(base64EncodedText);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String reveal(String filepath) {
        File file = new File(filepath);
        List<Byte> byteArray = new ArrayList<>();

        try(FileInputStream input = new FileInputStream(file)) {
            StringBuffer foundText = new StringBuffer();
            boolean foundConcealed = false;
            int content;
            while ((content = input.read()) != -1) {
                if(foundConcealed) {
                    byteArray.add((byte) content);
                } else {
                    foundText.append((char) content);
                }

                if(!foundConcealed && foundText.toString().contains(new String(base64StartCode))) {
                    foundConcealed = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decode(byteArray);
    }

    public void clean(String filepath) {
        File file = new File(filepath);

        if(isFileConcealed(file)) {
            cloneFile(file);
        }
    }

    private void cloneFile(File concealedFile) {
        File cloneFile = new File(concealedFile.getName() + "_clone");

        try(FileInputStream input = new FileInputStream(concealedFile);
            FileOutputStream output = new FileOutputStream(cloneFile)) {

            int offset = getConcealingOffset(concealedFile);
            byte[] bytes = new byte[(int) concealedFile.length()];
            input.read(bytes, 0, offset);
            output.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        concealedFile.delete();
        cloneFile.renameTo(new File(cloneFile.getName().replace("_clone", "")));
    }

    private int getConcealingOffset(File file) {
        int offset = 0;
        try(FileInputStream input = new FileInputStream(file)) {
            StringBuilder fileContent = new StringBuilder();

            int content;
            while ((content = input.read()) != -1) {
                fileContent.append((char) content);
                if(fileContent.toString().contains(new String(base64StartCode))) {
                    break;
                }

                offset++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return offset - base64StartCode.length;
    }

    private boolean isFileConcealed(File file) {
        byte[] bytes = new byte[(int) file.length()];

        try(FileInputStream input = new FileInputStream(file)) {
            input.read(bytes, 0, (int) file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(bytes).contains(new String(base64StartCode));
    }

    private String decode(List<Byte> byteArray) {
        String base64String = new String(convertToPrimitiveByteArray(byteArray));
        String[] splitBase64Strings = base64String.split(separator);

        StringBuilder encodedText = new StringBuilder();
        for(String base64Str: splitBase64Strings) {
            byte[] encodedBytes = Base64.getDecoder().decode(base64Str.getBytes());
            encodedText.append(new String(encodedBytes)).append(" " + separator + " ");
        }

        return encodedText.toString();
    }

    private byte[] convertToPrimitiveByteArray(List<Byte> byteArray) {
        byte[] bytes = new byte[byteArray.size()];
        for(int i = 0; i < byteArray.size(); i++) {
            bytes[i] = byteArray.get(i);
        }

        return bytes;
    }
}
