package com.mesosphere.challenge

trait ElevatorScheduler { self: ElevatorControlSystem =>
  def elevators: Seq[Elevator]
  def findElevatorForPassenger(passenger: Passenger): Elevator
}
