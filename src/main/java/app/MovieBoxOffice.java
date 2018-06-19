package app;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MovieBoxOffice {
    @Id
    private Integer movieId;
    private Boolean bool;
    private Integer weeklyBudget;

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

    public Integer getWeeklyBudget() {
        return weeklyBudget;
    }

    public void setWeeklyBudget(Integer weeklyBudget) {
        this.weeklyBudget = weeklyBudget;
    }
}