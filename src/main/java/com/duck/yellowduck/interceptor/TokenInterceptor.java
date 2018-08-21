package com.duck.yellowduck.interceptor;

import com.alibaba.fastjson.JSON;
import com.duck.yellowduck.domain.model.response.Result;
import com.duck.yellowduck.publics.TokenUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TokenInterceptor
        implements HandlerInterceptor
{
    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception
    {
        String requestURI = httpServletRequest.getRequestURI();

        String tokenStr = httpServletRequest.getHeader("token");
        if ((!requestURI.contains("/common")) &&
                (!requestURI.contains("/code")))
        {
            if ((tokenStr == null) && (tokenStr == null))
            {
                String str = JSON.toJSONString(Result.getErro(10083, "token����"));
                dealErrorReturn(httpServletRequest, httpServletResponse, str);
                return false;
            }
            try
            {
                TokenUtil.verifyToken(tokenStr);
            }
            catch (Exception e)
            {
                String str = "{'code':10083,'type':'error','message':'cookie����'}";
                dealErrorReturn(httpServletRequest, httpServletResponse, str);
                return false;
            }
        }
        httpServletResponse.setHeader("token", tokenStr);
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
            throws Exception
    {}

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
            throws Exception
    {}

    public void dealErrorReturn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj)
    {
        String json = (String)obj;
        PrintWriter writer = null;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html; charset=utf-8");
        try
        {
            writer = httpServletResponse.getWriter();
            writer.print(json);
        }
        catch (IOException ex)
        {
            logger.error("response error", ex);
        }
        finally
        {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static class HttpRequstUtils
    {
        private static HttpServletRequest request;
        private static HttpServletResponse response;

        private HttpRequstUtils(HttpServletRequest request, HttpServletResponse response)
        {
            request = request;
            response = response;
        }

        public static String getToken()
        {
            return request.getParameter("token");
        }

        public static void setToken(String token)
        {
            response.setHeader("token", token);
        }
    }
}
