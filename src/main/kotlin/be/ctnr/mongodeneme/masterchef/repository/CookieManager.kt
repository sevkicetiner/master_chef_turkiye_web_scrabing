package be.ctnr.mongodeneme.masterchef.repository

import be.ctnr.mongodeneme.masterchef.model.CookieModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.web.bind.annotation.RequestHeader

interface CookieManager: MongoRepository<CookieModel, String> {
    override fun findAllById(p0: MutableIterable<String>): MutableIterable<CookieModel>
}