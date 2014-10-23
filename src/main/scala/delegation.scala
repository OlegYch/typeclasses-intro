object delegation extends App {
  trait ToJson {
    def toJson: String
  }
  class AJson(a: A) extends ToJson {
    def toJson = "{}"
  }
  class A(jsoner: ToJson) extends ToJson {
    def toJson = jsoner.toJson
  }
}
