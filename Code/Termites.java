// Abstract class and basic parameters of ancestor termite
public class Termites {
    private int age;            // age
    private int lifeSpan;       // colony lifespan

    // Get age
    public int getAge() {
        return this.age;
    }

    // Get age
    public void incAge() {
        this.age++;
    }

    // Set age
    public void setAge(int age) {
        this.age = age;
    }

    // Get colony lifespan
    public int getLifeSpan(){
        return this.lifeSpan;
    }

    // Set colony lifespan
    public void setLifeSpan(int lifeSpan){
        this.lifeSpan = lifeSpan;
    }

}