package Tests;

import field.IslandField;
import lifeform.animal.herbivore.Caterpillar;
import lifeform.animal.herbivore.Mouse;
import lifeform.animal.predator.Bear;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import simulation.thread.animalLifecycleTask.task.AnimalEatTask;
import simulation.thread.animalLifecycleTask.task.AnimalMultiplyTask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiplyTest {

    Mouse mouse = new Mouse();
    Mouse mouse2 = new Mouse();
    Bear bear = new Bear();
    Caterpillar caterpillar = new Caterpillar();
    Caterpillar caterpillar2 = new Caterpillar();

    @Test
    public void testMultiply() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(mouse, 0, 0);
        IslandField.getInstance().addAnimal(mouse2, 0, 0);

        CountDownLatch latch = new CountDownLatch(1);
        AnimalMultiplyTask animalMultiplyTask = new AnimalMultiplyTask(latch);
        Thread thread = new Thread(animalMultiplyTask);
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(3, IslandField.getInstance().getAllAnimals().size(), "Mouses have multiplied and born a third mouse");
    }

    @Test
    public void testNotMultiply() {
        IslandField.getInstance().initializeLocations();
        IslandField.getInstance().addAnimal(mouse, 0, 0);
        IslandField.getInstance().addAnimal(bear, 0, 0);

        CountDownLatch latch = new CountDownLatch(1);
        AnimalMultiplyTask animalMultiplyTask = new AnimalMultiplyTask(latch);
        Thread thread = new Thread(animalMultiplyTask);
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(2, IslandField.getInstance().getAllAnimals().size(), "Mouse and bear didn't make any animals (strange)");
    }

}
