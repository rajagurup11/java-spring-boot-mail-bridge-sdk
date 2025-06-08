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

📄 Template Path Note
If you are using HTML templates with Mail Bridge:
- Templates should be placed under src/main/resources/templates/ in your project.
- The path is relative to the classpath, and by default, Spring Boot loads from this location.

```aiignore
src/main/resources/templates/welcome-email.html
```
When building the EmailRequest, you only need to provide the filename:
```aiignore
EmailRequest.builder()
    .template("welcome-email.html")
    .build();
```

🔧 Configuration (Spring Boot) 
In your application.yml:

```aiignore
# === MailBridge Configuration ===
# If 'mailbridge.enabled' is set to true, the following 'spring.mail.*' SMTP properties are mandatory.
# The application will fail to start if these values are missing or misconfigured.
# If 'mailbridge.enabled' is set to false, MailBridge is disabled and these values are not required.
mailbridge:
  enabled: true

spring:
  mail:
    host: smtp.yourprovider.com # Required if mailbridge.enabled = true
    port: <port> # Required if mailbridge.enabled = true
    username: ${SMTP_USERNAME} # Required if mailbridge.enabled = true
    password: ${SMTP_PASSWORD} # Required if mailbridge.enabled = true
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
@Autowired // This is just an example, prefer constructor injection.
private EmailService emailService;
```

2. Build and send an email
```aiignore
EmailRequest request = EmailRequest.builder()
    .from("no-reply@example.com")
    .to(List.of("user@example.com"))
    .cc(List.of("cc@example.com"))
    .bcc(List.of("bcc@example.com"))
    .subject("Welcome to MailBridge!")
    .template("welcome.html")
    .model(Map.of(
        "username", "Rajaguru",
        "activationLink", "https://example.com/activate?token=abc123"
    ))
    .attachments(List.of(
        Attachment.builder()
            .filename("invoice.pdf")
            .mimeType(SupportedMimeType.PDF) // compile-time safety
            .base64Content("JVBERi0xLjQKJcfs...") // truncated base64 content
            .build()
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
Includes unit testing
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
— Rajaguru P. Pelase reach me at rajagurup11@gmail.com for assitance or any tech discussios.
```

🔗 Links
- [GitHub](https://github.com/rajagurup/mail-bridge)
- [Documentation](https://rajagurup.github.io/mail-bridge/)
- [Support](https://github.com/rajagurup/mail-bridge/issues)

License Copyright © 2025 Rajaguru P

