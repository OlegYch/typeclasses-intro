object typeclass extends App {
  trait ToJson[T] {
    def toJson(t: T): String
  }
  implicit def AJson = new ToJson[A] {
    def toJson(t: A) = "{}"
  }
  class A()
  implicit class WithToJson[T](t: T)(implicit jsoner: ToJson[T]) {
    def toJson = jsoner.toJson(t)
  }
  new A().toJson
}

