package ai;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AITestScenarioGenerator uses AI-driven logic to generate test scenarios
 * based on impact analysis and release notes.
 * 
 * This class analyzes changes and automatically determines:
 * - Which tests should be categorized as "sanity" tests
 * - Which tests should be categorized as "regression" tests
 * - Test priorities based on risk and impact
 */
public class AITestScenarioGenerator {

    public static class TestScenario {
        private String scenarioId;
        private String title;
        private String description;
        private Set<String> testGroups; // e.g., "sanity", "regression", "smoke"
        private int priority; // 1 (highest) to 4 (lowest)
        private List<String> affectedModules;
        private String sourceType; // "IMPACT_ANALYSIS" or "RELEASE_NOTES"
        private Map<String, String> metadata;

        public TestScenario(String scenarioId, String title, String description) {
            this.scenarioId = scenarioId;
            this.title = title;
            this.description = description;
            this.testGroups = new HashSet<>();
            this.affectedModules = new ArrayList<>();
            this.metadata = new HashMap<>();
            this.priority = 3; // Default medium priority
        }

        public String getScenarioId() {
            return scenarioId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Set<String> getTestGroups() {
            return testGroups;
        }

        public void addTestGroup(String group) {
            this.testGroups.add(group);
        }

        public void setTestGroups(Set<String> groups) {
            this.testGroups = groups;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public List<String> getAffectedModules() {
            return affectedModules;
        }

        public void setAffectedModules(List<String> modules) {
            this.affectedModules = modules;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }

        public void addMetadata(String key, String value) {
            this.metadata.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("Scenario: %s\nTitle: %s\nGroups: %s\nPriority: %d\nModules: %s\nDescription: %s",
                    scenarioId, title, testGroups, priority, affectedModules, description);
        }

        /**
         * Generates TestNG annotation format for this scenario
         */
        public String toTestNGAnnotation() {
            String groups = testGroups.stream()
                    .map(g -> "\"" + g + "\"")
                    .collect(Collectors.joining(", "));
            return String.format("@Test(groups = {%s}, priority = %d)", groups, priority);
        }
    }

    /**
     * Generates test scenarios from impact analysis file
     * @param impactAnalysisFile Path to impact analysis file
     * @return List of generated test scenarios
     * @throws IOException if file cannot be read
     */
    public static List<TestScenario> generateFromImpactAnalysis(String impactAnalysisFile) throws IOException {
        List<TestScenario> scenarios = new ArrayList<>();
        List<ImpactAnalyzer.ImpactResult> impacts = ImpactAnalyzer.analyzeImpactFile(impactAnalysisFile);

        for (ImpactAnalyzer.ImpactResult impact : impacts) {
            String scenarioId = "IMP_" + impact.getModule().toUpperCase().replace(" ", "_");
            String title = "Test " + impact.getModule() + " after changes";
            String description = impact.getDescription();

            TestScenario scenario = new TestScenario(scenarioId, title, description);
            scenario.setSourceType("IMPACT_ANALYSIS");
            scenario.setAffectedModules(impact.getAffectedComponents());
            scenario.setPriority(ImpactAnalyzer.getTestPriority(impact));

            // AI Decision: Categorize based on impact level
            if (ImpactAnalyzer.requiresSanityTesting(impact)) {
                scenario.addTestGroup("sanity");
                scenario.addTestGroup("smoke");
                scenario.addMetadata("reason", "Critical impact - core functionality affected");
            }
            
            if (ImpactAnalyzer.requiresRegressionTesting(impact)) {
                scenario.addTestGroup("regression");
                scenario.addMetadata("regression_scope", impact.getLevel().toString());
            }

            scenario.addTestGroup("master"); // All tests in master suite
            scenarios.add(scenario);
        }

        return scenarios;
    }

    /**
     * Generates test scenarios from release notes file
     * @param releaseNotesFile Path to release notes file
     * @return List of generated test scenarios
     * @throws IOException if file cannot be read
     */
    public static List<TestScenario> generateFromReleaseNotes(String releaseNotesFile) throws IOException {
        List<TestScenario> scenarios = new ArrayList<>();
        List<ReleaseNotesParser.ReleaseNote> notes = ReleaseNotesParser.parseReleaseNotes(releaseNotesFile);

        for (ReleaseNotesParser.ReleaseNote note : notes) {
            String scenarioId = "REL_" + note.getVersion().replace(".", "_") + "_" + 
                               note.getType().toString();
            String title = note.getTitle();
            String description = note.getDescription();

            TestScenario scenario = new TestScenario(scenarioId, title, description);
            scenario.setSourceType("RELEASE_NOTES");
            scenario.setAffectedModules(note.getAffectedModules());
            scenario.setPriority(ReleaseNotesParser.getTestPriority(note));
            scenario.addMetadata("version", note.getVersion());
            scenario.addMetadata("change_type", note.getType().toString());

            // AI Decision: Categorize based on change type
            if (ReleaseNotesParser.requiresSanityTesting(note)) {
                scenario.addTestGroup("sanity");
                scenario.addTestGroup("smoke");
                scenario.addMetadata("reason", "High-risk change requiring immediate validation");
            }

            if (ReleaseNotesParser.requiresRegressionTesting(note)) {
                scenario.addTestGroup("regression");
            }

            scenario.addTestGroup("master"); // All tests in master suite
            scenarios.add(scenario);
        }

        return scenarios;
    }

    /**
     * Generates comprehensive test scenarios from both impact analysis and release notes
     * @param impactAnalysisFile Path to impact analysis file
     * @param releaseNotesFile Path to release notes file
     * @return Combined list of test scenarios
     * @throws IOException if files cannot be read
     */
    public static List<TestScenario> generateComprehensiveScenarios(String impactAnalysisFile, 
                                                                    String releaseNotesFile) throws IOException {
        List<TestScenario> scenarios = new ArrayList<>();
        
        // Generate from impact analysis
        scenarios.addAll(generateFromImpactAnalysis(impactAnalysisFile));
        
        // Generate from release notes
        scenarios.addAll(generateFromReleaseNotes(releaseNotesFile));
        
        // Sort by priority (highest priority first)
        scenarios.sort(Comparator.comparingInt(TestScenario::getPriority));
        
        return scenarios;
    }

    /**
     * Filters scenarios by test group (e.g., "sanity", "regression")
     * @param scenarios List of all scenarios
     * @param group Test group to filter by
     * @return Filtered list of scenarios
     */
    public static List<TestScenario> filterByGroup(List<TestScenario> scenarios, String group) {
        return scenarios.stream()
                .filter(s -> s.getTestGroups().contains(group))
                .collect(Collectors.toList());
    }

    /**
     * Filters scenarios by priority
     * @param scenarios List of all scenarios
     * @param maxPriority Maximum priority level (inclusive)
     * @return Filtered list of scenarios
     */
    public static List<TestScenario> filterByPriority(List<TestScenario> scenarios, int maxPriority) {
        return scenarios.stream()
                .filter(s -> s.getPriority() <= maxPriority)
                .collect(Collectors.toList());
    }

    /**
     * Generates a report of all test scenarios
     * @param scenarios List of scenarios to report
     * @return Formatted report string
     */
    public static String generateReport(List<TestScenario> scenarios) {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("AI-GENERATED TEST SCENARIOS REPORT\n");
        report.append("=".repeat(80)).append("\n\n");

        // Group by test groups
        Map<String, List<TestScenario>> groupedScenarios = new HashMap<>();
        for (TestScenario scenario : scenarios) {
            for (String group : scenario.getTestGroups()) {
                groupedScenarios.computeIfAbsent(group, k -> new ArrayList<>()).add(scenario);
            }
        }

        // Print summary
        report.append("SUMMARY:\n");
        report.append(String.format("Total Scenarios: %d\n", scenarios.size()));
        for (Map.Entry<String, List<TestScenario>> entry : groupedScenarios.entrySet()) {
            report.append(String.format("  - %s: %d scenarios\n", entry.getKey(), entry.getValue().size()));
        }
        report.append("\n");

        // Print scenarios by priority
        report.append("SCENARIOS BY PRIORITY:\n");
        report.append("-".repeat(80)).append("\n");
        for (TestScenario scenario : scenarios) {
            report.append(String.format("\n[Priority %d] %s\n", scenario.getPriority(), scenario.getTitle()));
            report.append(String.format("  ID: %s\n", scenario.getScenarioId()));
            report.append(String.format("  Groups: %s\n", scenario.getTestGroups()));
            report.append(String.format("  Modules: %s\n", scenario.getAffectedModules()));
            report.append(String.format("  Description: %s\n", scenario.getDescription()));
            if (!scenario.getMetadata().isEmpty()) {
                report.append("  Metadata:\n");
                scenario.getMetadata().forEach((k, v) -> 
                    report.append(String.format("    - %s: %s\n", k, v)));
            }
        }

        report.append("\n").append("=".repeat(80)).append("\n");
        return report.toString();
    }
}
