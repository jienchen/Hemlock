package app;

import java.io.Serializable;

public class ShowSeasonEpisodeKey implements Serializable {
    private Integer epId;
    private Integer tvId;
    private Integer seasonNum;

    public ShowSeasonEpisodeKey() {
	}
 
	public ShowSeasonEpisodeKey(int epId, int tvId, int seasonNum) {
		this.epId = epId;
		this.tvId = tvId;
        this.seasonNum = seasonNum;
	}
 
	public Integer getTvId() {
		return tvId;
	}

	public Integer getEpId() {
		return epId;
	}

    public Integer getSeasonNum() {
        return seasonNum;
    }
 
    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowSeasonEpisodeKey that = (ShowSeasonEpisodeKey) o;

        if (epId!=null? !epId.equals(that.epId) : that.epId!=null) return false;
        if (tvId!=null? !tvId.equals(that.tvId) : that.tvId!=null) return false;
        if (seasonNum!=null? !seasonNum.equals(that.seasonNum) : that.seasonNum!=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (tvId!=null? tvId.hashCode() : 0);
        result = 31 * result + (epId!=null? epId.hashCode() : 0);
        result = 31 * result + (seasonNum!=null? seasonNum.hashCode() : 0);
        return result;
    }   
}