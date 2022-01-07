package com.henleylee.lockpattern;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 *
 * @author Henley
 * @since 2019/8/27 17:41
 */
public final class PatternHelper {

    static List<Cell> getCellList(int length, int width, int height, int offsetX, int offsetY) {
        List<Cell> result = new ArrayList<>();
        float radius = Math.min(width, height) * 1f / (length * 2 + length - 1);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int index = (i * length + j) % (length * length);
                float x = (j * 3 + 1) * radius + offsetX;
                float y = (i * 3 + 1) * radius + offsetY;
                result.add(new Cell(index, x, y, radius, CellStatus.NORMAL));
            }
        }
        return result;
    }

    /**
     * 判断三个点是否在一条直线上
     */
    static boolean isInLine(double x1, double y1, double x2, double y2, double x3, double y3) {
        if ((x1 == x2 && x2 == x3) || (y1 == y2 && y2 == y3)) {
            return true;
        }
        return (x2 - x1) * (y3 - y1) == (x3 - x1) * (y2 - y1);
    }

    /**
     * 点(x2,y2)是否在以(x1,y1)为圆心，r为半径的圆内
     */
    static boolean isInRound(double x1, double y1, double x2, double y2, double r) {
        return distance(x1, y1, x2, y2) < r;
    }

    /**
     * 两点间距
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static String patternToString(int side, List<Cell> cells) {
        if (cells == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("$").append(side);
        for (Cell cell : cells) {
            builder.append("-").append(cell.getIndex());
        }
        return builder.toString();
    }

    public static String patternToHash(int side, List<Cell> cells) {
        if (cells == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("$").append(side);
        for (Cell cell : cells) {
            builder.append("-").append(cell.getIndex());
        }
        byte[] bytes = builder.toString().getBytes();
        try {
            return toHexString(MessageDigest.getInstance("SHA-1").digest(bytes));
        } catch (NoSuchAlgorithmException var1) {
            return toHexString(bytes);
        }
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(hex.toUpperCase());
        }
        return builder.toString();
    }

}