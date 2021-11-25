package be.ctnr.mongodeneme.masterchef.controllers

import be.ctnr.mongodeneme.masterchef.model.CookieModel
import be.ctnr.mongodeneme.masterchef.repository.CookieManager
import org.springframework.web.bind.annotation.*


@RestController()
@RequestMapping("/cookie")
class CookieController (val cookieManager: CookieManager){
    @GetMapping("get")
    fun getCookie():CookieModel{
        return cookieManager.findById("lastId").get()
    }

    @PostMapping("setNewCookie")
    fun setNewCookie(@RequestBody body: String):String {
        cookieManager.save(CookieModel(id = "lastId",cookie = body))
        return body;
    }
}