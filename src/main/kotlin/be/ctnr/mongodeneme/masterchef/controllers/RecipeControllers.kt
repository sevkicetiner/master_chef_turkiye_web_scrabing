package be.ctnr.mongodeneme.masterchef.controllers

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.model.RecipeList
import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.*
import org.springframework.data.mongodb.core.messaging.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.util.*


@RestController
@RequestMapping("/recipes")

class RecipeControllers(
    val recipeRepository: RecipeRepository,
    val env: Environment,
    val mongoOperations: MongoOperations
) {

    @GetMapping("/{page}")
    fun getRecipesByPage(@PathVariable("page") page: Int, @RequestHeader("token") token: String): String {
        return if (token == env.getProperty("token")) {
            val pageRequest = PageRequest.of(page, 10, Sort.by("_id").descending())
            val list = recipeRepository.findAll(pageRequest)
            println("istek alindi")
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

        val match: SampleOperation? = SampleOperation(1)
        val aggregate = Aggregation.newAggregation(SampleOperation(1))

        val orderAggregate: AggregationResults<Recipe> = mongoOperations.aggregate(aggregate, Recipe::class.java, Recipe::class.java)
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson(orderAggregate.getMappedResults()[0]))

    }

//    @GetMapping("/startUpdate")
//    fun startUpdate(@RequestHeader("token") token: String): String {
//        println("update started")
//        return if (env.getProperty("token") == token) {
//            var page = 74
//            var loop = true
//            while (loop) {
//                MasterchefRest.sendGet(page).let { recipeList ->
//                    page++
//                    if(recipeList.size == 0)
//                        loop = false
//                    recipeList.forEach { item ->
//                        if(!recipeRepository.existsById(item.id)) {
//                                saveItemWithImage(item)
//                        }else println("eklenmis$page")
//                    }
//                }
//            }
//            "database updated"
//        } else "token is not correct"
//    }

    @Async
    fun saveItemWithImage(item:Recipe) {
        val resimOriginal: String = JsonParser()
            .parse(item.resim.replace("\\", "").replace("\\", ""))
            .asJsonObject
            .get("original").asString ?: "null"
        if(resimOriginal != "null"){
            item.localImage = Base64.getEncoder().encodeToString(
                URL("${env.getProperty("imageUrl")}$resimOriginal").openStream().readAllBytes()
            )
        }
        recipeRepository.save(item)
    }
}