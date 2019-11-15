package cinematickets;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joj0s
 */
public class UserSceneController implements Initializable {
    
    private final DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("E dd/MM/yyyy HH:mm");

    @FXML
    private Label statusMessage;
    @FXML
    private Button usignOutButton;
    @FXML
    private Tab umoviesTab;;
    @FXML
    private TextField movieSearchField;
    @FXML
    private Button movieSearchButton;
    @FXML
    private Tab ureservationsTab;
    @FXML
    private TableColumn<?, ?> ureservationsTable;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie,String> colMoviesName;
    @FXML
    private TableColumn<Movie,String> colMoviesGenre;
    @FXML
    private ComboBox<String> movieGenreComboBox;
    
    @FXML 
    private TableView<Booking> bookingsTable; 
    @FXML 
    private TableColumn<Booking,String> colBMovie; 
    @FXML 
    private TableColumn<Booking,String> colBDate; 
    @FXML 
    private TableColumn<Booking,Integer> colBRoom; 
    

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        //ΑΡΧΙΚΟΠΟΙΗΣΗ ΠΙΝΑΚΩΝ
        colMoviesName.setCellValueFactory(new PropertyValueFactory<Movie,String>("name"));
        colMoviesGenre.setCellValueFactory(new PropertyValueFactory<Movie,String>("genre"));
        
        colBMovie.setCellValueFactory(new PropertyValueFactory<Booking,String>("movieName"));
        colBDate.setCellValueFactory(new PropertyValueFactory<Booking,String>("date"));
        colBRoom.setCellValueFactory(new PropertyValueFactory<Booking,Integer>("roomNumber"));

        //ΑΡΧΙΚΟΠΟΙΗΣΗ COMBO BOX ΓΙΑ ΕΠΙΛΟΓΗ ΕΙΔΟΥΣ ΤΑΙΝΙΑΣ
        movieGenreComboBox.getItems().addAll("Όλα","Action","Comedy","Drama","Sci-Fi","Documentary");
        
        addAllMoviesToTable();
        
        addAllBookingsToTable();
        
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
    
    
    
    private void addAllBookingsToTable() {
      
        try {
            
            //ΠΡΟΒΟΛΗ ΟΛΩΝ ΤΩΝ ΤΑΙΝΙΩΝ ΣΤΟΝ ΧΡΗΣΤΗ
           ResultSet queryResultSet = DBUtils.statement.executeQuery("select distinct m.name as moviename, b.sdate as date, s.rnumber as rnumber " +
           "from bookings b join screenings s on b.sdate=s.sdate and b.mid = s.mid " +
           "join movies m on s.mid = m.id " +
           "where username='"+CinemaTickets.user.getUsername()+"' ;");        
            bookingsTable.setItems(resultSetToBookingList(queryResultSet));
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            statusMessage.setText("Πρόβλημα στη βάση δεδομένων!");
            
        }
    }
    
    
    
    
    
    
    private ObservableList<Booking> resultSetToBookingList(ResultSet rs) throws SQLException{
        
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        String movieName;
        String formattedDate;
        int roomNumber;
        
        
        while(rs.next()){
            movieName = rs.getString("moviename");
            formattedDate = rs.getTimestamp("date").toLocalDateTime().format(formatter);
            roomNumber = rs.getInt("rnumber");
            bookings.add(new Booking(movieName, formattedDate, roomNumber));
        }
        
        return bookings;
        
        
    }
    
    
    
   
    
    public void bookTicket() {
        
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        
        if ( selectedMovie==null ){
            statusMessage.setText("Δεν υπάρχει επιλεγμένη ταινία");
            return;
        }
            
        BookingWindowSceneController.selectedMovie = selectedMovie;
        
        Stage modalWindow = new Stage();
        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.setTitle("Κράτηση εισιτηρίου");
        modalWindow.setHeight(300);
        modalWindow.setWidth(600);
        modalWindow.setResizable(false);
        
        try {
            Scene bookingModalScene = new Scene(FXMLLoader.load(getClass().getResource("bookingWindowScene.fxml")));
            modalWindow.setScene(bookingModalScene);
            modalWindow.showAndWait();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        
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
