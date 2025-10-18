# AI-Based Test Scenario Design System

## Overview

This system provides an AI-driven approach to designing sanity and regression test scenarios based on:
1. **Impact Analysis** - Analyzing the impact of code changes on the system
2. **Release Notes** - Understanding new features, bug fixes, and changes in each release

The system automatically categorizes tests into appropriate groups (sanity, regression, smoke) and assigns priorities based on risk and impact.

## Architecture

### Components

1. **ImpactAnalyzer** (`ai/ImpactAnalyzer.java`)
   - Analyzes code changes and their impact on the system
   - Categorizes impact as: CRITICAL, HIGH, MEDIUM, or LOW
   - Determines which modules require sanity vs regression testing
   - Assigns test priorities based on impact level

2. **ReleaseNotesParser** (`ai/ReleaseNotesParser.java`)
   - Parses structured release notes
   - Identifies change types: NEW_FEATURE, BUG_FIX, ENHANCEMENT, SECURITY_FIX, etc.
   - Maps changes to affected modules
   - Determines testing requirements based on change type

3. **AITestScenarioGenerator** (`ai/AITestScenarioGenerator.java`)
   - Main orchestrator that combines impact analysis and release notes
   - Generates comprehensive test scenarios with TestNG annotations
   - Categorizes tests into appropriate groups
   - Creates prioritized test execution plans

4. **TestScenarioGeneratorDemo** (`ai/TestScenarioGeneratorDemo.java`)
   - Demonstration of how to use the AI test scenario generator
   - Generates reports and statistics
   - Shows example usage patterns

## How It Works

### AI Decision Logic

The system uses rule-based AI logic to make decisions:

#### For Impact Analysis:
- **CRITICAL impact** → Sanity + Smoke + Regression tests (Priority 1)
- **HIGH impact** → Regression tests (Priority 2)
- **MEDIUM impact** → Regression tests (Priority 3)
- **LOW impact** → Minimal testing or skip (Priority 4)

#### For Release Notes:
- **BREAKING_CHANGE** → Sanity + Smoke + Regression (Priority 1)
- **SECURITY_FIX** → Sanity + Smoke + Regression (Priority 1)
- **NEW_FEATURE** → Sanity + Smoke + Regression (Priority 2)
- **BUG_FIX** → Regression (Priority 2)
- **ENHANCEMENT** → Regression (Priority 3)
- **PERFORMANCE_IMPROVEMENT** → Regression (Priority 3)
- **DEPRECATION** → Low priority or informational (Priority 4)

### Test Categorization

**Sanity Tests:**
- Core functionality validation
- Critical path testing
- Quick smoke tests to ensure basic functionality works
- Triggered by: Critical impacts, breaking changes, security fixes, new features

**Regression Tests:**
- Comprehensive testing of affected and related functionality
- Ensures no side effects from changes
- Covers edge cases and integration points
- Triggered by: Most changes except documentation-only updates

## Usage

### 1. Create Impact Analysis File

Create a text file (e.g., `testData/ai/impact_analysis.txt`) with the following format:

```
MODULE: User Authentication
IMPACT: CRITICAL
COMPONENTS: LoginPage, SessionManager, SecurityFilter
DESCRIPTION: Updated authentication mechanism to use OAuth2.0

MODULE: Shopping Cart
IMPACT: HIGH
COMPONENTS: CartManager, PriceCalculator
DESCRIPTION: Enhanced cart functionality with bulk discounts
```

**Impact Levels:**
- `CRITICAL` - Core functionality changes requiring immediate validation
- `HIGH` - Significant feature changes
- `MEDIUM` - Minor changes with limited scope
- `LOW` - Documentation or configuration changes

### 2. Create Release Notes File

Create a text file (e.g., `testData/ai/release_notes.txt`) with the following format:

```
VERSION: 2.1.0
DATE: 2025-10-15
TYPE: BREAKING_CHANGE
TITLE: Migration to OAuth2.0 Authentication
DESCRIPTION: Complete overhaul of authentication system
MODULES: Authentication, LoginPage, APIGateway
---

VERSION: 2.1.0
DATE: 2025-10-15
TYPE: NEW_FEATURE
TITLE: AI-Powered Product Recommendations
DESCRIPTION: ML-based recommendation engine
MODULES: ProductSearch, RecommendationEngine
---
```

**Change Types:**
- `NEW_FEATURE` - New functionality added
- `BUG_FIX` - Bug fixes
- `ENHANCEMENT` - Improvements to existing features
- `SECURITY_FIX` - Security vulnerabilities fixed
- `PERFORMANCE_IMPROVEMENT` - Performance optimizations
- `DEPRECATION` - Features being phased out
- `BREAKING_CHANGE` - Changes that break backward compatibility

### 3. Generate Test Scenarios

```java
// Generate from both sources
List<TestScenario> scenarios = AITestScenarioGenerator.generateComprehensiveScenarios(
    "./testData/ai/impact_analysis.txt",
    "./testData/ai/release_notes.txt"
);

// Filter sanity tests
List<TestScenario> sanityTests = AITestScenarioGenerator.filterByGroup(scenarios, "sanity");

// Filter regression tests
List<TestScenario> regressionTests = AITestScenarioGenerator.filterByGroup(scenarios, "regression");

// Generate report
String report = AITestScenarioGenerator.generateReport(scenarios);
System.out.println(report);
```

### 4. Run the Demo

```bash
cd /home/runner/work/opencartV1/opencartV1
mvn test-compile
mvn exec:java -Dexec.mainClass="ai.TestScenarioGeneratorDemo" -Dexec.classpathScope=test
```

Or compile and run directly:
```bash
javac -cp "target/test-classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" \
    src/test/java/ai/*.java
java -cp "target/test-classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" \
    ai.TestScenarioGeneratorDemo
```

## Integration with TestNG

The generated scenarios include TestNG annotations:

```java
@Test(groups = {"sanity", "smoke", "master"}, priority = 1)
public void imp_user_authentication() {
    // Test User Authentication after changes
    // Updated authentication mechanism to use OAuth2.0
    // Modules: [LoginPage, SessionManager, SecurityFilter]
}

@Test(groups = {"regression", "master"}, priority = 2)
public void imp_shopping_cart() {
    // Test Shopping Cart after changes
    // Enhanced cart functionality with bulk discounts
    // Modules: [CartManager, PriceCalculator, InventoryService]
}
```

### TestNG XML Configuration

You can run specific test groups:

```xml
<!-- Run only sanity tests -->
<suite name="Sanity Suite">
  <groups>
    <run>
      <include name="sanity"/>
    </run>
  </groups>
  <test name="Sanity Tests">
    <classes>
      <class name="testCases.GeneratedTests"/>
    </classes>
  </test>
</suite>

<!-- Run only regression tests -->
<suite name="Regression Suite">
  <groups>
    <run>
      <include name="regression"/>
    </run>
  </groups>
  <test name="Regression Tests">
    <classes>
      <class name="testCases.GeneratedTests"/>
    </classes>
  </test>
</suite>
```

## Example Files

The system includes example files demonstrating the format:
- `testData/ai/impact_analysis_example.txt` - Sample impact analysis
- `testData/ai/release_notes_example.txt` - Sample release notes

## Benefits

1. **Automated Test Planning** - AI determines which tests to run based on impact
2. **Risk-Based Testing** - Prioritizes tests based on risk and impact level
3. **Comprehensive Coverage** - Ensures critical changes are tested thoroughly
4. **Time Efficiency** - Focuses testing efforts on areas most likely to have issues
5. **Consistent Methodology** - Standardized approach to test scenario design
6. **Traceability** - Clear mapping from changes to test requirements

## Best Practices

1. **Keep Impact Analysis Current** - Update impact analysis for each sprint/release
2. **Detailed Release Notes** - Provide comprehensive release notes with affected modules
3. **Regular Review** - Periodically review generated scenarios for accuracy
4. **Customize Rules** - Adjust AI decision logic based on your project's needs
5. **Integrate with CI/CD** - Automate scenario generation in your pipeline
6. **Team Collaboration** - Share generated reports with the team for feedback

## Extending the System

### Custom AI Rules

You can extend the AI logic by modifying the decision rules in:
- `ImpactAnalyzer.requiresSanityTesting()`
- `ImpactAnalyzer.requiresRegressionTesting()`
- `ReleaseNotesParser.requiresSanityTesting()`
- `ReleaseNotesParser.requiresRegressionTesting()`

### Additional Data Sources

The system can be extended to analyze:
- Git commit history
- Code coverage reports
- Bug tracking systems
- CI/CD pipeline results
- Performance metrics

### Machine Learning Integration

Future enhancements could include:
- Training ML models on historical test results
- Predicting test failure probability
- Optimizing test suite selection
- Learning from production incidents

## Troubleshooting

### Common Issues

1. **File Not Found**
   - Ensure file paths are correct
   - Use absolute paths or verify working directory

2. **Parse Errors**
   - Check file format matches examples
   - Ensure proper capitalization of keywords
   - Verify no extra spaces or special characters

3. **Empty Scenarios**
   - Verify impact analysis and release notes have valid entries
   - Check that impact levels and change types are spelled correctly

## Support and Contributions

For questions or improvements:
1. Review the example files in `testData/ai/`
2. Run the demo: `ai.TestScenarioGeneratorDemo`
3. Check the generated reports for insights
4. Customize the AI logic for your specific needs

## License

This system is part of the opencartV1 project and follows the same license.
