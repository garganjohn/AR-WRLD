package org.pursuit.ar_wrld.weaponsInfo;

class Weapon {
    private int damageDone;
    private int ammo;

    Weapon(int damageDone, int ammo) {
        this.damageDone = damageDone;
        this.ammo = ammo;
    }

    public int getDamageDone() {
        return damageDone;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setDamageDone(int damageDone) {
        this.damageDone = damageDone;
    }
}

