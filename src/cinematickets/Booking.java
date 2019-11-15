/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinematickets;


/**
 *
 * @author joj0s
 */
public class Booking {
    
    private String movieName, date;
    private int roomNumber;

    public Booking(String movieName, String date, int roomNumber) {
        this.movieName = movieName;
        this.date = date;
        this.roomNumber = roomNumber;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    
}
