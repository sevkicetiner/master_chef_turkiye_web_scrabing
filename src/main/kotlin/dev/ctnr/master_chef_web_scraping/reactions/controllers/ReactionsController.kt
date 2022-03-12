package dev.ctnr.master_chef_web_scraping.reactions.controllers

import dev.ctnr.master_chef_web_scraping.reactions.model.Reactions
import dev.ctnr.master_chef_web_scraping.reactions.repository.ReactionsRepository
import com.google.gson.Gson
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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