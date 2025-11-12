---
mode: agent
---
You are acting as an AI UI test agent using Playwright MCP.

Generate a Playwright UI test in **Java using JUnit5** that:

1. Goes to https://depaul.bncollege.com/
2. Searches for “earbuds”
3. Expands the **Brand** filter and selects **JBL**
4. Clicks the **JBL Quantum True Wireless** product
5. Adds it to the cart
6. Asserts that the cart shows **1 item**

Requirements:
- Use `Playwright.create()` and JUnit structure (no Playwright Test Runner syntax)
- Use clear locator strategies (getByRole / getByText preferred)
- Do NOT use TypeScript or JavaScript — only Java
- Name the test class: `TestCaseLLMSearchAndAddToCart`
- The test should compile and run inside a standard Maven + JUnit project
- Place the generated .java file into 'src/test/java/playwrightLLM/'
- run it with 'mvn test'