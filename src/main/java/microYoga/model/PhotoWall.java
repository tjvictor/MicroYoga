package microYoga.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoWall {

    private String id;
    private String name;
    private String date;

    private List<Photo> photoList;

    public PhotoWall(){
        photoList = new ArrayList<Photo>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}