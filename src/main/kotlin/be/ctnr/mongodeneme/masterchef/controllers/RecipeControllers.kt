package be.ctnr.mongodeneme.masterchef.controllers

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.model.RecipeList
import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.springframework.boot.json.GsonJsonParser
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/recipes")
class RecipeControllers(val recipeRepository: RecipeRepository) {

    @GetMapping("/{page}")
    fun getRecipesByPage(@PathVariable("page") page:Int):String {
        return Gson().toJson(recipeRepository.findAll(Pageable.ofSize(20).withPage(page)).content)
    }

    @PostMapping()
    fun saveRecipe(@RequestBody recipe: Recipe): ResponseEntity<Recipe>{
        return ResponseEntity<Recipe>(recipeRepository.save(recipe), HttpStatus.CREATED);
    }
}