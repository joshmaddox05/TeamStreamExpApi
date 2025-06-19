# Security Policy

## Supported Versions

We release security updates for the following versions:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |

## Reporting a Vulnerability

We take security seriously. If you discover a security vulnerability, please follow these steps:

1. **DO NOT** create a public GitHub issue
2. Email us directly at: [your-security-email@teamstream.com]
3. Include detailed information about the vulnerability
4. Allow us time to address the issue before public disclosure

## Security Best Practices

### For Development:
- Never commit real API keys or certificates to version control
- Use environment variables for sensitive configuration
- Review the `.env.example` file for proper configuration

### For Production:
- Change the default API key (`teamstream-api-key-2025`)
- Use strong, unique API keys
- Enable HTTPS/TLS in production
- Regularly rotate API keys and certificates
- Monitor API usage and implement rate limiting

### Current Security Features:
- API Key authentication on protected endpoints
- Input validation on all endpoints
- Secure token generation with expiration
- CORS configuration for web integration
- No sensitive data in logs

## Dependencies

We regularly update dependencies to address security vulnerabilities. If you find a security issue in a dependency, please report it following the process above.

## Responsible Disclosure

We appreciate security researchers who follow responsible disclosure practices. We will:

1. Acknowledge your report within 48 hours
2. Provide regular updates on our progress
3. Credit you in our security advisory (if desired)
4. Work with you to ensure the issue is properly addressed

Thank you for helping keep TeamStream Experience API secure!
