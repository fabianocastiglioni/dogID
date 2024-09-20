package com.webapp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*") // Permite todas as origens
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            String name = principal.getAttribute("name");
            if (name == null) {
                name = principal.getAttribute("given_name");
            }
            if (name == null) {
                name = principal.getName(); // Fallback to ID if name is not available
            }
            model.addAttribute("username", name);

            // Optionally, you can add more attributes
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
        }
        return "dashboard";
    }

   
    @PostMapping("/api/dog-breed-identify")
    public ResponseEntity<?> identifyDogBreed(@RequestParam("file") MultipartFile file) {
        try {
            // Chame a API da Groq aqui e obtenha a resposta.
            String breed = callGroqVisionApi(file);

            // Supondo que o método retorne a raça como uma string
            Map<String, String> response = new HashMap<>();
            response.put("breed", breed);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Caso ocorra algum erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error identifying breed.");
        }
    }

    private String callGroqVisionApi(MultipartFile file) {
        // Lógica para chamar a API da Groq enviando o arquivo
        // Você precisaria usar RestTemplate ou HttpClient para fazer a chamada de API.
        return "Labrador Retriever"; // Exemplo de retorno da raça identificada
    }

}