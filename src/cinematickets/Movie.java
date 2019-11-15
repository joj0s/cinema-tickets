
package cinematickets;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author joj0s
 */
public class Movie {
    
    private int id;
    private SimpleStringProperty name, genre;

    public Movie(int id, String name, String genre) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.genre = new SimpleStringProperty(genre);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
  
    public String getName() {
        return name.get();
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(SimpleStringProperty genre) {
        this.genre = genre;
    }

    
    @Override
    public String toString() {
        return "Movie{name=" + name + ", genre=" + genre + '}';
    }
    
    
}
