package br.gov.saude.termosentrega.util;

import java.text.Normalizer;

public class StringNormalizer {
    
    public static String normalize(String input) {
        if (input == null) {
            return null;
        }
        
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        normalized = normalized.replaceAll("[''`´]", "");
        
        normalized = normalized.replaceAll("-", "");
        
        normalized = normalized.toUpperCase();
        
        return normalized.trim();
    }
}
