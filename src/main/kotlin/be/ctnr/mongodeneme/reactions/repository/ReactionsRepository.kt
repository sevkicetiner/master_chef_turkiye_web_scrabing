package be.ctnr.mongodeneme.reactions.repository

import be.ctnr.mongodeneme.reactions.model.Reactions
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ReactionsRepository : MongoRepository<Reactions, String> {
    override fun findAllById(p0: MutableIterable<String>): MutableIterable<Reactions>
    override fun <S : Reactions?> insert(p0: MutableIterable<S>): MutableList<S>
    override fun <S : Reactions?> insert(p0: S): S
    override fun <S : Reactions?> save(p0: S): S
    override fun delete(p0: Reactions)
}