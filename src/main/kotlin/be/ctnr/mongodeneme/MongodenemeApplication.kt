package be.ctnr.mongodeneme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@ServletComponentScan
@SpringBootApplication
@EnableScheduling
class MongodenemeApplication

fun main(args: Array<String>) {
	runApplication<MongodenemeApplication>(*args)
}