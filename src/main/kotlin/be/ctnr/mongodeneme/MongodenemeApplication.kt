package be.ctnr.mongodeneme

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

//@ServletComponentScan
@EnableScheduling
//@EnableAutoConfiguration(exclude= [MongoAutoConfiguration::class])
@SpringBootApplication
class MongodenemeApplication

fun main(args: Array<String>) {
	runApplication<MongodenemeApplication>(*args)
}