package na.przypale.fitter.controls

import na.przypale.fitter.interactions.{CreatingUser, DeletingUser}
import na.przypale.fitter.menu.{Action, ActionIntId, Menu}
import na.przypale.fitter.repositories.UsersRepository

import scala.annotation.tailrec

class AnonymousUserControls(usersRepository: UsersRepository) {
  private val CREATE_USER_ACTION_ID = 1
  private val DELETE_USER_ACTION_ID = 2
  private val EXIT_ACTION_ID = 3

  private val menu = Menu(List(
    Action(ActionIntId(CREATE_USER_ACTION_ID), s"$CREATE_USER_ACTION_ID - Create user"),
    Action(ActionIntId(DELETE_USER_ACTION_ID), s"$DELETE_USER_ACTION_ID - Delete user"),
    Action(ActionIntId(EXIT_ACTION_ID), s"$EXIT_ACTION_ID - Exit")
  ))

  @tailrec
  final def interact(): Unit = {
    menu.display()
    menu.read match {
      case Some(action) => action.id match {
        case ActionIntId(CREATE_USER_ACTION_ID) => CreatingUser.create(usersRepository)
        case ActionIntId(DELETE_USER_ACTION_ID) => DeletingUser.delete(usersRepository)
        case ActionIntId(EXIT_ACTION_ID) =>
        case _ => interact()
      }
      case _ => interact()
    }
  }
}

object AnonymousUserControls {
  def apply(usersRepository: UsersRepository): AnonymousUserControls = new AnonymousUserControls(usersRepository)
}
