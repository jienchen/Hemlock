package app;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Episode {
    @Id
    private Integer id;
    private Date airDate;
    private String epCast;
    private Integer epNum;
    private String epTitle;
    private Integer season;
    private String showTitle;
    private String fullSummary;
    private String shortSummary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEpCast() {
        return epCast;
    }

    public void setEpCast(String epCast) {
        this.epCast = epCast;
    }

    public Integer getEpNum() {
        return epNum;
    }

    public void setEpNum(Integer epNum) {
        this.epNum = epNum;
    }

    public String getEpTitle() {
        return epTitle;
    }

    public void setEpTitle(String epTitle) {
        this.epTitle = epTitle;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
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

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Date getAirDate() {
        return airDate;
    }
    
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }
}