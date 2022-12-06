package com.khvatid.postal_office_spring.data.service


import com.khvatid.postal_office_spring.data.repository.PostalRepositoryImp
import com.khvatid.postal_office_spring.domain.model.MailModel
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.stereotype.Service
import java.util.Locale

@Service
class PostalService(
  private val repository: PostalRepositoryImp, private val messages: MessageSource
) {

  fun getPostalOffice(id: String): PostalOfficeModel.Mongo? {
    return repository.findOfficeById(id)
  }

  fun getMailFromOfficeById(officeId: String, id: String): MailModel.Mongo? {
    return repository.findMailInOfficeById(officeId, id)
  }

  fun createPostalOffice(postalOffice: PostalOfficeModel.Mongo, locale: Locale): ResponseEntity<Any> {
    return try {
      val result = repository.createOffice(postalOffice)
      ResponseEntity(
        getMessage("office.create.message", arrayOf(result.id.toString()), locale), HttpStatus.OK
      )
    } catch (e: Exception) {
      ResponseEntity(getMessage(e), HttpStatus.BAD_REQUEST)
    }
  }

  fun createMailInPostalOffice(
    officeId: String,
    mail: MailModel.Mongo,
    locale: Locale
  ): ResponseEntity<Any> {
    return try {
      repository.createMailInOfficeById(officeId, mail)
      ResponseEntity(
        getMessage("mail.create.message", arrayOf(officeId, mail.id.toString()), locale), HttpStatus.OK
      )
    } catch (e: Exception) {
      ResponseEntity(getMessage(e), HttpStatus.BAD_REQUEST)
    }
  }

  fun updatePostalOffice(
    officeId: String,
    officeModel: PostalOfficeModel.Request,
    locale: Locale
  ): ResponseEntity<Any> {
    return try {
      val result = repository.updateOfficeById(officeId, officeModel)
      ResponseEntity(
        getMessage("office.update.message", arrayOf(result.modifiedCount), locale), HttpStatus.OK
      )
    } catch (e: Exception) {
      ResponseEntity(getMessage(e), HttpStatus.BAD_REQUEST)
    }
  }

  fun updateMailInPostalOffice(
    officeId: String,
    mailId: String,
    mail: MailModel.Mongo,
    locale: Locale
  ): ResponseEntity<Any> {
    return try {
      val result = repository.updateMailInOfficeById(officeId, mailId, mail)
      ResponseEntity(
        getMessage("mail.update.message", arrayOf(result.modifiedCount), locale), HttpStatus.OK
      )
    } catch (e: Exception) {
      ResponseEntity(getMessage(e), HttpStatus.BAD_REQUEST)
    }
  }

  fun deletePostalOffice(officeId: String, locale: Locale): ResponseEntity<Any> {
    return try {
      val result = repository.deleteOfficeById(officeId)
      ResponseEntity(
        getMessage(mProperties = "office.delete.message", arrayOf(result.deletedCount), locale),
        HttpStatus.OK
      )
    } catch (e: Exception) {
      ResponseEntity(getMessage(e), HttpStatus.BAD_REQUEST)
    }
  }

  fun deleteMailInOffice(
    officeId: String,
    mailId: String,
    locale: Locale
  ): ResponseEntity<Any> {
    return try {
      val result = repository.deleteMailInOfficeById(officeId, mailId)
      ResponseEntity(
        getMessage("mail.delete.message", arrayOf(officeId, mailId), locale), HttpStatus.OK
      )
    } catch (e: Exception) {
      ResponseEntity(getMessage(e), HttpStatus.BAD_REQUEST)
    }
  }


  private fun getMessage(mProperties: String, arg: Array<Any>, locale: Locale): String {
    return String.format(messages.getMessage(mProperties, arg, locale))
  }

  private fun getMessage(e: java.lang.Exception): String {
    return e.toString().plus(e.message)
  }
}