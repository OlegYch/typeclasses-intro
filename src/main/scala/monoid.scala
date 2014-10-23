object monoid extends App {
  import scalaz.Scalaz._
  import scalaz._
  case class Price(cost: BigDecimal, shipping: BigDecimal, tax: BigDecimal)
  implicit val PriceMonoid: Monoid[Price] = Monoid.instance(
    (p1, p2) => Price(cost = p1.cost + p2.cost, shipping = p1.shipping + p2.shipping, tax = p1.tax + p2.tax),
    Price(0, 0, 0))
  implicit val PriceShow: Show[Price] = Show.showA
  case class Box(items: List[(String, Price)])
  val boxes = List(Box(List("item1" -> Price(1, 2, 0.1))), Box(List("item2" -> Price(1, 2, 0.1))))
  val totalPrice = boxes.foldMap {_.items.map(_._2).suml}
  totalPrice.println
  case class Command(name: String)
  implicit val CommandMonoid: Monoid[Command] = Monoid.instance((c1, c2) => Command(s"${c1.name} && ${c2.name}"), Command(""))
  implicit val CommandShow: Show[Command] = Show.showA
  val servers = Set("a.com", "c.net")
  val defaultCommands = Command("hello") |+| Command("whoami")
  val userCommands = Map("a.com" -> Command("login"))
  val serverCommands = servers.map(s => s -> defaultCommands).toMap |+| userCommands
  serverCommands.println
}
