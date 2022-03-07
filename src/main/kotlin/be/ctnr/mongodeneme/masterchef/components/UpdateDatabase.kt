package be.ctnr.mongodeneme.masterchef.components

import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import com.google.gson.JsonParser
import org.springframework.core.env.Environment
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.Exception


@Component
class UpdateDatabase(val recipeRepository: RecipeRepository, val env: Environment)  {
    var counter = 1
    var newItemCount:Int= 0
    @Scheduled(fixedDelay = 1000*60*60*1, initialDelay = 30000)
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
                        if (!Files.exists(Paths.get(env.getProperty("localImagePath")+resimOriginal.split("/").last()))) {
                            try {
                                Files.copy(
                                    it,
                                    Paths.get(env.getProperty("localImagePath") + resimOriginal.split("/").last()),
                                    StandardCopyOption.REPLACE_EXISTING
                                )
                            }catch (e:Exception){
                                println(e.message)
                            }
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