package com.mesosphere.challenge.impl

import com.mesosphere.challenge.{ElevatorState, Elevator, Passenger, ElevatorControlSystem}

import scala.collection.mutable.ArrayBuffer


class ElevatorControlSystemImpl(numOfElevator: Int) extends ElevatorControlSystem with BestSuitableScheduler {
  val passengers: ArrayBuffer[Passenger] = ArrayBuffer[Passenger]()
  val callback: Elevator.PassengerProcessedCallback = passengers -= _

  override val elevators: Seq[Elevator] = 1 to numOfElevator map { i => new Elevator(i, callback) }

  override def status(): Seq[ElevatorState] = elevators map { e => e.getState() }

  override def pickup(passenger: Passenger): Unit = {
    passengers += passenger
  }

  override def step(): Unit = {
    val allScheduled: Set[Passenger] = (elevators flatMap { e =>
      e.scheduledPassengers ++ e.loadedPassengers
    })(scala.collection.breakOut)

    passengers.filterNot(allScheduled.contains(_)) foreach { p =>
      val elevator = findElevatorForPassenger(p)
      elevator.schedulePassenger(p)
    }

    elevators foreach { e => e.step() }
  }
}
