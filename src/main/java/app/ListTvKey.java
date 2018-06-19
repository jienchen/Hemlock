package app;

import java.io.Serializable;

public class ListTvKey implements Serializable {
    private String username;
    private Integer tvId;

    public ListTvKey() {
	}
 
	public ListTvKey(String username, int tvId) {
		this.username = username;
		this.tvId = tvId;
	}
 
	public Integer getTvId() {
		return tvId;
	}
 
	public String getUsername() {
		return username;
	}
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListTvKey that = (ListTvKey) o;

        if (username!=null? !username.equals(that.username) : that.username!=null) return false;
        if (tvId!=null? !tvId.equals(that.tvId) : that.tvId!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (tvId!=null? tvId.hashCode() : 0);
        result = 31 * result + (username!=null? username.hashCode() : 0);
        return result;
    }   
}