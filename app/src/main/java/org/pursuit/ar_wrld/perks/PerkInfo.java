package org.pursuit.ar_wrld.perks;

import android.graphics.drawable.Drawable;

import org.pursuit.ar_wrld.GameInformation;

public class PerkInfo {
    private String perkName;
    private String perkDescription;
    private Drawable perkImage;
    private String whichPerkFromInterface;

    public PerkInfo(String perkName, String perkDescription, Drawable perkImage, String whichPerkFromInterface){
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
