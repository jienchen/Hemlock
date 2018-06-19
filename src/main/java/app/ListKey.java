package app;

import java.io.Serializable;

public class ListKey implements Serializable {
    private String username;
    private Integer movieId;

    public ListKey() {
	}
 
	public ListKey(String username, int movieId) {
		this.username = username;
		this.movieId = movieId;
	}
 
	public Integer getMovieId() {
		return movieId;
	}
 
	public String getUsername() {
		return username;
	}
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListKey that = (ListKey) o;

        if (username!=null? !username.equals(that.username) : that.username!=null) return false;
        if (movieId!=null? !movieId.equals(that.movieId) : that.movieId!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (movieId!=null? movieId.hashCode() : 0);
        result = 31 * result + (username!=null? username.hashCode() : 0);
        return result;
    }   
}