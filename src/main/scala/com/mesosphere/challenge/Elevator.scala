package com.mesosphere.challenge

import com.mesosphere.challenge.Direction.Direction

import scala.collection.mutable.ArrayBuffer

case class ElevatorState(id: Int, floor: Int, direction: Direction, loaded: Seq[Passenger], scheduled: Seq[Passenger])

object Elevator {
  type PassengerProcessedCallback = Passenger => Unit
  val MaxFloor = 10
  val Capacity = 15
}

class Elevator(val id: Int, processedCallback: Elevator.PassengerProcessedCallback) {
  val loadedPassengers = ArrayBuffer[Passenger]()
  val scheduledPassengers = ArrayBuffer[Passenger]()

  private var _direction = Direction.Idle
  private var _floor = 1

  def currentFloor = _floor

  def direction = _direction

  def schedulePassenger(passenger: Passenger): Unit = {
    scheduledPassengers += passenger

    if (_direction == Direction.Idle) {
      if (currentFloor == passenger.startingFloor)
        _direction = passenger.direction
      else if (currentFloor < passenger.startingFloor)
        _direction = Direction.Up
      else
        _direction = Direction.Down
    }
  }

  def step(): Unit = {
    _direction match {
      case Direction.Up =>
        if (_floor == Elevator.MaxFloor) {
          _floor -= 1
          _direction = Direction.Down
        } else {
          _floor += 1
        }
      case Direction.Down =>
        if (_floor == 1) {
          _floor += 1
          _direction = Direction.Up
        } else {
          _floor -= 1
        }
      case Direction.Idle => // no-op
    }

    // load the scheduled passengers at current floor
    load()
    // unload the arrived passengers if any
    unload()

    if (scheduledPassengers.isEmpty && loadedPassengers.isEmpty)
      _direction = Direction.Idle
  }

  def update(floor: Int, direction: Direction): Unit = {
    _floor = floor
    _direction = direction
  }

  def getState(): ElevatorState = {
    ElevatorState(id, _floor, _direction, loadedPassengers.clone, scheduledPassengers.clone)
  }

  private def loadPassenger(passenger: Passenger): Unit = {
    if (loadedPassengers.size < Elevator.Capacity) {
      loadedPassengers += passenger
    }
  }

  private def load(): Unit = {
    val toLoadPassengers = scheduledPassengers filter { _.startingFloor == currentFloor }

    toLoadPassengers foreach { p =>
      loadPassenger(p)
      scheduledPassengers -= p
    }
  }

  private def unload(): Unit = {
    val arrivedPassengers = loadedPassengers filter { _.goalFloor == currentFloor }
    arrivedPassengers foreach { p =>
      loadedPassengers -= p
      processedCallback.apply(p)
    }
  }
}
