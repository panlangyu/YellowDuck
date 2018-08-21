package com.duck.yellowduck.domain.model.response;

public class Result
{
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int OK = 4;
    public static final int TOO_BUSY = 5;
    private int code;
    private String type;
    private String message;
    private Object data;

    public int getCode()
    {
        return this.code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Object getData()
    {
        return this.data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public Result(Object data)
    {
        this.data = data;
    }

    public Result(int code, String type, String message, Object data)
    {
        this.code = code;
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public static Result getErro(String msg)
    {
        return new Result(1, "error", msg, null);
    }

    public static Result getErro(int code, String msg)
    {
        return new Result(code, "error", msg, null);
    }

    public static Result getSuccess(Object data)
    {
        return new Result(200, "error", "suscc", data);
    }
}
