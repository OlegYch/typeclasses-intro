object inheritance extends App {
  trait ToJson {
    def toJson: String
  }
  class MyJson extends ToJson {
    def toJson = "{}"
  }
  class A extends MyJson
}
