object jsonformat extends App {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

import scalaz.Scalaz._
  import scalaz._
  case class A(i: Int)
  implicit def AFormat: Format[A] = Json.format
  implicit def AShow: Show[A] = Show.showA
  case class B(a: A, s: String)
  implicit def BFormat: Format[B] = (
    (__ \ 'a).format[A] and
      (__ \ 's).format[JsValue].inmap[String](Json.stringify, s => Json.parse(s))
    )(B.apply, unlift(B.unapply))
  implicit def BShow: Show[B] = Show.showA
  def js = Json.toJson(B(A(1), """{"hi": "there"}"""))
  Json.stringify(js).println
  Json.fromJson[B](js).asOpt.println
}
