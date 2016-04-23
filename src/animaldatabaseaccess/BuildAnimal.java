/*
This class creates a list of Animal objects to be used in the AnimalDatabaseAccess file
 */
package animaldatabaseaccess;

import java.util.ArrayList;

public class BuildAnimal {
    //Singleton parts
    private static final BuildAnimal INSTANCE = new BuildAnimal();
    public static BuildAnimal getInstance(){
        return INSTANCE;
    }
    private ArrayList<Animal> animals = new ArrayList<>();
    
    //Constructor, with the animals to be added
    private BuildAnimal(){
        
        animals.add(new Animal("Bird", "Blue", false, true, true));
        animals.add(new Animal("Cat", "Orange", true, true, false));
        animals.add(new Animal("Fish", "Green", false, true, false));
        animals.add(new Animal("Squirrel", "Red", true, true, false));
        
    }
    
    //Export the list
    public ArrayList<Animal> export(){
        return animals;
    }
}
