import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class app {
    private static boolean said = false;

    public void run(String fn) {
        Scanner in = new Scanner(System.in);

        getLongest(fn);

        System.out.println("Показать весь набор продуктов? Y/N");
        String choise = in.nextLine();

        switch (choise.toLowerCase()) {
            case "y":
                System.out.println(getCount(fn));
                break;
                case "n":
                    break;
        }
    }

    private String[] ConvertToArr(String fn) {
        String[] arr = new String[0];
        try (BufferedReader br = new BufferedReader(new FileReader(fn))) {
            String line = br.readLine();
            arr = line.split(" +");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    private TreeMap<String, Integer> getCount(String fn) {
        String[] arr = ConvertToArr(fn);
        if (arr == null) {
            return new TreeMap<>();
        }

        TreeMap<String, Integer> fruits = new TreeMap<>();

        final int[] maxCount = {0};
        AtomicReference<String> maxName = new AtomicReference<>("");

        for (String fruit : arr) {
            fruits.compute(fruit, (k, count) -> {
                int newCount = (count == null) ? 1 : count + 1;
                if (newCount > maxCount[0]) {
                    maxCount[0] = newCount;
                    maxName.set(fruit);
                }
                return newCount;
            });
        }

        /* за время работы приложения класс getCount вызывается дважды,
           что бы избавиться от повторения вывода maxName поставил флаг said. */
        if (!said) {
            System.out.println("Самый популярный фрукт на вашем пикнике: " + maxName);
            said = true;
        }
        return fruits;
    }

    private void getLongest(String fn) {
        TreeMap<String, Integer> map = getCount(fn);
        Iterator<String> iterator = map.keySet().iterator();

        int max = iterator.next().length();
        String maxName = iterator.next();
        boolean EachKeysSameLength = true;

        for (String fruit : map.keySet()) {
            if (fruit.length() > max) {
                if (EachKeysSameLength) {
                    EachKeysSameLength = false;
                }
                max = fruit.length();
                maxName = fruit;
            }
        }
        if (EachKeysSameLength) {
            System.out.println("Все длины названий одинаковы");
        } else {
            System.out.println("Самый <длинный> фрукт: " + maxName + " с длиной " + max);
        }
    }
}
