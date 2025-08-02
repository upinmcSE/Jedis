package init.upinmcSE.components;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class RespSerializer {

    private int getParts(char[] dataArray, int i, String[] subArray) {
        int j=0;
        while (i < dataArray.length && j < subArray.length) {
            if(dataArray[i] == '$'){
                // Bulk String
                // $<length>\r\n\<data>\r\n
                i++;
                String partLength = "";
                while (i < dataArray.length && Character.isDigit(dataArray[i])) {
                    partLength += dataArray[i];
                    i++;
                }
                i+=2;
                String part = "";
                for (int k = 0; k < Integer.parseInt(partLength); k++) {
                    part += dataArray[i];
                }
                i+=2;
                subArray[j++] = part;
            }
        }
        return i;

    }

    public List<String[]> deseralize(byte[] command){
//        [
//                ["set", "key", "value"],
//                ["set", "key", "value"]
//        ]
        String data = new String(command, StandardCharsets.UTF_8);
        char[] dataArray = data.toCharArray();

        List<String[]> result = new ArrayList<>();
        int i = 0;

        while(i < dataArray.length){
            char currentChar = dataArray[i];
            if(currentChar == '*'){
                //"*42\r\n#3set\r\n"
                // array
                String arrayLen = "";
                i++;
                while(i < dataArray.length && Character.isDigit(dataArray[i])){
                    arrayLen += dataArray[i];
                }
                i+=2;
                if(dataArray[i] == '*'){
                    // *2
                    // *3\r\n#3set\r\n#3key\r\n#5value
                    // *3\r\n#3set\r\n#3key\r\n#5value
                    for(int t=0; t< Integer.parseInt(arrayLen); t++){
                        String nestedLen = "";
                        i++;
                        while (i < dataArray.length && Character.isDigit(dataArray[i])){
                            nestedLen += dataArray[i];
                        }
                        i+=2;
                        String[] subArray = new String[Integer.parseInt(nestedLen)];
                        i = getParts(dataArray, i, subArray);
                        result.add(subArray);
                    }
                }else{
                    // *3\r\n#3set\r\n#3key\r\n#5value
                    String[] subArray = new String[Integer.parseInt(arrayLen)];
                    i = getParts(dataArray, i, subArray);
                    result.add(subArray);
                }
            }
        }

        return result;
    }
}
