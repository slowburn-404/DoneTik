package com.datahiveorg.donetik.feature.auth.domain

sealed interface DomainResponse<out T> {
    data class Success<T>(val data: T) : DomainResponse<T>
    data class Failure(val message: String) : DomainResponse<Nothing>
}