package app;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(FollowKey.class)
public class Follow {
    @Id
    private String username;
    @Id
    private String follow;
    private Boolean followBool;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public Boolean getFollowBool() {
        return followBool;
    }

    public void setFollowBool(Boolean followBool) {
        this.followBool = followBool;
    }
}