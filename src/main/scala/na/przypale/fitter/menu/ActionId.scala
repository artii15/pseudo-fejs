package na.przypale.fitter.menu

trait ActionId {
  def matches(command: String): Boolean
}
