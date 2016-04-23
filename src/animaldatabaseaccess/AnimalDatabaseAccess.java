/*
This program will read an ArrayList of Animal objects into a database.

The ArrayList is retrieved from the BuildAnimal object.

The database is called AnimalDB, and is running on Derby embedded mode.

The program will load the ArrayList, then read the full table and print the animal names.

The program makes sure to clear the database before loading in the objects, to prevent
an exception for duplicate entries.

The program will then ask for the user to select animals, until they choose not to,
displaying the characteristics of each animal selected.
 */
package animaldatabaseaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class AnimalDatabaseAccess {

    
    public static void main(String[] args) {
        
        //Driver load
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        }
        catch(Exception ex){
            System.out.println("There was an error in loading the database drivers.");
            //Debug print exception
            System.out.println(ex);
        }
        
        //Create the animal list that will be populated to the database
        BuildAnimal defaultList = BuildAnimal.getInstance();
        ArrayList<Animal> animals = defaultList.export();
        
        //Header
        System.out.println("This program will load a list of animals to database" + 
                " then allow you to select an animal to view it's characteristics.");
        System.out.println();
        
        //Connection url
        String url = "jdbc:derby:AnimalDB";
        //Create the connection
        try (Connection connection = DriverManager.getConnection(url)) {
            
            //Clear the table for the next example
            Statement clearTable = connection.createStatement();
            String delete = "DELETE FROM Animal";
            clearTable.execute(delete);

            //Input statement
            Statement input = connection.createStatement();

            //Input loop
            for (Animal animal : animals) {
                //Get variables
                String name = animal.getName();
                String color = animal.getColor();
                Boolean hasFur = animal.getHasFur();
                Boolean isVertebrate = animal.getIsVertebrate();
                Boolean canFly = animal.getCanFly();

                //Create the UPDATE SQL command
                String update = "INSERT INTO Animal VALUES ('" + name + "', '"
                        + color + "', " + hasFur + ", " + isVertebrate + ", " + canFly
                        + ")";

                //Update the table
                input.executeUpdate(update);
            }

            //Success message
            System.out.println("Database populated.");
            System.out.println();

            //Retrieve the animal table
            Statement queryStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT * FROM Animal";
            ResultSet tableData = queryStatement.executeQuery(query);

            //Print the animals names from the database
            System.out.println("The following animals are in the table:");
            while (tableData.next()) {
                System.out.println(tableData.getRow() + " - " + tableData.getString("Name"));
            }
            System.out.println();

            //Set the cursor to the last row, and ask the user to choose an animal
            tableData.last();
            int tableSize = tableData.getRow();
            
            int choice = animalSelection(tableSize);

            //Selection loop, only exited on a 0
            while (choice != 0) {
                //Set the cursor
                tableData.absolute(choice);

                //Read the row
                String name = tableData.getString("Name");
                String color = tableData.getString("Color");
                Boolean hasFur = tableData.getBoolean("HasFur");
                Boolean isVertebrate = tableData.getBoolean("IsVertebrate");
                Boolean canFly = tableData.getBoolean("CanFly");

                //Set the new animal object and print it
                Animal printAnimal = new Animal(name, color, hasFur, isVertebrate, canFly);
                System.out.println(printAnimal);
                System.out.println();

                //Ask for a new selection
                choice = animalSelection(tableSize);
            }
        }
        catch (SQLException ex) {
            System.out.println("There was a problem accessing the database.");
            //Debug print exception
            System.out.println(ex);
        }   
        
        try{
            //Shutdown the databse
            DriverManager.getConnection(url + ";shutdown=true");
        }
        catch (SQLNonTransientConnectionException ex){}//Thrown if the database shuts down
        catch (SQLException ex){
            System.out.println("There was a problem shutting down the database.");
            //Debug print exception
            System.out.println(ex);
        }
    }
    
    //Selection method
    private static int animalSelection(int tableSize){
        Scanner keyboard = new Scanner(System.in);        
        System.out.println("Please select an animal by typing its number from the list "
                + "(Type 0 to stop selecting animals)");
        
        //Loop until the user provides a valid selection
        do{
            try{
                //Get the number
                int selection = Integer.parseInt(keyboard.nextLine());
                
                //Test the number (0 to table size)
                if (selection > -1 && selection <= tableSize){
                    return selection;
                }
                else{
                    System.out.println("Please enter a number from the list or 0.");
                }
            }
            catch(Exception ex){
                System.out.println("Please enter a number.");
            }
        }while(true);
    }
}
            /* Table creation SQL code
            Statement createTable = connection.createStatement();
            String ct = "CREATE TABLE Animal"
                    + "("
                    + "Name Varchar(35),"
                    + "Color Varchar (35),"
                    + "HasFur Boolean,"
                    + "IsVertebrate Boolean,"
                    + "CanFly Boolean"
                    + ")";
            createTable.execute(ct);
            */