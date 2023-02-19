package nl.strmark.piradio.config

import io.swagger.v3.oas.annotations.responses.ApiResponse
import nl.strmark.piradio.model.ErrorResponse
import nl.strmark.piradio.model.FieldError
import nl.strmark.piradio.util.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException


@RestControllerAdvice(annotations = [RestController::class])
class RestExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                httpStatus = HttpStatus.NOT_FOUND.value(),
                exception = exception::class.simpleName,
                message = exception.message
            ), HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException):
            ResponseEntity<ErrorResponse> {
        val bindingResult: BindingResult = exception.bindingResult
        val fieldErrors: List<FieldError> = bindingResult.fieldErrors
            .map { error -> FieldError(field = error.field, errorCode = error.code) }
        return ResponseEntity(
            ErrorResponse(
                httpStatus = HttpStatus.BAD_REQUEST.value(),
                exception = exception::class.simpleName,
                fieldErrors = fieldErrors
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatus(exception: ResponseStatusException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                httpStatus = exception.statusCode.value(),
                exception = exception::class.simpleName,
                message = exception.message
            ), exception.statusCode
        )


    @ExceptionHandler(Throwable::class)
    @ApiResponse(
        responseCode = "4xx/5xx",
        description = "Error"
    )
    fun handleThrowable(exception: Throwable): ResponseEntity<ErrorResponse> {
        exception.printStackTrace()
        return ResponseEntity(
            ErrorResponse(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception = exception::class.simpleName
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

}
