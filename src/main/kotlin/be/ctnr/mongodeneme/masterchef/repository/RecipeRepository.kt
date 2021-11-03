package be.ctnr.mongodeneme.masterchef.repository

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.model.RecipeList
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RecipeRepository :MongoRepository<Recipe, String> {
    override fun <S : Recipe?> insert(p0: S): S
    override fun findById(p0: String): Optional<Recipe>
    override fun <S : Recipe?> insert(p0: MutableIterable<S>): MutableList<S>
}