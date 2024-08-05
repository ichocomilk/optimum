package site.ichocomilk.optimum.inventory.utils;

public final class IntegerUtils {

    public static int parsePositive(final String text, final int errorDefaultValue) {
        if (text == null) {
            return 1;
        } 
        int length = text.length();
        if (length == 1) {
            length = text.charAt(0) - '0';
            return (length > 9 || length < 0) ? errorDefaultValue : length;
        }
        int result = 0;
        for (int i = 0; i < length; i++) {
            final int value = text.charAt(i) - '0';
            if (value > 9 || value < 0) {
                return errorDefaultValue;
            }
            result = (result + value) * 10;
        } 
        return result / 10;
    }
}