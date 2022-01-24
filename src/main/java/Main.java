import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Employee> parseCSV(String[] columnMapping, String filename) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return staff;
    }

    private static void writeFile(String filename, List<String[]> list) {
        File file = new File(filename);
        if (file.exists()) {
            System.out.println("Файл уже существует");
        } else {
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), ',',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {
                csvWriter.writeAll(list);
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .setPrettyPrinting()
                .create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String filename, String json) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String absolutPath = "C:/Users/weyma/IdeaProjects/Task1 (JSV-JSON)/";
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"1", "John", "Smith", "USA", "25"});
        data.add(new String[]{"2", "Ivan", "Petrov", "RU", "23"});
        data.add(new String[]{"3", "Petr", "Bakanov", "RU", "35"});

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        writeFile(absolutPath + fileName, data);

        List<Employee> list = parseCSV(columnMapping, absolutPath + fileName);

        String json = listToJson(list);
        System.out.println(json);
        writeString(absolutPath + "data.json", json);



    }
}

