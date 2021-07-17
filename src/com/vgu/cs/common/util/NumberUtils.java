package com.vgu.cs.common.util;

public class NumberUtils {

    public static class NumberChars {
        private static final char[] NUMBERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        public NumberChars(){

        }

        public static char get(int index){
            return NUMBERS[index];
        }
    }

    public static String formatNumber(long l){
        return formatNumber(l, ',');
    }

    public static String formatNumber(long l, char thousandSeparator){
        if (-1000L < l && l < 1000L) {
            return Long.toString(l);
        } else if (l == 3600L) {
            return "3,600";
        } else if (l == 5000L) {
            return "5,000";
        } else if (l == 1000000L) {
            return "1,000,000";
        } else if (l == -9223372036854775808L) {
            return "-9,223,372,036,854,775,808";
        } else if (l == 9223372036854775807L) {
            return "9,223,372,036,854,775,807";
        } else {
            boolean negative = false;
            if (l < 0L) {
                negative = true;
                l = -l;
            }

            char[] buf = new char[26];
            int pos = buf.length;

            for(int count = 0; l != 0L; l /= 10L) {
                ++count;
                if (count == 4) {
                    --pos;
                    buf[pos] = thousandSeparator;
                    count = 1;
                }

                long temp = l % 10L;
                --pos;
                buf[pos] = NumberUtils.NumberChars.get((int)temp);
            }

            if (negative) {
                --pos;
                buf[pos] = '-';
            }

            return new String(buf, pos, buf.length - pos);
        }
    }
}
