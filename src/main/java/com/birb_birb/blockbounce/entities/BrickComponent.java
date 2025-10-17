package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;


public class BrickComponent extends Component {

    public void destroy() {
        entity.removeFromWorld();
    }
}
