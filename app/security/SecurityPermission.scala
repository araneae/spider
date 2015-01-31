package security

import be.objectify.deadbolt.core.models.Permission

class SecurityPermission(permissionName: String) extends Permission {
  
  def getValue: String = permissionName
  
}
