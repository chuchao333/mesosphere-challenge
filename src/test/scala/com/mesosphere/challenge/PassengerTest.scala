package com.mesosphere.challenge

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PassengerTest extends FunSuite {
  test("The direction should be set correctly") {
    assert(Passenger(1, 2, 5).direction == Direction.Up)

    assert(Passenger(1, 5, 3).direction == Direction.Down)
  }

  test("The staringFloor and goalFloor should be different") {
    intercept[IllegalArgumentException] {
      Passenger(1, 3, 3)
    }
  }
}
