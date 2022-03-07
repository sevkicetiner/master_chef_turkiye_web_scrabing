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
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


@Component
class UpdateDatabase(val recipeRepository: RecipeRepository, val env: Environment)  {
    var counter = 1
    var newItemCount:Int= 0
    @Scheduled(fixedDelay = 1000*60*60*3, initialDelay = 30000)
    fun calisiyormusun(){
        val list = MasterchefRest.sendGet(1)
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
//                    it.localImage = Base64.getEncoder().encodeToString(URL("${env.getProperty("imageUrl")}$resimOriginal").openStream().readAllBytes())
                    URL("${env.getProperty("imageUrl")}$resimOriginal").openStream().use {
                        if (!Files.exists(Paths.get("${env.getProperty("localImagePath")}"))) {
                            Files.copy(
                                it,
                                Paths.get(env.getProperty("localImagePath") + resimOriginal.split("/").last()),
                                StandardCopyOption.REPLACE_EXISTING
                            )
                        }
                    }

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