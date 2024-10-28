package Pr3.Task1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        int[] array1 = new int[20];
        int[] array2 = new int[20];
        int[] array3 = new int[20];
        fillArray(array1);
        fillArray(array2);
        fillArray(array3);
        writeArrayToFile(array1, "array1.txt");
        writeArrayToFile(array2, "array2.txt");
        writeArrayToFile(array3, "array3.txt");

        int[] loadedArray1 = readArrayFromFile("array1.txt");
        int[] loadedArray2 = readArrayFromFile("array2.txt");
        int[] loadedArray3 = readArrayFromFile("array3.txt");

        Callable<Integer[]> callable1 = () -> {
            Integer[] result = new Integer[loadedArray1.length];
            for (int i = 0; i < loadedArray1.length; i++) {
                result[i] = loadedArray1[i] * 3;
            }
            return result;
        };
        Callable<Integer[]> callable2 = () -> {
            List<Integer> resultList = new ArrayList<>();
            for (int i = 0; i < loadedArray2.length; i++) {
                if (loadedArray2[i] % 2 == 0) {
                    resultList.add(loadedArray2[i]);
                }

            }
            return resultList.toArray(new Integer[0]);
        };
        Callable<Integer[]> callable3 = () -> {
            List<Integer> resultList = new ArrayList<>();
            for (int i = 0; i < loadedArray3.length; i++) {
                if (loadedArray3[i] >= 10 && loadedArray3[i] <= 175) {
                    resultList.add(loadedArray3[i]);
                }
            }
            return resultList.toArray(new Integer[0]);
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);


        List<Future<Integer[]>> futures = new ArrayList<>();
        futures.add(executorService.submit(callable1));
        futures.add(executorService.submit(callable2));
        futures.add(executorService.submit(callable3));

        List<Integer> tempList = new ArrayList<>();

        for (Future element : futures){
            try {
                if (!element.isCancelled()) {
                    System.out.println("Результат виконання: ");
                    Integer[] result = (Integer[]) element.get();
                    int[] intArr = new int[result.length];
                    for (int i = 0; i < result.length; i++) {
                        intArr[i] = result[i];
                        tempList.add(result[i]);
                    }
                    Arrays.sort(intArr);
                    System.out.println(Arrays.toString(intArr));
                } else {
                    System.err.println("Cancelled");
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        List<Integer> result = new ArrayList<>();

        for (int j = 0; j < loadedArray3.length; j++) {
            if(!tempList.contains(loadedArray3[j])) {
                result.add(loadedArray3[j]);
                tempList.remove((Integer) loadedArray3[j]);

            }
        }

        System.out.println("Result:" + result);

    }

    public static void fillArray(int[] a){
        Random random = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(0, 1001);
        }
    }

    public static void writeArrayToFile(int[] arr, String fileName) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Pr3/Task1/" + fileName))) {
            bufferedWriter.write(Arrays.toString(arr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] readArrayFromFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Pr3/Task1/" + fileName));
            String string = bufferedReader.readLine();

            string = string.replace("[", "");
            string = string.replace("]", "");

            String[] splittedString = string.split(", ");

            int[] result = new int[splittedString.length];

            for (int i = 0; i < splittedString.length; i++) {
                result[i] = Integer.parseInt(splittedString[i]);
            }

            return  result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
