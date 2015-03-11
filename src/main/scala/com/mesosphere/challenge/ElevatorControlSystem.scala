package com.mesosphere.challenge

trait ElevatorControlSystem {
  // Query the state of the elevators
  def status(): Seq[ElevatorState]

  // receiving update command for a given elevator
  // def update(elevatorId: Int): Unit

  // receiving a pickup request of a passenger
  def pickup(passenger: Passenger): Unit

  // time-stepping the simulation
  def step(): Unit
}
