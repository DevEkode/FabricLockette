package fr.ekode.fabriclockette.core;

import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.math.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChestHelpersTest {

    @BeforeEach
    void setUp() {
    }

    /* - getDirectionOfSecondChest() testing - */

    private Direction getDirectionSingle(Direction facing){
        return ChestHelpers.getDirectionOfSecondChest(facing, DoubleBlockProperties.Type.SINGLE);
    }

    private Direction getDirectionDouble(Direction facing){
        return ChestHelpers.getDirectionOfSecondChest(facing, DoubleBlockProperties.Type.FIRST);
    }

    @Test
    void DirectionDefault() {
        Direction output = getDirectionSingle(Direction.DOWN);
        assertNull(output);
    }

    /* Testing single chest implementations */

    @Test
    void DirectionSingleEast() {
        Direction output = getDirectionSingle(Direction.EAST);
        assertEquals(Direction.SOUTH,output);
    }

    @Test
    void DirectionSingleWest() {
        Direction output = getDirectionSingle(Direction.WEST);
        assertEquals(Direction.NORTH,output);
    }

    @Test
    void DirectionSingleNorth() {
        Direction output = getDirectionSingle(Direction.NORTH);
        assertEquals(Direction.EAST,output);
    }

    @Test
    void DirectionSingleSouth() {
        Direction output = getDirectionSingle(Direction.SOUTH);
        assertEquals(Direction.WEST,output);
    }

    /* Testing double chest implementations */

    @Test
    void DirectionDoubleEast() {
        Direction output = getDirectionDouble(Direction.EAST);
        assertEquals(Direction.NORTH,output);
    }

    @Test
    void DirectionDoubleWest() {
        Direction output = getDirectionDouble(Direction.WEST);
        assertEquals(Direction.SOUTH,output);
    }

    @Test
    void DirectionDoubleNorth() {
        Direction output = getDirectionDouble(Direction.NORTH);
        assertEquals(Direction.WEST,output);
    }

    @Test
    void DirectionDoubleSouth() {
        Direction output = getDirectionDouble(Direction.SOUTH);
        assertEquals(Direction.EAST,output);
    }

    /* - searchSecondChestEntity() testing - */
}