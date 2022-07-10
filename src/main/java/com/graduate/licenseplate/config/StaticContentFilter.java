package com.graduate.licenseplate.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


@Component
public class StaticContentFilter implements Filter {
    private List<String> fileExtensions = Arrays.asList(".css", ".js", ".svg");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = request.getServletPath();

        boolean isStatic = path.startsWith("/static");
        boolean isResourceFile = isStatic & fileExtensions.stream().anyMatch(path::contains);

        
        if (isResourceFile) {
        	resourceToResponse("/public" + path, response);
        }
        else {
        	chain.doFilter(request, response);
        }
    }

    private void resourceToResponse(String resourcePath, HttpServletResponse response) throws IOException {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
            return;
        }

        //headers
        if (resourcePath.endsWith(".html")) {
            response.setContentType("text/html");
        }
        if (resourcePath.endsWith(".css")) {
            response.setContentType("text/css");
        }
        if (resourcePath.endsWith(".js")) {
            response.setContentType("text/javascript");
        }

        inputStream.transferTo(response.getOutputStream());
    }
}