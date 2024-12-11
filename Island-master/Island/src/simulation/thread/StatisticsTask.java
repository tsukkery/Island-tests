package simulation.thread;

import field.IslandField;
import simulation.IslandSimulation;
import simulation.thread.animalLifecycleTask.task.AnimalEatTask;
import simulation.thread.animalLifecycleTask.task.AnimalHpDecreaseTask;
import simulation.thread.animalLifecycleTask.task.AnimalMultiplyTask;


public class StatisticsTask implements Runnable {
    private boolean isTimeOver;
    private int babies;
    private int animalsEaten;
    private int animalsDiedByHungry;
    private int countAnimalsEnd;
    private int countPlants;
    private static int currentDay = 0;
    private final AnimalMultiplyTask animalMultiplyTask;
    private final AnimalEatTask animalEatTask;
    private final AnimalHpDecreaseTask animalHpDecreaseTask;

    public StatisticsTask(AnimalEatTask animalEatTask, AnimalHpDecreaseTask animalHpDecreaseTask, AnimalMultiplyTask animalMultiplyTask) {
        this.animalEatTask = animalEatTask;
        this.animalHpDecreaseTask = animalHpDecreaseTask;
        this.animalMultiplyTask = animalMultiplyTask;
    }

    @Override
    public void run() {
        long timeNow = IslandSimulation.getInstance().getTimeNow();
        isTimeOver = checkTime(timeNow);

        babies = animalMultiplyTask.getBabies();
        countAnimalsEnd = IslandField.getInstance().getAllAnimals().size(); // кол-во животных на острове
        animalsEaten = animalEatTask.getAnimalsEaten(); // кол-во животных умерло (съедение)
        animalsDiedByHungry = animalHpDecreaseTask.getAnimalsDiedByHunger(); // кол-во животных умерло (голод)
        countPlants = IslandField.getInstance().getAllPlants().size(); // кол-во растений
        printStats();

        if (isTimeOver) {
            IslandSimulation.getInstance().getExecutorService().shutdown();
            System.exit(0);
        }
    }


    private boolean checkTime(long timeNow) {
        return timeNow / 60 >= 5;
    }


    private void printStats() {
        if (isTimeOver) {
            System.out.println("ПОБЕДА!!! ВЫ ПРОДЕРЖАЛИСЬ 5 МИНУТ!");
            System.out.println("----------------------------------");
        } else {
            System.out.printf("--- ДЕНЬ %d ---", currentDay);
            currentDay++;
            System.out.println();
        }
        System.out.println();
        System.out.println("СТАТИСТИКА ПО ОСТРОВУ");
        System.out.println();

        System.out.print("Животных на острове: ");
        System.out.println(countAnimalsEnd);

        System.out.print("Животных умерло от голода: ");
        System.out.println(animalsDiedByHungry);

        System.out.print("Животных съедено: ");
        System.out.println(animalsEaten);

        System.out.print("Детенышей родилось: ");
        System.out.println(babies);

        System.out.print("Растений на острове: ");
        System.out.println(countPlants);

        System.out.println();
        System.out.println("----------------------------------");
        System.out.println();
    }

    public static int getCurrentDay() {
        return currentDay;
    }
}
