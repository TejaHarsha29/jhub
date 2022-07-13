package com.appsByHarsha.jhub.Model;

public class PlacementModel {
    private String txt;
    private String link,postedBy;
    private String placement_id;



    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPlacement_id() {
        return placement_id;
    }

    public void setPlacement_id(String placement_id) {
        this.placement_id = placement_id;
    }



    public PlacementModel(String txt, String link) {
        this.txt = txt;
        this.link = link;
    }

    public PlacementModel() {
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
