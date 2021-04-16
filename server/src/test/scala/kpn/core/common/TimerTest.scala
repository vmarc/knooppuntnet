package kpn.core.common

import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class TimerTest extends UnitTest with BeforeAndAfterEach {

  private val cpuStartTime = 100000L
  private val epochStartTime = 200000L
  private val timeout = 1000L

  override def afterEach(): Unit = {
    CpuTime.clear()
    EpochTime.clear()
  }

  test("timer has elapsed when both epoch and cpu time exceed timeout duration") {

    val timer = buildTimer()

    CpuTime.set(cpuStartTime + timeout + 1)
    EpochTime.set(epochStartTime + timeout + 1)

    val timerState = timer.poll()
    assert(timerState.isElapsed)
    assert(timerState.epochTimeElapsed)
    assert(timerState.cpuTimeElapsed)
    timerState.epochElapsed should equal(timeout + 1)
    timerState.cpuElapsed should equal(Some(timeout + 1))
  }

  test("timer has not elapsed when only epoch time elapsed") {

    val timer = buildTimer()

    CpuTime.set(cpuStartTime + (timeout / 2))
    EpochTime.set(epochStartTime + timeout + 1)

    val timerState = timer.poll()
    assert(!timerState.isElapsed)
    assert(timerState.epochTimeElapsed)
    assert(timerState.cpuTimeElapsed)
    timerState.epochElapsed should equal(timeout + 1)
    timerState.cpuElapsed should equal(Some(timeout / 2))
  }

  test("time between cpu time retrieval is minimum 50 milliseconds") {

    val timer = buildTimer()

    CpuTime.set(cpuStartTime + (timeout / 2)) // cpu time not exceeded timeout yet
    EpochTime.set(epochStartTime + timeout + 1) // epoch time exceeded timeout

    {
      val timerState = timer.poll()
      assert(!timerState.isElapsed)
      assert(timerState.epochTimeElapsed)
      assert(!timerState.cpuTimeElapsed)
      timerState.epochElapsed should equal(timeout + 1)
      timerState.cpuElapsed should equal(Some(timeout / 2))
    }

    CpuTime.set(cpuStartTime + timeout + 1) // cpu time duration exceeded
    EpochTime.set(epochStartTime + timeout + 21) // but previous cpu time retrieval was only 20ms ago

    {
      val timerState = timer.poll()
      assert(!timerState.isElapsed)
      assert(timerState.epochTimeElapsed)
      assert(!timerState.cpuTimeElapsed)
      timerState.epochElapsed should equal(timeout + 21)
      timerState.cpuElapsed should equal(None)
    }

    CpuTime.set(cpuStartTime + timeout + 1) // cpu time duration exceeded
    EpochTime.set(epochStartTime + timeout + 81) // previous cpu time retrieval 60ms ago

    {
      val timerState = timer.poll()
      assert(timerState.isElapsed)
      assert(timerState.epochTimeElapsed)
      assert(timerState.cpuTimeElapsed)
      timerState.epochElapsed should equal(timeout + 81)
      timerState.cpuElapsed should equal(Some(timeout + 1))
    }
  }

  private def buildTimer(): Timer = {
    CpuTime.set(cpuStartTime)
    EpochTime.set(epochStartTime)
    new Timer(timeout)
  }
}
