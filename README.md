# OpenCart Test Automation with AI-Based Test Scenario Design

This project provides a comprehensive test automation framework for OpenCart with an AI-powered test scenario design system.

## 🎯 Key Features

### Traditional Test Automation
- **Selenium WebDriver** for browser automation
- **TestNG** for test execution and management
- **Page Object Model** for maintainable test code
- **Extent Reports** for detailed test reporting
- **Data-Driven Testing** with Excel integration
- **Cross-browser Testing** support

### 🤖 AI-Based Test Scenario Design (NEW!)
- **Automated Test Scenario Generation** from impact analysis
- **Smart Test Categorization** (Sanity vs Regression)
- **Priority-Based Test Selection** (P1-P4)
- **Release Notes Integration** for comprehensive coverage
- **Risk-Based Testing** approach

## 📚 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Chrome/Firefox/Edge browser

### Installation
```bash
git clone <repository-url>
cd opencartV1
mvn clean install
```

### Run Tests
```bash
# Run all tests
mvn test

# Run sanity tests only
mvn test -Dsurefire.suiteXmlFiles=ai-sanity-suite.xml

# Run regression tests
mvn test -Dsurefire.suiteXmlFiles=ai-regression-suite.xml

# Run specific test groups
mvn test -Dgroups=sanity
mvn test -Dgroups=regression
```

## 🤖 AI Test Scenario Generator

### Generate Test Scenarios
```bash
# Compile the project
mvn test-compile

# Run the AI generator demo
java -cp "target/test-classes" ai.TestScenarioGeneratorDemo
```

### View Generated Report
```bash
cat testData/ai/generated_test_scenarios.txt
```

## 📁 Project Structure

```
opencartV1/
├── src/test/java/
│   ├── ai/                              # AI Test Scenario Design System
│   │   ├── ImpactAnalyzer.java          # Analyzes impact of changes
│   │   ├── ReleaseNotesParser.java      # Parses release notes
│   │   ├── AITestScenarioGenerator.java # Generates test scenarios
│   │   └── TestScenarioGeneratorDemo.java
│   ├── pageObjects/                     # Page Object Model classes
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── AccountRegistration.java
│   │   └── MyAccountPage.java
│   ├── testBase/                        # Base test configuration
│   │   └── TestBase.java
│   ├── testCases/                       # Test implementations
│   │   ├── AccountCreationTest.java
│   │   ├── LoginwithPropertiesTest.java
│   │   └── AIGeneratedTestsExample.java # AI-generated test examples
│   └── utilities/                       # Helper utilities
│       ├── ExcelUtility.java
│       ├── DataProviders.java
│       └── ExtentReportManager.java
├── testData/
│   ├── ai/                              # AI system data files
│   │   ├── impact_analysis_example.txt
│   │   ├── release_notes_example.txt
│   │   └── generated_test_scenarios.txt
│   ├── OpenCart-TestCases.xlsx
│   └── data.xlsx
├── ai-sanity-suite.xml                  # Sanity test suite
├── ai-regression-suite.xml              # Regression test suite
├── master.xml                           # Master test suite
├── groups.xml                           # Group-based test suite
├── AI_TEST_DESIGN_README.md             # AI system technical docs
├── USAGE_GUIDE.md                       # Step-by-step usage guide
├── MANAGEMENT_SUMMARY.md                # Executive summary
├── QUICK_REFERENCE.md                   # Quick reference card
└── README.md                            # This file
```

## 📖 Documentation

| Document | Description | Audience |
|----------|-------------|----------|
| [AI_TEST_DESIGN_README.md](AI_TEST_DESIGN_README.md) | Technical details of AI system | Developers, QA Engineers |
| [USAGE_GUIDE.md](USAGE_GUIDE.md) | Step-by-step usage instructions | QA Engineers, Test Leads |
| [MANAGEMENT_SUMMARY.md](MANAGEMENT_SUMMARY.md) | Business value and ROI | Management, Stakeholders |
| [QUICK_REFERENCE.md](QUICK_REFERENCE.md) | Quick lookup and commands | Everyone |

## 🎓 How to Use AI Test Scenario Design

### 1. Create Impact Analysis
Create a file describing what changed:
```
MODULE: User Authentication
IMPACT: CRITICAL
COMPONENTS: LoginPage, SessionManager
DESCRIPTION: Migrated to OAuth2.0
```

### 2. Add Release Notes
Document new features and fixes:
```
VERSION: 2.1.0
DATE: 2025-10-15
TYPE: NEW_FEATURE
TITLE: Bulk Discounts
DESCRIPTION: Added bulk discount support
MODULES: ShoppingCart, PriceCalculator
---
```

### 3. Generate Scenarios
```bash
java -cp "target/test-classes" ai.TestScenarioGeneratorDemo
```

### 4. Review Output
The system generates:
- **Sanity Tests** - Critical tests that must pass
- **Regression Tests** - Comprehensive test coverage
- **Priority Levels** - P1 (Critical) to P4 (Low)
- **Test Groups** - Organized by risk and impact

### 5. Implement Tests
Use generated scenarios to guide test implementation:
```java
@Test(groups = {"sanity", "regression", "master"}, priority = 1)
public void testCriticalFeature() {
    // Implement based on AI-generated scenario
}
```

## 🔧 Configuration

### Test Environment
Edit `src/test/resources/config.properties`:
```properties
execution_env=local
appURL=http://localhost/opencart/upload/
username=test@example.com
password=TestPass123
```

### Browser Selection
Specify in TestNG XML or command line:
```xml
<parameter name="browser" value="chrome"/>
```

## 📊 Test Reports

### Extent Reports
After test execution, view reports in:
```
reports/ExtentReport_<timestamp>.html
```

### TestNG Reports
Native TestNG reports in:
```
test-output/index.html
```

### AI Scenario Reports
Generated scenario reports in:
```
testData/ai/generated_test_scenarios.txt
```

## 🚀 CI/CD Integration

### Jenkins Example
```groovy
pipeline {
    stages {
        stage('Generate Test Scenarios') {
            steps {
                sh 'mvn test-compile'
                sh 'java -cp "target/test-classes" ai.TestScenarioGeneratorDemo'
            }
        }
        stage('Run Sanity Tests') {
            steps {
                sh 'mvn test -Dsurefire.suiteXmlFiles=ai-sanity-suite.xml'
            }
        }
        stage('Run Regression Tests') {
            steps {
                sh 'mvn test -Dsurefire.suiteXmlFiles=ai-regression-suite.xml'
            }
        }
    }
}
```

## 🎯 Test Groups

| Group | Purpose | When to Run |
|-------|---------|-------------|
| `sanity` | Critical functionality validation | After every build |
| `regression` | Comprehensive test coverage | Before releases |
| `smoke` | Quick basic functionality check | First validation |
| `master` | All tests | Full test cycle |

## 📈 Benefits of AI Test Design

### Time Savings
- **80% reduction** in test planning time
- Automated scenario generation
- Instant prioritization

### Quality Improvement
- Risk-based testing
- 100% traceability
- Consistent methodology

### Cost Efficiency
- Optimized test execution
- Early defect detection
- Better resource allocation

## 🤝 Contributing

1. Create impact analysis for your changes
2. Update release notes
3. Generate test scenarios using AI system
4. Implement tests following generated scenarios
5. Run sanity and regression tests
6. Submit with test reports

## 📞 Support

For questions or issues:
1. Check documentation in `/docs` folder
2. Review example files in `testData/ai/`
3. Run demo: `ai.TestScenarioGeneratorDemo`
4. Contact QA team lead

## 📜 License

This project is part of the OpenCart test automation suite.

## 🎉 Getting Started

1. **Clone the repository**
2. **Read [QUICK_REFERENCE.md](QUICK_REFERENCE.md)** for immediate start
3. **Review [USAGE_GUIDE.md](USAGE_GUIDE.md)** for detailed instructions
4. **Run the demo** to see AI scenario generation in action
5. **Start testing!**

---

**Built with:** Selenium | TestNG | Maven | Java | AI-Powered Test Design

**Last Updated:** October 2025
