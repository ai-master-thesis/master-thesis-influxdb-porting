package it.e6h.influxdb.util;

import it.e6h.influxdb.benchmark.ItemProperties;
import it.e6h.influxdb.benchmark.ItemProperty;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class Util {
    public static Properties loadPropertiesFromClasspath(String filename) {
        Properties prop = new Properties();

        try(InputStream input = Util.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("Something went wrong trying to load properties file");
            }

            prop.load(input);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return prop;
    }

    public static PortingMenuItem getValidPortingMenuItemInput(String prompt) {
        final Scanner sc = new Scanner(System.in);
        boolean waitingForInput = true;
        PortingMenuItem selection = PortingMenuItem.INVALID;

        while(waitingForInput) {
            System.out.println(prompt);
            for(PortingMenuItem item : PortingMenuItem.values()) {
                if (item == PortingMenuItem.INVALID)
                    continue;
                System.out.println(String.format("\t%d - for %s", item.numValue, item.name()));
            }
            selection = PortingMenuItem.valueOf(sc.nextInt()).orElse(PortingMenuItem.INVALID);

            if(selection != PortingMenuItem.INVALID) {
                waitingForInput = false;
            }
        }

        return selection;
    }

    public static String getValidPortingStringInput(String prompt) {
        final Scanner sc = new Scanner(System.in);

        System.out.println(prompt);

        return sc.nextLine();
    }

    public static BenchmarkMenuItem getValidBenchmarkMenuItemInput(String prompt) {
        final Scanner sc = new Scanner(System.in);
        boolean waitingForInput = true;
        BenchmarkMenuItem selection = BenchmarkMenuItem.INVALID;

        while(waitingForInput) {
            System.out.println(prompt);
            for(BenchmarkMenuItem item : BenchmarkMenuItem.values()) {
                if (item == BenchmarkMenuItem.INVALID)
                    continue;
                System.out.println(String.format("\t%d - for %s", item.numValue, item.name()));
            }
            selection = BenchmarkMenuItem.valueOf(sc.nextInt()).orElse(BenchmarkMenuItem.INVALID);

            if(selection != BenchmarkMenuItem.INVALID) {
                waitingForInput = false;
            }
        }

        return selection;
    }

    public static ItemProperty getRandomItemProperty(List<ItemProperties> itemPropertiesList) {
        Random rand = new Random();
        int itemIdIndex = rand.nextInt(itemPropertiesList.size());
        ItemProperties itemProperties = itemPropertiesList.get(itemIdIndex);
        Long itemId = itemProperties.getItemId();
        List<String> properties = itemProperties.getProperties();
        int propertyIndex = rand.nextInt(properties.size());
        String property = properties.get(propertyIndex);

        return new ItemProperty(itemId, property);
    }
}
