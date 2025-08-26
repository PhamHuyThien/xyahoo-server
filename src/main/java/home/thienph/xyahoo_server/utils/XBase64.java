package home.thienph.xyahoo_server.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class XBase64 {

    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static String encodeBytes(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public static String decodeWithReverse(String input) {
        String reversed = new StringBuilder(input).reverse().toString();
        return decode(reversed);
    }

    public static String encodeWithReverse(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        String encoded = new String(encodedBytes, StandardCharsets.UTF_8);
        return new StringBuilder(encoded).reverse().toString();
    }

}
