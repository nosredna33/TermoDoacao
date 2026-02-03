package br.gov.saude.termosentrega.util;

public class MascaraUtil {
    
    public static String aplicarMascaraCpf(String cpf) {
        if (cpf == null) {
            return null;
        }
        
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return cpf;
        }
        
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9, 11);
    }
    
    public static String aplicarMascaraCep(String cep) {
        if (cep == null) {
            return null;
        }
        
        cep = cep.replaceAll("[^0-9]", "");
        
        if (cep.length() != 8) {
            return cep;
        }
        
        return cep.substring(0, 5) + "-" + cep.substring(5, 8);
    }
    
    public static String removerMascara(String valor) {
        if (valor == null) {
            return null;
        }
        
        return valor.replaceAll("[^0-9]", "");
    }
}
