package alquiler.trajes.util;

public abstract class UtilityTicket {
    
    public static String alignRight(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }
    
    public static String center(String text, int length){
        String out = String.format("%"+length+"s%s%"+length+"s", "",text,"");
        float mid = (out.length()/2);
        float start = mid - (length/2);
        float end = start + length; 
        return out.substring((int)start, (int)end);
    }
    
    public static void appendLargeStringToStringBuilderByLengthToPrinterTermica (
            StringBuilder builder,
            int lengthByLine, 
            String originalString, 
            String lineBreak, 
            int breakLines) {
        if (originalString.length() > lengthByLine) {
                int lines = originalString.length() / lengthByLine;
                int beginLenght = 0;
                int discountLines = originalString.length();
                
                for (int i = 0; i <= lines ; i++) {
                    try {
                        if (discountLines < lengthByLine) {
                            builder.append(originalString.substring(beginLenght, (beginLenght+discountLines)));
                        } else {
                            builder.append(originalString.substring(beginLenght, (beginLenght+lengthByLine)));
                        }
                    } catch (StringIndexOutOfBoundsException e) {                       
                        break;
                    }
                    beginLenght += lengthByLine;
                    discountLines -= lengthByLine;
                    builder.append(lineBreak);
                    if (i > breakLines){break;}
                }
            } else {
               builder.append(originalString);
            }
    }
    
}
