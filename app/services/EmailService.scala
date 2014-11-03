package services

import play.mvc._
import com.typesafe.plugin._
import play.api.Play.current
import models.tables._
import models.dtos._
import utils.Configuration

object EmailService {
 
   def inviteContact(receiver: User, senderName:String, token: String) = {
     println("in EmailService.inviteContact... ")
     val content = views.html.email.inviteContact(receiver.firstName)(senderName)(token)(Configuration.applicationBaseUrl)
     val mail = use[MailerPlugin].email
     mail.setSubject(s"Hello ${receiver.firstName}")
     mail.setRecipient(receiver.email)
     mail.setFrom("Araneae Team <noreply@araneae.com>")
     mail.sendHtml(content.body)
   }
   
   def inviteAdviser(receiver: User, senderName:String, token: String) = {
     println("in EmailService.inviteContact... ")
     val content = views.html.email.inviteAdviser(receiver.firstName)(senderName)(token)(Configuration.applicationBaseUrl)
     val mail = use[MailerPlugin].email
     mail.setSubject(s"Hello ${receiver.firstName}")
     mail.setRecipient(receiver.email)
     mail.setFrom("Araneae Team <noreply@araneae.com>")
//       mail.sendHtml(content.body)
   }
   
   def sendWelcomeEmail(receiver: User) = {
     println("in EmailService.sendSignupEmail()")
     val content = views.html.email.welcome(receiver.firstName)(receiver.activationToken)(Configuration.applicationBaseUrl)
     val mail = use[MailerPlugin].email
     mail.setSubject(s"${receiver.firstName}, activate your account now")
     mail.setRecipient(receiver.email)
     mail.setFrom("Araneae Team <noreply@araneae.com>")
     mail.sendHtml(content.body)
   }
   
   def sendOtpEmail(receiver: User, otp: String) = {
     println("in EmailService.sendSignupEmail()")
     val content = views.html.email.otp(receiver.firstName)(otp)(Configuration.applicationBaseUrl)
     val mail = use[MailerPlugin].email
     mail.setSubject(s"${receiver.firstName}, your one time password")
     mail.setRecipient(receiver.email)
     mail.setFrom("Araneae Team <noreply@araneae.com>")
     mail.sendHtml(content.body)
   }
}