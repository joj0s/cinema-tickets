/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinematickets;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joj0s
 */
public class AdminSceneController implements Initializable {

    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie,String> colMoviesName;
    @FXML
    private TableColumn<Movie,String> colMoviesGenre;    
    @FXML
    private ComboBox<String> movieGenreComboBox;
    @FXML
    private Label statusMessage;
    @FXML
    private TextField movieSearchField;
    @FXML
    private TextField newMovieNameField;
    @FXML
    private ComboBox<String> newMovieGenreComboBox;
    @FXML
    private TableView<Screening> screeningTable;
    @FXML
    private TableColumn<Screening,String> colScreeningMovie;
    @FXML
    private TableColumn<Screening,String> colScreeningDate;  
    @FXML
    private TableColumn<Screening,Integer> colScreeningRoom;  
    @FXML
    private TableColumn<Screening,Integer> colScreeningAttendance;  
    @FXML
    private ComboBox<String> newScreeningMoviesComboBox;
    @FXML
    private ComboBox<Integer> newScreeningRoomComboBox;
    @FXML
    private TextField newScreeningDateField;
    @FXML
    private Button promoteUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User,String> colUserUsername;
    @FXML
    private TableColumn<User,String> colUserName;  
    @FXML
    private TableColumn<User,String> colUserLastName;  
    @FXML
    private TableColumn<User,Boolean> colUserIsAdmin; 
    @FXML
    private TextArea logsTextArea;
    @FXML
    private ComboBox<String> logsSelectTableComboBox;
    
    private final DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("E dd/MM/yyyy HH:mm");

    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        colMoviesName.setCellValueFactory(new PropertyValueFactory<Movie,String>("name"));
        colMoviesGenre.setCellValueFactory(new PropertyValueFactory<Movie,String>("genre"));
        
        colScreeningMovie.setCellValueFactory(new PropertyValueFactory<Screening,String>("movieName"));
        colScreeningDate.setCellValueFactory(new PropertyValueFactory<Screening,String>("date"));
        colScreeningRoom.setCellValueFactory(new PropertyValueFactory<Screening,Integer>("mid"));
        colScreeningAttendance.setCellValueFactory(new PropertyValueFactory<Screening,Integer>("rnumber"));
        
        colUserUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colUserName.setCellValueFactory(new PropertyValueFactory<User,String>("fname"));
        colUserLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lname"));
        colUserIsAdmin.setCellValueFactory(new PropertyValueFactory<User,Boolean>("admin"));
        
        movieGenreComboBox.getItems().addAll("Όλα","Action","Comedy","Drama","Sci-Fi","Documentary");
        newMovieGenreComboBox.getItems().addAll("Action","Comedy","Drama","Sci-Fi","Documentary");
        
        addAllMoviesToTable();
        addAllScreeningsToTable();
        addMoviesToComboBox();
        addRoomsToComboBox();
        addAllUsersToTable();
        addAllDBTablesToComboBox();
    }    
    
    
    public void addAllMoviesToTable() {
        
        try {
            
            //ΠΡΟΒΟΛΗ ΟΛΩΝ ΤΩΝ ΤΑΙΝΙΩΝ ΣΤΟΝ ΧΡΗΣΤΗ
            ResultSet queryResultSet = DBUtils.statement.executeQuery("SELECT * FROM movies");        
            movieTable.setItems(resultSetToMovieList(queryResultSet));
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
            
        }
            
    }
    
    
    
    public void searchMovies() {
        
        String genreFilter = movieGenreComboBox.getSelectionModel().getSelectedItem();
        String nameFilter = movieSearchField.getText();
        
        String sqlQuery = "SELECT * FROM movies WHERE ";
        
        try {
            
            if ( ( genreFilter.isEmpty() || genreFilter.equals("Όλα") ) && nameFilter.isEmpty() ){
                addAllMoviesToTable();
                return;
            }
            else if ( genreFilter.isEmpty() || genreFilter.equals("Όλα") )
                sqlQuery += "name LIKE '%"+nameFilter+"%' ;";
            else if ( nameFilter.isEmpty())
                sqlQuery += "genre='"+genreFilter+"' ;";
            else
                sqlQuery += "genre='"+genreFilter+"' AND name LIKE '%"+nameFilter+"%' ;";
            
            
            ResultSet queryResult = DBUtils.statement.executeQuery(sqlQuery);
            
            movieTable.setItems(resultSetToMovieList(queryResult));
                    
        }
        catch (SQLException e){
            e.printStackTrace();
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
        }
    
        
    }
    
    
    
    private ObservableList<Movie> resultSetToMovieList(ResultSet rs) throws SQLException{
        
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        int movieId;
        String movieName;
        String movieGenre;
        
        while(rs.next()){
            movieId = rs.getInt(1);
            movieName = rs.getString(2);
            movieGenre = rs.getString(3);
            movies.add(new Movie(movieId,movieName, movieGenre));
        }
        
        return movies;
        
        
    }
    
    
    
    public void addMovie() {
        
        int movieId;
        String movieName = newMovieNameField.getText();
        String movieGenre = newMovieGenreComboBox.getSelectionModel().getSelectedItem();
        
        if(movieName.isEmpty() || movieGenre.isEmpty()){
            statusMessage.setText("Τα πεδία δεν πρέπει να είναι κενά!");
            return;
        }
            
        
        try{
            movieId = getNewMovieId();
            DBUtils.statement.execute("insert into movies values( '"+movieId+"', '"+movieName+"', '"+movieGenre+"' );");
            statusMessage.setText("Η ταινία προστέθηκε!");
        }
        catch(SQLException e){
            e.printStackTrace();
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
        }
        
    }
    
    
    public void deleteMovie() {
        
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        int movieId = selectedMovie.getId();
       
        try {
            DBUtils.statement.execute("delete from movies where id="+movieId);
            movieTable.getItems().remove(selectedMovie);
            statusMessage.setText("Η ταινία διαγράφηκε");
        }
        catch(SQLException e){
            e.printStackTrace();
            statusMessage.setText("Ποβλημα με τη βάση!");
        }
        
    }
    
    
    private int getNewMovieId() throws SQLException{
        ResultSet rs = DBUtils.statement.executeQuery("SELECT MAX(id) as id FROM movies");
        int id=0;
        
        while(rs.next())
            id = rs.getInt("id");
        
        return ++ id;
    }
    
    
    
    private void addAllScreeningsToTable(){
        
        try {
            
            //ΠΡΟΒΟΛΗ ΟΛΩΝ ΤΩΝ ΤΑΙΝΙΩΝ ΣΤΟΝ ΧΡΗΣΤΗ
            ResultSet queryResultSet = DBUtils.statement.executeQuery("SELECT m.name as moviename, s.sdate as date, s.rnumber as rnumber, s.attendance as attendance FROM screenings s join movies m on m.id=s.mid");        
            screeningTable.setItems(resultSetToScreeningList(queryResultSet));
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
            
        }
    }
    
    
    private ObservableList<Screening> resultSetToScreeningList(ResultSet rs) throws SQLException{
        
        ObservableList<Screening> screenings = FXCollections.observableArrayList();
        String movieName;
        String date;
        int roomNumber;
        int attendance;
        
        while(rs.next()){
            movieName = rs.getString("moviename");
            date = rs.getTimestamp("date").toLocalDateTime().format(formatter);
            roomNumber = rs.getInt("rnumber");
            attendance = rs.getInt("attendance");
            screenings.add(new Screening(movieName,date, roomNumber, attendance));
        }
        
        return screenings;
        
        
    }
    
    
    public void deleteScreening() {
        
        Screening selectedScreening = screeningTable.getSelectionModel().getSelectedItem();
        int movieId = selectedScreening.getMid();
        String stringSdate = selectedScreening.getDate();
        
        Timestamp sdate = formattedDateToTimestamp(stringSdate);
                
        
       
        try {
            System.out.println("delete from screenings where mid="+movieId+"' and sdate='"+sdate+"' ;");
            DBUtils.statement.execute("delete from screenings where mid="+movieId+" and sdate='"+sdate+"' ;");
            screeningTable.getItems().remove(selectedScreening);
            statusMessage.setText("Η προβολή διαγράφηκε");
        }
        catch(SQLException e){
            e.printStackTrace();
            statusMessage.setText("Ποβλημα με τη βάση!");
        }
        
    }
    
    
    private Timestamp formattedDateToTimestamp(String date){
        SimpleDateFormat format = new SimpleDateFormat("E dd/MM/yyyy HH:mm");
        Date parsedDate = null;
        try{
            parsedDate = (Date) format.parse(date);
        }
        catch(ParseException e){
            
        }
        
        return new Timestamp(parsedDate.getTime());
    }
    
    
    
    public void addScreening(){
        
        
        try {
            
            String movieName = newScreeningMoviesComboBox.getSelectionModel().getSelectedItem();
            String dateString = newScreeningDateField.getText();
            int roomNumber = newScreeningRoomComboBox.getSelectionModel().getSelectedItem();
            
            
            if(movieName.isEmpty() || dateString.isEmpty() || roomNumber<1)
                statusMessage.setText("Ta πεδία δεν πρέπει να είναι κενά!");
            
            
            Timestamp sdate = parseDate(dateString);
            
            ResultSet movieResultSet = DBUtils.statement.executeQuery("select id from movies where name='"+movieName+"' ;");
            movieResultSet.next();
            int movieId = movieResultSet.getInt("id");
            
            
            if (screeningExists(sdate, roomNumber)){
                statusMessage.setText("Υπάρχει ήδη προβολή σε αυτήν την ώρα και αίθουσα!");
                return;
            }
                
            
            DBUtils.statement.execute("insert into screenings values ('"+sdate+"', "+movieId+", "+roomNumber+", 0);");
            statusMessage.setText("Επιτυχής εισαγωγή!");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    
    private void addMoviesToComboBox(){
        
        try{
            ResultSet queryResultSet = DBUtils.statement.executeQuery("SELECT * FROM movies");
            ObservableList<Movie> movies = resultSetToMovieList(queryResultSet);
            
            
            for(Movie m: movies)
                newScreeningMoviesComboBox.getItems().add(m.getName());
        }
        
        catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    
    
    private void addRoomsToComboBox() {
        
        try{
            
            ResultSet queryResultSet = DBUtils.statement.executeQuery("SELECT id FROM rooms");
            
            ObservableList<Integer> rooms = FXCollections.observableArrayList();
            
            while(queryResultSet.next())
                rooms.add(queryResultSet.getInt("id"));
            
            newScreeningRoomComboBox.getItems().addAll(rooms);
        }
        
        catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    
    
    
    private Timestamp parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date parsedDate = null;
        try{
            parsedDate = (Date) format.parse(date);
        }
        catch(ParseException e){
            e.printStackTrace();
            statusMessage.setText("Η ημερομηνία έχει λάθος μορφή!");
        }
        
        return new Timestamp(parsedDate.getTime());
    }
    
    
    private boolean screeningExists(Timestamp sdate, int room) throws SQLException{
        
        ResultSet rs = DBUtils.statement.executeQuery("select * from screenings where sdate='"+sdate+"' and rnumber="+room);
        
        return rs.next();
        
    } 
    
    
    private void addAllUsersToTable() {
        
       try{
           ResultSet queryResultSet = DBUtils.statement.executeQuery("select * from users");
           usersTable.getItems().addAll(resultSetToUserList(queryResultSet));
           
       }
       catch(SQLException e){
           e.printStackTrace();
       }
       
    }
    
    
    private ObservableList<User> resultSetToUserList(ResultSet rs) {
        
        ObservableList<User> users = FXCollections.observableArrayList();
        String username = "";
        String fname = "";
        String lname = "";
        boolean isAdmin = false;
        
        try{
                while(rs.next()){
                username = rs.getString("username");
                fname = rs.getString("fname");
                lname = rs.getString("lname");
                isAdmin = rs.getBoolean("isAdmin");
                users.add(new User(username,fname,lname,isAdmin));
            }
            
            
        }
        catch(SQLException e){
                e.printStackTrace();
       }
        return users;
    }
    
    
    
    
    public void promoteUser() {
        
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        
        if( selectedUser==null ){
            statusMessage.setText("Δεν έχει επιλεχθεί χρήστης");
            return;
        }
        
        
        if(selectedUser.getAdmin()){
            statusMessage.setText("Ο χρήστης είναι ήδη admin");
            return;
        }
            
        
        try {
            DBUtils.statement.executeUpdate("UPDATE users set isAdmin=true where username='"+selectedUser.getUsername()+"' ;");
            statusMessage.setText("Επιτυχία!");
            refreshUsersTable();
        }
        catch(SQLException e){
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
            e.printStackTrace();
        }
        
        
    }
    
    
    

    
    public void deleteUser() {
        
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        
        if( selectedUser==null ){
            statusMessage.setText("Δεν έχει επιλεχθεί χρήστης");
            return;
        }
        
        try {
            DBUtils.statement.execute("DELETE from users where username='"+selectedUser.getUsername()+"' ;");
            statusMessage.setText("Επιτυχία!");
            refreshUsersTable();
        }
        catch(SQLException e){
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
            e.printStackTrace();
        }
        
        
    }
    
    
    
    
    private void refreshUsersTable() {
        
        usersTable.getItems().setAll();
        addAllUsersToTable();
        
    }
    
    
    
    
    private void addAllDBTablesToComboBox(){
        
        logsSelectTableComboBox.getItems().addAll("movies","rooms","screenings","bookings","users");
        
    }
    
    
    
    
    public void showLogs() {
        
        String selectedTable = logsSelectTableComboBox.getSelectionModel().getSelectedItem();
                
        if (selectedTable.isEmpty()){
            statusMessage.setText("Δεν υπάρχει επιλεγμένος πίνακας!");
            return;
        }
        
        
        try{
            logsTextArea.setText(getLogs(selectedTable));
        }
        catch(SQLException e){
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων");
            e.printStackTrace();
            return;
        }
        
            
    }
    
    
    
    
    private String getLogs(String selectedTable) throws SQLException{    
        
        String log ="";
        
        
        ResultSet queryResultSet = DBUtils.statement.executeQuery("select * from "+selectedTable+"_log_file;");

        
        if(!queryResultSet.next()){
            statusMessage.setText("Δεν υπάρχουν logs!");
            return "";
        }
        
        
        switch(selectedTable){
            case "movies":
                log = resultSetMoviesToLog(queryResultSet);
                break;
            case "rooms":
                log = resultSetRoomsToLog(queryResultSet);
                break;
            case "screenings":
                log = resultSetScreeningsToLog(queryResultSet);
                break;
            case "bookings":
                log = resultSetBookingsToLog(queryResultSet);
                break;
            case "users":
                log = resultSetUsersToLog(queryResultSet);
                break;
        }
        
        
        return log;
        
        
    }
    
    
    
    
    
    private String resultSetMoviesToLog(ResultSet rs) throws SQLException{
                
        String log = "";
        
        String operation = "";
        String oper_time = "";
        int id = 0;
        String name = "";
        String genre = "";
        
        
        do{
          operation = rs.getString("operation");
            oper_time = rs.getTimestamp("oper_time").toLocalDateTime().toString();
            id = rs.getInt("id");
            name = rs.getString("name");
            genre = rs.getString("genre");
            
            log += operation+"  "+oper_time+"  "+id+"  "+name+"  "+genre+"  \n";  
        }
        while(rs.next());
          
        
        
        return log;
            
    }
    
    
    
    private String resultSetRoomsToLog(ResultSet rs) throws SQLException{
        
        String log = "";
        
        String operation = "";
        String oper_time = "";
        int id = 0;
        int capacity = 0;
        
        
        do{
             operation = rs.getString("operation");
            oper_time = rs.getTimestamp("oper_time").toLocalDateTime().toString();
            id = rs.getInt("id");
            capacity = rs.getInt("capacity");
            
            log += operation+"  "+oper_time+"  "+id+"  "+capacity+"  \n";
        }
        while(rs.next());
        
        return log;
        
    }
    
    
    
    private String resultSetScreeningsToLog(ResultSet rs) throws SQLException{
        
        String log = "";
        
        String operation = "";
        String oper_time = "";
        String sdate = "";
        int mid = 0;
        int rnumber = 0;
        int attendance = 0;
        
        do{
            operation = rs.getString("operation");
            oper_time = rs.getTimestamp("oper_time").toLocalDateTime().toString();
            sdate = rs.getTimestamp("sdate").toLocalDateTime().toString();
            mid = rs.getInt("mid");
            rnumber = rs.getInt("rnumber");
            attendance = rs.getInt("attendance");
            
            log += operation+"  "+oper_time+"  "+sdate+"  "+mid+"  "+rnumber+"  "+attendance+"  \n";
        }
        while(rs.next());
        
        return log;
        
        
    }
    
    
    
    
    private String resultSetBookingsToLog(ResultSet rs) throws SQLException{
        
        String log = "";
        
        String operation = "";
        String oper_time = "";
        String username = "";
        String sdate = "";
        int mid = 0;
        
        
        do{
            operation = rs.getString("operation");
            oper_time = rs.getTimestamp("oper_time").toLocalDateTime().toString();
            username = rs.getString("username");
            sdate = rs.getTimestamp("sdate").toLocalDateTime().toString();
            mid = rs.getInt("mid");
            
            log += operation+"  "+oper_time+"  "+username+" "+sdate+"  "+mid+"  \n";
        }
        while(rs.next());
        
        return log;
        
    }
    
    
    
    
    private String resultSetUsersToLog(ResultSet rs) throws SQLException{
        
        String log = "";
        
        String operation = "";
        String oper_time = "";
        String username = "";
        String fname = "";
        String lname = "";
        boolean isAdmin = false;
        
        
        do{
            operation = rs.getString("operation");
            oper_time = rs.getTimestamp("oper_time").toLocalDateTime().toString();
            username = rs.getString("username");
            fname = rs.getString("fname");
            lname = rs.getString("lname");
            isAdmin = rs.getBoolean("isAdmin");
            
            log += operation+"  "+oper_time+"  "+username+" "+fname+"  "+lname+" "+isAdmin+"  \n";
        }
        while(rs.next());
        
        return log;
        
        
        
    }
    
    
    
    
    
    
    public void signOut(ActionEvent event) {
        
        
        try{
            Scene loginScene = new Scene (FXMLLoader.load(getClass().getResource("loginScene.fxml")));
            CinemaTickets.user=null;
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.setResizable(false);
            window.setTitle("CinemaTickets - Login");
            window.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
            
        
    }
    
    
    
}
