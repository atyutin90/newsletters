package com.example.newsletters

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewslettersApplication

fun main(args: Array<String>) {
	runApplication<NewslettersApplication>(*args)
}
