package com.spotbook.personalguide.data.remote

import com.spotbook.personalguide.data.token.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.io.File
import kotlinx.serialization.json.Json

class ApiService(
    private val baseUrl: String,
    private val tokenStorage: TokenStorage
) {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                }
            )
        }
    }

    suspend fun register(email: String, password: String): AuthResponseDto {
        return client.post("$baseUrl/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequestDto(email, password))
        }.checkedBody()
    }

    suspend fun login(email: String, password: String): AuthResponseDto {
        return client.post("$baseUrl/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequestDto(email, password))
        }.checkedBody()
    }

    suspend fun me(): UserDto {
        return client.get("$baseUrl/api/auth/me") {
            authHeader()
        }.checkedBody()
    }

    suspend fun getPlaces(): List<PlaceDto> {
        return client.get("$baseUrl/api/places") { authHeader() }.checkedBody()
    }

    suspend fun createPlace(request: PlaceRequestDto): PlaceDto {
        return client.post("$baseUrl/api/places") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.checkedBody()
    }

    suspend fun updatePlace(id: Long, request: PlaceRequestDto): PlaceDto {
        return client.put("$baseUrl/api/places/$id") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.checkedBody()
    }

    suspend fun deletePlace(id: Long) {
        client.delete("$baseUrl/api/places/$id") { authHeader() }.checked()
    }

    suspend fun updatePlaceStatus(id: Long, request: UpdatePlaceStatusRequestDto): PlaceDto {
        return client.patch("$baseUrl/api/places/$id/status") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.checkedBody()
    }

    suspend fun uploadPlacePhoto(id: Long, photoFile: File): PhotoUploadResponseDto {
        return client.post("$baseUrl/api/places/$id/photo") {
            authHeader()
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            key = "photo",
                            value = photoFile.readBytes(),
                            headers = Headers.build {
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "form-data; name=\"photo\"; filename=\"${photoFile.name}\""
                                )
                            }
                        )
                    }
                )
            )
        }.checkedBody()
    }

    suspend fun getGroups(): List<GroupDto> {
        return client.get("$baseUrl/api/groups") { authHeader() }.checkedBody()
    }

    suspend fun createGroup(request: GroupRequestDto): GroupDto {
        return client.post("$baseUrl/api/groups") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.checkedBody()
    }

    suspend fun deleteGroup(id: Long) {
        client.delete("$baseUrl/api/groups/$id") { authHeader() }.checked()
    }

    suspend fun addPlaceToGroup(groupId: Long, placeId: Long): GroupPlacesResponseDto {
        return client.post("$baseUrl/api/groups/$groupId/places/$placeId") {
            authHeader()
        }.checkedBody()
    }

    suspend fun removePlaceFromGroup(groupId: Long, placeId: Long): GroupPlacesResponseDto {
        return client.delete("$baseUrl/api/groups/$groupId/places/$placeId") {
            authHeader()
        }.checkedBody()
    }

    suspend fun exportData(request: SyncExportRequestDto): SyncExportResponseDto {
        return client.post("$baseUrl/api/sync/export") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.checkedBody()
    }

    suspend fun importData(): SyncImportResponseDto {
        return client.get("$baseUrl/api/sync/import") { authHeader() }.checkedBody()
    }

    private fun io.ktor.client.request.HttpRequestBuilder.authHeader() {
        tokenStorage.getToken()?.let { token ->
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    private suspend inline fun <reified T> HttpResponse.checkedBody(): T {
        checked()
        return body()
    }

    private fun HttpResponse.checked() {
        if (status.value !in 200..299 && status != HttpStatusCode.NoContent) {
            throw ApiException("Ошибка сервера: ${status.value}")
        }
    }
}

class ApiException(message: String) : RuntimeException(message)
