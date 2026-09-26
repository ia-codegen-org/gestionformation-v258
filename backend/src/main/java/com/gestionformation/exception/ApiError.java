package com.gestionformation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private LocalDateTime       timestamp;
    private int                 status;
    private String              error;
    private String              errorCode;
    private String              message;
    private String              path;
    private Map<String, String> errors;

    public ApiError() {}

    public ApiError(LocalDateTime timestamp, int status, String error,
                    String errorCode, String message, String path) {
        this.timestamp = timestamp;
        this.status    = status;
        this.error     = error;
        this.errorCode = errorCode;
        this.message   = message;
        this.path      = path;
    }

    public LocalDateTime getTimestamp()          { return timestamp; }
    public void setTimestamp(LocalDateTime t)    { this.timestamp = t; }

    public int getStatus()                       { return status; }
    public void setStatus(int s)                 { this.status = s; }

    public String getError()                     { return error; }
    public void setError(String e)               { this.error = e; }

    public String getErrorCode()                 { return errorCode; }
    public void setErrorCode(String c)           { this.errorCode = c; }

    public String getMessage()                   { return message; }
    public void setMessage(String m)             { this.message = m; }

    public String getPath()                      { return path; }
    public void setPath(String p)                { this.path = p; }

    public Map<String, String> getErrors()       { return errors; }
    public void setErrors(Map<String, String> e) { this.errors = e; }
}
