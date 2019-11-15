
package cinematickets;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {
    
    static String     driverClassName = "org.postgresql.Driver" ;
    static String     url = "jdbc:postgresql://dblabs.it.teithe.gr:5432/it175012" ;
    static Connection dbConnection = null;
    static String     username = "it175012";
    static String     passwd = "@Lnsn1991"; 
    static Statement statement = null;
    
    static void initStatement() throws Exception {
       Class.forName(driverClassName);
       dbConnection = DriverManager.getConnection(url, username, passwd);
       statement = dbConnection.createStatement();
    }
    
}
