package com.flippingflop.fastmailer.util;

public class SequenceUtils {

    private static int sequence = 0;

    /**
     * Generates a string representation of a sequential number, padded with leading zeros to achieve a specified length.
     * The method keeps track of a sequence number, which is incremented by 1 each time the method is called, ensuring a unique value throughout the application's lifecycle.
     *
     * @param fixedLength The desired length of the output string. If the sequence number is shorter than this length, leading zeros are added.
     *                    If the sequence number is longer, it is truncated from the left to match the specified length.
     * @return A string of the specified length representing the current state of a sequentially incrementing number.
     *         The string is padded with leading zeros if necessary, or truncated to ensure it matches the fixed length.
     *
     * Example:
     *   - Calling getString(5) sequentially might return "00001", then "00002", and so on.
     *   - If the sequence number exceeds the fixed length, e.g., 100000 for fixedLength 5, it returns "00000".
     */
    public static String getString(int fixedLength) {
        StringBuilder returnValue = new StringBuilder(String.valueOf(sequence++));

        while (returnValue.length() < fixedLength) {
            returnValue.insert(0, "0");
        }

        while (fixedLength < returnValue.length()) {
            returnValue = new StringBuilder(returnValue.substring(1));
        }

        return returnValue.toString();
    }

}
