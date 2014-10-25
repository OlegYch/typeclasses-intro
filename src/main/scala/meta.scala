object meta extends App {
  trait Table[T] {
    def tableName: String
    def insert(t: T): String
  }
  case class A(i: Int)
  case class B(s: String, l: Long)
  implicit def ATable: Table[A] = new Table[A] {
    def tableName = "a"
    def insert(t: A) = t.i.toString
  }
  implicit def BTable: Table[B] = new Table[B] {
    def tableName = "b"
    def insert(t: B) = s"${t.s}, ${t.l}"
  }
  def saveToTable[T](t: T)(implicit table: Table[T]) =
    println(s"insert into ${table.tableName} values (${table.insert(t)})")
  saveToTable(A(2))
}
