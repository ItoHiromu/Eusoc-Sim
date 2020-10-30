// Parameter definition class of queen (colony) derived from termites.java
public class Queen extends Termites {
    private int seedNumBase;        // Basic number of eggs reproduced by founder parents (F0)
    private int seedNumMax;         // The maximum reproductive capacity (K)
    private int seedNum;            // The number of eggs reproduced (F) with helper contribution
    private double canalize;        // Imprinting level (I)
    private double canalize_ini;    // Initial level of imprinting (I_ini)
    private int canalize_max;       // The maximum imprinting level
    private int[] childNum;         // The number of offsprings (for each age)

    // Constructor to generate a queen (colony)
    public Queen(int seedNumBase, int seedNumMax, int lifeSpan, double canalize_ini) {
        this.setAge(1);
        this.setLifeSpan(lifeSpan);
        this.seedNumBase = seedNumBase;
        this.seedNumMax = seedNumMax;
        this.seedNum = seedNumBase;
        this.canalize = canalize_ini;
        this.canalize_ini = canalize_ini;
        this.canalize_max = (int)canalize_ini;
        this.childNum = new int[lifeSpan];
    }

    // Get basic number of eggs reproduced (F0)
    public int getSeedNumBase() {
        return this.seedNumBase;
    }

    // Set basic number of eggs reproduced (F0)
     public void setSeedNumBase(int seedNum) {
        this.seedNumBase = seedNum;
    }

    // Get the maximum reproductive capacity (K)
    public int getSeedNumMax() {
        return this.seedNumMax;
    }

    // Set the maximum reproductive capacity (K)
    public void setSeedNumMax(int seedNum) {
        this.seedNumMax = seedNum;
    }

    // Get the initial imprinting level (I_ini)
    public double getCanalizeIni() {
        return this.canalize_ini;
    }

    // Set the initial imprinting level (I_ini)
    public void setCanalizeIni(double canalize_ini) {
        this.canalize_ini = canalize_ini;
    }

    // Get the imprinting level (I)
    public double getCanalize() {
        return this.canalize;
    }

    // Set the imprinting level (I)
    public void setCanalize(double canalize) {
        this.canalize = canalize;
        if(this.canalize_max < canalize) {
            this.canalize_max = (int)canalize;
        }
    }

    // Calculation of the number of eggs reproduced (F) with considering helper contribution
    public void calcSeedNum(long totalJ, int eggJ, double alpha, int RCap) {
        int tempSeedNum = 0;
        tempSeedNum = (int)(alpha * totalJ / eggJ);
        if(tempSeedNum <= this.seedNumBase){
            tempSeedNum = this.seedNumBase;
        }else if(tempSeedNum >= RCap){
            tempSeedNum = RCap;
        }
        
        if(tempSeedNum >= this.seedNumMax){
            tempSeedNum = this.seedNumMax;
        }
        this.seedNum = tempSeedNum;      
    }
    
    // Get the number of eggs reproduced (F) with considering helper contribution
    public int getSeedNum() {
        return this.seedNum;
    }
    
    // Get the maximum imprinting level
    public int getCanalizeMax() {
        return this.canalize_max;
    }

    // Get the number of offsprings of the specified age
    public int getChildNum(int age) {
        return this.childNum[age];
    }

    // Set the number of newborn offsprings as the number of 0-year-old for calculate the family composition of the colony
    public int setChildNum(int num) {
        return this.childNum[0] = num;
    }

    // Increase the number of offsprings of the specified age
    public int incrementChildNum(int age) {
        return this.childNum[age]++;
    }

    // Dicrease the number of offsprings of the specified age
    public int decrementChildNum(int age) {
        return this.childNum[age]--;
    }

   // Offsprings get old
    public void shiftChildNum(int age) {
        for(int i = (age - 1); i > 0; i--) {
            this.childNum[i] = this.childNum[i-1];
        }
    }

}