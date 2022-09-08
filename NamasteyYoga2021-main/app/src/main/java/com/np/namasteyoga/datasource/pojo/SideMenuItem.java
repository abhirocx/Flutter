package com.np.namasteyoga.datasource.pojo;

public class SideMenuItem {

    int itemName;
    int drawable;

    public SideMenuItem(int itemName, int drawable) {
        this.itemName = itemName;
        this.drawable = drawable;
    }

    public int getItemName() {
        return itemName;
    }

    public void setItemName(int itemName) {
        this.itemName = itemName;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
