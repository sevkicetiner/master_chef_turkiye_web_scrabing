package dev.ctnr.master_chef_web_scraping.reactions.repository

import dev.ctnr.master_chef_web_scraping.reactions.model.Reactions
import org.springframework.data.mongodb.repository.MongoRepository

interface ReactionsRepository : MongoRepository<Reactions, String> {
    override fun findAllById(p0: MutableIterable<String>): MutableIterable<Reactions>
    override fun <S : Reactions?> insert(p0: MutableIterable<S>): MutableList<S>
    override fun <S : Reactions?> insert(p0: S): S
    override fun <S : Reactions?> save(p0: S): S
    override fun delete(p0: Reactions)
}