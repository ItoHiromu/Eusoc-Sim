import java.util.*;
import java.io.*;
//import java.lang.*;

public class Main {
	private static final int GENE_NUM = 200;				// Maximum number of Generation (G_max)
	private static final int SEED_NUM_MAX_INI = 20;			// The initial maximum reproductive capacity (K) 
//	private static final int LIFE_SPAN_QUEEN_MIN = 10;		// Longevity of a colony (T_min)
	private static final int LIFE_SPAN_QUEEN = 13;			// Longevity of a colony (T)
	private static final int HELP_START_AGE = 1;			// Age at which a child becomes a helper (1)
	private static final int INDEPENDENCE_AGE_MIN = 4;		// Minimum time from birth to dispersal (tau_min)
	private static final double CANALIZE_INI = 3.0;			// Minimum imprinting level (I_min)
//	private static final int SEED_NUM_BASE_MIN = 5;
//	private static final int SEED_NUM_BASE_MAX = 50;
//	private static final int DELTA_SEED_NUM_BASE = 1;
//	private static final double BETA_MIN = 0.0;
//	private static final double BETA_MAX = 1.0;
//	private static final double DELTA_BETA = 0.1;
	private static final double sigma = 0.20;				// Relative strength of epigenetic inheritance (epsilon)
	private static final double sRate = 0.0;				// S curve rate (if you want to change the shape of curve showing the relationship between age (i) and work efficience (beta) in helper)
	private static int seed_num_base = 10;					// The number of eggs produced in first year (F0)
	private static int eggJ = 10;							// Cost of producing one egg (r)
	private static boolean evol = true;						// Imprinting Positive: 1, Negative: 0
	//private static double border = 0.85; 					// 
	private static double alpha = 0.5;						// Metabolic efficiency of the parents to change the resource into eggs
	private static double beta = 0.6;						// Relative work performance of a helper to that of a parent (rho)
	private static long[] before_ind = new long[LIFE_SPAN_QUEEN+1];		// Storage for all alates for single generation of the queen at all ages
	private static String filePass1 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\All_data.csv";					// Results output file path 1: All data on yearly colony status
	private static String filePass2 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\Generation_dynamics.csv";		// Results output file path 2: Colony output of each generation such as, Fig.4 b-d
	private static String filePass3 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\Evolvability_phase_WLH.csv";	// Results output file path 3: Evolvability of eusociality for phase-plane creation (whole life helper standard)
	private static String filePass4 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\Generation_phase_WLH.csv";		// Results output file path 4: Generation when evolution has stopped or evolved to eusociality (whole life helper standard)
	private static String filePass5 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\Imp_Level.csv";					// Results output file path 5: Final (maximum) implinting level
	private static String filePass6 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\Evolvability_phase_DF.csv";		// Results output file path 6: Evolvability of eusociality for phase-plane creation such as, Fig. 4a (direct fitness standard)
	private static String filePass7 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\Generation_phase_DF.csv";		// Results output file path 7: Generation when evolution has stopped or evolved to eusociality (direct fitness standard)
	private static String filePass8 = "C:\\Users\\Hiromu Ito\\Desktop\\Code\\output\\InclusiveFit.csv";				// Results output file path 8: Inclusive fitness of first brood
	
	public static void main(String[] args) {
		int lifespan_queen = LIFE_SPAN_QUEEN;
		String fileHeader1;
		String fileHeader2;
		String fileHeader3;
		String fileHeader4;
		String fileHeader5;
		String fileHeader6;
		String fileHeader7;
		String fileHeader8;
		long tempInd = 0;

		if(SEED_NUM_MAX_INI < seed_num_base) {
			System.out.println("Check condition: K >= F0");
		} else {
			// make csv file
			fileHeader1 = "Generation (G),Age of Colony (t),No. of alates (A),No. of helpers,No. of not helper,Age to dispersal (tau),No. of eggs (F),No. of dead offsprings,Imprinting level (I),The maximum reproductive capacity (K),Available resource (R),Peak available resource,Surplus resouce,Peak surplus resouce (delta_R),No. of alates in previous generation,Fitness of colony (W)";
			for(int age = 0; age < lifespan_queen; age++) {
				fileHeader1 += ("," + age + "-year-old");
			}
			fileHeader2 = "Generation (G),The maximum reproductive capacity (K),Peak available resource (R),Peak surplus resouce (delta_R),Fitness of colony (W),Inclusive fitness of the first brood,Direct fitness of the first brood";
			fileHeader3 = "Evolvability positive: 1, negative: 0 (Whole life helper standard)";
			fileHeader4 = "Generation when evolution has stopped or evolved to eusociality (whole life helper standard)";
			fileHeader5 = "Final (maximum) implinting level";
			fileHeader6 = "Evolvability positive: 1, negative: 0 (Direct fitness standard)";
			fileHeader7 = "Generation when evolution has stopped or evolved to eusociality (direct fitness standard)";
			fileHeader8 = "Inclusive fitness of first brood";
			
			outputString(filePass1, fileHeader1, 1, true, false);		// All data on yearly colony status 
			outputString(filePass2, fileHeader2, 1, true, false);		// Colony output of each generation such as, Fig.4 b-d
			outputString(filePass3, fileHeader3, 1, true, false);		// Evolvability of eusociality for phase-plane creation (whole life helper standard)
			outputString(filePass4, fileHeader4, 1, true, false);		// Generation when evolution has stopped or evolved to eusociality (whole life helper standard)
			outputString(filePass5, fileHeader5, 1, true, false);		// Final (maximum) implinting level
			outputString(filePass6, fileHeader6, 1, true, false);		// Evolvability of eusociality for phase-plane creation such as, Fig. 4a (direct fitness standard)
			outputString(filePass7, fileHeader7, 1, true, false);		// Generation when evolution has stopped or evolved to eusociality (direct fitness standard)
			outputString(filePass8, fileHeader8, 1, true, false);		// Inclusive fitness of first brood

			System.out.println("F0:" + seed_num_base + ", beta:"+ beta + ", T:" + lifespan_queen + ", epsilon:"+ sigma + "  START");
			// Initialize "before_ind"
			for(int i = 0; i <= lifespan_queen; i++) {
				if(i <= (INDEPENDENCE_AGE_MIN + 1)) {
					before_ind[i] = 0;
				} else {
					tempInd += SEED_NUM_MAX_INI;
					before_ind[i] = tempInd;
				}
			}

			// Main loop
			mainLoopGenerations(lifespan_queen);
		}
	}

	// Array index for passing values between main process
	protected enum RESULTS_LIST {
		TEMP_COLONY_NUM,		// The number of total alates after end of single colony generation loop (int)
		INCLUSIVE_FIT,			// Inclusive fitness of the first brood afrer end of single colony generation loop (int)
		CANALISE_MAX,			// Implinting levele afrer end of single colony generation loop (int)
		SELECTION;				// Selection judgement afrer end of single colony generation loop (boolean)
	}
	
	// Main process
	private static void mainLoopGenerations(int lifespan_queen) {
		final int SUCCEED = 1;
		final int FAILED = 0;
		final int INCOMPLETE = -1;
		int seedNum = seed_num_base;
		int seedNumMax = SEED_NUM_MAX_INI;		// The maximum reproductive capacity when generation is updated (K_G)
		long beforetempcolonytotalNum = 0;		// Total number of alates of the previous generation colony (Total fitness of the previous generation colony: W_G-1) with considering reproductive value
		double canalize = CANALIZE_INI;			// Initial value of implinting level when changing generation (I)
		int result_1 = INCOMPLETE;
		int result_2 = INCOMPLETE;
		boolean output_1 = false;
		boolean output_2 = false;

		// Loop of colony generations
		for(int gen = 1; gen <= GENE_NUM; gen++) {
			Queen queen = new Queen(seedNum, seedNumMax, lifespan_queen, canalize);
	
			
			Object[] results = mainLoopQueenLifeSpan(gen, queen);

			// Judgment of selection
			if(beforetempcolonytotalNum > before_ind[lifespan_queen]) {
				result_1 = FAILED;
				result_2 = FAILED;
			} else {
				// Save the total number of alates in before generation
				beforetempcolonytotalNum = before_ind[lifespan_queen];

				// Judgment of mutation
				seedNumMax = queenMutation((boolean)results[RESULTS_LIST.SELECTION.ordinal()], seedNumMax);
				
				if((long)results[RESULTS_LIST.CANALISE_MAX.ordinal()] >= queen.getLifeSpan()) {
					result_1 = SUCCEED;
				}

				if(((long)results[RESULTS_LIST.INCLUSIVE_FIT.ordinal()] - (long)results[RESULTS_LIST.TEMP_COLONY_NUM.ordinal()]) == 0) {
					result_2 = SUCCEED;
				}
			}
			// Display when the simulation is complete
			if(gen==GENE_NUM) {
				result_1 = ((result_1 == SUCCEED) ? SUCCEED : FAILED);
				result_2 = ((result_2 == SUCCEED) ? SUCCEED : FAILED);
			}
			// Results output
			if((result_1 != INCOMPLETE) && (output_1 == false)) {
				// Evolved to eusociality: 1, and did not evolve to eusociality: 0
				System.out.printf("G:%d, K:%d, CASE_1:%d\n",gen,seedNumMax, result_1);
				// Reflection in judgment file 1
				outputString(filePass3, String.valueOf(result_1), 1, true, true);
				// Reflection in judgment file 2
				outputString(filePass4, String.valueOf(gen), 1, true, true);
				// Reflection in judgment file 3
				outputString(filePass5, String.valueOf((long)results[RESULTS_LIST.CANALISE_MAX.ordinal()]), 1, true, true);
				
				output_1 = true;
			}
			if((result_2 != INCOMPLETE) && (output_2 == false)) {
				// Evolved to eusociality: 1, and did not evolve to eusociality: 0
				System.out.printf("G:%d, K:%d, CASE_2:%d\n",gen,seedNumMax, result_2);
				// Reflection in judgment file 4
				outputString(filePass6, String.valueOf(result_2), 1, true, true);
				// Reflection in judgment file 5
				outputString(filePass7, String.valueOf(gen), 1, true, true);
				// Reflection in judgment file 6
				outputString(filePass8, String.valueOf((long)results[RESULTS_LIST.INCLUSIVE_FIT.ordinal()]), 1, true, true);
				
				output_2 = true;
			}

			
			
			
			
		}  // GENE_NUM loop

	}

	// Loop for a single generation of colony
	private static Object[] mainLoopQueenLifeSpan(int gen, Queen queen) {
		Object[] results = new Object[RESULTS_LIST.values().length];
		ArrayList<Child> children = new ArrayList<Child>();
		long colonytotalNum = 0;					// The simple number of alates (without considering of reproductive value)
		long tempcolonytotalNum = 0;				// The true number of alates (with considering of reproductive value)
		long inclusivefit = 0;						// Inclusive fitness
		long directfit = 0;							// Direct fitness
		int firstbroodind = INDEPENDENCE_AGE_MIN;
		int colonyPeakborn = 0;
		long colonyPeakJ = 0;						// Peak amount of resource
		long exPeakJ = 0;							// Peak amount of surplus resouce
		int RCap = (int)(seed_num_base / 4);
		int independenceAge;						// The age when the offspring becomes alate
		int lifespanChild;
		long JFounder = (long)(seed_num_base * eggJ /alpha);	// Resource made by founder parents
		long[] current_ind = new long[queen.getLifeSpan()+1];	// The storage of total number of alates for culculation of inclusive fitness

		lifespanChild = queen.getLifeSpan();
		for(int i = 0; i <= queen.getLifeSpan(); i++) {
			current_ind[i] = before_ind[i];
		}

		// Loop of a single colony lifecycle
		for(int age = 1; age <= queen.getLifeSpan(); age++) {
			int deadNum = 0;
			int numOfHelper = 0;
			int numOfNotHelper = 0;
			long independenceNum = 0;
			double tempindependenceNum = 0;

			long totalJ = 0;				// The total available resource (R)
			long J2y = 0;					// Resouce made by 2-year-old helpes
			long J3y = 0;					// Resouce made by 3-year-old helpes
			long J4y = 0;					// Resouce made by 4-year-old helpes
			long Jover5y = 0;				// Resouce made by over 5-year-old helpes
			int numOf2yHelper = 0;			// The number of 2-year-old helpes
			int numOf3yHelper = 0;			// The number of 3-year-old helpes
			int numOf4yHelper = 0;			// The number of 4-year-old helpes
			int numOfover5yHelper = 0;		// The number of 5-year-old helpes
			long exJ=0;						// Amount of surplus resource (delta_R)
			int numOfnewborn = seed_num_base;

			// Update of offsprings' status
			for(int j = 0; j < children.size(); j++) {

				if(true == children.get(j).isEffective()) {
					
					// Offsprings become alates
					if(children.get(j).getIndependenceAge() <= children.get(j).getAge()) {
						queen.decrementChildNum(children.get(j).getAge());
						children.get(j).toInvalid();
						independenceNum++;
					}
					// Offsprings become alates with considering of the reproductive value discount
					if(children.get(j).getIndependenceAge() <= children.get(j).getAge()) {
						int RequiredTimeToBecomeAlate = children.get(j).getAge() - INDEPENDENCE_AGE_MIN;

						// (age of offspring) - (lower limit of independent age) = 0
						if(RequiredTimeToBecomeAlate == 0) {
							tempindependenceNum++;
						} 
						// 1<= (age of offspring) - (lower limit of independent age) <= 15
						else if((RequiredTimeToBecomeAlate >= 1) && (RequiredTimeToBecomeAlate <= queen.getLifeSpan())) {
							double penalty = 0.0;
							penalty = (double)(before_ind[queen.getLifeSpan()-RequiredTimeToBecomeAlate]) / (double)(before_ind[queen.getLifeSpan()]);
							tempindependenceNum = tempindependenceNum + penalty;
						}
						// (age of offspring) - (lower limit of independent age) < 0 or (age of offspring) - (lower limit of independent age) > 15
						else{
							//tempindependenceNum = tempindependenceNum;
						}

					}
					// Helper death
					else if(children.get(j).getLifeSpan() == children.get(j).getAge()) {
						queen.decrementChildNum(children.get(j).getAge());
						children.get(j).toInvalid();
						deadNum++;
					}
					// Offsprings become helper
					else if(HELP_START_AGE <= children.get(j).getAge()) {
						children.get(j).beHelper();
					}
					
					// Calculation of helpers and offsprings (not helper)
					if(true == children.get(j).isEffective()) {
						if(true == children.get(j).isHelper()) {
							numOfHelper++;
						} else {
							numOfNotHelper++;
						}
						
						// Offsprings get older
						children.get(j).incAge();
					}
					
				
				}
			}
			
			// Calculation of the number of total alates
			colonytotalNum = colonytotalNum + independenceNum;						// The simple number of alates (no considering of reproductive value)
			tempcolonytotalNum = tempcolonytotalNum + (long)tempindependenceNum;	// Calculation of the number of total alates with reproductive value (W) (cutoff after the decimal point)
//			tempcolonytotalNum = tempcolonytotalNum + round(tempindependenceNum);	// alculation of the number of total alates with reproductive value (W) (round after the decimal point)

			// Remove disabled offsprings from list
			children.removeIf(child -> true != child.isEffective());

			// Calculation of time from birth to dispersal (tau)
			queen.setCanalize(calcCanalize(queen.getAge(), queen.getCanalizeIni(), queen.getSeedNumMax()));
			independenceAge = (int)(((int)queen.getCanalize() >= INDEPENDENCE_AGE_MIN) ? (int)queen.getCanalize() : INDEPENDENCE_AGE_MIN);
			if(age == 1){
				firstbroodind = independenceAge;
			}
			
			// Count the number of helpers
			for(int j = 0; j < children.size(); j++) {
				if(true == children.get(j).isEffective()) {
					if(true == children.get(j).isHelper()) {
						// Count 2-, 3-, 4-, over 5-year old helpers
						if(children.get(j).getAge()==2) {
							numOf2yHelper++;
						}
						else if(children.get(j).getAge()==3) {
							numOf3yHelper++;
						}
						else if(children.get(j).getAge()==4) {
							numOf4yHelper++;
						}
						else if(children.get(j).getAge()>=5) {
							numOfover5yHelper++;
						}
					}
				}
			}

			// Calculation of reproductive capacity (C_t)
			if(age<3){
				RCap = (int)(queen.getSeedNumMax() * Math.tanh(age+1));
				if(RCap < seed_num_base){
					RCap = seed_num_base;
				}
			}
			else{
				RCap = queen.getSeedNumMax();
			}
			
			// Calculation of total available resource (R_t)
			J2y = (long)((beta * eggJ * seed_num_base * numOf2yHelper / (8 * alpha)) * 0.25 * (1.0 - sRate));
			J3y = (long)(beta * eggJ * seed_num_base * numOf3yHelper / (4 * alpha));
			J4y = (long)((beta * eggJ * seed_num_base * numOf4yHelper * 3 / (8 * alpha)) * (0.75 + (0.25 * sRate)));
			Jover5y = (long)(beta * eggJ* seed_num_base * numOfover5yHelper / (2 * alpha));

			totalJ = JFounder + J2y + J3y + J4y+ Jover5y;

			// Calculation of the available resource (R_t)
			if(colonyPeakJ < totalJ) colonyPeakJ = totalJ;
			
			// Calculation of the number of eggs (F_t)
			if(colonyPeakborn < numOfnewborn) colonyPeakborn = numOfnewborn;
			
			// Reproduction
			queen.shiftChildNum(age);
			queen.calcSeedNum(totalJ, eggJ, alpha, RCap);
			numOfnewborn = queen.getSeedNum();
			queen.setChildNum(numOfnewborn);
			for(int j = 0; j < numOfnewborn; j++) {
				children.add(new Child(independenceAge, lifespanChild));
			}

			// Calclulation of surplus resource (delta_R)
			exJ = totalJ - (long)(numOfnewborn * eggJ);


			if(exPeakJ < exJ) {
				exPeakJ = exJ;
			}
			current_ind[age] = tempcolonytotalNum;	//For calculation of inclusive fitness

			// csv output (every year)
			String out = (String.valueOf(gen) + "," + String.valueOf(queen.getAge()) + ",");
			out += (String.valueOf(independenceNum) + "," + String.valueOf(numOfHelper) + "," + String.valueOf(numOfNotHelper) + ",");
			out += (String.valueOf(independenceAge) + "," + String.valueOf(numOfnewborn) + "," + String.valueOf(deadNum) + ",");
			out += (String.valueOf(queen.getCanalize()) + "," + String.valueOf(queen.getSeedNumMax()) + ",");
			out += (String.valueOf(totalJ) + "," + String.valueOf(colonyPeakJ) + "," + String.valueOf(exJ) + "," + String.valueOf(exPeakJ) + ",");
			out += (String.valueOf(before_ind[queen.getAge()]) + "," + String.valueOf(tempcolonytotalNum) + ",");
			
			for(int i = 0; i < queen.getLifeSpan(); i++) {
				if(i < age)  {
					out += String.valueOf(queen.getChildNum(i));
				}
				out += (",");
			}
			outputString(filePass1, out, 1, true, true);

			if(queen.getAge() == queen.getLifeSpan()) {
				int inddiff = firstbroodind - INDEPENDENCE_AGE_MIN;

				if(inddiff == 0) {
					inclusivefit = 2 * tempcolonytotalNum;
				} else if ((inddiff > 0) && (inddiff < (queen.getLifeSpan() - 1))) {
					inclusivefit = tempcolonytotalNum + current_ind[age-inddiff-1];
				} else {
					inclusivefit = tempcolonytotalNum;
				}

				// Calculation of direct fitness
				directfit = inclusivefit - tempcolonytotalNum;

				// csv output (for a single generation)
				String out2 = (String.valueOf(gen) + "," + String.valueOf(queen.getSeedNumMax()) + ",");
				out2 += (String.valueOf(colonyPeakJ) + "," + String.valueOf(exPeakJ) + ",");
				out2 += (String.valueOf(tempcolonytotalNum) + "," + String.valueOf(inclusivefit) + "," + String.valueOf(directfit) + ",");
				outputString(filePass2, out2, 1, true, true);
			}

			before_ind[queen.getAge()] = tempcolonytotalNum;
			
			// The colony gets older
			queen.incAge();
			

		}  // Single generation loop

		// Organize the results
		results[RESULTS_LIST.TEMP_COLONY_NUM.ordinal()] = (long)tempcolonytotalNum;		// The total number of alates (W)
		results[RESULTS_LIST.INCLUSIVE_FIT.ordinal()] = (long)inclusivefit;				// Inclusive fitness
		results[RESULTS_LIST.CANALISE_MAX.ordinal()] = (long)queen.getCanalizeMax();	// Maximum implinting level (I)
		results[RESULTS_LIST.SELECTION.ordinal()] = (boolean)(exPeakJ/eggJ >= 1);		// Judgment of mutation and selection
		
		return results;
	}   // mainLoopQueenLifeSpan
	
	// Calculation of implinting level
	private static double calcCanalize(int queenAge, double canalize_ini, int seedNumMax) {
		double canalize = canalize_ini;

		if(evol==false){
			canalize = 0.0;
		} else {
			if(queenAge <= 3) {
				canalize = canalize_ini + (int)(sigma * seedNumMax * 4 / (Math.pow(Math.exp(queenAge)+Math.exp(-1*queenAge), 2)));
			} else {
				canalize = canalize_ini;
			}
		}

		return canalize;
	}


	// Mutation
	private static int queenMutation(boolean selection, int seedNumMax) {
		int tempseedNumMax = SEED_NUM_MAX_INI;
		// Selection: if positive K=K+1
		if(selection == true) {
			tempseedNumMax = seedNumMax + 1;
			if(tempseedNumMax < SEED_NUM_MAX_INI){
				seedNumMax = SEED_NUM_MAX_INI;
			}else{
				seedNumMax = tempseedNumMax;
			}
			//Selection: if negative K=K
		}else{
			tempseedNumMax = seedNumMax;
			if(tempseedNumMax < SEED_NUM_MAX_INI){
				seedNumMax = SEED_NUM_MAX_INI;
			}else{
				seedNumMax = tempseedNumMax;
			}
		}

		return seedNumMax;
	}

	private static long round(double val) {
		long roundVal = 0;
		double tempVal;

		tempVal = val * 10.0;
		roundVal = (long)tempVal + 3;
		roundVal /= 10;

		return roundVal;
	}

	/**
	 * Write a string to the specified file
	 * @param filePass	File name (specify with full path)
	 * @param data		The string to write to file
	 * @param dataNum	The number of strings to wite to the file
	 * @param indention	true: line break positive / false: no line breaks
	 * @param add		true: Additional writing mode / false: New mode
	 */
	private static void outputString(String filePass, String data, int dataNum, boolean indention, boolean add) {
		try {
			FileWriter fw = new FileWriter(filePass, add);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			
			// Write the specified string to the file
			// No line breaks
			if(false == indention) {
				pw.print(data);
			
			// With line breaks
			} else {
				pw.println(data);
			}
			
			pw.close();
		} catch(IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

}


