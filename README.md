# MailBridge SDK

A Java SDK for sending emails with support for HTML templates, attachments, and base64 content encoding using Spring Boot and Thymeleaf.

## 📬 Mail Bridge – Simplifying Java Email Delivery

> A lightweight, extensible Java library for sending templated emails with ease. Built with Spring Boot, designed for scale, and crafted for the modern Java developer.

---

## 🚀 Why Mail Bridge?

Email is still one of the most vital communication tools in software — yet sending dynamic, templated emails in Java often feels clunky, repetitive, and error-prone.

**Mail Bridge** aims to simplify this. Whether you’re sending OTPs, reports, or branded newsletters, this library helps you do it right.

---

## ✨ Features

- **Spring Boot Native** — Easily plug into existing Spring Boot apps
- **Template Support** — Send HTML/Thymeleaf emails out-of-the-box
- **Attachment Handling** — Add single or multiple attachments
- **Safe Defaults** — Mime types, character sets, and error handling
- **Builder Pattern** — Fluent API for creating rich email content
- **Pluggable Design** — Override and extend components as needed

---

## 🧱 Installation

Add the dependency via Maven:

```xml
<dependency>
  <groupId>io.github.rajagurup</groupId>
  <artifactId>mail-bridge</artifactId>
  <version>1.0.0</version>
</dependency>
```

Or with Gradle:

```
implementation 'io.github.rajagurup:mail-bridge:1.0.0'
```

🔧 Configuration (Spring Boot) 
In your application.yml:

```aiignore
mailbridge:
  from: no-reply@yourdomain.com
  reply-to: support@yourdomain.com
  template-prefix: classpath:/templates/
  enabled: true

spring:
  mail:
    host: smtp.yourprovider.com
    port: 587
    username: ${SMTP_USERNAME}
    password: ${SMTP_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

📦 Quick Start Example

1. Inject the service
```aiignore
@Autowired
private EmailService emailService;
```

2. Build and send an email
```aiignore
EmailRequest request = EmailRequest.builder()
    .to("recipient@example.com")
    .subject("Welcome to Mail Bridge!")
    .template("welcome-email.html")
    .model(Map.of("name", "Rajaguru", "link", "https://example.com/verify"))
    .attachments(List.of(
        Attachment.fromFile("invoice.pdf", new File("/path/to/invoice.pdf"))
    ))
    .build();

emailService.send(request);
```

## Code Formatting with Spotless

This project uses the [Spotless Maven Plugin](https://github.com/diffplug/spotless) to enforce consistent code style and formatting.

- **Java** code is formatted with [Google Java Format](https://github.com/google/google-java-format).
- **XML files** (e.g., `pom.xml`) are formatted using Eclipse WTP XML formatter.
- To **check** if files comply with formatting rules, run:

  ```bash
  mvn spotless:check
  
- To apply formatting fixes automatically, run:
  ```bash
  mvn spotless:check
 
Note: This project uses Spotless Maven Plugin version 6.18.0. See the pom.xml for configuration details.

📁 Directory Structure
```aiignore
src/
└── main/
    └── java/
        └── io/github/rajagurup/mailbridge/
            ├── config/         # Configuration classes
            ├── exception/      # Custom exceptions
            ├── model/          # EmailRequest, Attachment, etc.
            ├── service/        # Interfaces and implementations
```

🧪 Testing & Validation
```aiignore
Includes unit and integration tests with real SMTP scenarios using:
- GreenMail
-In-memory templates
- CI support (GitHub Actions / Maven Wrapper)
```

🤝 Contributing
```aiignore
We welcome contributions! If you’ve used Mail Bridge and have ideas or improvements:

Fork this repo
Create a feature branch (git checkout -b feature/my-feature)
Open a PR with a clear description
```

🙌 A Note from the Author

```aiignore
After 14 years in Java and Spring Boot, I built Mail Bridge to save developers from reinventing the wheel for every email feature. Hope it makes your job easier — and your emails more powerful.
— Rajaguru P. Pelase reach me at rajagurup11@gmail.com for assitance or any tech dicussios.
```

🔗 Links
- [GitHub](https://github.com/rajagurup/mail-bridge)
- [Documentation](https://rajagurup.github.io/mail-bridge/)
- [Support](https://github.com/rajagurup/mail-bridge/issues)

License Copyright © 2025 Rajaguru P

