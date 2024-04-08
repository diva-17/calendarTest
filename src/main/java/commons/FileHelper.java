package commons;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import java.util.*;

public class FileHelper {



    // To Read data from Excel sheet
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