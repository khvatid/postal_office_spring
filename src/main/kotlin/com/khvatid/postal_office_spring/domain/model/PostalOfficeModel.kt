package com.khvatid.postal_office_spring.domain.model


import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.linkTo


@Serializable
sealed class PostalOfficeModel {

  @Serializable
  data class Request(
    val address: String,
    val status: String = "Close"
  ) : PostalOfficeModel()

  /*
    @Serializable
    data class Response(
      val id: String,
      val address: String,
      val mails: List<MailModel>,
      val status: String
    ) : PostalOfficeModel()
  */

  @Document("postal_offices")
  data class Mongo(
    @Id val id: ObjectId = ObjectId.get(),
    val address: String,
    val mails: List<MailModel>,
    val status: String
  )

  companion object {
    fun Request.toMongo(): Mongo {
      return Mongo(id = ObjectId.get(), address = address, mails = listOf(), status = status)
    }

    data class Response(
      val id: String,
      val address: String,
      val mails: List<MailModel>,
      val status: String
    ) : RepresentationModel<Response>()

    fun Mongo.toResponse(): Response {
      return Response(id.toHexString(), address, mails, status)
    }

  }
}


