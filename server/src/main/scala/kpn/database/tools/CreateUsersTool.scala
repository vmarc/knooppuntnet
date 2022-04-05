package kpn.database.tools

import kpn.api.common.common.User
import kpn.database.util.Mongo

object CreateUsersTool {

  private val users: Seq[User] = Seq(
    User("vmarc"),
    User("foxandpotatoes"),
    User("s8evq"),
    User("joost schouppe"),
    User("Pieter Vander Vennet"),
    User("Thierry1030"),
    User("Peter Elderson"),
    User("pyrog"),
    User("StC")
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-4") { database =>
      users.foreach(user => database.users.save(user))
    }
  }
}
