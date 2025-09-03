package org.styly.efm.health;

public class Limb {
    private final int maxHealth;
    private int currentHealth;

    public Limb(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void damage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public boolean isBlackout() {
        return currentHealth == 0;
    }
}
