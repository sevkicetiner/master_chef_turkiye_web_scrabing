package be.ctnr.mongodeneme

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@ServletComponentScan
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration(exclude= [MongoAutoConfiguration::class])
class MongodenemeApplication

fun main(args: Array<String>) {
	runApplication<MongodenemeApplication>(*args)
}