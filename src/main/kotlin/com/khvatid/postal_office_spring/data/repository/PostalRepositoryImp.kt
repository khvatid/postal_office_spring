package com.khvatid.postal_office_spring.data.repository

import com.khvatid.postal_office_spring.domain.model.MailModel
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel
import com.khvatid.postal_office_spring.domain.repository.PostalRepository
import com.mongodb.client.MongoClient
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.*
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation
import org.springframework.data.mongodb.core.aggregation.UnwindOperation
import org.springframework.data.mongodb.core.query.*
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Component
class PostalRepositoryImp(private val mongoTemplate: MongoTemplate) : PostalRepository {

  override fun findOfficeById(id: String): PostalOfficeModel.Mongo? {
    return mongoTemplate.findById(id = id)
  }

  override fun findMailInOfficeById(officeId: String, id: String): MailModel.Mongo? {
    val match: MatchOperation = Aggregation.match(Criteria("_id").`is`(ObjectId(officeId)))
    val unwind: UnwindOperation = Aggregation.unwind("\$mails", true)
    val replaceRoot: ReplaceRootOperation = Aggregation.replaceRoot("\$mails")
    val match2: MatchOperation = Aggregation.match(Criteria("_id").`is`(ObjectId(id)))
    val aggregation = Aggregation.newAggregation(match, unwind, replaceRoot, match2)
    val result: AggregationResults<MailModel.Mongo> =
      mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MailModel.Mongo::class.java)
    return result.uniqueMappedResult
  }

  override fun createOffice(officeModel: PostalOfficeModel.Mongo): PostalOfficeModel.Mongo {
    return mongoTemplate.save(officeModel, COLLECTION_NAME)
  }

  override fun createMailInOfficeById(officeId: String, mailModel: MailModel.Mongo): UpdateResult {
    val update: Update = Update().push("mails", mailModel)
    return mongoTemplate.upsert(
      Query.query(
        Criteria.where("_id").`is`(
          ObjectId(officeId)
        )
      ), update, COLLECTION_NAME
    )
  }

  override fun updateOfficeById(officeId: String, officeModel: PostalOfficeModel.Request): UpdateResult {
    val update: Update = Update().set("address", officeModel.address).set("status", officeModel.status)
    return mongoTemplate.updateFirst(
      Query(
        Criteria.where("_id").`is`(ObjectId(officeId))
      ), update, PostalOfficeModel.Mongo::class.java, COLLECTION_NAME
    )
  }

  override fun updateMailInOfficeById(officeId: String, mailId: String, mailModel: MailModel.Mongo): UpdateResult {
    val update: Update =
      Update()
        .set("mails.\$.sender", mailModel.sender)
        .set("mails.\$.recipient", mailModel.recipient)
        .set("mails.\$.weight", mailModel.weight)
        .set("mails.\$.status", mailModel.status)
    return mongoTemplate.updateFirst(
      Query(
        Criteria.where("_id").`is`(ObjectId(officeId)).and("mails._id").`is`(ObjectId(mailId))
      ), update, COLLECTION_NAME
    )
  }

  override fun deleteOfficeById(officeId: String): DeleteResult {
    return mongoTemplate.remove(
      Query(
        Criteria.where("_id").`is`(ObjectId(officeId))
      ), COLLECTION_NAME
    )
  }

  override fun deleteMailInOfficeById(officeId: String, mailId: String): UpdateResult {
    val update: Update = Update().pull(
      "mails",
      Query
        .query(
          Criteria.where("_id").`is`(ObjectId(mailId))
        )
    )
    return mongoTemplate.updateFirst(
      Query(
        Criteria.where("_id").`is`(ObjectId(officeId))
      ),
      update, COLLECTION_NAME
    )
  }

  companion object {
    private const val COLLECTION_NAME: String = "postal_offices"
  }

}
