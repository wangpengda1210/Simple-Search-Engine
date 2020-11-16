package search

import java.io.File

val people = mutableListOf<String>()
val pairs = mutableMapOf<String, MutableList<Int>>()

fun main(args: Array<String>) {
    if (!init(args)) return
    
    mainLoop@while (true) {
        
        println("\n=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit")
    
        when (readLine()!!.toInt()) {
            0 -> break@mainLoop
            1 -> {
                println("\nSelect a matching strategy: ALL, ANY, NONE")
                val strategy = Strategy.findStrategy(readLine()!!.toLowerCase())
                println("\nEnter a name or email to search all suitable people.")
                val peopleFound = searchPeople(readLine()!!, strategy)
                if (peopleFound.isNotEmpty()) {
                    for (person in peopleFound) {
                        println(person)
                    }
                } else println("No matching people found.")
            }
            2 -> {
                println("\n=== List of people ===")
                for (person in people) {
                    println(person)
                }
            }
            else -> println("\nIncorrect option! Try again.")
        }
        
    }
    
    println("\nBye!")
}

fun init(args: Array<String>): Boolean {
    if (args.size == 2 && args[0] == "--data") {
        val lines = File(args[1]).readLines()
        for (i in lines.indices) {
            people.add(lines[i])
            for (word in lines[i].split(" ")) {
                if (pairs.containsKey(word.toLowerCase())) pairs[word.toLowerCase()]!!.add(i)
                else pairs[word.toLowerCase()] = mutableListOf(i)
            }
        }
    } else return false
    return true
}

fun searchPeople(peopleToFind: String, strategy: Strategy): List<String> {
    val result = mutableSetOf<String>()
    val words = peopleToFind.split(" ")
    when (strategy) {
        Strategy.ALL -> {
            var toAdd = people.toSet()
            for (word in words) {
                val hasThisWord = mutableSetOf<String>()
                if (pairs.containsKey(word.toLowerCase())) {
                    for (id in pairs[word.toLowerCase()]!!) hasThisWord.add(people[id])
                }
                toAdd = toAdd.intersect(hasThisWord)
            }
            result.addAll(toAdd)
        }
        Strategy.ANY -> {
            for (word in words) {
                if (pairs.containsKey(word.toLowerCase())) {
                    for (id in pairs[word.toLowerCase()]!!) result.add(people[id])
                }
            }
        }
        Strategy.NONE -> {
            val toAdd = people.toMutableList()
            for (word in words) {
                val notHasThisWord = mutableSetOf<String>()
                if (pairs.containsKey(word.toLowerCase())) {
                    toAdd -= pairs[word.toLowerCase()]!!.map { people[it] }
                }
            }
            result.addAll(toAdd)
        }
        Strategy.NULL -> return emptyList()
    }
    
    return result.toList()
}

enum class Strategy(val text: String) {
    ALL("all"), ANY("any"), NONE("none"), NULL("");
    
    companion object {
        fun findStrategy(strategy: String): Strategy {
            for (i in values()) {
                if (i.text == strategy) return i
            }
            return NULL
        }
    }
}
