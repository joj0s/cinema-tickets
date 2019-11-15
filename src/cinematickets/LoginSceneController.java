package cinematickets;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joj0s
 */
public class LoginSceneController implements Initializable {

    @FXML
    private Font x1;
    @FXML
    private TextField logInUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private CheckBox loginAdmin;
    @FXML
    private Button loginButton;
    @FXML
    private Label signupErrorMessage;
    @FXML
    private Label loginErrorMessage;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpFname;
    @FXML
    private TextField signUpLname;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpPasswordRepeat;
    @FXML
    private Label signUpErrorMessage;
    
    //TO ΓΕΓΟΝΟΣ ΠΟΥ ΔΗΜΙΟΥΡΓΕΙΤΑΙ ΟΤΑΝ ΕΝΑ ΚΟΥΜΠΙ ΠΙΕΖΕΤΑΙ, ΧΡΗΣΙΜΟΠΟΙΕΙΤΑΙ ΓΙΑ ΤΗΝ ΕΝΑΛΛΑΓΗ ΣΚΗΝΩΝ ΣΤΗ JAVAFX
    private ActionEvent event = null;
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    //ΧΕΙΡΙΣΜΟΣ ΣΥΝΔΕΣΗΣ
    
    @FXML
    private void login(ActionEvent event) {
        String usernameFieldValue = logInUsername.getText();
        String passwordFieldValue = loginPassword.getText();
        Boolean adminLoginFieldValue = loginAdmin.isSelected();
        this.event = event;
        
        //ΕΛΕΓΧΟΣ ΠΕΔΙΩΝ USERNAME ΚΑΙ PASSWORD
        if ( usernameFieldValue.isEmpty() || passwordFieldValue.isEmpty()){
            loginErrorMessage.setText("Τα πεδία username και password δεν πρέπει να είναι κενά!");
            return;
        }
        
        //ΕΚΤΕΛΕΣΗ SQL ΕΡΩΤΗΜΑΤΟΣ ΜΕ ΤΟ USERNAME ΚΑΙ ΤΟΝ ΚΩΔΙΚΟ ΠΟΥ ΔΩΘΗΚΑΝ
        try{
            ResultSet userQueryResult = DBUtils.statement.executeQuery("SELECT * FROM users WHERE username='"+usernameFieldValue+"' and password='"+passwordFieldValue+"'");
            
            //ΕΛΕΓΧΟΣ ΑΝ ΒΡΕΘΗΚΕ Ο ΧΡΗΣΤΗΣ
            if(!userQueryResult.next()){
                 loginErrorMessage.setText("Τα στοιχεία δεν είναι σωστά!");
                 return;
            }
            
            //ΔΗΜΙΟΥΡΓΙΑ ΑΝΤΙΚΕΙΜΕΝΟΥ ΧΡΗΣΤΗ 
            User user = initializeUserFromResultSet(userQueryResult);
            
        //ΕΛΕΓΧΟΣ ΑΝ ΤΟ CHECKBOX ADMINLOGIN ΕΠΙΛΕΧΘΗΚΕ ΚΑΙ ΚΛΗΣΗ ΚΑΤΑΛΛΗΛΗΣ ΜΕΘΟΔΟΥ ΓΙΑ ΕΙΣΟΔΟ ΣΤΟ ΣΥΣΤΗΜΑ    
        if(adminLoginFieldValue)
            adminLogin(user);
        else
            regularUserLogin(user);
            
              
                
        }       
        catch(SQLException e){
            e.printStackTrace();
        }
        
        
        
        
    }
    
    private User initializeUserFromResultSet(ResultSet rs) throws SQLException{
        return new User(rs.getString("username"),rs.getString("fname"),rs.getString("lname"),rs.getBoolean("isAdmin"));
    }
    
    
    
    
    
    
    
    
    
    private void loadScene(String sceneType){
        Parent sceneParent = null;
        String windowTitle = "";
        
        
        try{
            if(sceneType.equals("admin")){
                sceneParent = FXMLLoader.load(getClass().getResource("adminScene.fxml"));
                windowTitle = "CinemaTickets - Admin";
            }
                
            else{
                sceneParent = FXMLLoader.load(getClass().getResource("userScene.fxml"));
                windowTitle = "CinemaTickets";
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        
        Scene scene = new Scene(sceneParent); 
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.setResizable(true);
        window.setTitle(windowTitle);
        window.show();
        
    }
    
    
    
    
    //ΧΕΙΡΙΣΜΟΣ ΕΓΓΡΑΦΗΣ
    
    @FXML
    private void signUp(ActionEvent event){
        String signUpUsernameValue = signUpUsername.getText();
        String signUpPasswordValue = signUpPassword.getText();
        String signUpPasswordRepeatValue = signUpPasswordRepeat.getText();
        String signUpFnameValue = signUpFname.getText();
        String signUpLnameValue = signUpLname.getText();
        this.event=event;
        
        //ΕΛΕΓΧΟΣ ΓΙΑ ΚΕΝΑ ΠΕΔΙΑ
        if(signUpUsernameValue.isEmpty() || signUpPasswordValue.isEmpty() || signUpPasswordRepeatValue.isEmpty() || signUpFnameValue.isEmpty() || signUpLnameValue.isEmpty()){
            signUpErrorMessage.setText("Υπάρχουν κενά πεδία!");
            return;
        }
        
        //ΕΛΕΓΧΟΣ ΕΑΝ ΟΙ ΚΩΔΙΚΟΙ ΤΑΙΡΙΑΖΟΥΝ
        if (!signUpPasswordValue.equals(signUpPasswordRepeatValue)){
            signUpErrorMessage.setText("Οι κωδικοί δεν ταιριάζουν!");
            return;
        }
        
        
        //ΕΛΕΓΧΟΣ ΕΑΝ ΥΠΑΡΧΕΙ ΧΡΗΣΤΗΣ ΜΕ ΤΟ ΙΔΙΟ ΟΝΟΜΑ
        try{
            ResultSet userQueryResult = DBUtils.statement.executeQuery("SELECT * FROM users WHERE username='"+signUpUsernameValue+"'");
            
            //ΕΛΕΓΧΟΣ ΑΝ ΒΡΕΘΗΚΕ Ο ΧΡΗΣΤΗΣ
            if(userQueryResult.next()){
                 signUpErrorMessage.setText("Υπάρχει ήδη χρήστης με αυτό το username!");
                 return;
            }
            
            //ΕΙΣΑΓΩΓΗ ΤΟΥ ΧΡΗΣΤΗ ΣΤΗ ΒΑΣΗ
            DBUtils.statement.executeUpdate("INSERT INTO users VALUES ('"+signUpUsernameValue+"', '"+signUpPasswordValue+"', '"+signUpFnameValue+"', '"+signUpLnameValue+"', false) ;");
            
            
            User user = new User(signUpUsernameValue, signUpFnameValue, signUpLnameValue, false);
            
            regularUserLogin(user);
            
        
                
        }       
        catch(SQLException e){
            e.printStackTrace();
        }
        
        
    }
    
    private void adminLogin(User user) {
        
       if(!user.getAdmin()) {
           loginErrorMessage.setText("Ο χρήστης δεν έχει δικαιώαμα admin");
           return;
       }
       
       CinemaTickets.user = user; 
       loadScene("admin");       
        
    }
    
    
    
    
    
    private void regularUserLogin(User user){
        CinemaTickets.user = user;
        loadScene("user");
    }
    
  
    
}
