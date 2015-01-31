package security

import play.libs.Scala
import be.objectify.deadbolt.core.models.Subject

class DeadboltUser(userName: String, roles: Seq[String]) extends Subject {
  
  val securityRoles = Scala.asJava(roles.map { x => new SecurityRole(x) })
  
  def getRoles: java.util.List[SecurityRole] = securityRoles 

  def getPermissions: java.util.List[SecurityPermission] = {
    Scala.asJava(List[SecurityPermission]())
  }

  def getIdentifier: String = userName
  
}
