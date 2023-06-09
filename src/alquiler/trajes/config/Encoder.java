package alquiler.trajes.config;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Logger;

/**
 * Transform encryption
 *
 * Transform encryption uses three keys that are mathematically related: one to encrypt plaintext to a recipient, a second to decrypt the ciphertext,
 * and a third to transform ciphertext encrypted to one recipient so it can be decrypted by a different recipient.
 * The first and second keys are the same pieces of a key pair that are used in asymmetric encryption.
 *
 * TRANSFORMATION >> key value public
 * RANDOM_NUMBER >> random number key public
 * inKey allow in methods encrypt & decrypt, this value is required in each method (recommended userm)
 *
 *
 */

public abstract class Encoder {

    private static final Logger LOGGER = Logger.getLogger(Encoder.class.getName());
    private static PBEParameterSpec pbeParamSpec;
    // This is an array for creating hex chars
    static final char[] HEX_TABLE = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static void setPbeParamSpec(byte[] salt )throws EncryptionException {
        int iterationCount = 65;
        try{
            pbeParamSpec = new PBEParameterSpec( salt, iterationCount );
        }catch( Exception e ){
            LOGGER.warning(e.getMessage());
            throw new EncryptionException(e.getMessage());
        }
    }

    private static SecretKey getSecretKey(String key ) throws Exception {

        PBEKeySpec pbeKeySpec = new PBEKeySpec( key.toCharArray() );
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance( EncryptionConstants.TRANSFORMATION );
        return keyFac.generateSecret( pbeKeySpec );
    }

    /**
     *
     * @param inKey >> value key used for the encrypt, value recommended user m
     * @param text >> text to encrypt
     * @return string encrypted
     * @throws EncryptionException
     */
    public static String encrypt( String inKey, String text )throws EncryptionException{

        try{
            setPbeParamSpec(EncryptionConstants.RANDOM_NUMBER);
            SecretKey secretKey = getSecretKey( inKey.toUpperCase() );
            Cipher encrypter = Cipher.getInstance( EncryptionConstants.TRANSFORMATION );
            encrypter.init( Cipher.ENCRYPT_MODE, secretKey, pbeParamSpec );
            return byteToHexString(encrypter.doFinal( text.getBytes() ));
        }catch( Exception e ) {
            LOGGER.warning(">>> Error when encrypted inKey: "+inKey.toUpperCase()+" - text: "+text);
            throw new EncryptionException(e.getMessage());
        }
    }

    /**
     *
     * @param inKey >> value key used for the encrypt, value recommended user m
     * @param data >> encrypted data to decrypt
     * @return string decrypted
     * @throws EncryptionException
     */

    public static String decrypt( String inKey, String data )throws EncryptionException {
        try {
            setPbeParamSpec(EncryptionConstants.RANDOM_NUMBER);
            byte[] raw = stringToHexArrayByte(data);
            SecretKey secretKey = getSecretKey( inKey.toUpperCase() );
            Cipher decrypter = Cipher.getInstance( EncryptionConstants.TRANSFORMATION );
            decrypter.init( Cipher.DECRYPT_MODE, secretKey, pbeParamSpec );
            return new String( decrypter.doFinal( raw ) );
        } catch( Exception e ) {
            LOGGER.warning(">>> Error when decrypted: "+e.getMessage()+" - inKey: " + inKey.toUpperCase() + " - data: " + data);
            throw new EncryptionException(e.getMessage());
        }
    }

    public static String hashAllFields(Map<String,String> fields, String secureSecret) {

        // create a list and sort it
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);

        // create a buffer for the md5 input and add the secure secret first
        StringBuffer buf = new StringBuffer();
        buf.append(secureSecret);

        // iterate through the list and add the remaining field values
        Iterator itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                buf.append(fieldValue);
            }
        }

        MessageDigest md5 = null;
        byte[] ba = null;

        // create the md5 hash and UTF-8 encode it
        try {
            md5 = MessageDigest.getInstance("MD5");
            ba = md5.digest(buf.toString().getBytes("UTF-8"));
        } catch (Exception e) {} // wont happen

        return hex(ba);
    }

    /**
     * Returns Hex output of byte array
     * @param input the input data to be converted to HEX.
     * @return the string in HEX format.
     */
    private static String hex(byte[] input) {
        // create a StringBuffer 2x the size of the hash array
        StringBuffer sb = new StringBuffer(input.length * 2);

        // retrieve the byte array data, convert it to hex
        // and add it to the StringBuffer
        for (int i = 0; i < input.length; i++) {
            sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
            sb.append(HEX_TABLE[input[i] & 0xf]);
        }
        return sb.toString();
    }

    private static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }

    private static byte[] stringToHexArrayByte(String hexString){

        byte[] val = new byte[hexString.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }

        return val;
    }
    
    public static void main(String[] args){
        System.out.println("holasd");
    }
}
