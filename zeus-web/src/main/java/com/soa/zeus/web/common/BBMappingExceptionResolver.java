package com.soa.zeus.web.common;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: shizhizhong
 * Date: 13-7-10
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */
public class BBMappingExceptionResolver extends SimpleMappingExceptionResolver {
    private static Logger log = Logger.getLogger(BBMappingExceptionResolver.class);

    private Boolean showError;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName(ex, request);
        if (viewName != null) {
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                applyStatusCodeIfPossible(request, response, statusCode);
            }
            return getModelAndView(viewName, ex, request, response);
        } else {
            return null;
        }
    }

    protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = super.getModelAndView(viewName, ex);
//        log.error("==>", ex);
        if (showError != null && showError)
            printExceptionAsString(mv, ex);
        //printExceptionByRespone(mv,response, ex);
        return mv;
    }

    ///
    protected void printExceptionAsString(ModelAndView mv, Exception ex) {
        PrintWriter pw = null;
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            StringBuffer sb = sw.getBuffer();
            mv.addObject("errorMag", sb.toString());

        } catch (Exception e) {
            log.error(e);
        } finally {
            if (sw != null)
                try {
                    sw.close();
                } catch (IOException e) {
                    log.error(e);
                }
            if (pw != null)
                pw.close();
        }

    }

    protected void printExceptionByRespone(ModelAndView mv, HttpServletResponse response, Exception ex) {
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.println("error!");
            ex.printStackTrace(pw);
            response.setContentType("text/plain");

        } catch (Exception e) {
            log.error(e);
        } finally {
            if (pw != null)
                pw.close();
        }

    }

    public Boolean getShowError() {
        return showError;
    }

    public void setShowError(Boolean showError) {
        this.showError = showError;
    }

}
