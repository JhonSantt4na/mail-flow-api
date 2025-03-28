# **MailFlow API** 📨

**API de Envio de Emails com SMTP, JavaMail e Web Integrados**

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.5-green?logo=spring)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

Uma API robusta e escalável para envio e gerenciamento de emails, combinando o poder do **JavaMail**, simplicidade do **SMTP**, ambiente de testes com **Mailtrap** e endpoints web para integração perfeita em aplicações modernas.

---

## **Recursos Principais** 🚀

✅ **Envio de Emails via SMTP**

- Suporte a texto simples, HTML, anexos e templates dinâmicos.
- Configuração flexível para provedores (Gmail, Outlook, Amazon SES) ou serviços locais.

✅ **Testes com Mailtrap**

- Ambiente sandbox integrado para capturar emails em desenvolvimento.
- Dashboard visual para análise de aberturas, cliques e bounces.

✅ **API Web RESTful**

- Endpoints documentados com Swagger/OpenAPI.
- Autenticação via JWT ou API Key.
- Logs detalhados de transações (sucessos/falhas).

✅ **Integração JavaMail Simplificada**

- Abstraction layer para evitar complexidades do `javax.mail`.
- Retry automático em falhas de conexão.

---

## **Tecnologias** 🔧

- **Backend**: Java 17+, Spring Boot 3.x
- **Email**: JavaMail API, Apache Commons Email
- **Testes**: Mailtrap, JUnit 5, Mockito
- **Web**: REST, Swagger, Spring Security
- **Outras**: Docker, Maven, Lombok

---

## **Configuração Rápida** ⚡

### 1. **Clonar o Repositório**

```bash
git clone https://github.com/seu-usuario/mailflow-api.git
cd mailflow-api
```

### 2. **Configurar SMTP/Mailtrap**

Crie um `.env` na raiz do projeto (baseado em `.env.example`):

```properties
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=seu_username
MAIL_PASSWORD=sua_senha
MAIL_FROM=noreply@mailflow.dev
```

### 3. **Executar com Docker**

```bash
docker-compose up --build
```

_A API estará em `http://localhost:8080`._

---

## **Como Usar** 💻

### **Enviar Email via POST**

```bash
curl -X POST "http://localhost:8080/api/v1/emails" \
     -H "Content-Type: application/json" \
     -H "X-API
```
