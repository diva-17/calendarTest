package commons;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileHelper {

    public  String readFileContent(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public List<HashMap<String, String>> csvReader( String fileName)  {
        try {
            String currDir = System.getProperty("user.dir")+"/testdata/";
            String filePath = currDir + fileName + ".csv";
            List<HashMap<String, String>> resultValues = new ArrayList<>();
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            Iterator<HashMap<String, String>> iterator = new CsvMapper()
                    .readerFor(Map.class)
                    .with(CsvSchema.emptySchema().withHeader())
                    .readValues(csvReader);
            iterator.forEachRemaining(resultValues::add);
            return resultValues;
        } catch (IOException e) {
            System.out.println("Problem reading CSV File please Check if File Exists");
            e.printStackTrace();
            return null;
        }
    }



    public static List<HashMap<String, String>> getDataFromExcel(String file, String Query) {
        Connection connection = null;
        Recordset recordset = null;
        HashMap<String, String> resultValue = new HashMap<>();
        List<HashMap<String, String>> resultValues = new ArrayList<>();
        String currDir = System.getProperty("user.dir")+"/testdata/";
        String filepath = currDir + file + ".xlsx";
        try {
            System.out.println(Query);
            Fillo fillo = new Fillo();
            connection = fillo.getConnection(filepath);
            recordset = connection.executeQuery(Query);
            ArrayList<String> data = recordset.getFieldNames();
            while (recordset.next()) {
                for (String datum : data) {
                    resultValue.put(datum,
                            recordset.getField(datum));
                }
                resultValues.add(resultValue);
                resultValue = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.fail( "Unable to read file ->  " + file + "  Excel Sheet ");
        } finally {
            if (recordset != null) {
                recordset.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return resultValues;
    }

}