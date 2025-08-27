package co.com.pragma.api.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(String tittle, String message, int status) {
}
