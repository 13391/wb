package com.jiavan.weibo.util;

/**
 * Created by Jiavan on 2017/4/23.
 * <p>
 * Filter text info.
 * Some tweets contain some non-utf8 characters, and need filter it.
 * Like emoji it used utf8mb4 spec, but our database used utf8, so we
 * need deal them.
 */
public class TextHandle {
    public static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static String filterEmoji(String source) {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            } else {

                buf.append("*");

            }
        }
        return buf.toString();
    }
}
