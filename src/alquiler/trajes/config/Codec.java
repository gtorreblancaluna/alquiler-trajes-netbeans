package alquiler.trajes.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import java.util.UUID;

public abstract class Codec {

    /**
     * @return an UUID String
     */
    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Encode a String to base64
     * @param value The plain String
     * @return The base64 encoded String
     */
    public static String encodeBASE64(String value) throws EncryptionException {
        try {
            return new String(Base64.encodeBase64(value.getBytes("utf-8")));
        } catch (UnsupportedEncodingException ex) {
            throw new EncryptionException(ex.getMessage());
        }
    }

    /**
     * Encode binary data to base64
     * @param value The binary data
     * @return The base64 encoded String
     */
    public static String encodeBASE64(byte[] value) {
        return new String(Base64.encodeBase64(value));
    }

    /**
     * Decode a base64 value
     * @param value The base64 encoded String
     * @return decoded binary data
     */
    public static byte[] decodeBASE64(String value) throws EncryptionException {
        try {
            return Base64.decodeBase64(value.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new EncryptionException(ex.getMessage());
        }
    }

    /**
     * Build an hexadecimal MD5 hash for a String
     * @param value The String to hash
     * @return An hexadecimal Hash
     */
    public static String hexMD5(String value) throws EncryptionException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes("utf-8"));
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new EncryptionException(ex.getMessage());
        }
    }

    /**
     * Build an hexadecimal SHA1 hash for a String
     * @param value The String to hash
     * @return An hexadecimal Hash
     */
    public static String hexSHA1(String value) throws EncryptionException {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new EncryptionException(ex.getMessage());
        }
    }

    /**
     * Write a byte array as hexadecimal String.
     */
    public static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }

    /**
     * Transform an hexadecimal String to a byte array.
     */
    public static byte[] hexStringToByte(String hexString) throws EncryptionException {
        try {
            return Hex.decodeHex(hexString.toCharArray());
        } catch (DecoderException e) {
            throw new EncryptionException(e.getMessage());
        }
    }

}
