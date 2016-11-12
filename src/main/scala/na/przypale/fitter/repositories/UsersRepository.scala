package na.przypale.fitter.repositories

import na.przypale.fitter.entities.User

trait UsersRepository {
  def insertUnique(user: User): Unit
  def getByNick(nick: String): User
}
