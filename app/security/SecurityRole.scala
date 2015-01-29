package security

import be.objectify.deadbolt.core.models.Role

class SecurityRole(roleName: String) extends Role {
  
  def getName: String = roleName
  
}