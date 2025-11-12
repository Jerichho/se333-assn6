Jericho Guiang
SE333

 											Assignment 5 - UI Testing

Repository Link:

	https://github.com/Jerichho/se333-assn6

Test Packages:

	This project contains two test packages:
		- src/test/java/playwrightTraditional — Manually written Playwright UI tests in Java.
		- src/test/java/playwrightLLM — AI-generated Playwright UI tests using Playwright MCP.

Reflection (Part 4):

	In the manual UI testing approach, I wrote the Playwright tests directly in Java. This required identifying selectors, adding waits, and determining the correct sequence of 	interactions. The manual tests provided full control over assertions and behavior, but they were more time-consuming to create and adjust.

	In the AI-assisted UI testing approach, I used Playwright MCP to generate test steps based on natural language prompts. This allowed the test structure to be created more 	quickly. However, the generated tests still required human review and refinement to ensure correct selectors and stable waits. The AI approach reduced initial development 	time, but reliability and correctness still depended on manual adjustments.


Overall, manual testing offered more precision and stability, while using AI speeds up initial creation but still benefits from a real person verifying for accuracy and maintainability.