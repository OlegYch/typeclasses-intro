object typeclass2 extends App {
  import typeclass._
  implicit def ListJson[T: ToJson] = new ToJson[List[T]] {
    def toJson(t: List[T]) = t.map(_.toJson).mkString("[", ",", "]")
  }
  List(new A()).toJson
}
