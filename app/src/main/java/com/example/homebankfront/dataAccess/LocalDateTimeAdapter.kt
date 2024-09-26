package com.example.homebankfront.dataAccess

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun serialize(
        src : LocalDateTime?,
        typeOfSrc : Type?,
        context : JsonSerializationContext?
    ) : JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(
        json : JsonElement,
        typeOfT : Type,
        context : com.google.gson.JsonDeserializationContext
    ) : LocalDateTime {
        return LocalDateTime.parse(
            json.asString,
            formatter
        )
    }
}

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(
        src : LocalDate?,
        typeOfSrc : Type?,
        context : JsonSerializationContext?
    ) : JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(
        json : JsonElement,
        typeOfT : Type,
        context : com.google.gson.JsonDeserializationContext
    ) : LocalDate {
        return LocalDate.parse(
            json.asString,
            DateTimeFormatter.ISO_DATE
        )
    }
}