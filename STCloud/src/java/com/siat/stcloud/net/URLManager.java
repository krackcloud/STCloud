/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.net;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KRACK
 */
@WebFilter("*.xhtml")
public class URLManager implements Filter {

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        String requestURL = req.getRequestURL().toString();

        if (requestURL.contains("css") || requestURL.contains("jpg") || requestURL.contains("js") || requestURL.contains("png")|| requestURL.contains("gif")|| requestURL.contains("ico")) {
            chain.doFilter(request, response);
            return;
        }

        if (session.getAttribute("usuario") == null && !requestURL.contains("login") && !requestURL.contains("Registro")) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else {
            if (session.getAttribute("usuario") != null && !requestURL.contains("Registro") && !requestURL.contains("index")) {
                res.sendRedirect(req.getContextPath() + "/index.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}