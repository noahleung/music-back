package com.graduate.musicback.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduate.musicback.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    private HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(session.getAttribute("admin") !=null) {
            return true;
        }else
        {
            response.setHeader("authorization", "expired");
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            OutputStream outputStream = response.getOutputStream();
            outputStream.write((new ObjectMapper().writeValueAsString(new Result<String>().setCode(HttpStatus.UNAUTHORIZED).setMessage("nologin").setData("nologin"))).getBytes());
            outputStream.flush();
            outputStream.close();
            return false;
        }
    }
}
