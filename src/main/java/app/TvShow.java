package app;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TvShow {
    @Id
    private Integer id;
    private String cast;
    private String creators;
    private Integer episodes;
    private String fullSummary;
    private String shortSummary;
    private String genres;
    private String rated;
    private Date releaseDate;
    private Integer seasons;
    private String title;
    private Integer avgGenRating;
    private Integer avgCriticRating;
    private Integer avgRating;
    private Boolean watchBool;
    private Boolean blackBool;

    public Integer getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(Integer avgRating) {
		this.avgRating = avgRating;
	}

    public Integer getAvgGenRating() {
        return avgGenRating;
    }

    public void setAvgGenRating(Integer avgGenRating) {
        this.avgGenRating = avgGenRating;
    }

    public Integer getAvgCriticRating() {
        return avgCriticRating;
    }

    public void setAvgCriticRating(Integer avgCriticRating) {
        this.avgCriticRating = avgCriticRating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public Integer getSeasons() {
        return seasons;
    }

    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCreators() {
        return creators;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public String getFullSummary() {
        return fullSummary;
    }

    public void setFullSummary(String fullSummary) {
        this.fullSummary = fullSummary;
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getWatchBool() {
		return watchBool;
	}

	public void setWatchBool(Boolean bool) {
		this.watchBool = bool;
	}

	public Boolean getBlackBool() {
		return blackBool;
	}

	public void setBlackBool(Boolean bool) {
		this.blackBool = bool;
	}
}