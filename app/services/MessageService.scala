package services

import play._
import play.mvc._
import com.typesafe.plugin._
import play.api.Play.current
import models.tables._
import models.dtos._
import models.repositories._

object MessageService {
 
     def send(senderUserId: Long, parentMessageId: Option[Long], subject: String, body: String, recipientUserIds : List[Long]) = {
       // create a message
       val message = Message(None, parentMessageId, senderUserId, false, subject, Some(body), senderUserId)
       val messageId = MessageRepository.create(message)
          
       // iterate through the recipients
       recipientUserIds.map{ receiverUserId =>
                                // send message to recipient
                                // find recipient's inbox
                                val inbox = MessageBoxRepository.findInbox(receiverUserId)
                                inbox match {
                                  case Some(box) =>
                                            // add the recipient
                                            val recipient = MessageRecipient(receiverUserId, messageId, false, senderUserId)
                                            MessageRecipientRepository.create(recipient)
                                            
                                            // add the message into recipient's inbox
                                            val userMessage = UserMessage(receiverUserId, 
                                                                          messageId,
                                                                          box.messageBoxId.get,
                                                                          false,
                                                                          false,
                                                                          false,
                                                                          false,
                                                                          senderUserId)
                                            UserMessageRepository.create(userMessage)
                                  case None => 
                                              // ignore the recipient
                                }
                             }
          
          // add the message in the user's sent items 
          val sentItem = MessageBoxRepository.findSentItems(senderUserId)
          sentItem match {
            case Some(box) => 
                          // add the message into recipient's inbox
                          val userMessage = UserMessage(senderUserId, 
                                                        messageId,
                                                        box.messageBoxId.get,
                                                        true,
                                                        false,
                                                        false,
                                                        false,
                                                        senderUserId)
                          UserMessageRepository.create(userMessage)
            case None => 
                          // error - ignore for now
          }
          
          // mark the original user message as replied
          parentMessageId match {
              case Some(orgMsgId) => UserMessageRepository.markReplied(orgMsgId, senderUserId)
              case None => // nothing to update
          }
     }
}