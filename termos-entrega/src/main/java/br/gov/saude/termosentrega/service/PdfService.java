package br.gov.saude.termosentrega.service;

import br.gov.saude.termosentrega.model.TermoEntrega;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {
    
    private final TemplateEngine templateEngine;
    
    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    public byte[] gerarTermoEntregaPdf(TermoEntrega termo) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        
        Paragraph title = new Paragraph("TERMO DE ENTREGA DE EQUIPAMENTOS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        Paragraph subtitle = new Paragraph("Ministério da Saúde", headerFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(30);
        document.add(subtitle);
        
        document.add(new Paragraph("UUID: " + termo.getUuid(), normalFont));
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("DADOS DO RESPONSÁVEL", headerFont));
        document.add(new Paragraph("Tipo de Entidade: " + termo.getTipoEntidade(), normalFont));
        document.add(new Paragraph("Nome do Órgão: " + termo.getNomeOrgao(), normalFont));
        document.add(new Paragraph("Nome do Responsável: " + termo.getNomeResponsavel(), normalFont));
        document.add(new Paragraph("CPF: " + termo.getCpf(), normalFont));
        document.add(new Paragraph("Cargo: " + termo.getCargo(), normalFont));
        document.add(new Paragraph("E-mail Pessoal: " + termo.getEmailPessoal(), normalFont));
        if (termo.getEmailCorporativo() != null && !termo.getEmailCorporativo().isEmpty()) {
            document.add(new Paragraph("E-mail Corporativo: " + termo.getEmailCorporativo(), normalFont));
        }
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("ENDEREÇO", headerFont));
        document.add(new Paragraph("Endereço: " + termo.getEndereco(), normalFont));
        document.add(new Paragraph("Bairro: " + termo.getBairro(), normalFont));
        document.add(new Paragraph("Cidade: " + termo.getCidade() + " - " + termo.getUf(), normalFont));
        document.add(new Paragraph("CEP: " + termo.getCep(), normalFont));
        document.add(new Paragraph("Município: " + termo.getNomeMunicipio(), normalFont));
        document.add(new Paragraph("Código IBGE: " + termo.getCodigoIbge(), normalFont));
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("UNIDADE RECEBEDORA", headerFont));
        document.add(new Paragraph("Endereço Completo: " + termo.getEnderecoCompletoUnidade(), normalFont));
        if (termo.getSiglaOrgao() != null && !termo.getSiglaOrgao().isEmpty()) {
            document.add(new Paragraph("Sigla do Órgão: " + termo.getSiglaOrgao(), normalFont));
        }
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("EQUIPAMENTO DOADO", headerFont));
        document.add(new Paragraph(termo.getDescricaoEquipamento(), normalFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        document.add(new Paragraph("Data de Registro: " + termo.getDataCriacao().format(formatter), normalFont));
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("_______________________________", normalFont));
        document.add(new Paragraph("Assinatura do Responsável", normalFont));
        
        document.close();
        
        return baos.toByteArray();
    }
    
    public byte[] gerarRelatorioHtml(String templateName, Context context) throws Exception {
        String htmlContent = templateEngine.process(templateName, context);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(baos);
        
        return baos.toByteArray();
    }
}
