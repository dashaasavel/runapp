package com.dashaasavel.runapplib.healthcheck

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.InitializingBean

/**
 * Менеджер всех
 */
class JsonStatusManager(
    private val registry: HealthCheckRegistrarImpl
): InitializingBean {
    private val map: MutableMap<String, HealthCheckBuilder> = HashMap()

    private val objectMapper = ObjectMapper()
    private val rootNode = objectMapper.createObjectNode()
    fun getJson(): String {
        registry.healthChecks.forEach { (name, healthCheck) ->
            val builder = map.computeIfAbsent(name) {createBuilder(it)}
            healthCheck.processHealthCheck(builder)
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode)
    }

    override fun afterPropertiesSet() {
        registry.healthChecks.keys.forEach {
            map[it] = createBuilder(it)
        }
    }

    private fun createBuilder(name: String): HealthCheckBuilder {
        val node = createNode(name)
        return HealthCheckBuilderImpl(node)
    }

    private fun createNode(nodeName: String): ObjectNode {
        val newNode = objectMapper.createObjectNode()
        rootNode.set<JsonNode>(nodeName, newNode)
        return newNode
    }
}