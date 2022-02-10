package be.ctnr.mongodeneme.masterchef.controllers

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.messaging.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.random.Random


@RestController
@RequestMapping("/recipes")

class RecipeControllers(
    val recipeRepository: RecipeRepository,
    val env: Environment
) {

    @GetMapping("/{page}")
    fun getRecipesByPage(@PathVariable("page") page: Int, @RequestHeader("token") token: String): String {
        return if (token == env.getProperty("token")) {
            val pageRequest = PageRequest.of(page, 20, Sort.by("id").descending())
            val list = recipeRepository.findAll(pageRequest)
            Gson().toJson(list.content)
        } else {
            Gson().toJson("{\"token\": \"not correct\"}")
        }
    }

    @GetMapping("/id/{id}")
    fun getRecipeByI(@PathVariable("id") id: Int, @RequestHeader("token") token: String): ResponseEntity<String> {
        return if (token == env.getProperty("token")) {
            val recipe = recipeRepository.findById(id.toString()).get()
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson(recipe))
        } else {
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(Gson().toJson("{\"token\": \"not correct\"}"))
        }
    }

    @PostMapping()
    fun saveRecipe(@RequestBody recipe: Recipe, @RequestHeader("token") token: String): String {
        return if (token == env.getProperty("token")) {
            Gson().toJson(recipeRepository.save(recipe))//, HttpStatus.CREATED)
        } else {
            Gson().toJson("{\"token\": \"not correct\"}")
        }
    }

    @GetMapping("/random")
    fun getRecipeRandom(@RequestHeader("token") token: String): ResponseEntity<String> {
        return if (token == env.getProperty("token")) {
            val recipeList = recipeRepository.findAll()
            val randomInt = Random.nextInt(recipeList.count() - 1)
            println("$recipeList   $randomInt")
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(Gson().toJson(recipeList[randomInt]))
        } else {
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(Gson().toJson("{\"token\": \"not correct\"}"))
        }
    }

    @GetMapping("/startUpdate")
    fun startUpdate(@RequestHeader("token") token: String): String {
        return if (env.getProperty("token") == token) {
            var page = 1
            var loop = true
            while (loop) {
                MasterchefRest.sendGet(page).let { recipeList ->
                    if(recipeList.size == 0)
                        loop = false

                    recipeList.forEach { item ->
                        val resimOriginal:String = JsonParser().parse(item.resim.replace("\\","").replace("\\","")).asJsonObject.get("original").asString ?: "bulamadik"
                        resimOriginal.let {
                            val url = URL("https://img.acunn.com/$resimOriginal")
                            val inputstream = url.openStream()
                            Files.copy(inputstream, Paths.get("${env.getProperty("imagePath")}${resimOriginal.split("/").last()}"), StandardCopyOption.REPLACE_EXISTING)
                            inputstream.close()
                        }
                        recipeRepository.save(item)
                    }
                }
            }
            "started update"
        } else "token is not correct"

    }
}