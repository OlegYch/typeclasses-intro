object meta extends App {
  trait Table[T] {
    def tableName: String
    def insert(t: T): String
  }
  case class A(i: Int)
  case class B(s: String, l: Long)
  implicit val ATable: Table[A] = new Table[A] {
    def tableName = "a"
    def insert(t: A) = t.i.toString
  }
  implicit val BTable: Table[B] = new Table[B] {
    def tableName = "b"
    def insert(t: B) = s"${t.s}, ${t.l}"
  }
  def saveToTable[T](t: T)(implicit table: Table[T]) =
    println(s"insert into ${table.tableName} values (${table.insert(t)})")
  implicit class WithTable[T](value: T)(implicit val table: Table[T]) {
    def insert = table.insert(value)
  }
  def saveBatch(batch: List[WithTable[_]]) = batch.groupBy(_.table).foreach { case (table, values) =>
    val insert = values.map { t => t.insert}.mkString("(", "), (", ")")
    println(s"insert into ${table.tableName} values $insert")
  }
  saveToTable(A(2))
  saveBatch(List(A(1), B("q", 2), B("b", 3)))
}
