package be.ctnr.mongodeneme.masterchef.controllers

import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import com.google.gson.Gson
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.random.Random


@RestController
@RequestMapping("/recipes")

class RecipeControllers(
    val recipeRepository: RecipeRepository,
    val env: Environment
) {

    @GetMapping("/{page}")
    fun getRecipesByPage(@PathVariable("page") page:Int, @RequestHeader("token") token:String):String {
        return if(token == env.getProperty("token")){
            val pageRequest = PageRequest.of(page, 20, Sort.by("id").descending())
            val list = recipeRepository.findAll(pageRequest)
            Gson().toJson(list.content)
        } else {
            Gson().toJson("{\"token\": \"not correct\"}")
        }
    }

    @GetMapping("/id/{id}")
    fun getRecipeByI(@PathVariable("id") id:Int, @RequestHeader("token") token:String):ResponseEntity<String> {
        return if(token == env.getProperty("token")){
            val recipe = recipeRepository.findById(id.toString()).get()
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body( Gson().toJson(recipe))
        } else {
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson("{\"token\": \"not correct\"}"))
        }
    }

    @PostMapping()
    fun saveRecipe(@RequestBody recipe: Recipe, @RequestHeader("token") token:String): String{
       return if(token == env.getProperty("token")){
            Gson().toJson(recipeRepository.save(recipe))//, HttpStatus.CREATED)
        } else {
            Gson().toJson("{\"token\": \"not correct\"}")
        }
    }

    @GetMapping("/random")
    fun getRecipeRandom(@RequestHeader("token") token:String):ResponseEntity<String> {
        return if(token == env.getProperty("token")){
            val recipeList = recipeRepository.findAll()
            val randomInt = Random.nextInt(recipeList.count()-1)
            println("$recipeList   $randomInt")
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body( Gson().toJson(recipeList[randomInt]))
        } else {
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(Gson().toJson("{\"token\": \"not correct\"}"))
        }
    }

    @GetMapping("/startUpdate")
    fun startUpdate(@RequestHeader("token") token:String):String{
        return if(env.getProperty("token") == token){
            for (page in 1..5000){
                MasterchefRest.sendGet(page).let {
                    recipeRepository.saveAll(it).let {
                        println("kaydedildi")
                    }
                }
            }
            "started update"
        } else "token is not correct"

    }


//    @GetMapping("/getStream")
//    fun streamData(@RequestHeader("token") token:String): ResponseEntity<StreamingResponseBody?>? {
//        val responseBody = StreamingResponseBody { response: OutputStream ->
//            for (i in 1..1000) {
//                try {
//                    Thread.sleep(10)
//                    response.write("Data stream line - $i\n".toByteArray())
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        return ResponseEntity.ok()
//            .contentType(MediaType.TEXT_PLAIN)
//            .body(responseBody)
//    }
//
}