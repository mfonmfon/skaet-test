package com.skaet.ussdapp.common.dto;

public class ApiResponse<T> {
    private Boolean isSuccessful;
    private String message;
    private Integer status;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(Boolean isSuccessful, String message, Integer status, T data) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private Boolean isSuccessful;
        private String message;
        private Integer status;
        private T data;

        public ApiResponseBuilder<T> isSuccessful(Boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
            return this;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> status(Integer status) {
            this.status = status;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(isSuccessful, message, status, data);
        }
    }

    // Getters and Setters
    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
