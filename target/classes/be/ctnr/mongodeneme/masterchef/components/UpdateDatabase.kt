package be.ctnr.mongodeneme.masterchef.components

import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import com.fasterxml.jackson.databind.util.JSONPObject
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.springframework.data.mongodb.core.aggregation.VariableOperators
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.Exception


@Component
class UpdateDatabase(val recipeRepository: RecipeRepository)  {
    var counter = 1
    var newItemCount:Int= 0
    @Scheduled(fixedRate = 1000*60*60*6)
    fun calisiyormusun(){
        val list = MasterchefRest.sendGet(1)
        counter++
        list.forEach {
            val finded = recipeRepository.findByIdOrNull(it.id)
            println(finded?.id ?: "bos geldi");
            println(JsonParser().parse(it.resim.replace("\\","").replace("\\","")).asJsonObject.get("original") ?: "bulamadik")
            if(finded != null ){
                println("bu daha once eklenmis")
            } else {
                println("bu daha once eklenmemis")
                try {

                    recipeRepository.save(it)
                    newItemCount++
                } catch (err:Exception) {
                    print(err)
                }
            }
        }
        if(newItemCount>0){
            println("burda notification gonderilecek")
        }
        println("update calisiyor $counter")
    }
}