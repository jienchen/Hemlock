package app;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(ListTvKey.class)
public class WatchlistTv {
    @Id
    private String username;
    @Id
    private Integer tvId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }
}