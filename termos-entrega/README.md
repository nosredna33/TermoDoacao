# Sistema de Termos de Entrega - Ministério da Saúde

Sistema completo de gerenciamento de termos de entrega de equipamentos para municípios brasileiros com geração de PDF, assinatura digital via GOV.BR e upload de termos assinados.

## 🚀 Funcionalidades

### ✅ Área Pública
- Formulário de registro de termos de entrega
- Validação de CPF em tempo real
- Autocomplete inteligente de municípios (5570 municípios cadastrados)
- Máscaras automáticas para CPF e CEP
- Interface responsiva Bootstrap 5

### ✅ Área Administrativa
- **Dashboard com gráficos interativos:**
  - Gráfico de barras: Termos por Região (Norte, Nordeste, Centro-Oeste, Sudeste, Sul)
  - Gráfico de rosca: Top 10 Estados com mais termos
- **Gerenciamento de Termos:**
  - Listagem com paginação
  - Busca avançada por nome, CPF, e-mail, município, órgão
  - Visualização detalhada
  - **Geração de PDF do Termo de Doação oficial**
  - **Download e visualização de PDF**
  - **Link direto para assinatura digital via GOV.BR**
  - **Upload de termo assinado**
  - **Rastreamento de status** (Pendente/Assinado)
- **Gerenciamento de Usuários:**
  - CRUD completo
  - Controle de perfis (ADMIN, USER)
  - Validação de e-mails duplicados
- **Relatórios:**
  - API REST para dados agregados
  - Exportação em PDF
- **Monitoramento:**
  - Spring Actuator (health, metrics, info)
  - Logs estruturados

### 🆕 Termo de Doação com Encargos (NOVO!)

**Fluxo completo implementado:**

1. **Gerar PDF do Termo**
   - Template profissional com todos os dados preenchidos
   - Identificação única (UUID)
   - Dados do responsável e órgão
   - Endereço completo e código IBGE
   - Descrição dos equipamentos doados
   - Texto legal completo

2. **Baixar/Visualizar**
   - Download direto do PDF
   - Visualização no navegador

3. **Assinar via GOV.BR**
   - Link direto para portal de assinatura digital
   - https://www.gov.br/governodigital/pt-br/assinatura-eletronica

4. **Upload do Termo Assinado**
   - Campo de upload de arquivo PDF
   - Validação de tipo (apenas PDF)
   - Limite de tamanho: 10MB
   - Armazenamento seguro em `uploads/termos-assinados/`

5. **Rastreamento de Status**
   - Status: Pendente → Assinado
   - Data de assinatura registrada
   - Nome do arquivo armazenado

## 📋 Pré-requisitos

- **Java 11** ou superior
- **Maven 3.6+**
- **STS 4** (Spring Tool Suite) ou qualquer IDE Java

## 🔧 Instalação e Execução

### Opção 1: Via STS 4 (Recomendado para Demonstração)

1. **Extrair o projeto:**
   ```bash
   unzip termos-entrega-municipios-final.zip
   cd termos-entrega-municipios
   ```

2. **Importar no STS 4:**
   - File → Import → Maven → Existing Maven Projects
   - Selecione a pasta `termos-entrega-municipios`
   - Finish

3. **Inicializar o banco de dados:**
   ```bash
   chmod +x init-database.sh
   ./init-database.sh
   ```
   
   **Ou no Windows (Git Bash ou PowerShell):**
   ```bash
   sh init-database.sh
   ```

4. **Executar a aplicação:**
   - Botão direito no projeto → Run As → Spring Boot App
   - Aguarde a mensagem: `Started TermosEntregaApplication`

5. **Acessar o sistema:**
   - URL: http://localhost:8080
   - Login: admin@saude.gov.br
   - Senha: 123456

### Opção 2: Via Linha de Comando

```bash
# 1. Extrair e entrar no diretório
unzip termos-entrega-municipios-final.zip
cd termos-entrega-municipios

# 2. Inicializar banco de dados
./init-database.sh

# 3. Compilar
mvn clean package -DskipTests

# 4. Executar
java -jar target/termos-entrega-municipios-1.0.0.jar

# 5. Acessar
# http://localhost:8080
```

## 🔑 Credenciais de Acesso

### Usuário Administrador
- **E-mail:** admin@saude.gov.br
- **Senha:** 123456
- **Perfil:** ADMIN (acesso total)

## 📊 Dados de Demonstração

O banco de dados já vem com **12 termos de exemplo** de diversas regiões do Brasil:
- São Paulo (SP) - Sudeste
- Rio de Janeiro (RJ) - Sudeste  
- Belo Horizonte (MG) - Sudeste
- Curitiba (PR) - Sul
- Porto Alegre (RS) - Sul
- Salvador (BA) - Nordeste
- Fortaleza (CE) - Nordeste
- Recife (PE) - Nordeste
- Manaus (AM) - Norte
- Brasília (DF) - Centro-Oeste

Esses dados permitem visualizar os **gráficos funcionando** imediatamente!

## 🎨 Páginas Principais

### Área Pública
- **Formulário:** http://localhost:8080/public/formulario
- **Página de Sucesso:** http://localhost:8080/public/sucesso

### Área Administrativa (requer login)
- **Dashboard:** http://localhost:8080/admin/dashboard
- **Lista de Termos:** http://localhost:8080/admin/termos
- **Detalhes do Termo:** http://localhost:8080/admin/termos/{uuid}
- **Gerenciar Usuários:** http://localhost:8080/admin/usuarios
- **Health Check:** http://localhost:8080/actuator/health

## 🛠️ Tecnologias Utilizadas

- **Backend:**
  - Spring Boot 2.7.18
  - Spring MVC
  - Spring Data JDBC
  - Thymeleaf (template engine)
  - jBCrypt (criptografia de senhas)
  
- **Frontend:**
  - Bootstrap 5.3.2
  - Bootstrap Icons 1.11.1
  - Chart.js 4.4.0 (gráficos interativos)
  - JavaScript vanilla

- **Banco de Dados:**
  - SQLite3 (arquivo: termos_entrega.db)
  - 5570 municípios brasileiros cadastrados

- **Geração de PDF:**
  - Flying Saucer PDF
  - iText 2.1.7

- **Monitoramento:**
  - Spring Boot Actuator

## 📁 Estrutura do Projeto

```
termos-entrega-municipios/
├── src/
│   ├── main/
│   │   ├── java/br/gov/saude/termosentrega/
│   │   │   ├── config/          # Configurações (DB, Web, Auth)
│   │   │   ├── controller/      # Controllers MVC e REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Tratamento de exceções
│   │   │   ├── model/           # Entidades
│   │   │   ├── repository/      # Repositórios JDBC
│   │   │   ├── service/         # Lógica de negócio
│   │   │   └── util/            # Utilitários (validação, máscaras)
│   │   └── resources/
│   │       ├── db/              # Scripts SQL
│   │       ├── static/          # CSS, JS, imagens
│   │       └── templates/       # Templates Thymeleaf
│   │           ├── admin/       # Área administrativa
│   │           ├── auth/        # Login e autenticação
│   │           ├── public/      # Formulário público
│   │           ├── pdf/         # Template do Termo de Doação
│   │           └── layout/      # Layout base
├── uploads/                     # Termos assinados (criado automaticamente)
├── termos_entrega.db            # Banco SQLite (criado pelo script)
├── init-database.sh             # Script de inicialização do BD
├── pom.xml                      # Dependências Maven
└── README.md                    # Este arquivo
```

## 🎯 Roteiro para Demonstração no Teams

### 1. Preparação (5 minutos antes)
```bash
# Iniciar aplicação
cd termos-entrega-municipios
./init-database.sh
mvn spring-boot:run
```

### 2. Demonstração (20-25 minutos)

#### a) **Dashboard Administrativo** (5 min)
- Fazer login: admin@saude.gov.br / 123456
- Mostrar cards de estatísticas
- **Destacar gráficos interativos:**
  - Gráfico de barras por região
  - Gráfico de rosca por estado
- Explicar distribuição geográfica

#### b) **Gerenciamento de Termos** (3 min)
- Acessar lista de termos
- Demonstrar busca avançada (ex: buscar "São Paulo")
- Abrir detalhes de um termo

#### c) **Termo de Doação - DESTAQUE!** (7 min)
- **Mostrar seção "Termo de Doação com Encargos"**
- **Passo 1:** Clicar em "Baixar PDF" e mostrar o documento gerado
- **Passo 2:** Clicar em "Visualizar" para abrir no navegador
- **Passo 3:** Mostrar link "Ir para GOV.BR" para assinatura digital
- **Passo 4:** Demonstrar upload de termo assinado
- **Mostrar status:** Pendente → Assinado

#### d) **Formulário Público** (3 min)
- Acessar formulário público
- Demonstrar validação de CPF
- Demonstrar autocomplete de municípios
- Submeter um novo termo

#### e) **Gerenciamento de Usuários** (2 min)
- Listar usuários
- Criar novo usuário
- Mostrar validação de e-mail duplicado

### 3. Perguntas e Respostas (5 min)

## 🔧 Solução de Problemas

### Erro: "Port 8080 already in use"
```bash
# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Erro: "termos_entrega.db not found"
```bash
# Executar script de inicialização
./init-database.sh
```

### Erro: "Java version"
```bash
# Verificar versão do Java
java -version

# Deve ser Java 11 ou superior
```

### Erro: "uploads directory not found"
```bash
# O diretório é criado automaticamente na primeira execução
# Se necessário, crie manualmente:
mkdir -p uploads/termos-assinados
```

## 📝 Notas Importantes

1. **Banco de Dados:** O arquivo `termos_entrega.db` é criado automaticamente no diretório raiz do projeto
2. **Logs:** Os logs são salvos em `app.log`
3. **Porta:** A aplicação roda na porta 8080 por padrão
4. **Autenticação:** Sistema de autenticação customizado (sem Spring Security)
5. **Sessão:** Sessão HTTP padrão do Tomcat
6. **Uploads:** Termos assinados são salvos em `uploads/termos-assinados/`
7. **PDF:** Gerado dinamicamente com Flying Saucer

## 🎯 Destaques para o Chefe

1. **Gráficos Interativos** - Visualização clara da distribuição geográfica
2. **Interface Moderna** - Design profissional estilo Oracle APEX
3. **Termo de Doação Completo** - Geração, assinatura e upload integrados
4. **Busca Inteligente** - Encontra termos por qualquer campo
5. **Validações Robustas** - CPF, e-mails, dados duplicados
6. **Pronto para Produção** - Código limpo, sem placeholders
7. **Escalável** - Arquitetura MVC com boas práticas
8. **Rastreamento Completo** - Status de assinatura de cada termo

## 🚀 Próximos Passos (Sugestões)

- [ ] Integração com e-mail real (SMTP configurado)
- [ ] Exportação de relatórios em Excel
- [ ] Filtros avançados por data e região
- [ ] Dashboard com mais gráficos (linha do tempo, pizza)
- [ ] Notificações em tempo real
- [ ] Integração com API do IBGE
- [ ] Deploy em produção (Heroku, AWS, Azure)
- [ ] Integração direta com API do GOV.BR para assinatura

## 📞 Suporte

Para dúvidas ou problemas, consulte a documentação do Spring Boot:
- https://spring.io/projects/spring-boot
- https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/

---

**Desenvolvido para o Ministério da Saúde - 2026**

**Versão:** 1.0.0  
**Data:** Fevereiro 2026  
**Licença:** Uso interno governamental

