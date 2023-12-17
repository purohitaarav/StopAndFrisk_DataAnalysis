import java.util.ArrayList;


/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line

        
       while(StdIn.hasNextLine()){
        String[] recordEntries = StdIn.readLine().split(",");

        int year = Integer.parseInt(recordEntries[0]);
        
        String description = recordEntries[2];
        
        String gender = recordEntries[52];
        
        String race = recordEntries[66];
        
        String location = recordEntries[71];

        Boolean arrested = recordEntries[13].equals("Y");

        Boolean frisked = recordEntries[16].equals("Y");

        SFRecord rec = new SFRecord(description, arrested, frisked, gender, race, location);
        
        boolean contains = false;
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                database.get(i).addRecord(rec);
                contains = true;
            }
        }
        if(contains == false){
            SFYear newYear = new SFYear(year);
            newYear.addRecord(rec);
            database.add(newYear);
        }
    }
        }

    

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

     /*
    public ArrayList<SFRecord> populationStopped2 ( int year, String race ) {
        ArrayList<SFRecord> population = new ArrayList<>();
       Stream<SFYear> yearStream = database.stream()
        .filter(database -> database.getcurrentYear()==year);
      
        Stream<SFRecord> recordStream = yearStream.stream()

       .filter(yearStream-> yearStream.getRecordsForYear());
        

        return population;
    }
     */

    public ArrayList<SFRecord> populationStopped( int year, String race ) {
         ArrayList<SFRecord> population = new ArrayList<>();
         for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                   ArrayList<SFRecord> currentYearRecords = database.get(i).getRecordsForYear();  
                 for(int j = 0; j < currentYearRecords.size(); j++){
                    if(currentYearRecords.get(j).getRace().equals(race)){
                        population.add(currentYearRecords.get(j));
                    }
                
            }
            break;
        }else{
            continue;
         }
        }
        return population;
    }


    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        double frisked = 0;
        double arrested = 0;
        double[] percentage = new double[2];
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                ArrayList<SFRecord> currentYearRecords = database.get(i).getRecordsForYear();  
                 for(int j = 0; j < currentYearRecords.size(); j++){
                    if(currentYearRecords.get(j).getFrisked()){
                        frisked++;
                        percentage[0] = 100*(frisked/currentYearRecords.size());
                    }
                    if(currentYearRecords.get(j).getArrested()){
                        arrested++;
                        percentage[1] = 100*(arrested/currentYearRecords.size());
                    }
                }
        
        }
    }
    
    return percentage; 
}



    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        double black = 0;
        double white = 0;
        double blackMale = 0;
        double blackFemale = 0;
        double whiteMale = 0;
        double whiteFemale = 0;
        double[][] genderPercentages = new double[2][3];
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                ArrayList<SFRecord> currentYearRecords = database.get(i).getRecordsForYear();  
                 for(int j = 0; j < currentYearRecords.size(); j++){
                    if(currentYearRecords.get(j).getRace().equals("B")){
                        black++;
                        if(currentYearRecords.get(j).getGender().equals("M"))
                        blackMale++;
                        if(currentYearRecords.get(j).getGender().equals("F"))
                        blackFemale++;
                    }
                    if(currentYearRecords.get(j).getRace().equals("W")){
                        white++;
                        if(currentYearRecords.get(j).getGender().equals("M"))
                        whiteMale++;
                        if(currentYearRecords.get(j).getGender().equals("F"))
                        whiteFemale++;
                    }
                }
            }
        }
        genderPercentages[0][0] = (blackFemale/black)*0.5*100;
        genderPercentages[0][1] = (whiteFemale/white)*0.5*100;
        genderPercentages[0][2] = genderPercentages[0][0] + genderPercentages[0][1];
        genderPercentages[1][0] = (blackMale/black)*0.5*100;
        genderPercentages[1][1] = (whiteMale/white)*0.5*100;
        genderPercentages[1][2] = genderPercentages[1][0] + genderPercentages[1][1];
        return genderPercentages; 
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        double year1Count = 0.0;
        double year2Count = 0.0;
        
        if(year1>year2){
            int temp = year1;
            year1 = year2;
            year2=temp;
        }
        double year1Size=0.0;
double year2Size=0.0;
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year1){
                   ArrayList<SFRecord> year1Records = database.get(i).getRecordsForYear(); 
                   year1Size= year1Records.size();
                   for(int j = 0; j<year1Records.size(); j++){

                    if(year1Records.get(j).getDescription().indexOf(crimeDescription)>-1){
                        System.out.println("1 " + year1Records.get(j).getDescription());
                        year1Count++;
                    }
                   }
            }
            else if(database.get(i).getcurrentYear() == year2){
                ArrayList<SFRecord> year2Records = database.get(i).getRecordsForYear(); 
                 year2Size= year2Records.size();
                for(int j = 0; j<year2Records.size(); j++){
                   
                    if(year2Records.get(j).getDescription().indexOf(crimeDescription)>-1){
                        System.out.println("2 " + year2Records.get(j).getDescription());
                        year2Count++;
                    }
                   }
            }
        }
            
        System.out.println("1 " + year1Count);
        System.out.println("2 " + year2Count);



	return ((year2Count/year2Size)-(year1Count/year1Size))*100; // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        String[] locations = {"Brooklyn", "Manhattan", "Bronx", "Queens", "Staten Island"};
        int[] count = new int[5];
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){
                   ArrayList<SFRecord> yearRecords = database.get(i).getRecordsForYear(); 
                   for(int j = 0; j<yearRecords.size(); j++){
                   if(yearRecords.get(j).getLocation().equalsIgnoreCase(locations[0]))
                        count[0]++;
                    else if(yearRecords.get(j).getLocation().equalsIgnoreCase(locations[1]))
                        count[1]++;
                    else if(yearRecords.get(j).getLocation().equalsIgnoreCase(locations[2]))
                        count[2]++;
                    else if(yearRecords.get(j).getLocation().equalsIgnoreCase(locations[3]))
                        count[3]++;
                    else if(yearRecords.get(j).getLocation().equalsIgnoreCase(locations[4]))
                        count[4]++;
                   }
    }
}
       
        int max = count[0];  
        int index = 0;
        for(int i = 1; i<count.length; i++){
            if(count[i]>max){
                max = count[i];
                index = i;
            }
        }      

        return locations[index];
        
}
}
