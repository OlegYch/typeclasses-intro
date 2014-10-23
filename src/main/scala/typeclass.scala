object typeclass extends App {
  trait ToJson[T] {
    def toJson(t: T): String
  }
  implicit def AJson = new ToJson[A] {
    def toJson(t: A) = "{}"
  }
  implicit def ListJson[T: ToJson] = new ToJson[List[T]] {
    def toJson(t: List[T]) = t.map(_.toJson).mkString
  }
  class A
  def toJson[T](t: T)(implicit jsoner: ToJson[T]) = jsoner.toJson(t)
  implicit class WithToJson[T: ToJson](t: T) {
    def toJson = typeclass.toJson(t)
  }
}

