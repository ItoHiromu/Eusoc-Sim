// Parameter definition class for offsprings derived from termites.java
public class Child extends Termites {
    private boolean helper;          // Helper flag
    private int independenceAge;     // Age become alates (tau)
    private boolean effectiveness;   // Effectiveness flag (already become alate or already dead: false / effective offspring: true)

    // Constructor to generate a offspring
    public Child(int independenceAge, int lifeSpan) {
        this.setAge(0);
        this.setLifeSpan(lifeSpan);
        this.helper = false;
        this.independenceAge = independenceAge;
        this.effectiveness = true;
    }

    // Judge helper or not
    public boolean isHelper() {
        return this.helper;
    }

    // Set as helper
    public void beHelper() {
        this.helper = true;
    }

    // Unset helper
    public void beNotHelper() {
        this.helper = false;
    }

    // Get age become alate
    public int getIndependenceAge() {
        return this.independenceAge;
    }

    // Get effectiveness
    public boolean isEffective() {
        return this.effectiveness;
    }

    // Make ineffective
    public void toInvalid() {
        this.effectiveness = false;
    }
}