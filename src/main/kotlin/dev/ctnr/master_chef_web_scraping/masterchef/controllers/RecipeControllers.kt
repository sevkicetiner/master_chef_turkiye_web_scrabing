package dev.ctnr.master_chef_web_scraping.masterchef.controllers

import dev.ctnr.master_chef_web_scraping.masterchef.model.Recipe
import dev.ctnr.master_chef_web_scraping.masterchef.repository.RecipeRepository
import dev.ctnr.master_chef_web_scraping.masterchef.utils.MasterchefRest
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.SampleOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.*
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path


@RestController
@RequestMapping("/recipes")
class RecipeControllers(
    @Autowired val recipeRepository: RecipeRepository,
    @Autowired val env: Environment,
    @Autowired val mongoOperations: MongoOperations,
) {

    @GetMapping("/{page}")
    fun getRecipesByPage(@PathVariable("page") page: Int, @RequestHeader("token") token: String): String {
        return if (token == env.getProperty("token")) {
            val pageRequest = PageRequest.of(page, 10, Sort.by("_id").descending())
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
            Gson().toJson(recipeRepository.save(recipe))
        } else {
            Gson().toJson("{\"token\": \"not correct\"}")
        }
    }

    @GetMapping("/random")
    fun getRecipeRandom(@RequestHeader("token") token: String): ResponseEntity<String> {

        val match: SampleOperation? = SampleOperation(1)
        val aggregate = Aggregation.newAggregation(SampleOperation(1))
        val orderAggregate: AggregationResults<Recipe> =
            mongoOperations.aggregate(aggregate, Recipe::class.java, Recipe::class.java)
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
            .body(Gson().toJson(orderAggregate.getMappedResults()[0]))
    }

    @GetMapping("/startUpdate")
    fun startUpdate(@RequestHeader("token") token: String): String {
        println("update started")
        return if (env.getProperty("token") == token) {
            var page = 1
            var loop = true
            while (loop) {
                MasterchefRest.sendGet(page).let { recipeList ->
                    page++
                    if (recipeList.size == 0)
                        loop = false
                    recipeList.forEach { item ->
                        saveItemWithImage(item)
                    }
                    println(page)
                }
            }
            "database updated"
        } else "token is not correct"
    }

    @Async
    fun saveItemWithImage(item: Recipe) {
        val resimOriginal: String = JsonParser()
            .parse(item.resim.replace("\\", "").replace("\\", ""))
            .asJsonObject
            .get("original").asString ?: "null"
        if (resimOriginal != "null") {
            URL("${env.getProperty("imageUrl")}$resimOriginal").openStream().use { inputstream ->
                val filename = resimOriginal.split("/").last();
                try{
                    Files.copy(
                        inputstream,
                        Paths.get(env.getProperty("localImagePath") + filename),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }catch (e:Exception){
                    println(e.message)
                }
                item.localImage = filename
                println("resim indirildi")
            }
        }
        recipeRepository.save(item)
    }

    @GetMapping("/getImage/{image}")
    fun image(@PathVariable("image") image: String): ResponseEntity<Resource?> {
        return try {
            val inputStream = ByteArrayResource(
                Files.readAllBytes(Path("${env.getProperty("localImagePath")}$image"))
            )
            ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(inputStream.contentLength())
                .body(inputStream)
        } catch (e:Exception) {
            print(e)
            ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).body(null)
        }
    }

    @GetMapping("/search")
    fun search(@RequestHeader("searchText") searchText:String, @RequestHeader("token") token:String) : ResponseEntity<String> {
        return if (token == env.getProperty("token")) {
            try {
                val query = Query()
                query.addCriteria(Criteria.where("baslik_orig").regex( ".*$searchText.*")) //.regex(toLikeRegex(like))
                val response = mongoOperations.find(query, Recipe::class.java)
                ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson(response))
            }catch (e:Exception){
                ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson(e.message))
            }

        } else {
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(Gson().toJson("{\"token\": \"not correct\"}"))
        }
    }
}