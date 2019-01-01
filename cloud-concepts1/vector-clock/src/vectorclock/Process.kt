package vectorclock

import java.util.LinkedList

class Process(private val id: Int,
              numOfProcesses: Int,
              private val logTimestamps: Boolean = false) {

  private var timestamps: IntArray = IntArray(numOfProcesses)

  var buffer: LinkedList<Message> = LinkedList()

  fun instruction() {
    timestamps[id] += 1
    log()
  }

  fun send(process: Process) {
    timestamps[id] += 1
    process.putMessage(timestamps)
    log()
  }

  fun receive() {
    val message = buffer.poll()
    timestamps[id] += 1
    for (i in timestamps.indices) {
      if (i != id) {
        timestamps[i] = Math.max(message.timestamps[i], timestamps[i])
      }
    }
    log()
  }

  private fun putMessage(timestamps: IntArray) {
    buffer.push(Message(timestamps))
  }

  private fun log() {
    if (logTimestamps) {
      println(this)
    }
  }

  override fun toString(): String {
    val description = timestamps.joinToString(separator = ",")
    return "P$id($description)"
  }

  class Message(
    val timestamps: IntArray
  )
}