package com.jit.appcloud.ui.adapter.multi;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;

/**
 * @author zxl on 2018/8/2.
 *         discription:
 */

public class TalkPicItem implements MultiItemEntity {
    public static final int ADD_PIC = 1;
    public static final int SHOW_PIC  = 2;

    private File imageFile;
    private int itemType;

    public TalkPicItem() {
        this.itemType = ADD_PIC;
    }

    public TalkPicItem(File imageFile) {
        this.imageFile = imageFile;
        this.itemType = SHOW_PIC;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
