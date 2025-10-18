package ai;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Demonstration class showing how to use the AI Test Scenario Generator
 * to create sanity and regression test scenarios based on impact analysis
 * and release notes.
 */
public class TestScenarioGeneratorDemo {

    public static void main(String[] args) {
        try {
            System.out.println("=".repeat(80));
            System.out.println("AI-BASED TEST SCENARIO GENERATOR DEMO");
            System.out.println("=".repeat(80));
            System.out.println();

            // File paths
            String impactAnalysisFile = "./testData/ai/impact_analysis_example.txt";
            String releaseNotesFile = "./testData/ai/release_notes_example.txt";
            String outputFile = "./testData/ai/generated_test_scenarios.txt";

            // Generate scenarios from both sources
            System.out.println("Analyzing impact analysis and release notes...");
            List<AITestScenarioGenerator.TestScenario> allScenarios = 
                AITestScenarioGenerator.generateComprehensiveScenarios(
                    impactAnalysisFile, releaseNotesFile);

            System.out.println("Generated " + allScenarios.size() + " test scenarios.");
            System.out.println();

            // Generate and display report
            String report = AITestScenarioGenerator.generateReport(allScenarios);
            System.out.println(report);

            // Save report to file
            saveReportToFile(report, outputFile);
            System.out.println("Report saved to: " + outputFile);
            System.out.println();

            // Show sanity test scenarios
            System.out.println("\n" + "=".repeat(80));
            System.out.println("SANITY TEST SCENARIOS (Critical/High Priority)");
            System.out.println("=".repeat(80));
            List<AITestScenarioGenerator.TestScenario> sanityScenarios = 
                AITestScenarioGenerator.filterByGroup(allScenarios, "sanity");
            for (AITestScenarioGenerator.TestScenario scenario : sanityScenarios) {
                System.out.println("\n" + scenario.toTestNGAnnotation());
                System.out.println("public void " + scenario.getScenarioId().toLowerCase() + "() {");
                System.out.println("    // " + scenario.getTitle());
                System.out.println("    // " + scenario.getDescription());
                System.out.println("    // Modules: " + scenario.getAffectedModules());
                System.out.println("}");
            }

            // Show regression test scenarios
            System.out.println("\n" + "=".repeat(80));
            System.out.println("REGRESSION TEST SCENARIOS");
            System.out.println("=".repeat(80));
            List<AITestScenarioGenerator.TestScenario> regressionScenarios = 
                AITestScenarioGenerator.filterByGroup(allScenarios, "regression");
            System.out.println("Total Regression Test Scenarios: " + regressionScenarios.size());
            System.out.println("\nTop 5 High Priority Regression Tests:");
            regressionScenarios.stream()
                .limit(5)
                .forEach(scenario -> {
                    System.out.println("\n" + scenario.toTestNGAnnotation());
                    System.out.println("// " + scenario.getTitle());
                });

            // Statistics
            System.out.println("\n" + "=".repeat(80));
            System.out.println("STATISTICS");
            System.out.println("=".repeat(80));
            System.out.println("Total Test Scenarios: " + allScenarios.size());
            System.out.println("Sanity Tests: " + sanityScenarios.size());
            System.out.println("Regression Tests: " + regressionScenarios.size());
            
            long priority1 = allScenarios.stream().filter(s -> s.getPriority() == 1).count();
            long priority2 = allScenarios.stream().filter(s -> s.getPriority() == 2).count();
            long priority3 = allScenarios.stream().filter(s -> s.getPriority() == 3).count();
            long priority4 = allScenarios.stream().filter(s -> s.getPriority() == 4).count();
            
            System.out.println("\nBy Priority:");
            System.out.println("  Priority 1 (Critical): " + priority1);
            System.out.println("  Priority 2 (High): " + priority2);
            System.out.println("  Priority 3 (Medium): " + priority3);
            System.out.println("  Priority 4 (Low): " + priority4);

            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMO COMPLETED SUCCESSFULLY");
            System.out.println("=".repeat(80));

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the report to a file
     */
    private static void saveReportToFile(String report, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(report);
        }
    }
}
