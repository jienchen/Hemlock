package app;

import java.io.Serializable;

public class MovieKey implements Serializable {
    private Integer personId;
    private Integer movieId;

    public MovieKey() {
	}
 
	public MovieKey(int personId, int movieId) {
		this.personId = personId;
		this.movieId = movieId;
	}
 
	public Integer getMovieId() {
		return movieId;
	}

	public Integer getPersonId() {
		return personId;
	}
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieKey that = (MovieKey) o;

        if (personId!=null? !personId.equals(that.personId) : that.personId!=null) return false;
        if (movieId!=null? !movieId.equals(that.movieId) : that.movieId!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (movieId!=null? movieId.hashCode() : 0);
        result = 31 * result + (personId!=null? personId.hashCode() : 0);
        return result;
    }   
}