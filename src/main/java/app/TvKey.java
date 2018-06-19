package app;

import java.io.Serializable;

public class TvKey implements Serializable {
    private Integer personId;
    private Integer tvId;

    public TvKey() {
	}
 
	public TvKey(int personId, int tvId) {
		this.personId = personId;
		this.tvId = tvId;
	}
 
	public Integer getTvId() {
		return tvId;
	}

	public Integer getPersonId() {
		return personId;
	}
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TvKey that = (TvKey) o;

        if (personId!=null? !personId.equals(that.personId) : that.personId!=null) return false;
        if (tvId!=null? !tvId.equals(that.tvId) : that.tvId!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (tvId!=null? tvId.hashCode() : 0);
        result = 31 * result + (personId!=null? personId.hashCode() : 0);
        return result;
    }   
}