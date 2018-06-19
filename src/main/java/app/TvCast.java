package app;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(TvKey.class)
public class TvCast {
    @Id
    private Integer tvId;
    @Id
    private Integer personId;
    private String role;

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getRole() {
        return role;
    }

    private void setRole(String role) {
        this.role = role;
    }
}