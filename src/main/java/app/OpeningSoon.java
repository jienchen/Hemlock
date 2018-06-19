package app;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OpeningSoon {
    @Id
    private Integer movieId;
    private Boolean bool;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Boolean getBool() {
        return bool;
    }
    
    public void setBool(Boolean bool) {
        this.bool = bool;
    }
}