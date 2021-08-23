package fr.ekode.fabriclockette.core;

import net.minecraft.block.enums.DoorHinge;
import net.minecraft.util.math.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoorHelperTest {

    /* - getDirectionOfSecondDoor() testing - */

    @Test
    void getDirectionOfSecondDoorDefault() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.DOWN, DoorHinge.RIGHT);
        assertNull(dir);
    }

    @Test
    void getDirectionOfSecondDoorEastRight() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.EAST, DoorHinge.RIGHT);
        assertEquals(Direction.NORTH,dir);
    }

    @Test
    void getDirectionOfSecondDoorEastLeft() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.EAST, DoorHinge.LEFT);
        assertEquals(Direction.SOUTH,dir);
    }

    @Test
    void getDirectionOfSecondDoorWestRight() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.WEST, DoorHinge.RIGHT);
        assertEquals(Direction.SOUTH,dir);
    }

    @Test
    void getDirectionOfSecondDoorWestLeft() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.WEST, DoorHinge.LEFT);
        assertEquals(Direction.NORTH,dir);
    }

    @Test
    void getDirectionOfSecondDoorNorthRight() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.NORTH, DoorHinge.RIGHT);
        assertEquals(Direction.WEST,dir);
    }

    @Test
    void getDirectionOfSecondDoorNorthLeft() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.NORTH, DoorHinge.LEFT);
        assertEquals(Direction.EAST,dir);
    }

    @Test
    void getDirectionOfSecondDoorSouthRight() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.SOUTH, DoorHinge.RIGHT);
        assertEquals(Direction.EAST,dir);
    }

    @Test
    void getDirectionOfSecondDoorSouthLeft() {
        Direction dir = DoorHelper.getDirectionOfSecondDoor(Direction.SOUTH, DoorHinge.LEFT);
        assertEquals(Direction.WEST,dir);
    }

    /* - searchSecondDoorBlock() testing - */

    @Test
    void searchSecondDoorBlock() {
    }
}