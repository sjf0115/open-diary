package com.sjf.open;

/**
 * 判断选项是否有效
 *
 * 单字符:? @ 字母
 * 多字符:只能包含字母
 *
 * Created by xiaosi on 17-6-29.
 */
public class OptionValidator {

    /**
     * 判断选项是否有效
     * @param opt
     * @throws IllegalArgumentException
     */
    public static void validateOption(final String opt) throws IllegalArgumentException {

        if (opt == null) {
            return;
        }

        if (opt.length() == 1) {
            final char ch = opt.charAt(0);
            if (!isValidOpt(ch)) {
                throw new IllegalArgumentException("Illegal option name '" + ch + "'");
            }
        }
        else {
            for (final char ch : opt.toCharArray()) {
                if (!isValidChar(ch)) {
                    throw new IllegalArgumentException(
                            "The option '" + opt + "' contains an illegal " + "character : '" + ch + "'");
                }
            }
        }
    }

    /**
     * 判断指定的字符是否是有效选项
     * @param c
     * @return
     */
    private static boolean isValidOpt(final char c) {
        return isValidChar(c) || c == '?' || c == '@';
    }

    /**
     * 判断指定的字符是否是有效字符
     * @param c
     * @return
     */
    private static boolean isValidChar(final char c) {
        return Character.isJavaIdentifierPart(c);
    }
}
