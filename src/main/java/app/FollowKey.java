package app;

import java.io.Serializable;

public class FollowKey implements Serializable {
    private String username;
    private String follow;

    public FollowKey() {
	}
 
	public FollowKey(String username, String follow) {
		this.username = username;
		this.follow = follow;
	}
 
	public String getFollow() {
		return follow;
	}
 
	public String getUsername() {
		return username;
	}
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FollowKey that = (FollowKey) o;

        if (username!=null? !username.equals(that.username) : that.username!=null) return false;
        if (follow!=null? !follow.equals(that.follow) : that.follow!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (follow!=null? follow.hashCode() : 0);
        result = 31 * result + (username!=null? username.hashCode() : 0);
        return result;
    } 
}