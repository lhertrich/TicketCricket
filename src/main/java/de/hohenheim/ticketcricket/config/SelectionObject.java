package de.hohenheim.ticketcricket.config;

public class SelectionObject {

    private String searchString;

    private String filterString;

    private String sortString;

    public SelectionObject(String searchString, String filterString, String sortString){
        this.searchString = searchString;
        this.filterString = filterString;
        this.sortString = sortString;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String getSortString() {
        return sortString;
    }

    public void setSortString(String sortString) {
        this.sortString = sortString;
    }
}
