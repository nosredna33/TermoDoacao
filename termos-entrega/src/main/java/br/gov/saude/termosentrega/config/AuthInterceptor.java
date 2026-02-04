package br.gov.saude.termosentrega.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // Permitir acesso público
        if (uri.startsWith("/public/") || 
            uri.startsWith("/auth/") || 
            uri.startsWith("/css/") || 
            uri.startsWith("/js/") || 
            uri.startsWith("/images/") ||
            uri.startsWith("/actuator/health") ||
            uri.startsWith("/api/municipios/") ||
            uri.equals("/admin/dashboard") ||
            uri.startsWith("/api/relatorios/") ||
            uri.equals("/error")) {
            return true;
        }
        
        // Verificar se usuário está logado
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogado") != null) {
            return true;
        }
        
        // Redirecionar para dashboard
        response.sendRedirect("/admin/dashboard");
        return false;
    }
}
