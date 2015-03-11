package com.mesosphere.challenge

case class Passenger(id: Int, startingFloor: Int, goalFloor: Int) {
  require(startingFloor != goalFloor, s"${id}: Trying go to the floor he/she is currently at!")

  val direction = if (startingFloor > goalFloor) Direction.Down else Direction.Up
}
