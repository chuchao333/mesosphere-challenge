package com.mesosphere.challenge.impl

import com.mesosphere.challenge._

trait BestSuitableScheduler extends ElevatorScheduler { self: ElevatorControlSystem =>
  // this implements the schedule algorithm
  override def findElevatorForPassenger(passenger: Passenger): Elevator = {
    // Get the first elevator with max weight and assign the elevator to serve the passenger
    (elevators map { e => (calcSuitableWeight(e, passenger), e) } maxBy { x => x._1})._2
  }

  /**
   * The weight is calculated with following algorithm:
   * weight = (N+1) - d if elevator is moving towards passenger and in same directions
   *        = N -d if elevator is moving towards passenger and in opposite directions
   *        = 1 otherwise
   * where N is the max floor and d is the distance between elevator and passenger
   */
  private def calcSuitableWeight(elevator: Elevator, passenger: Passenger): Int = {
    val res = if (elevator.direction == Direction.Idle && elevator.currentFloor == passenger.startingFloor)
      Elevator.MaxFloor
    else {
      val floorDiff = elevator.currentFloor - passenger.startingFloor

      val movingTowards = elevator.direction == Direction.Up && floorDiff <= 0 ||
        elevator.direction == Direction.Down && floorDiff >= 0
      val sameDirection = elevator.direction == passenger.direction

      (movingTowards, sameDirection) match {
        case (true, true) => Elevator.MaxFloor + 1 - floorDiff
        case (true, false) => Elevator.MaxFloor - floorDiff
        case (false, _) => 1
      }
    }

    // calculate the weight of the suitability
    res + (Elevator.Capacity - elevator.loadedPassengers.size - elevator.scheduledPassengers.size)
  }

}
