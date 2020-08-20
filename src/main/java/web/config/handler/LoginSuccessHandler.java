package web.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
//        httpServletResponse.sendRedirect("/users");
//        httpServletResponse.sendRedirect("/admin");
        //set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        boolean admin = false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            System.out.println("Authorities:" + auth);
            if ("ROLE_ADMIN".equals(auth.getAuthority())){
                admin = true;
//                System.out.println("admin authority =" + admin);
            }
        }

        if(admin){
            httpServletResponse.sendRedirect("/admin");
        }else{
            httpServletResponse.sendRedirect("/profile");
        }
    }
}