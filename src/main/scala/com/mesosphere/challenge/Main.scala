package com.mesosphere.challenge

import com.mesosphere.challenge.impl.ElevatorControlSystemImpl
import scala.annotation.tailrec
import scala.util.Random

object Main extends App {
  println("Hello, mesosphere!")

  val NumOfElevators = 3
  val NumOfBatchesOfPassengers = 5
  val MaxBatchSize = 10

  val system = new ElevatorControlSystemImpl(3)
  val iter = passengersIter(NumOfBatchesOfPassengers)

  @tailrec
  def run(): Unit = {
    val passengersGenerated = if (iter.hasNext) iter.next() else Nil
    passengersGenerated foreach { system.pickup(_) }

    if (system.passengers.nonEmpty) {
      system.step()
      println(system.status())
      run()
    }
  }

  def passengersIter(num: Int): Iterator[Seq[Passenger]] = {
    val random = new Random()
    var nextPassengerId = 0

    def generate(): Seq[Passenger] = {

      val numOfPassenger = random.nextInt(MaxBatchSize)
      val passengers = 1 to numOfPassenger map { _ =>
        val startFloor = random.nextInt(Elevator.MaxFloor - 1) + 1
        def genGoalFloor(): Int = {
          val cur = random.nextInt(Elevator.MaxFloor - 1) + 1
          if (cur != startFloor) cur
          else genGoalFloor()
        }
        val goalFloor = genGoalFloor()
        nextPassengerId += 1
        Passenger(nextPassengerId, startFloor, goalFloor)
      }
      passengers
    }

    (1 to num) map { _ => generate() } toIterator
  }

  run()
}