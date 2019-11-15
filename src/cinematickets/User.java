
package cinematickets;

public class User {
    
    private String username;
    private String fname;
    private String lname;
    private boolean admin;
    

    public User(String username, String fname, String lname, boolean admin) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", fname=" + fname + ", lname=" + lname + ", isAdmin=" + admin + '}';
    }
    
    
    
    
}
