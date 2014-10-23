object eqshow extends App {
  import scalaz.Scalaz._
  import scalaz._
  List(1, 2) === List(1, 2)
  List(1, 2) == 1
  //  List(1, 2) === 1
  case class A(i: Int)
  implicit val AEqual: Equal[A] = Equal.equalA
  implicit val AShow: Show[A] = Show.showA
  case class B(a: A)
  implicit val BEqual: Equal[B] = Equal.equalBy(_.a)
  implicit val BShow: Show[B] = Show.shows(b => s"b(${b.a})")
  List(A(12)) === List(A(1))
  List(B(A(1))).shows
}
