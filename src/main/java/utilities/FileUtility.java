package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtility {
    public static  String readData(String key) throws IOException {
        FileInputStream file=new FileInputStream("./src/main/resources/commonData.properties");
        Properties props=new Properties();
        props.load(file);

        return  props.getProperty(key);
    }
}
