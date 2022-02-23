package be.ctnr.mongodeneme.masterchef.components

import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import com.fasterxml.jackson.databind.util.JSONPObject
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.aggregation.VariableOperators
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.Exception
import java.net.URL
import java.util.*


@Component
class UpdateDatabase(val recipeRepository: RecipeRepository, val env: Environment)  {
    var counter = 1
    var newItemCount:Int= 0
    @Scheduled(fixedRate = 1000*60*60*6)
    fun calisiyormusun(){
        val list = MasterchefRest.sendGet(counter)
        counter++
        list.forEach {
            val finded = recipeRepository.findByIdOrNull(it.id)
            println(JsonParser().parse(it.resim.replace("\\","").replace("\\","")).asJsonObject.get("original") ?: "bulamadik")
            if(finded != null ){
                println("bu daha once eklenmis")
            } else {
                println("bu daha once eklenmemis")
                try {
                    val resimOriginal:String = JsonParser()
                        .parse(it.resim.replace("\\","").replace("\\",""))
                        .asJsonObject
                        .get("original").asString ?: "null"
                    it.localImage = Base64.getEncoder().encodeToString(URL("${env.getProperty("imageUrl")}$resimOriginal").openStream().readAllBytes())
                    recipeRepository.save(it).let {
                        println("${it.baslik} kaydedildi")
                    }
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