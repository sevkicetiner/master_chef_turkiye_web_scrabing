package be.ctnr.mongodeneme.masterchef.repository

import be.ctnr.mongodeneme.masterchef.model.Recipe
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

interface RecipeRepository :MongoRepository<Recipe, String> {
    override fun <S : Recipe?> insert(p0: S): S
    override fun findById(p0: String): Optional<Recipe>
    override fun <S : Recipe?> insert(p0: MutableIterable<S>): MutableList<S>
    override fun findAll(): MutableList<Recipe>
    override fun <S : Recipe?> save(p0: S): S
}