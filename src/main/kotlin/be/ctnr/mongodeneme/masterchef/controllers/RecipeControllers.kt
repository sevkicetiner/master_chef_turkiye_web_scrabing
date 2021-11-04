package be.ctnr.mongodeneme.masterchef.controllers
import be.ctnr.mongodeneme.masterchef.model.Recipe
import be.ctnr.mongodeneme.masterchef.model.RecipeList
import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import com.google.gson.Gson
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/recipes")

class RecipeControllers(val recipeRepository: RecipeRepository) {

    @GetMapping("/{page}")
    fun getRecipesByPage(@PathVariable("page") page:Int):String {
        val pageRequest = PageRequest.of(page, 20, Sort.by("id").descending())
        val list = recipeRepository.findAll(pageRequest)
        return Gson().toJson(list.content)
    }

    @PostMapping()
    fun saveRecipe(@RequestBody recipe: Recipe): ResponseEntity<Recipe>{
        return ResponseEntity<Recipe>(recipeRepository.save(recipe), HttpStatus.CREATED);
    }


}