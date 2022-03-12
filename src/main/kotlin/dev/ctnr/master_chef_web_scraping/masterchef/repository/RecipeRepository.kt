package dev.ctnr.master_chef_web_scraping.masterchef.repository

import dev.ctnr.master_chef_web_scraping.masterchef.model.Recipe
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RecipeRepository :MongoRepository<Recipe, String> {
    override fun <S : Recipe?> insert(p0: S): S
    override fun findById(p0: String): Optional<Recipe>
    override fun <S : Recipe?> insert(p0: MutableIterable<S>): MutableList<S>
    override fun findAll(): MutableList<Recipe>
    override fun <S : Recipe?> save(p0: S): S
}