package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.entity.Herbivore;
import ru.ecosystem.model.entity.Predator;
import ru.ecosystem.model.event.*;
import ru.ecosystem.model.stats.GenerationStats;

import static org.junit.jupiter.api.Assertions.*;

class SimulationMomentTest {

    @Test
    void testGrassGrowthStep() {
        SimulationConfig config = SimulationConfig.builder()
                .width(5).height(5)
                .initialGrassCells(5)
                .grassGrowthPerTick(2)
                .grassMaxLevel(5)
                .initialHerbivores(0)
                .initialPredators(0)
                .build();
        Simulation sim = new Simulation(config);
        GenerationResult result = sim.step();
        assertTrue(result.events().stream().noneMatch(e -> e instanceof GrassGrowthEvent));
    }

    @Test
    void testAnimalsDieWithoutFood() {
        SimulationConfig config = SimulationConfig.builder()
                .width(5).height(5)
                .initialGrassCells(0)
                .initialHerbivores(1)
                .herbivoreStartEnergy(1)
                .herbivoreMetabolism(2)
                .initialPredators(0)
                .build();
        Simulation sim = new Simulation(config);
        
        GenerationResult result = sim.step();
        GenerationStats stats = result.stats();
        
        assertEquals(0, stats.herbivores(), "Травоядное должно умереть от голода");
        assertTrue(result.events().stream().anyMatch(e -> e instanceof DeathEvent de && de.cause() == DeathCause.STARVATION));
    }

    @Test
    void testPredatorEatsHerbivore() {
        SimulationConfig config = SimulationConfig.builder()
                .width(1).height(2)
                .initialGrassCells(0)
                .initialHerbivores(0) 
                .initialPredators(0)  
                .build();
        Simulation sim = new Simulation(config);

        Position hPos = new Position(0, 0);
        Position pPos = new Position(0, 1);
        
        sim.grid().cell(hPos).setAnimal(new Herbivore(hPos, 5, 0));
        sim.grid().cell(pPos).setAnimal(new Predator(pPos, 5, 0));

        GenerationResult result = sim.step();

        boolean eaten = result.events().stream().anyMatch(e -> e instanceof EatEvent ee && ee.eaterType().equals("P"));
        assertTrue(eaten, "Хищник должен съесть травоядное");
    }

    @Test
    void testDeterminismWithSameSeed() {
        SimulationConfig config = SimulationConfig.builder()
                .width(10).height(10)
                .seed(12345L)
                .initialGrassCells(20)
                .initialHerbivores(10)
                .initialPredators(5)
                .build();

        Simulation sim1 = new Simulation(config);
        Simulation sim2 = new Simulation(config);

        for (int i = 0; i < 5; i++) {
            sim1.step();
            sim2.step();
        }

        SimulationSnapshot snap1 = sim1.snapshot();
        SimulationSnapshot snap2 = sim2.snapshot();

        for (int i = 0; i < snap1.cells().size(); i++) {
            assertEquals(snap1.cells().get(i), snap2.cells().get(i));
        }
    }
}