package org.pursuit.ar_wrld.weaponsInfo;

public class WeaponsAvailable {
    private Weapon weakWeapon;
    private Weapon medWeapon;

    public void weakWeaponSetup(){
        weakWeapon = new Weapon(1, -1);
    }

    public void medWeaponSetup(int startAmmo){
        medWeapon = new Weapon(3, startAmmo);
    }

    public Weapon getWeakWeapon() {
        return weakWeapon;
    }

    public Weapon getMedWeapon() {
        return medWeapon;
    }
}
