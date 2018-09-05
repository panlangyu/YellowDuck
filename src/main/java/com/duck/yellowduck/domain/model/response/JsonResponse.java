package com.duck.yellowduck.domain.model.response;

/**
 * 定义响应类
 */
public class JsonResponse {

    private static final String OK = "ok";
    private static final String ERROR = "error";
    private static final Integer CODE = Integer.valueOf(200);
    private Meta meta;
    private Object data;

    public JsonResponse success() {

        this.meta = new Meta(true, "ok", CODE);
        return this;
    }

    public JsonResponse success(Object data) {

        this.meta = new Meta(true, "ok", CODE);
        this.data = data;
        return this;
    }

    public JsonResponse failure(Integer code) {

        this.meta = new Meta(false, "error", code);
        return this;
    }

    public JsonResponse failure(String message, Integer code) {

        this.meta = new Meta(false, message, code);
        return this;
    }

    public Meta getMeta() {

        return this.meta;
    }

    public Object getData() {

        return this.data;
    }

    /**
     * 内部类
     */
    public class Meta {

        private boolean success;
        private String message;
        private Integer code;

        public Meta(boolean success, Integer code) {

            this.success = success;
            this.code = code;
        }

        public Meta(boolean success, String message, Integer code) {

            this.success = success;
            this.message = message;
            this.code = code;
        }

        public boolean isSuccess() {

            return this.success;
        }

        public String getMessage() {

            return this.message;
        }

        public Integer getCode() {

            return this.code;
        }

    }


}
