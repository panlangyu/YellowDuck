package com.duck.yellowduck.domain.model.response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ApiResponseResult
{
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int OK = 4;
    public static final int TOO_BUSY = 5;
    int code;
    String type;
    Object message;
    Object data;

    public ApiResponseResult() {}

    public ApiResponseResult(Integer code, String type, Object message)
    {
        this.code = code.intValue();
        this.type = type;
        this.message = message;
        this.data = "";
    }

    public static ApiResponseResult build(Integer code, String type, Object message, Object data)
    {
        return new ApiResponseResult(code, type, message, data);
    }

    public ApiResponseResult(Integer code, String type, Object message, Object data)
    {
        this.code = code.intValue();
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public static ApiResponseResult failure(Object message, Integer code) {

        return new ApiResponseResult(message, code);
    }

    public ApiResponseResult(Object message, Integer code)
    {
        this.message = message;
        this.code = code.intValue();
    }

    public Object getData()
    {
        return this.data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public ApiResponseResult(int code, Object message)
    {
        this.code = code;
        switch (code)
        {
            case 1:
                setType("error");
                break;
            case 2:
                setType("warning");
                break;
            case 3:
                setType("info");
                break;
            case 4:
                setType("ok");
                break;
            case 5:
                setType("too busy");
                break;
            default:
                setType("unknown");
        }
        this.message = message;
    }

    @XmlTransient
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

    public Object getMessage()
    {
        return this.message;
    }

    public void setMessage(Object message)
    {
        this.message = message;
    }
}
