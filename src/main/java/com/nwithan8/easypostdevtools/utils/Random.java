package com.nwithan8.easypostdevtools.utils;

import com.nwithan8.easypostdevtools.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Random {

    private static final java.util.Random random = new java.util.Random();

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static int getRandomIntInRange(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static int getRandomInt() {
        return random.nextInt();
    }

    public static double getRandomDoubleInRange(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    public static double getRandomDouble() {
        return random.nextDouble();
    }

    public static float getRandomFloatInRange(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static float getRandomFloat() {
        return random.nextFloat();
    }

    public static char getRandomChar() {
        return (char) (random.nextInt(26) + 'a');
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(getRandomChar());
        }
        return sb.toString();
    }

    public static String getRandomString() {
        return getRandomString(getRandomIntInRange(3, 10));
    }

    public static List<Object> getRandomItemsFromList(List<?> list, int amount, boolean allowDuplicates) {
        if (!allowDuplicates && amount > list.size()) {
            throw new IllegalArgumentException("Amount must be less than or equal to list size when unique is true");
        }
        ArrayList<Object> arrayList = new ArrayList<>(list);

        List<Object> items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Object item = arrayList.get(getRandomIntInRange(0, arrayList.size() - 1));
            items.add(item);
            if (!allowDuplicates) {
                arrayList.remove(item);
            }
        }
        return items;
    }

    public static Object getRandomItemFromList(List<?> list) {
        List<Object> items = getRandomItemsFromList(list, 1, true);
        return items.get(0);
    }
}
