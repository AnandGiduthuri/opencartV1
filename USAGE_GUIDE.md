# AI-Based Test Scenario Design - Usage Guide

## Quick Start for Management and Testing Teams

This guide explains how to use the AI-based test scenario design system to create sanity and regression test plans based on impact analysis and release notes.

## For Management

### Overview

The AI system automatically:
- **Analyzes Impact** of code changes on the system
- **Parses Release Notes** to understand new features and fixes
- **Categorizes Tests** into sanity and regression groups
- **Assigns Priorities** based on risk assessment
- **Generates Reports** for decision-making

### Benefits

1. **Data-Driven Testing** - Tests are selected based on actual impact and risk
2. **Time Savings** - Automated test scenario generation reduces planning time
3. **Better Coverage** - Ensures critical changes are thoroughly tested
4. **Risk Mitigation** - Prioritizes high-risk areas for early testing
5. **Traceability** - Clear link between changes and test requirements

### Key Metrics

The system provides:
- Total number of test scenarios generated
- Breakdown by test type (sanity vs regression)
- Priority distribution (P1-P4)
- Module coverage analysis
- Test execution time estimates

## For QA Engineers

### Step 1: Prepare Input Files

#### A. Create Impact Analysis File

When developers complete changes, create a file: `testData/ai/impact_analysis_sprint_XX.txt`

```
MODULE: User Authentication
IMPACT: CRITICAL
COMPONENTS: LoginPage, SessionManager, SecurityFilter
DESCRIPTION: Updated authentication mechanism to use OAuth2.0

MODULE: Shopping Cart
IMPACT: HIGH
COMPONENTS: CartManager, PriceCalculator
DESCRIPTION: Added bulk discount functionality

MODULE: UI Improvements
IMPACT: LOW
COMPONENTS: CSS, Images
DESCRIPTION: Updated color scheme
```

**Impact Levels:**
- `CRITICAL` - Core functionality, security fixes, breaking changes
- `HIGH` - Major features, significant logic changes
- `MEDIUM` - Minor features, enhancements
- `LOW` - UI tweaks, documentation, config changes

#### B. Create Release Notes File

Extract from release notes: `testData/ai/release_notes_vX.X.X.txt`

```
VERSION: 2.1.0
DATE: 2025-10-15
TYPE: NEW_FEATURE
TITLE: Bulk Discount System
DESCRIPTION: Added support for bulk purchase discounts
MODULES: ShoppingCart, PriceCalculator
---

VERSION: 2.1.0
DATE: 2025-10-15
TYPE: BUG_FIX
TITLE: Fixed Cart Total Calculation
DESCRIPTION: Resolved cart calculation error
MODULES: ShoppingCart
---
```

**Change Types:**
- `BREAKING_CHANGE` - Backward incompatible changes
- `SECURITY_FIX` - Security vulnerabilities fixed
- `NEW_FEATURE` - New functionality
- `BUG_FIX` - Bug fixes
- `ENHANCEMENT` - Improvements to existing features
- `PERFORMANCE_IMPROVEMENT` - Performance optimizations
- `DEPRECATION` - Features being phased out

### Step 2: Generate Test Scenarios

Run the AI test scenario generator:

```bash
cd /home/runner/work/opencartV1/opencartV1

# Compile the project
mvn test-compile

# Run the generator
java -cp "target/test-classes" ai.TestScenarioGeneratorDemo
```

This will:
1. Analyze both input files
2. Generate comprehensive test scenarios
3. Create a detailed report in `testData/ai/generated_test_scenarios.txt`
4. Display statistics and recommendations

### Step 3: Review Generated Scenarios

The report shows:
- **Sanity Tests** - Must-run tests for critical functionality (typically 10-20% of total)
- **Regression Tests** - Comprehensive tests for all affected areas (typically 60-80% of total)
- **Priority Levels** - P1 (Critical) to P4 (Low)
- **Affected Modules** - Which components are impacted

### Step 4: Implement Test Cases

Use the generated scenarios as a guide:

```java
/**
 * AI-Generated Scenario: IMP_USER_AUTHENTICATION
 * Source: Impact Analysis - CRITICAL impact
 * Groups: sanity, smoke, regression
 * Priority: 1
 */
@Test(groups = {"sanity", "smoke", "regression", "master"}, priority = 1)
public void testUserAuthenticationAfterOAuthMigration() {
    // Implement test based on scenario description
}
```

See `testCases/AIGeneratedTestsExample.java` for examples.

### Step 5: Execute Tests

Run tests using TestNG XML suites:

**Sanity Tests Only:**
```bash
mvn test -Dsurefire.suiteXmlFiles=ai-sanity-suite.xml
```

**Regression Tests:**
```bash
mvn test -Dsurefire.suiteXmlFiles=ai-regression-suite.xml
```

**All Tests:**
```bash
mvn test -Dsurefire.suiteXmlFiles=master.xml
```

## Test Execution Strategy

### When to Run Sanity Tests

- **After Every Build** - Quick validation (15-30 minutes)
- **Before Production Deployment** - Final verification
- **After Critical Fixes** - Ensure core functionality works

Sanity tests should cover:
- User authentication and authorization
- Core business workflows
- Critical integrations
- Security-sensitive features

### When to Run Regression Tests

- **End of Sprint** - Comprehensive validation
- **Before Major Releases** - Full system testing
- **After Significant Changes** - Impact validation
- **Weekly/Nightly Builds** - Continuous validation

Regression tests should cover:
- All affected modules
- Integration between components
- Edge cases and error scenarios
- Performance-sensitive areas

## Interpreting AI Recommendations

### Priority Levels

**Priority 1 (Critical):**
- Tests for breaking changes
- Security vulnerability fixes
- Core functionality changes
- Must pass before deployment

**Priority 2 (High):**
- New feature testing
- Major bug fixes
- Significant enhancements
- Should pass before deployment

**Priority 3 (Medium):**
- Minor enhancements
- Integration testing
- Non-critical bug fixes
- Can be deferred if needed

**Priority 4 (Low):**
- Documentation updates
- UI/UX improvements
- Deprecation notices
- Informational only

### Test Group Selection

**Sanity Group:**
- Critical path testing
- Core functionality validation
- Quick smoke tests
- Average execution: 15-30 minutes

**Regression Group:**
- Comprehensive test coverage
- All affected areas
- Integration testing
- Average execution: 2-4 hours

## Customization

### Adjusting AI Decision Logic

Edit `src/test/java/ai/ImpactAnalyzer.java` or `ReleaseNotesParser.java`:

```java
public static boolean requiresSanityTesting(ImpactResult impactResult) {
    // Customize logic based on your project needs
    return impactResult.getLevel() == ImpactLevel.CRITICAL ||
           impactResult.getModule().contains("Payment"); // Add custom rules
}
```

### Adding Custom Metadata

Extend the test scenario with project-specific metadata:

```java
scenario.addMetadata("jira_ticket", "PROJ-1234");
scenario.addMetadata("test_owner", "QA_Team_A");
scenario.addMetadata("estimated_duration", "10 minutes");
```

## Integration with CI/CD

### Jenkins Pipeline Example

```groovy
stage('Generate Test Scenarios') {
    steps {
        sh 'mvn test-compile'
        sh 'java -cp "target/test-classes" ai.TestScenarioGeneratorDemo'
        archiveArtifacts artifacts: 'testData/ai/generated_test_scenarios.txt'
    }
}

stage('Run Sanity Tests') {
    steps {
        sh 'mvn test -Dsurefire.suiteXmlFiles=ai-sanity-suite.xml'
    }
}

stage('Run Regression Tests') {
    when {
        branch 'main'
    }
    steps {
        sh 'mvn test -Dsurefire.suiteXmlFiles=ai-regression-suite.xml'
    }
}
```

## Best Practices

1. **Update Input Files Regularly** - Keep impact analysis current for each sprint
2. **Review AI Recommendations** - Always review generated scenarios for accuracy
3. **Maintain Test Groups** - Keep @Test annotations synchronized with AI recommendations
4. **Track Metrics** - Monitor test execution times and success rates
5. **Collaborate** - Share AI reports with developers and management
6. **Iterate** - Refine AI rules based on historical data and feedback

## Troubleshooting

### Issue: No scenarios generated

**Solution:** Check that input files exist and follow the correct format

### Issue: Too many sanity tests

**Solution:** Review impact levels - only CRITICAL should be sanity

### Issue: Missing test scenarios for a module

**Solution:** Ensure the module is listed in impact analysis or release notes

### Issue: Incorrect priority assignment

**Solution:** Review impact levels and change types in input files

## Support

For questions or issues:
1. Review the detailed documentation: `AI_TEST_DESIGN_README.md`
2. Check example files in `testData/ai/`
3. Run the demo: `ai.TestScenarioGeneratorDemo`
4. Contact the QA team lead

## Example Workflow

### Sprint Planning

1. **Day 1:** Collect impact analysis from developers
2. **Day 2:** Extract release notes from release planning
3. **Day 3:** Run AI generator and review scenarios
4. **Day 4-10:** Implement test cases based on scenarios
5. **Day 11-13:** Execute sanity and regression tests
6. **Day 14:** Review results and report to management

### Release Cycle

1. **Week 1-2:** Development with ongoing impact analysis
2. **Week 3:** Generate test scenarios for sprint changes
3. **Week 3:** Implement and execute sanity tests daily
4. **Week 4:** Execute full regression suite
5. **Week 4:** Final sanity tests before production
6. **Post-Release:** Update AI rules based on production feedback

## Metrics and Reporting

### Key Metrics to Track

- **Test Coverage:** % of modules with test scenarios
- **Execution Time:** Time for sanity vs regression suites
- **Pass Rate:** Success rate by priority level
- **Defect Detection:** Bugs found by AI-recommended tests
- **Time Savings:** Reduction in test planning time

### Sample Report Format

```
Sprint XX Test Summary
======================
Total Scenarios Generated: 25
- Sanity Tests: 5 (20%)
- Regression Tests: 20 (80%)

Priority Distribution:
- P1 (Critical): 6 scenarios
- P2 (High): 10 scenarios
- P3 (Medium): 7 scenarios
- P4 (Low): 2 scenarios

Execution Results:
- Sanity Suite: 5/5 passed (25 minutes)
- Regression Suite: 18/20 passed (3.5 hours)
- Failed Tests: 2 (investigated and fixed)

AI Effectiveness:
- All P1 tests identified critical issues
- 90% pass rate demonstrates good coverage
- 2 hours saved in test planning
```

## Conclusion

The AI-based test scenario design system helps teams make data-driven decisions about testing. By analyzing impact and release notes, it ensures that the right tests are run at the right time, maximizing quality while minimizing effort.

For detailed technical information, see `AI_TEST_DESIGN_README.md`.
