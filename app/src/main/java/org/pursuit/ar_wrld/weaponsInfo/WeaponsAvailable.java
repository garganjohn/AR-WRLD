package org.pursuit.ar_wrld.weaponsInfo;

public class WeaponsAvailable {
    private Weapon weakWeapon;
    private Weapon medWeapon;

    public WeaponsAvailable(int medAmmo) {
        weakWeapon = new Weapon(1, -1);
        medWeapon = new Weapon(3, medAmmo);
    }

    public int getWeakWeaponDamage(){
        return weakWeapon.getDamageDone();
    }

    public int getMedWeaponAmmo(){
        return medWeapon.getAmmo();
    }

    public int getMedWeaponDamage(){
        return medWeapon.getDamageDone();
    }

    public void setMedWeaponAmmo(int ammo){
        medWeapon.setAmmo(ammo);
    }

    public void setMedWeaponDamage(int damage){medWeapon.setDamageDone(damage);}
}

