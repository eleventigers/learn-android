package com.eleventigers.app.models;


import android.net.Uri;
import java.util.Date;
import java.util.Map;

/**
 * Created by eleventigers on 14/02/14.
 */
public class Scene {

    String app_link;
    String caption;
    Date captured_at;
    Integer comments_count;
    Boolean staff_pick;
    Integer flash_level;
    Integer likes_count;
    Integer orientation;
    Integer safety_level;
    String shared_at;
    String short_code;
    String state;
    Date deleted_at;
    Date created_at;
    Date updated_at;
    Map<String, String> poster;

    public String getImageUrl() {
        return poster.get("iphone");
    }

    @Override
    public String toString() {
        return this.caption;
    }

}
