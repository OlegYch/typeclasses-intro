object acl extends App {
  case class Account(id: Int)
  case class Project(id: Int)
  case class User(id: Int)
  case class Admin(id: Int)
  object ACL {
    case class Id(name: String, id: Int)
    trait Subject[T] {
      def id(t: T): Id
    }
    trait Target[T] {
      def id(t: T): Id
      protected def permission(name: String) = Permission[T](name, this)
    }
    case class Permission[T](name: String, target: Target[T])
    def can[S, T](p: Permission[T])(s: S, t: T)(implicit subject: Subject[S]): Boolean = {
      println(s"can ${subject.id(s)} do ${p.name} to ${p.target.id(t)} ?")
      true
    }
  }
  implicit def UserSubject = new ACL.Subject[User] {
    def id(t: User) = ACL.Id("user", t.id)
  }
  implicit def AdminSubject = new ACL.Subject[Admin] {
    def id(t: Admin) = ACL.Id("admin", t.id)
  }
  def AccountTarget = new ACL.Target[Account] {
    def id(t: Account) = ACL.Id("account", t.id)
    def Read = permission("read")
    def CreateProject = permission("createProject")
  }
  def UserTarget = new ACL.Target[User] {
    def id(t: User) = ACL.Id("user", t.id)
    def Create = permission("create")
  }
  def ProjectTarget = new ACL.Target[Project] {
    def id(t: Project) = ACL.Id("project", t.id)
    def Read = permission("read")
    def Start = permission("start")
  }
  ACL.can(AccountTarget.Read)(User(1), Account(2))
  ACL.can(UserTarget.Create)(Admin(1), User(2))
  //  ACL.can(ProjectTarget.Start)(Admin(1), Account(2))

  object web {
    import play.api.libs.json.{JsError, JsResult}
    import play.api.mvc.{Result, Results}
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import scalaz.Scalaz._
    def authorized[R, S: ACL.Subject, T](p: ACL.Permission[T])(s: S, t: T)
                                        (r: => R)(implicit uar: UnauthorizedResult[R]): R = {
      if (ACL.can(p)(s, t)) r else uar.get
    }
    case class UnauthorizedResult[R](get: R)
    implicit def UnauthorizedSimpleResult: UnauthorizedResult[Result] =
      UnauthorizedResult(Results.Redirect("/login").flashing("error" -> "Unauthorized"))
    implicit def UnauthorizedJSResult[T]: UnauthorizedResult[JsResult[T]] =
      UnauthorizedResult(JsError("unauthorized"))
    implicit def UnauthorizedApplicative[T, M[_]](implicit uar: UnauthorizedResult[T],
                                                  app: scalaz.Applicative[M]): UnauthorizedResult[M[T]] =
      UnauthorizedResult[M[T]](app.point(uar.get))
    object adminArea {
      def subject = Admin(1)
      def createProject(acc: Account): Future[Result] = {
        authorized(AccountTarget.CreateProject)(subject, acc) {
          def project = Future(Project(1))
          project.map(p => Results.Ok(s"created project ${p.id}"))
        }
      }
    }
  }
}
