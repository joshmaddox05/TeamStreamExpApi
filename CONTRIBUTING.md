# Contributing to TeamStream Experience API

We welcome contributions to the TeamStream Experience API! This document provides guidelines for contributing to the project.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/yourusername/TeamStreamExpApi.git`
3. Create a branch: `git checkout -b feature/your-feature-name`

## Development Setup

### Prerequisites
- Java 21
- Git

### Setup
```bash
# Clone the repository
git clone https://github.com/yourusername/TeamStreamExpApi.git
cd TeamStreamExpApi

# Build the project
./gradlew build

# Run tests
./gradlew test

# Start the application
./gradlew bootRun
```

## Making Changes

1. **Code Style**: Follow Java coding conventions and Spring Boot best practices
2. **Testing**: Add tests for new features and ensure all tests pass
3. **Documentation**: Update README.md if you add new features
4. **Commit Messages**: Use clear, descriptive commit messages

### Testing Your Changes
```bash
# Run all tests
./gradlew test

# Test the API endpoints
./test-api.sh
```

## Submitting Changes

1. Push your changes to your fork
2. Create a pull request with a clear description of your changes
3. Ensure all tests pass
4. Wait for review

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help maintain a welcoming environment

## Questions?

Feel free to open an issue for questions or discussions about the project.

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
