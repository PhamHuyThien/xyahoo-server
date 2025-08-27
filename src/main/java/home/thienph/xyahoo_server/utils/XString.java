package home.thienph.xyahoo_server.utils;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.regex.Pattern;

public final class XString {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHABET_UPPER = ALPHABET.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{};:,.<>?";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private XString() {
        // private constructor để không cho new
    }

    /**
     * Random chuỗi chữ thường
     */
    public static String randomLower(int length) {
        return randomFrom(ALPHABET, length);
    }

    /**
     * Random chuỗi chữ hoa
     */
    public static String randomUpper(int length) {
        return randomFrom(ALPHABET_UPPER, length);
    }

    /**
     * Random chuỗi số
     */
    public static String randomDigits(int length) {
        return randomFrom(DIGITS, length);
    }

    /**
     * Random chuỗi chữ + số
     */
    public static String randomAlphaNumeric(int length) {
        return randomFrom(ALPHABET + ALPHABET_UPPER + DIGITS, length);
    }

    /**
     * Random chuỗi có cả ký tự đặc biệt
     */
    public static String randomPassword(int length) {
        return randomFrom(ALPHABET + ALPHABET_UPPER + DIGITS + SPECIAL, length);
    }

    /**
     * Hàm core để build random
     */
    private static String randomFrom(String chars, int length) {
        if (length <= 0) return "";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String removeVietnameseDiacritics(String input) {
        if (input == null) return null;
        // Chuẩn hóa về NFD để tách ký tự + dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Xóa dấu (combining diacritical marks)
        String noDiacritics = DIACRITICS.matcher(normalized).replaceAll("");
        // Một số ký tự đặc biệt không phải dấu nhưng vẫn khác, ví dụ Đ/đ
        return noDiacritics.replace("Đ", "D").replace("đ", "d");
    }

    /**
     * Thay thế toàn bộ dấu cách thành dấu gạch dưới
     * Ví dụ: "xin chao viet nam" -> "xin_chao_viet_nam"
     */
    public static String replaceSpaceWithUnderscore(String input) {
        if (input == null) return null;
        return input.replace(" ", "_");
    }
}
