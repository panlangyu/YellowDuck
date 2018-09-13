package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import org.apache.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 系统异常的统一处理
 */
@RestController
@ControllerAdvice
public class RestExceptionHandler {


    private final static Logger logger = Logger.getLogger(RestExceptionHandler.class);


    //运行参数数值转换
    @ExceptionHandler(value=NumberFormatException.class)
    public ApiResponseResult numberFormatException(NumberFormatException ex){

        this.logger.error("【服务器运行数值转换异常】 {} : "+ ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(500,"error","[服务器运行数值转换异常]："+ex.getMessage(),"");
    }

    //参数数值转换
    @ExceptionHandler(value=MethodArgumentTypeMismatchException.class)
    public ApiResponseResult methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){

        this.logger.error("【服务器参数数值转换异常】 {} : "+ ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(500,"error","[服务器参数数值转换异常]：Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'","");
    }

    //RSA加密异常
    @ExceptionHandler(value=ArrayIndexOutOfBoundsException.class)
    public ApiResponseResult arrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {

        this.logger.error("【服务器加密算法异常】 {} : "+ ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(1012,"error","[服务器加密算法异常]：too much data for RSA block","");
    }

    //空值异常
    @ExceptionHandler(value = NullPointerException.class)
    public ApiResponseResult nullPointerException(NullPointerException ex) {

        this.logger.error("【系统异常】 {} : "+ ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(500,"error","[服务器]空值异常","");
    }

    @ExceptionHandler({ClassCastException.class})
    public ApiResponseResult classCastExceptionHandler(ClassCastException ex) {

        this.logger.error("[服务器]数据类型转换异常" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(1002,"error","[服务器]数据类型转换异常","");
    }

    @ExceptionHandler({IOException.class})
    public ApiResponseResult iOExceptionHandler(IOException ex) {

        this.logger.error("[服务器]IO异常" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(1005,"error","[服务器]IO异常", "");
    }

    @ExceptionHandler({NoSuchMethodException.class})
    public ApiResponseResult noSuchMethodExceptionHandler(NoSuchMethodException ex) {

        this.logger.error("[服务器]未知方法异常" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(1006,"error","[服务器]未知方法异常", "");
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ApiResponseResult requestNotReadable(HttpMessageNotReadableException ex) {

        this.logger.error("400..requestNotReadable" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(400,"error","400..requestNotReadable", "");
    }

    @ExceptionHandler(value = TypeMismatchException.class)
    public ApiResponseResult requestTypeMismatch(TypeMismatchException ex) {

        this.logger.error("400..TypeMismatchException" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(400,"error","400..TypeMismatchException", "");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ApiResponseResult requestMissingServletRequest(MissingServletRequestParameterException ex) {

        this.logger.error("400..MissingServletRequest" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(400,"error","400..MissingServletRequest", "");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ApiResponseResult request405(HttpRequestMethodNotSupportedException ex) {

        this.logger.error("405..." + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(405,"error","405...Method not allowed", "");
    }

    //406异常
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public ApiResponseResult request406(HttpMediaTypeNotAcceptableException ex) {

        this.logger.error("406...Not Acceptable" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(406,"error","406...Not Acceptable", "");
    }

    //500错误
    @ExceptionHandler(value = HttpMessageNotWritableException.class)
    public ApiResponseResult server500(HttpMessageNotWritableException ex) {

        this.logger.error("500...Internal Server Error");
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(500,"error","500...Internal Server Error", "");

    }

    //url跳转异常
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ApiResponseResult noHandlerFoundException(NoHandlerFoundException ex) {

        this.logger.error("404...No message available" + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return new ApiResponseResult().build(404,"error","404...No message available" + ex.getMessage(),"");
    }

    // 数据库服务器异常
    @ExceptionHandler(value = SQLException.class)
    public ApiResponseResult sqlException(SQLException ex) {

        this.logger.error("[服务器错误] : " + ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(1002,"error","[服务器错误,请联系管理员]"+ex.getMessage(),"");
    }

    //所有异常
    @ExceptionHandler(value = Exception.class)
    public ApiResponseResult exception(Exception ex) {

        this.logger.error("[服务器错误] : "+ ex.getMessage());
        this.logger.error(ex.getMessage(),ex);
        ex.printStackTrace();
        return ApiResponseResult.build(1001,"error","[服务器错误,请联系管理员]"+ex.getMessage(),"");
    }


}
