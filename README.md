# mesosphere-challenge

## Introduction

Instead of the over-simplied the interfaces in the original problem specification, the signatures are changed according to how I model the domains in my solution:

* *status()*: State of an elevator is represented as instances of ElevatorState

* *update(elevatorId, floor, direction)* can be used to set the floor and direction of
given elevator (but it's not used in my simulation)

* *pickup* takes an instance of Passenger

** Instances of Passenger specify the starintFloor and goalFloor, (thus the direction could be determined). More properties can be added to this when extending the design to meet the real world requirements. E.g., add a 'weight' and the capacity of an elevator could be checked based on the current total weight.

* *step()* will try to schedule the current outstanding passengers in the system

And it will also move all the elevators based on the current state and update the

states accordingly

## Classes Overview

### Passenger

This models the passenger entering into the control system.

### Elevator

Each elevator will have a 'scheduledPassengers' (to be picked up) and 'loadedPassengers'.

After each step, it will check if need to 'load' scheduled passengers or 'unload' arrived

passengers and update the state of the two sequences, as well as other states like the

direction, and the floor number.

### ElevatorState

The case class wrapps the state of an elevator

### ElevatorControlSystem

the trait with modified interfaces

### ElevatorScheduler

the trait with only one API, which represents the actual schedule algorithm. The control

system could mixin different impls of the trait to use different scheduling strategy.

### BestSuitableScheduler

The implementation of the scheduler trait in my solution. It will calculate the weight

while trying to assign an elevator for a given passenger based on a few ad-hoc criteria.

The one with largest weight will be chosen. (The details of the schedule algoritm will

be explained in the next section)

### ElevatorControlSystemImpl

This implements the ElevatorControlSystem and mixin the scheduler above.

### Main

This is the driver program, it have some utilities to generate batches of passengers to simuate the pickup requests and send commands to the control system to run the simulation until all the passengers in all the batches are processed.

## Schedule Algorithm

Each passenger is assigned to the 'nearest' elevator as determined by elevator position, diretion of the call, and elevator direction.

The weight of the suitability is computed as:

* towards the call, same direction: weight = (N+2) - d + C
* towards the call, opposite direction: weight = (N+1) -d + C
* away form the call, 1 + C

where N = max floor - 1, d = distance between elevator and the call and C is the extra capacity.

This algoritm tend to keep the waiting time small while also have a good enough sojourn time.

Compared with the FCFS algorithm, it honors both temporal and spatial efficiencies, and also have considered the load banlance among the multiple elevators.
