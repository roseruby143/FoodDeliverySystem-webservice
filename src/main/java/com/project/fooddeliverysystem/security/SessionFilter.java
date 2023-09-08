package com.project.fooddeliverysystem.security;
import java.io.IOException;

import org.springframework.http.HttpStatus;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		
		  HttpServletRequest httpRequest = (HttpServletRequest) request;
		  HttpServletResponse httpResponse = (HttpServletResponse) response;
		  
		  HttpSession session = httpRequest.getSession();
		  
		  String path = httpRequest.getRequestURI(); // /v1/user/login
		  
		  String method = httpRequest.getMethod();
		  
		  //System.out.println("path and Session ID in the filter = "+path +" && " + method +" && " + session.getId());
//		  System.out.println("Session ID in Filter = " + session.getId()) ;
		  
		 
		  // Create a wrapper around the response to capture the content
		    //CharResponseWrapper wrapperResponse = new CharResponseWrapper((HttpServletResponse) response);

		    // Call the next filter (or servlet) in the chain with the wrapped response
		    chain.doFilter(request, response);
		  
		// these are preflight request
 		  if(!method.equalsIgnoreCase("OPTIONS")) {
			  if (!(path.equalsIgnoreCase("/v1/user/login") || path.equalsIgnoreCase("/v1/user/register") || path.equalsIgnoreCase("/v1/admin/login"))) {

					//bypass the logout session validation, as it is already invalidated
					if(path.endsWith("/logout")) 
					{ 
						return; 
					}
					 
					String id = (String) session.getAttribute("userIdentifier");

					if (id == null || id.isEmpty()) {
						
//						throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
						httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			            // Optionally, you can provide a message in the response body
//						httpResponse.getWriter().write("Authentication required.");
						
//			            
//						httpResponse.sendRedirect("http://localhost:4200/login");
//						httpResponse.setHeader("Error_Type", "AUTHENTICAITON_FAILURE");
						return;
					} 

				}
		  }

        // Continue the filter chain
//        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
