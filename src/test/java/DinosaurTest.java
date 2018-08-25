
import models.dinosaurs.Dinosaur;
import models.enums.DietaryType;
import models.enums.DinosaurType;
import models.enums.HungerLevelType;
import models.paddocks.Paddock;
import models.parks.Park;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DinosaurTest {
    private Dinosaur trex;
    private Dinosaur triceratops;
    private Dinosaur velociraptor;
    private Dinosaur giganotosaurus;
    private Park park;
    private Paddock carnivorePaddock;
    private Paddock herbivorePaddock;
    @Before
    public void before() {
        trex = new Dinosaur(DinosaurType.TREX);
        triceratops = new Dinosaur(DinosaurType.TRICERATOPS);
        velociraptor = new Dinosaur(DinosaurType.VELOCIRAPTOR);
        giganotosaurus = new Dinosaur(DinosaurType.GIGANOTOSAURUS);
        park = new Park();
        carnivorePaddock = new Paddock("Carnivores", park, DietaryType.CARNIVORE);
        herbivorePaddock = new Paddock("Herbivores", park, DietaryType.HERBIVORE);

    }


    @Test
    public void canAddOnlyDinoOfPaddockType() {
        trex.addPaddockToDinosaur(carnivorePaddock);
        assertEquals("Carnivores", trex.getPaddock().getName());
        trex.addPaddockToDinosaur(herbivorePaddock);
        assertEquals("Carnivores", trex.getPaddock().getName());
    }

//    @Test
//    public void wontAddCarnivoreOfDifferentTypeToCarnivorePaddock() {
//        trex
//    }

    @Test
    public void wontAddHerbivoreToCarnivorePaddock(){
        triceratops.addPaddockToDinosaur(carnivorePaddock);
        assertEquals(false, triceratops.checkIfPaddockAssigned());
        triceratops.addPaddockToDinosaur(herbivorePaddock);
        assertEquals(true, triceratops.checkIfPaddockAssigned());
    }

    @Test
    public void wontReassignPaddockToCarnivoreForHerbivore() {
        triceratops.addPaddockToDinosaur(herbivorePaddock);
        assertEquals(DietaryType.HERBIVORE, triceratops.getPaddock().getDietaryType());
        triceratops.addPaddockToDinosaur(carnivorePaddock);
        assertEquals(DietaryType.HERBIVORE, triceratops.getPaddock().getDietaryType());
    }

    @Test
    public void canCheckIfPaddockAssigned() {
        assertEquals(false, trex.checkIfPaddockAssigned());
        trex.addPaddockToDinosaur(carnivorePaddock);
        assertEquals(true, trex.checkIfPaddockAssigned());
    }

    @Test
    public void canAssignHungerLevel() {
        velociraptor.setStomach(10);
        velociraptor.assignHungerLevel();
        assertEquals(HungerLevelType.STARVING, velociraptor.getHungerLevel());
    }

}
