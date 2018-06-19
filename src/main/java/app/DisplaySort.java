package app;

import java.util.List;

public class DisplaySort {
    private List<Movie> displayM;
    private List<TvShow> displayT;
    private List<Person> displayP;
    private String sortBy;

    public List<Movie> getDisplayM() {
        return displayM;
    }

    public void setDisplayM(List<Movie> displayM) {
        this.displayM = displayM;
    }

    public List<TvShow> getDisplayT() {
        return displayT;
    }

    public void setDisplayT(List<TvShow> displayT) {
        this.displayT = displayT;
    }

    public List<Person> getDisplayP() {
        return displayP;
    }

    public void setDisplayP(List<Person> displayP) {
        this.displayP = displayP;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}