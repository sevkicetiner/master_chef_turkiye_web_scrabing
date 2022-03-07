package be.ctnr.mongodeneme.reactions.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("reactions")
data class Reactions(
    @Id()
    val commentId:String,
    val recipeID:String,
    val commentText:String,
    val subComments:Map<String,String>?,
    val subEmojies:Map<String,String>?,
    val userID:String,
    val mediaBase64:String
)
