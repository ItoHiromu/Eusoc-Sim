# Eusoc-Sim
Set of programs for evolution simulation of eusociality drived by genomic imprinting.
This simulation demonstrates the life cycle  of colony of ancestor termite.

## Main files

* Main.java: Main file of the simulation.
* Termites.java: Abstract class and basic parameters of ancestor termite.
* Queen.java: Parameter definition class for queen (colony) derived from termites.java.
* Child.java: Parameter definition class for offsprings derived from termites.java.


## System requirements
### Required software
* OS Windows 10 
* The main codes of the simulations were implemented in Java, Java version "12.0.1" and Java (TM) SE Runtime Environment 12
* For running of simulations, JDK (Java SE Development Kit) is needs to be installed (https://www.oracle.com/jp/java/technologies/javase-downloads.html).
* For compiling, path setting is needed (https://docs.oracle.com/javase/tutorial/essential/environment/paths.html).

### Versions the software has been tested on
* OS: Windows 10 Pro Ver.1903
* CPU: Intel Core i9-7900X CPU 3.30GHz


## Demo
### Instructions to run the simulation
* To compile, enter the command below at the command prompt, 
  ```bash
  $ javac Main.java
  ```
  then we get 5 class files as below
 1. Main.class
 2. Main$RESULTS_LIST.class
 3. Termites.class
 4. Queen.class
 5. Child.class

* To run the simulation, enter the command below at the command prompt,
  ```bash
  $ Java Main
  ```
  then we get 8 csv files as below,
 1. All_data.csv ()
 All data on yearly colony status. 
 2. Colony output of each generation such as, Fig.4 b-d.
 3. Evolvability of eusociality for phase-plane creation (whole life helper standard).
 4. Generation when evolution has stopped or evolved to eusociality (whole life helper standard).
 5. Final (maximum) implinting level
 6. Evolvability of eusociality for phase-plane creation such as, Fig. 4a (direct fitness standard).
 7. Generation when evolution has stopped or evolved to eusociality (direct fitness standard).
 8. Inclusive fitness of first brood

  
### Example output on console
* [Example 1] Output on console is displayed as below,
(Major parameter set is F0=10; β=0.6; T=13; ε=0.2)
  ```bash
  $ Java Main
  F0:10, beta:0.6, T:13, epsilon:0.2  START 
  G:65, K:85, CASE_2:1
  G:101, K:121, CASE_1:1
  ```
  This means that, the direct fitness of the first brood becomes zero in the 65th generation when K=85, and the first brood become whole life helper in the 101th generation when K=121.
  In CASE_2, evolvability to eusociality is judged by the direct fitness of the first brood.
  In CASE_1, evolvability to eusociality is judged by the appearance whole life helper.
  When CASE is displayed as "1", it means that the simulation reach to eusociality.
  Note that, if colony fitness does not decrease from the previous generation, the simulation continues up to 200 generations.
  
 * [Example 2] Output on console is displayed as below,
(Major parameter set is F0=10; β=0.1; T=13; ε=0.2)
   ```bash
   $ Java Main
   F0:10, beta:0.1, T:13, epsilon:0.2  START 
   G:41, K:60, CASE_2:0
   G:41, K:60, CASE_1:0
   ```
   In this case, evolution is stopped at 41th generation when K=60 because of decrease in colony fitness due to small beta (work performance of helper).
   Therefore, CASE_1 and CASE_2 are both 0 (did not reach to eusociality).
   Note that, even if evolution is stopped, 
  
