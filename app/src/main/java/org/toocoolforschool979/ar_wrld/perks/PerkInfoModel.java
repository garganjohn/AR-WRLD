package org.toocoolforschool979.ar_wrld.perks;

import android.graphics.drawable.Drawable;

public class PerkInfoModel {
    private String perkName;
    private String perkDescription;
    private Drawable perkImage;
    private String whichPerkFromInterface;

    public PerkInfoModel(String perkName, String perkDescription, Drawable perkImage, String whichPerkFromInterface){
        this.perkName = perkName;
        this.perkDescription = perkDescription;
        this.perkImage = perkImage;
        this.whichPerkFromInterface = whichPerkFromInterface;
    }

    public String getPerkName() {
        return perkName;
    }

    public String getPerkDescription() {
        return perkDescription;
    }

    public Drawable getPerkImage() {
        return perkImage;
    }

    public String getWhichPerkFromInterface() {
        return whichPerkFromInterface;
    }
}
