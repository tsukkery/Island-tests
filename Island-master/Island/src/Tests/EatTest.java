package Tests;
import field.IslandField;
import lifeform.plant.Plant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lifeform.animal.herbivore.Mouse;
import lifeform.animal.predator.Bear;
import simulation.thread.animalLifecycleTask.task.AnimalEatTask;

import java.util.concurrent.*;

public class EatTest {

    Bear bear = new Bear();
    Mouse mouse = new Mouse();
    Plant plant = new Plant();

    @Test
    public void test_ChanceToEat() {
        assertEquals(0.9, bear.getChanceToEat(mouse.getName()));
        assertEquals(0, bear.getChanceToEat(plant.getName()));
        assertEquals(1.0, mouse.getChanceToEat(plant.getName()));
    }

    @Test
    public void test_PredatorFeed() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(bear, 0, 0);
        IslandField.getInstance().addAnimal(mouse, 0, 0);
        assertTrue(bear.eat(mouse), "Bear eats mouse");
    }

    @Test
    public void test_PredatorNotHerbivore() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(bear, 0, 0);
        IslandField.getInstance().addPlant(plant, 0, 0);
        assertTrue(!bear.eat(plant), "Bear doesn't eat grass!");
    }

    @Test
    public void test_HerbivoreFeed() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(mouse, 0, 0);
        IslandField.getInstance().addPlant(plant, 0, 0);
        assertTrue(mouse.eat(plant), "Mouse eats plant");
    }

    @Test
    public void test_EatenPlant() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(mouse, 0, 0);
        IslandField.getInstance().addPlant(plant, 0, 0);

        CountDownLatch latch = new CountDownLatch(1);
        AnimalEatTask animalEatTask = new AnimalEatTask(latch);
        Thread thread = new Thread(animalEatTask);
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, IslandField.getInstance().getAllPlants().size(), "The plant is deleted after it's eaten");
    }

    @Test
    public void test_EatenAnimal() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(mouse, 0, 0);
        IslandField.getInstance().addAnimal(bear, 0, 0);

        CountDownLatch latch = new CountDownLatch(1);
        AnimalEatTask animalEatTask = new AnimalEatTask(latch);
        Thread thread = new Thread(animalEatTask);
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, IslandField.getInstance().getAllAnimals().size(), "The mouse is eaten and deleted");
    }

}

