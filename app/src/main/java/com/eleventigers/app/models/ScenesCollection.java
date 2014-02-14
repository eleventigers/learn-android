package com.eleventigers.app.models;

import java.util.List;

/**
 * Created by eleventigers on 14/02/14.
 */

public class ScenesCollection extends CollectionResponseContainer {
    private List<Scene> scenes;
    public List<Scene> getModels () {
        return this.scenes;
    }
}
