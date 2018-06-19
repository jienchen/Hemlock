package app;

import java.io.Serializable;

public class EpisodeKey implements Serializable {
    private Integer epId;
    private Integer tvId;

    public EpisodeKey() {
	}
 
	public EpisodeKey(int epId, int tvId) {
		this.epId = epId;
		this.tvId = tvId;
	}
 
	public Integer getTvId() {
		return tvId;
	}

	public Integer getEpId() {
		return epId;
	}
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EpisodeKey that = (EpisodeKey) o;

        if (epId!=null? !epId.equals(that.epId) : that.epId!=null) return false;
        if (tvId!=null? !tvId.equals(that.tvId) : that.tvId!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (tvId!=null? tvId.hashCode() : 0);
        result = 31 * result + (epId!=null? epId.hashCode() : 0);
        return result;
    }   
}