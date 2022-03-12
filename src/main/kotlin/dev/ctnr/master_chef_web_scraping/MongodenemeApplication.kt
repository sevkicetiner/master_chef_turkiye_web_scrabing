package dev.ctnr.master_chef_web_scraping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@ServletComponentScan
@EnableScheduling
@SpringBootApplication()
class MongodenemeApplication
fun main(args: Array<String>) {
	runApplication<MongodenemeApplication>(*args)
}
