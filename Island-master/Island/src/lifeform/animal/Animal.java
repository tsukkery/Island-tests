package lifeform.animal;

import error.ObjectNotLifeFormException;
import field.IslandField;
import field.Location;
import lifeform.LifeForm;
import lifeform.plant.Plant;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends LifeForm {
    private final int step; // Скорость перемещения
    private final double maxHp; // Максимальное количество килограммов пищи для полного насыщения
    private double hp; // Здоровье животного

    public Animal(double weight, int step, double maxHp, int maxPopulation, String name) {
        super(weight, maxPopulation, name);
        this.step = step;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }


    public boolean eat(Object food) {
        double chanceToEat;
        LifeForm lifeForm = null;
        boolean animalEatFood;

        if (food instanceof LifeForm) {
            lifeForm = (LifeForm) food;
        } else {
            try {
                throw new ObjectNotLifeFormException("Объект не является животным/растением.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        String foodName = lifeForm.getName();
        chanceToEat = getChanceToEat(foodName);

        animalEatFood = ThreadLocalRandom.current().nextDouble() <= chanceToEat;
        if (animalEatFood) {
            setHp(Math.min((getHp() + lifeForm.getWeight()), getMaxHp())); // Показатель здоровья повышается после съедения
            Location location = IslandField.getInstance().getLocation(lifeForm.getRow(), lifeForm.getColumn()); // Животное/растение удаляется из списка обиталей локации после съедения
            if (lifeForm instanceof Animal animal) {
                if (location.getAnimals().contains(animal)) {
                    IslandField.getInstance().removeAnimal(animal, location.getRow(), location.getColumn());
                } else {
                    return false;
                }
            } else {
                Plant plant = (Plant) lifeForm;
                if (location.getPlants().size() > 0) {
                    IslandField.getInstance().removePlant(plant, location.getRow(), location.getColumn());
                } else {
                    return false;
                }
            }
        }
        return animalEatFood;
    }


    public abstract double getChanceToEat(String foodName);


    public abstract void multiply(Animal partner);


    public void move() {
        Random random = new Random();
        int randomCells = random.nextInt(getStep()) + 1;
        int direction = random.nextInt(4);
        int newRow = getRow();
        int newColumn = getColumn();
        switch (direction) {
            case 0 -> // Вверх
                    newRow -= randomCells;
            case 1 -> // Вниз
                    newRow += randomCells;
            case 2 -> // Влево
                    newColumn -= randomCells;
            case 3 -> // Вправо
                    newColumn += randomCells;
        }
        // Проверяем, чтобы новые координаты не выходили за границы
        while (newRow < 0 || newRow >= IslandField.getInstance().getNumRows() || newColumn < 0 || newColumn >= IslandField.getInstance().getNumColumns()) {
            randomCells = random.nextInt(getStep()) + 1;
            direction = random.nextInt(4);

            newRow = getRow();
            newColumn = getColumn();
            switch (direction) {
                case 0 -> // Вверх
                        newRow -= randomCells;
                case 1 -> // Вниз
                        newRow += randomCells;
                case 2 -> // Влево
                        newColumn -= randomCells;
                case 3 -> // Вправо
                        newColumn += randomCells;
            }
        }
        IslandField.getInstance().removeAnimal(this, getRow(), getColumn());
        // Обновляем новые координаты
        setRow(newRow);
        setColumn(newColumn);
        IslandField.getInstance().addAnimal(this, newRow, newColumn);
    }

    public int getStep() {
        return step;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHp() {
        return maxHp;
    }
}
