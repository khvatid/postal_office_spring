package com.khvatid.postal_office_spring.domain.repository

import com.khvatid.postal_office_spring.domain.model.MailModel
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult

interface PostalRepository {

  fun findOfficeById(id: String): PostalOfficeModel.Mongo?

  fun findMailInOfficeById(officeId: String, id: String): MailModel.Mongo?

  fun createOffice(officeModel: PostalOfficeModel.Mongo): PostalOfficeModel.Mongo

  fun createMailInOfficeById(officeId: String, mailModel: MailModel.Mongo): UpdateResult

  fun updateOfficeById(officeId: String, officeModel: PostalOfficeModel.Request): UpdateResult

  fun updateMailInOfficeById(officeId: String, mailId: String, mailModel: MailModel.Mongo): UpdateResult

  fun deleteOfficeById(officeId: String): DeleteResult

  fun deleteMailInOfficeById(officeId: String, mailId: String): UpdateResult

}