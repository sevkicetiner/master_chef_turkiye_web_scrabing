package be.ctnr.mongodeneme.masterchef.components

import be.ctnr.mongodeneme.masterchef.repository.RecipeRepository
import be.ctnr.mongodeneme.masterchef.utils.MasterchefRest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.Exception


@Component
class UpdateDatabase(val recipeRepository: RecipeRepository)  {
    var counter = 1
    @Scheduled(fixedRate = 1000*60*60)
    fun calisiyormusun(){
        val list = MasterchefRest.sendGet(1)
        counter +=1
        list.forEach {

            val finded = recipeRepository.findById(it.id)
            if(finded.isEmpty){
                println("bu daha once eklenmemis")
            }else {
                try {
                    recipeRepository.insert(it)
                } catch (err:Exception) {
                    print(err)
                }
            }
        }
        println("calisiyoruz $counter")
    }
}