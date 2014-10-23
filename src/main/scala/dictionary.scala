object dictionary extends App {
  trait ToJson[T] {
    def toJson(t: T): String
  }
  class AJson extends ToJson[A] {
    def toJson(t: A) = "{}"
  }
  class A(jsoner: ToJson[A]) {
    def toJson = jsoner.toJson(this)
  }
  def toJson[T](t: T, jsoner: ToJson[T]) = jsoner.toJson(t)
}
