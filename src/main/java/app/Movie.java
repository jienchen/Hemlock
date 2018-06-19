package app;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Movie {
    @Id
    private Integer id;
    private String title;
    private Integer boxOffice;
    private String director;
    private String releaseDate;
    private String description;
    private String shortDesc;
    private String runtime;
    private Integer avgGenRating;
    private Integer avgCriticRating;
	private String mpaaRating;
    private String cast;
    private String writers;
    private String genres;
	private Boolean watchBool;
	private Boolean blackBool;
	private Integer avgRating;
	private Boolean oscarBool;

	public Integer getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(Integer avgRating) {
		this.avgRating = avgRating;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBoxOffice() {
		return boxOffice;
	}

	public void setBoxOffice(Integer boxOffice) {
		this.boxOffice = boxOffice;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
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

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getMpaaRating() {
		return mpaaRating;
	}

	public void setMpaaRating(String mpaaRating) {
		this.mpaaRating = mpaaRating;
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

	public Boolean getOscarBool() {
		return oscarBool;
	}

	public void setOscarBool(Boolean bool) {
		this.oscarBool = bool;
	}

}