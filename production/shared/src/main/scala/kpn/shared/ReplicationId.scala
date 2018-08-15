package kpn.shared

object ReplicationId {

  def apply(sequenceNumber: Int): ReplicationId = {
    val level1 = sequenceNumber / 1000000
    val remainder = sequenceNumber % 1000000
    val level2 = remainder / 1000
    val level3 = remainder % 1000
    this(level1.toInt, level2.toInt, level3.toInt)
  }

  def apply(string: String): ReplicationId = {
    val substrings = string.split("/")
    val level1 = substrings(0).toInt
    val level2 = substrings(1).toInt
    val level3 = substrings(2).toInt
    this(level1, level2, level3)
  }

  def range(begin: ReplicationId, end: ReplicationId): Stream[ReplicationId] = {
    (begin.number to end.number).toStream.map { sequenceNumber =>
      ReplicationId(sequenceNumber)
    }
  }
}

case class ReplicationId(level1: Int, level2: Int, level3: Int) {

  def number: Int = 1000000 * level1 + level2 * 1000 + level3

  def key: String = "%03d:%03d:%03d".format(level1, level2, level3)

  def name: String = "%03d/%03d/%03d".format(level1, level2, level3)

  def next: ReplicationId = ReplicationId(number + 1)

}
