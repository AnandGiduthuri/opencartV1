# AI Test Scenario Design - Quick Reference Card

## 🚀 Quick Start (5 Minutes)

### 1. Run the Demo
```bash
cd /home/runner/work/opencartV1/opencartV1
mvn test-compile
java -cp "target/test-classes" ai.TestScenarioGeneratorDemo
```

### 2. View Generated Report
```bash
cat testData/ai/generated_test_scenarios.txt
```

### 3. Run Sanity Tests
```bash
mvn test -Dsurefire.suiteXmlFiles=ai-sanity-suite.xml
```

---

## 📝 Input File Templates

### Impact Analysis Template
```
MODULE: <module_name>
IMPACT: CRITICAL|HIGH|MEDIUM|LOW
COMPONENTS: Component1, Component2, Component3
DESCRIPTION: Brief description of changes
```

### Release Notes Template
```
VERSION: X.X.X
DATE: YYYY-MM-DD
TYPE: NEW_FEATURE|BUG_FIX|SECURITY_FIX|BREAKING_CHANGE
TITLE: Short title
DESCRIPTION: Detailed description
MODULES: Module1, Module2
---
```

---

## 🎯 Impact Levels

| Level | When to Use | Test Category | Priority |
|-------|-------------|---------------|----------|
| CRITICAL | Core functionality, security fixes | Sanity + Regression | P1 |
| HIGH | Major features, significant changes | Regression | P2 |
| MEDIUM | Minor features, enhancements | Regression | P3 |
| LOW | UI changes, documentation | Minimal/Skip | P4 |

---

## 🏷️ Change Types

| Type | Test Category | Priority | Example |
|------|---------------|----------|---------|
| BREAKING_CHANGE | Sanity + Regression | P1 | API changes |
| SECURITY_FIX | Sanity + Regression | P1 | Vulnerability fix |
| NEW_FEATURE | Sanity + Regression | P2 | New functionality |
| BUG_FIX | Regression | P2 | Bug correction |
| ENHANCEMENT | Regression | P3 | Improvement |
| PERFORMANCE_IMPROVEMENT | Regression | P3 | Optimization |
| DEPRECATION | Minimal | P4 | Phase out |

---

## 🧪 Test Execution Commands

### Sanity Tests (15-30 min)
```bash
mvn test -Dsurefire.suiteXmlFiles=ai-sanity-suite.xml
```

### Regression Tests (2-4 hours)
```bash
mvn test -Dsurefire.suiteXmlFiles=ai-regression-suite.xml
```

### All Tests
```bash
mvn test -Dsurefire.suiteXmlFiles=master.xml
```

### Specific Group
```bash
mvn test -Dgroups=sanity
mvn test -Dgroups=regression
```

---

## 📊 Understanding Reports

### Report Sections
1. **SUMMARY** - Total scenarios and distribution
2. **SCENARIOS BY PRIORITY** - Detailed scenario list
3. **STATISTICS** - Priority breakdown

### Key Metrics
- **Sanity Tests** - Must pass before deployment (typically 20-30%)
- **Regression Tests** - Comprehensive coverage (typically 70-90%)
- **P1 Tests** - Critical, must run first
- **P2-P3 Tests** - Important but can be deferred if needed
- **P4 Tests** - Low priority, optional

---

## 🔧 Common Tasks

### Generate Scenarios for New Sprint
```bash
# 1. Create impact analysis file
vim testData/ai/impact_analysis_sprint_15.txt

# 2. Create release notes file
vim testData/ai/release_notes_v2.2.0.txt

# 3. Update generator to use your files
# Edit: src/test/java/ai/TestScenarioGeneratorDemo.java
# Change file paths in main() method

# 4. Generate scenarios
mvn test-compile
java -cp "target/test-classes" ai.TestScenarioGeneratorDemo
```

### Add Test Implementation
```java
@Test(groups = {"sanity", "regression", "master"}, priority = 1)
public void testCriticalFeature() {
    // 1. Setup test data
    // 2. Execute test steps
    // 3. Verify expected results
    // 4. Cleanup
}
```

### Create Custom Test Suite
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Custom Suite">
    <groups>
        <run>
            <include name="sanity"/>
        </run>
    </groups>
    <test name="Custom Tests">
        <classes>
            <class name="testCases.YourTestClass"/>
        </classes>
    </test>
</suite>
```

---

## 🐛 Troubleshooting

### Problem: No scenarios generated
**Solution:** Check file paths and format in input files

### Problem: Compilation errors
**Solution:** Run `mvn clean test-compile`

### Problem: Tests not executing
**Solution:** Verify test groups match suite XML configuration

### Problem: AI decisions seem incorrect
**Solution:** Review and adjust impact levels in input files

---

## 📚 Documentation

| Document | Purpose | Audience |
|----------|---------|----------|
| `AI_TEST_DESIGN_README.md` | Technical details | Developers, QA |
| `USAGE_GUIDE.md` | Step-by-step guide | QA Engineers |
| `MANAGEMENT_SUMMARY.md` | Business value | Management |
| `QUICK_REFERENCE.md` | Quick lookup | Everyone |

---

## 🔗 File Locations

```
opencartV1/
├── src/test/java/ai/
│   ├── ImpactAnalyzer.java              # Impact analysis logic
│   ├── ReleaseNotesParser.java          # Release notes parser
│   ├── AITestScenarioGenerator.java     # Main AI engine
│   └── TestScenarioGeneratorDemo.java   # Demo runner
├── src/test/java/testCases/
│   └── AIGeneratedTestsExample.java     # Example implementations
├── testData/ai/
│   ├── impact_analysis_example.txt      # Sample impact analysis
│   ├── release_notes_example.txt        # Sample release notes
│   └── generated_test_scenarios.txt     # Generated report
├── ai-sanity-suite.xml                  # Sanity test suite
├── ai-regression-suite.xml              # Regression test suite
└── Documentation files                   # README, guides, etc.
```

---

## ⚡ Best Practices

1. ✅ Update impact analysis for every sprint
2. ✅ Keep release notes detailed and current
3. ✅ Review AI recommendations before implementing
4. ✅ Run sanity tests after every build
5. ✅ Run regression tests before deployment
6. ✅ Track metrics and improve over time

---

## 📞 Getting Help

1. Check example files in `testData/ai/`
2. Run the demo: `ai.TestScenarioGeneratorDemo`
3. Review detailed guides in documentation
4. Contact QA team lead

---

**Version:** 1.0 | **Last Updated:** October 2025
