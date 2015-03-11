package com.mesosphere.challenge

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElevatorTest extends FunSuite {
  test("Initial state should be correct") {
    assertResult(ElevatorState(1, 1, Direction.Idle, Nil, Nil))(new Elevator(1, { _ => /*no-op*/}).getState)
  }

  test("Invoking head on an empty Set should produce NoSuchElementException") {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }

  test("Set the elevator's direction when first passenger scheduled") {
    val elevator = new Elevator(1, { _ => /*no-op*/})
    elevator.schedulePassenger(Passenger(1, 2, 3))
    assertResult(Direction.Up)(elevator.direction)
  }

  test("Set the elevator's direction to first passenger's when at the starting floor") {
    val elevator = new Elevator(1, { _ => /*no-op*/})
    elevator.update(3, Direction.Idle)
    elevator.schedulePassenger(Passenger(1, 3, 2))
    assertResult(Direction.Down)(elevator.direction)
  }

  test("When not idle, direction won't be changed") {
    val elevator = new Elevator(1, { _ => /*no-op*/})
    elevator.update(1, Direction.Up)
    elevator.schedulePassenger(Passenger(1, 3, 2))
    assertResult(Direction.Up)(elevator.direction)
  }

  test("Move elevator one step will have updated state") {
    val elevator = new Elevator(1, { _ => /*no-op*/})
    elevator.update(1, Direction.Up)
    elevator.schedulePassenger(Passenger(1, 3, 2))

    elevator.step()
    assertResult(2)(elevator.currentFloor)
    assertResult(1)(elevator.scheduledPassengers.head.id)

    elevator.step()
    assertResult(3)(elevator.currentFloor)
    assert(elevator.scheduledPassengers.isEmpty)
    assertResult(1)(elevator.loadedPassengers.size)
  }
}
