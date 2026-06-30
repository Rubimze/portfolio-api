# Portfólio Profissional - API (Back-end)

Este repositório contém a API RESTful desenvolvida para dar suporte ao meu portfólio profissional. Construída no ecossistema **Java com Spring Boot**, a aplicação atua como o microsserviço responsável por processar as mensagens enviadas através do formulário de contato do front-end e realizar o disparo seguro de e-mails transacionais.

O projeto foi arquitetado com foco em resiliência, segurança de credenciais e alta disponibilidade em ambientes de nuvem (Cloud).

---

## 🚀 Tecnologias e Ferramentas

- **Linguagem:** Java 21
- **Framework:** Spring Boot
- **Módulos Spring:** Spring Web, Spring Mail (JavaMailSender)
- **Serviço de E-mail (SMTP):** Brevo (antigo Sendinblue)
- **Hospedagem:** Render (Web Service)
- **Monitoramento/Disponibilidade:** Cron-job.org

---

## 🛠️ Arquitetura e Soluções de Infraestrutura

Durante o desenvolvimento e deploy desta API, algumas estratégias avançadas de infraestrutura foram implementadas para garantir o funcionamento impecável do serviço em produção:

### 1. Serviço Transacional de E-mail Robusto
Em vez de depender de servidores SMTP genéricos que frequentemente bloqueiam conexões automatizadas em nuvem, a aplicação foi integrada ao **Brevo SMTP Relay**. O Spring Boot foi configurado para autenticar as requisições utilizando Chaves SMTP específicas da aplicação, garantindo entregabilidade e proteção anti-spam.

### 2. Bypass de Egress Firewall (Porta 2525)
Plataformas de nuvem frequentemente aplicam regras rígidas de *Egress Firewall* nas portas tradicionais de e-mail (587 e 25) para evitar abusos. Para garantir que as requisições de e-mail saíssem da Render sem sofrer `SocketTimeoutException`, a aplicação foi configurada para rotear o tráfego SMTP através da **porta alternativa 2525**, mantendo a segurança via TLS (`starttls.enable=true`).

### 3. Sistema Anti-Hibernação (Health Check)
Como a aplicação está hospedada em um ambiente que suspende instâncias ociosas para economizar recursos (*cold start*), foi criada uma rota específica de status (`/api/status`). Um serviço externo de monitoramento (*Cron-job.org*) realiza um *ping* HTTP GET nesta rota a cada 5 minutos. Isso mantém a JVM ativa 24/7, garantindo que as mensagens dos recrutadores sejam enviadas com zero latência.

### 4. Segurança e Boas Práticas
- **Injeção de Dependências:** Utilização de injeção via construtor, seguindo as melhores práticas do Spring.
- **Data Transfer Objects (DTOs):** Utilização de `Records` do Java para encapsular e transferir os dados do payload de forma imutável e performática.
- **Ocultação de Credenciais:** Nenhuma chave, senha ou dado sensível está *hardcoded* no repositório. O `application.properties` é alimentado dinamicamente através de **Environment Variables** configuradas diretamente no painel da infraestrutura (Render).

---

## 📡 Endpoints da API

### `POST /api/contato`
Recebe o *payload* do formulário de contato e dispara o e-mail transacional para a caixa de entrada configurada.

**Corpo da Requisição (JSON):**
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "celular": "(11) 99999-9999",
  "mensagem": "Olá, vi seu portfólio e gostaria de marcar uma entrevista."
}
```

### `GET /api/status`
Health check endpoint utilizado para monitoramento de *uptime* e prevenção de hibernação da instância na nuvem.

**Resposta de Sucesso (200 OK):**
```text
Servidor está online e pronto para uso!
```

---

## 📂 Estrutura Principal do Código

```bash
src/main/java/com/portfolio/api/
├── controller/
│   ├── ContatoController.java     # Mapeamento do endpoint de contato POST
│   └── StatusController.java      # Mapeamento do endpoint de Health Check GET
├── dto/
│   └── ContatoRequest.java        # Record para mapeamento do JSON (nome, email, etc.)
└── service/
    └── EmailService.java          # Lógica de negócio, montagem e disparo via JavaMailSender
```

---

## 🌐 Deploy Contínuo (CI/CD)

O pipeline de implantação está configurado na plataforma **Render**. Cada novo *commit* enviado à branch principal do GitHub dispara automaticamente o processo de build (utilizando Maven) e a reimplantação do container em produção.
