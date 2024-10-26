# Lexical Analyzer Design and Syntax Analyzer Development

## Overview

This project focuses on the development of a **Lexical Analyzer** and **Syntax Analyzer**, fundamental components in the process of compiling or interpreting code. The Lexical Analyzer processes input strings to recognize tokens (keywords, identifiers, symbols, etc.), while the Syntax Analyzer checks for correct grammatical structure based on a given set of rules. This project demonstrates the application of compiler theory principles to analyze and validate code syntax.

## Features

- **Lexical Analysis**: Tokenizes input to identify lexical elements like keywords, operators, and identifiers.
- **Syntax Analysis**: Validates the syntax of the tokenized input against predefined grammar rules.
- **Error Reporting**: Provides feedback on syntax errors for debugging and code correction.
- **Extensible Design**: Modular structure allows for updates to grammar rules and token definitions.

## Project Structure

The main components of the application are organized as follows:

- `LexicalAnalyzer` - Processes input strings and categorizes tokens.
- `SyntaxAnalyzer` - Validates token sequences based on grammar rules.
- Additional files and configurations required for running lexical and syntax analysis on code input.

## File Naming Conventions

Configuration files for grammar and tokens, if used, should be saved with the `.yaml` or `.yml` extension to ensure consistency and compatibility in parsing settings.

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <project-folder>
Compile the project: Make sure Java or your chosen language environment is set up, then run:

bash

javac -d bin src/**/*.java
Run the application: Navigate to the bin folder and execute:

bash

java MainClassName
## Technologies Used
- Java: Core programming language for lexical and syntax analysis.
- Compiler Theory: Implementation based on concepts of lexical and syntax analysis.
## License
This project is licensed under the MIT License.
