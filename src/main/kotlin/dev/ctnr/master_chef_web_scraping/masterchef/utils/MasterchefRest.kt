package dev.ctnr.master_chef_web_scraping.masterchef.utils

import dev.ctnr.master_chef_web_scraping.masterchef.model.RecipeList
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class MasterchefRest {
    companion object {
        fun sendGet(page: Int): RecipeList {
            val url = URL("https://www.acunn.com/Ajax/icerik/haberler/1202/$page?type=json")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                var response = ""
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response+=line
                    }
                }
                return Gson().fromJson(response, RecipeList::class.java);
            }
        }
        suspend fun sendGetSuspend(page: Int):RecipeList {
            val url = URL("https://www.acunn.com/Ajax/icerik/haberler/1202/$page?type=json")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                var response = ""
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response+=line
                    }
                }
                return Gson().fromJson(response, RecipeList::class.java);
            }
        }
    }
}