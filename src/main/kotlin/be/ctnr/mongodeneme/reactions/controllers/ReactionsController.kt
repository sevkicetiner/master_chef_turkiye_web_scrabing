package be.ctnr.mongodeneme.reactions.controllers

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.reactions.model.Reactions
import be.ctnr.mongodeneme.reactions.repository.ReactionsRepository
import com.google.gson.Gson
import org.apache.coyote.Response
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/reactions")
class ReactionsController(
    val repository: ReactionsRepository,
    val env: Environment
    ) {

    @PostMapping("/addComment")
    fun addComment(@RequestBody reactions: Reactions, @RequestHeader("token") token: String): ResponseEntity<String>{
        print(token)
        return ResponseEntity
            .status(200)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Gson().toJson(repository.insert(reactions)))
    }

    @GetMapping("/getComments/{recipeId}")
    fun getCommentsByID(
        @PathVariable("recipeId") recipeId: String,
        @RequestHeader("token") token: String): ResponseEntity<String> {
        return ResponseEntity
            .status(200)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Gson().toJson(repository.findAllById(mutableListOf(recipeId))))
    }

    @PostMapping("/update")
    fun updateReaction(@RequestBody reaction:Reactions):ResponseEntity<String>{
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson(repository.save(reaction)))
    }

    @PostMapping("/delete")
    fun deleteReaction(@RequestBody reactions: Reactions):ResponseEntity<String>{
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson(repository.delete(reactions)))
    }
}