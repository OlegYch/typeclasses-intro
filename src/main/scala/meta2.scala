object meta2 extends App {
  import meta._
  implicit class WithTable[T](value: T)(implicit val table: Table[T]) {
    def insert = table.insert(value)
  }
  def saveBatch(batch: List[WithTable[_]]) = batch.groupBy(_.table).foreach {
    case (table, values) =>
      def insert = values.map { t => t.insert}.mkString("(", "), (", ")")
      println(s"insert into ${table.tableName} values $insert")
  }
  saveBatch(List(A(1), B("q", 2), B("b", 3)))
}
