package utilities;

import java.io.File;
import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {
    
    @DataProvider(name="LoginData")
    public String [][] getData() throws IOException {
        String path = ".\\testData\\data.xlsx";  // Specify path to Excel file
        
        // Check if the file exists before proceeding
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + path);
        }

        ExcelUtility xlutil = new ExcelUtility(path);  // Create an object for ExcelUtility class

        int totalrows = xlutil.getRowCount("Sheet1");    
        int totalcols = xlutil.getCellCount("Sheet1",1);
                
        String logindata[][] = new String[totalrows][totalcols];  // Two-dimensional array for login data
        
        for (int i = 1; i <= totalrows; i++) {  // Start reading from the second row (index 1)
            for (int j = 0; j < totalcols; j++) {
                logindata[i - 1][j] = xlutil.getCellData("Sheet1", i, j);  // Read data and store in array
            }
        }
        return logindata;  // Return the login data array
    }
}
