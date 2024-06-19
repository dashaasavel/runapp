package com.dashaasavel.runapplib.healthcheck

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Экземпляр, обрабатывающий статус и сообщения хелсчека
 */
class HealthCheckBuilderImpl(
    private val node: ObjectNode
): HealthCheckBuilder {
    private var appStatus = Status.OK

    override fun ok() {
        appStatus = Status.OK
    }

    override fun ok(paramName: String, message: String): HealthCheckBuilder {
        node.put(paramName, message)
        appStatus = Status.OK
        return this
    }

    override fun warning(paramName: String, message: String): HealthCheckBuilder {
        node.put(paramName, message)
        appStatus = Status.WARNING
        return this
    }

    override fun error(paramName: String, message: String): HealthCheckBuilder {
        node.put(paramName, message)
        appStatus = Status.ERROR
        return this
    }

    fun clearData() {
        node.removeAll()
    }
}

fun main() {
    val mapper = ObjectMapper()
    val rootNode = mapper.createObjectNode()

    val childNode1 = mapper.createObjectNode()
    childNode1.put("name1", "val1")
    childNode1.put("name2", "val2")

    rootNode.set<JsonNode>("obj1", childNode1)

    val childNode2 = mapper.createObjectNode()
    childNode2.put("name3", "val3")
    childNode2.put("name4", "val4")

    rootNode.set<JsonNode>("obj2", childNode2)

    val childNode3 = mapper.createObjectNode()
    childNode3.put("name5", "val5")
    childNode3.put("name6", "val6")

    rootNode.set<JsonNode>("obj3", childNode3)


    val jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode)
    println(jsonString)
}