package br.gov.saude.termosentrega.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Redireciona qualquer erro (404, 500, etc.) para o dashboard
        return "redirect:/admin/dashboard";
    }
}
