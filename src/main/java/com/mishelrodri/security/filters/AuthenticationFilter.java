package com.mishelrodri.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mishelrodri.entities.Usuario;
import com.mishelrodri.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        Usuario usuario = null;

        try{
            usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

//        Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
        User user = (User) authResult.getPrincipal();
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("token", jwtUtils.crearToken(user.getUsername()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(respuesta));

        response.setContentType(MediaType.APPLICATION_JSON.toString());

        response.getWriter().flush();
    }
}
