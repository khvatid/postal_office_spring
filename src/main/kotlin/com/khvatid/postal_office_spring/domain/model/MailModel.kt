package com.khvatid.postal_office_spring.domain.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.hateoas.RepresentationModel


@Serializable
sealed class MailModel() {

  @Serializable
  data class Request(
    val sender: String,
    val recipient: String,
    val weight: Float,
    val status: String = "Await"
  ) : MailModel()

  /* @Serializable
   data class Response(
     val id: String,
     val sender: String,
     val recipient: String,
     val weight: Float,
     val status: String,
   ) : MailModel()*/

  @Document("mail")
  data class Mongo(
    @Id val id: ObjectId,
    val sender: String,
    val recipient: String,
    val weight: Float,
    val status: String,
  )

  companion object {
    fun Request.toMongo(): Mongo {
      return Mongo(id = ObjectId.get(), sender = sender, recipient = recipient, weight = weight, status = status)
    }

    data class Response(
      val id: String,
      val sender: String,
      val recipient: String,
      val weight: Float,
      val status: String
    ) : RepresentationModel<Response>()

    fun Mongo.toResponse(): Response {
      return Response(id.toHexString(), sender, recipient, weight, status)
    }
  }
}


