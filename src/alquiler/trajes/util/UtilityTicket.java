package alquiler.trajes.util;

public class UtilityTicket {
    
    private UtilityTicket () {
        throw new IllegalStateException("UtilityTicket");
    }
    
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
    
    /**
     * Justify text to length by line.
     * @param lengthByLine - Length by line.
     * @param originalString - Complete String.
     * @param lineBreak - Salto de linea /n.
     * @param breakLines - Limit lines.
     * @return builder to string.
     */
    public static String justify (            
            int lengthByLine, 
            String originalString, 
            String lineBreak, 
            int breakLines) {
        
        StringBuilder builder = new StringBuilder();
        int lengthOriginString = originalString.length();
        
        if (lengthOriginString > lengthByLine) {
                
            double division = (double) lengthOriginString / lengthByLine;
            double lines = Math.ceil(division);
                
                int beginLenght = 0;
                int discountLines = originalString.length();
                
                for (int i = 1; i < lines ; i++) {
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
                    
                    if (i >= breakLines){break;}
                }
            } else {
               builder.append(originalString)
                       .append(lineBreak);
            }
        
        return builder.toString();
    }
    
}
