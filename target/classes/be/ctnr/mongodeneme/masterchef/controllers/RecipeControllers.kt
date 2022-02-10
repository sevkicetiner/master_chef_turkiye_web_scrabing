package be.ctnr.mongodeneme.masterchef.controllers

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.apache.catalina.User
import org.bson.Document
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.messaging.*
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.net.URL
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
            for (page in 1..10) {
                MasterchefRest.sendGet(page).let { recipeList ->

                    recipeList.forEach { item ->
                        val resimOriginal = JsonParser().parse(item.resim.replace("\\","").replace("\\","")).asJsonObject.get("original") ?: "bulamadik")
//                        item.photo = Base64.getEncoder()
//                            .encodeToString(
//                                URL("https://img.acunn.com/${(Gson().toJson(item.resim).toString())}")
//                                .openStream()
//                                .readAllBytes())
//                        recipeRepository.save(item)
                    }
                }//{"original":"uploads\/icerikler\/2021\/11\/04\/binardkavurmasitarifi104918562461841be2cb46e.jpg","300_452":"uploads\/icerikler\/2021\/11\/04\/300_452_4-kasim-masterchef-2021-binard-kavurmasi-salgam-turbu-tarifi-malzemeler-puf-noktasi-357.61589403973517x540.0000000000001_61841be2ebffe.jpg","620_340":"uploads\/icerikler\/2021\/11\/04\/620_340_4-kasim-masterchef-2021-binard-kavurmasi-salgam-turbu-tarifi-malzemeler-puf-noktasi-750x409.4202898550725_61841be2f047c.jpg","620_400":"uploads\/icerikler\/2021\/11\/04\/620_400_4-kasim-masterchef-2021-binard-kavurmasi-salgam-turbu-tarifi-malzemeler-puf-noktasi-750x481.88405797101444_61841be3012db.jpg"}
            }
            "started update"
        } else "token is not correct"

    }
}