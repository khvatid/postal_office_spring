package com.khvatid.postal_office_spring.presentation.controller


import com.khvatid.postal_office_spring.data.service.PostalService
import com.khvatid.postal_office_spring.domain.controller.SpringController
import com.khvatid.postal_office_spring.domain.model.MailModel
import com.khvatid.postal_office_spring.domain.model.MailModel.Companion.toMongo
import com.khvatid.postal_office_spring.domain.model.MailModel.Companion.toResponse
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel.Companion.toMongo
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel.Companion.toResponse
import org.springframework.context.MessageSource
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class SpringControllerImp(
  private val service: PostalService,
  private val messages: MessageSource
) : SpringController {

  override fun getPostalOffice(officeId: String, locale: Locale): ResponseEntity<Any> {
    val office = service.getPostalOffice(officeId)?.toResponse()
    return if (office != null) {
      office.add(
        linkTo<SpringController> {
          postPostalOffice(PostalOfficeModel.Request(""), locale)
        }.withRel(getHateoas(mProperties = "hateoas.create.message", emptyArray(), locale)),
        linkTo<SpringController> {
          putPostOffice(officeId, officeModel = PostalOfficeModel.Request(address = ""), locale)
        }.withRel(getHateoas(mProperties = "hateoas.update.message", emptyArray(), locale)),
        linkTo<SpringController> {
          deletePostalOffice(officeId, locale)
        }.withRel(getHateoas(mProperties = "hateoas.delete.message", emptyArray(), locale)),
      )
      ResponseEntity(office, HttpStatus.OK)
    } else {
      ResponseEntity(HttpStatus.BAD_REQUEST)
    }
  }

  override fun getMail(officeId: String, id: String, locale: Locale): ResponseEntity<Any> {
    val mail = service.getMailFromOfficeById(officeId, id)?.toResponse()
    return if (mail != null) {
      mail.add(
        linkTo<SpringController> {
          postMail(officeId, MailModel.Request("", "", 0f), locale)
        }.withRel(getHateoas(mProperties = "hateoas.create.message", emptyArray(), locale)),
        linkTo<SpringController> {
          putMail(officeId, mail.id, MailModel.Request("", "", 0f), locale)
        }.withRel(getHateoas(mProperties = "hateoas.update.message", emptyArray(), locale)),
        linkTo<SpringController> {
          deleteMail(officeId, mail.id, Locale.getDefault())
        }.withRel(getHateoas(mProperties = "hateoas.delete.message", emptyArray(), locale)),
      )
      ResponseEntity(mail, HttpStatus.OK)
    } else {
      ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  override fun postPostalOffice(
    postalOfficeModel: PostalOfficeModel.Request,
    locale: Locale
  ): ResponseEntity<Any> {
    return service.createPostalOffice(postalOfficeModel.toMongo(), locale)
  }


  override fun postMail(
    officeId: String,
    mailModel: MailModel.Request,
    locale: Locale
  ): ResponseEntity<Any> {
    return service.createMailInPostalOffice(officeId, mailModel.toMongo(), locale)
  }

  override fun putPostOffice(
    officeId: String,
    officeModel: PostalOfficeModel.Request,
    locale: Locale
  ): ResponseEntity<Any> {
    return service.updatePostalOffice(officeId, officeModel, locale)
  }

  override fun putMail(
    officeId: String,
    mailId: String,
    mailModel: MailModel.Request,
    locale: Locale
  ): ResponseEntity<Any> {
    return service.updateMailInPostalOffice(officeId, mailId, mailModel.toMongo(), locale)
  }


  override fun deletePostalOffice(
    officeId: String,
    locale: Locale
  ): ResponseEntity<Any> {
    return service.deletePostalOffice(officeId, locale)
  }

  override fun deleteMail(
    officeId: String,
    id: String,
    locale: Locale
  ): ResponseEntity<Any> {
    return service.deleteMailInOffice(officeId, id, locale)
  }

  private fun getHateoas(mProperties: String, arg: Array<Any>, locale: Locale): String {
    return String.format(messages.getMessage(mProperties, arg, locale))
  }

}


