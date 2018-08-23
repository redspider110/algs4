package edu.princeton.cs.algs4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public class TestTreeMap {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        Integer[] keys = {12, 1, 9, 2, 0, 11, 7, 19, 4, 15, 18, 5, 14, 13, 10, 16, 6, 3, 8, 17};

        for (int i = 0; i < keys.length; i++){
            treeMap.put(keys[i], "a" + i);
        }

        Map.Entry<Integer, String> treeMapEntry = null;
        for (Map.Entry<Integer, String> entry : treeMap.entrySet()){
            if (entry.getKey().equals(4)) treeMapEntry = entry;
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }

        System.out.println("parameter: " + treeMapEntry);

        try {
            String treeMapEntryName = "java.util.TreeMap$Entry";
            Class treeMapEntryClazz = Class.forName(treeMapEntryName);
            Method predecessorMethod = treeMap.getClass().getDeclaredMethod("predecessor", new Class[]{treeMapEntryClazz});
            predecessorMethod.setAccessible(true);
            Object predecessor = predecessorMethod.invoke(treeMap, treeMapEntry);

            Method successorMethod = treeMap.getClass().getDeclaredMethod("successor", treeMapEntryClazz);
            successorMethod.setAccessible(true);
            Object successor = successorMethod.invoke(treeMap, treeMapEntry);

            System.out.println("predecessor = " + predecessor);
            System.out.println("successor = " + successor);
            treeMap.remove(4);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        treeMap.remove(4);
    }
}
