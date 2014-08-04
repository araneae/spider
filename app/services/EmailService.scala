package services

import play._
import play.mvc._
import java.util._
import com.typesafe.plugin._
import play.api.Play.current
import models.tables._
import models.dtos._

object EmailService {
 
     def inviteContact(receiver: User, senderName:String, token: String, applicationBaseUrl: String) = {
       println("in EmailService.inviteContact... ")
       val content = views.html.email.inviteContact(receiver.firstName)(senderName)(token)(applicationBaseUrl)
       val mail = use[MailerPlugin].email
       mail.setSubject(s"Hello ${receiver.firstName}")
       mail.addRecipient(receiver.email)
       mail.addFrom("Spider App <noreply@spider.com>")
//       mail.sendHtml(content.body)
     }
     
     def inviteAdviser(receiver: User, senderName:String, token: String, applicationBaseUrl: String) = {
       println("in EmailService.inviteContact... ")
       val content = views.html.email.inviteAdviser(receiver.firstName)(senderName)(token)(applicationBaseUrl)
       val mail = use[MailerPlugin].email
       mail.setSubject(s"Hello ${receiver.firstName}")
       mail.addRecipient(receiver.email)
       mail.addFrom("Spider App <noreply@spider.com>")
//       mail.sendHtml(content.body)
     }
}