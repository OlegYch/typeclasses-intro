object monoid extends App {
  import scalaz.Scalaz._
  import scalaz._
  case class Price(cost: BigDecimal, shipping: BigDecimal, tax: BigDecimal)
  implicit def PriceMonoid: Monoid[Price] = Monoid.instance(
    (p1, p2) => Price(cost = p1.cost + p2.cost, shipping = p1.shipping + p2.shipping, tax = p1.tax + p2.tax),
    Price(0, 0, 0))
  implicit def PriceShow: Show[Price] = Show.showA
  case class Box(items: List[(String, Price)])
  def boxes = List(Box(List("item1" -> Price(1, 2, 0.1))), Box(List("item2" -> Price(1, 2, 0.1))))
  def totalPrice = boxes.foldMap {_.items.map(_._2).suml}
  totalPrice.println
  case class Command(name: String)
  implicit def CommandMonoid: Monoid[Command] = Monoid.instance((c1, c2) => Command(s"${c1.name} && ${c2.name}"), Command(""))
  implicit def CommandShow: Show[Command] = Show.showA
  def servers = Set("a.com", "c.net")
  def defaultCommands = Command("hello") |+| Command("whoami")
  def userCommands = Map("a.com" -> Command("login"))
  def serverCommands = servers.map(s => s -> defaultCommands).toMap |+| userCommands
  serverCommands.println
}
trait MyMonoid[F] {
  def zero: F
  def append(f1: F, f2: => F): F
}