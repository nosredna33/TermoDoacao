# Prompt com espefificações do projeto

Meu chefe quer uma aplicação simples para registro e controle de termos de entregas a 5.000+ 
municipios de equipamentos, o sistema tem um crud de usuários na área administrativa e uma 
área livre para preenchimento dos dados: tipo de entidade (prefeito / Secretário Municipal de Saúde),
Nome do órgão, nome do responsável no município, cpf, cargo, e-mail pessoal, email corporativo,
endereço, bairro, cidade, UF estaso, CEP, nme do municipio, sigla/Órgão, código ibge do munícipio, 
endereço completo da unidade / órgão recebedor, descrção detalhada do equipamento doado. 

Deve ser uma aplicação Web em Java 21+, de Natureza maven, com suporte ao Java Boot Spring, 
com banco de dados SQLite3 e suporte ao FTS5 (indexando todos os campo), 
suporte thymelef, sem uso de ORM (pois não tem suporte adequado para SQLITE3 + FTS + Stored Functions e Procedures), 
suporte ao monitoramento com Spring Actuator sem integração com qualquer ferramenta externa.

## Requitos funcionais e não funcionais

* Interfwce Web HTML5 com Bootstrap 5+, CSS via CDN Rápido no Brasil;
* A Segurança requer proteção contra CSRF, XSS e SQL Injection;
* O campo "descrição detalhada do equipamento" será texto livre;
* A busca FTS5 estará disponibilizada apenas na área administrativa;
* Já existe CSV de municípios;
* O template do termo de entrega já existe em formato PDF, assinado digitalmente;
	* Cuja a imgagem do PDF deverá ser incluída como template (background image) e textos escritos por cima
* A área administrativa requer login? (presumo que sim)
	* Usar Spring Security básico com roles específicas ADMIN e USER
	* Precisa de recuperação de senha enviada ao e-mail pessoal
	* O login é com e-mail + Senha
* A área livre é pública e não requer qualquer tipo de autenticação por município
* Já existe uma base dos 5.000 municípios com código IBGE
* O formulário deve ter autocomplete para municípios, ou aceitar municipio informado para 
atualização da base cntrala posteriori
	* Marcar campo booleano novo_municipio = 'S';
	* Usar técnica lewienstain para encontrar o município na base, normalizando os nomes para: 
	MAIUSCULO, SEM ACENTO ou APÓSTROFES, HÍFENS NO NOME e juntando UF, para sugerir munícpios prováveis ou incluir novo
* Deve ter validação com ou sem máscara e sempre presistir com máscara para fasilitar pesquisa textual.
* CPF É chave única candidats, pois UUID deverá ser usado por município Registro de Doação.
	*  formatação para persistência e validação de dígitos verificadores.
* As datas com fuso horário ajusrtadas para o SAO_PAULO/BRAZIL
* Sim a aplicação em produção Será containerizada, por hora java standalone usando FATJAR até a homologação
* Ambiente de produção na AWS Cloud usando Kubernets (Futruro)
* Relatórios usando Thymeleaf exportados para PDF (HTML2PDF);
	* Templates específicos estáticos em: resources/tempaltes/relatorios;
	* Não precisa de assinatura digital;	
* Consultas paginadas para listagens;
* Busca textual em todos os campos via FTS5;
* Validação de e-mail, CPF, todosos campos campos obrigatórios, municiopios fora da base devem ser incluídos, com idicadorde incliusão;
* As métricas de monitoramento mais importantes são, monitoradas  por Spring Actuator são: health, metrics e log.
* Logs estruturados (JSONL) para captura de logs centralizados;
* Volumes Esperado é de 5673 por ano, com concentração entre Março e junho de 2026.
* 

## 📋 Sugestão de Estrutura Inicial
```text
projeto-termos-entrega/
├── src/main/java/br/gov/.../
│   ├── config/            # Configurações (DB, Security, FTS)
│   ├── controller/        # Controladores MVC
│   ├── service/           # Lógica de negócio
│   ├── repository/        # JDBC/DAO puro (sem JPA)
│   ├── model/             # POJOs/DTOs
│   └── utils/             # Validações, helpers
├── src/main/resources/    
│   ├── static/            # CSS, JS
│   ├── templates/         # Thymeleaf
│   └── db/                # Scripts SQL, migration
├── pom.xml
└── README.md

>> ATENÇÃO !
> Antes de desenvolver qualquer código, confirmar entendimentos.
