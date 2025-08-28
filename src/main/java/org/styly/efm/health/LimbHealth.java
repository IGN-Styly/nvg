package org.styly.efm.health;

public class LimbHealth {
    private final Limb head     = new Limb(35);
    private final Limb thorax   = new Limb(85);
    private final Limb stomach  = new Limb(70);
    private final Limb leftArm  = new Limb(60);
    private final Limb rightArm = new Limb(60);
    private final Limb leftLeg  = new Limb(65);
    private final Limb rightLeg = new Limb(65);

    // Example getters
    public Limb getHead() { return head; }
    public Limb getThorax() { return thorax; }
    public Limb getStomach() { return stomach; }
    public Limb getLeftArm() { return leftArm; }
    public Limb getRightArm() { return rightArm; }
    public Limb getLeftLeg() { return leftLeg; }
    public Limb getRightLeg() { return rightLeg; }
}


