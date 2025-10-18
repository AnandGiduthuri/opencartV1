package ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * ImpactAnalyzer analyzes code changes and their impact on the system
 * to help determine which test scenarios should be included in sanity and regression testing.
 */
public class ImpactAnalyzer {

    public enum ImpactLevel {
        CRITICAL,  // Core functionality changes - requires sanity testing
        HIGH,      // Significant feature changes - requires regression testing
        MEDIUM,    // Minor changes - selective regression
        LOW        // Documentation/config changes - minimal testing
    }

    public static class ImpactResult {
        private String module;
        private ImpactLevel level;
        private List<String> affectedComponents;
        private String description;

        public ImpactResult(String module, ImpactLevel level, List<String> affectedComponents, String description) {
            this.module = module;
            this.level = level;
            this.affectedComponents = affectedComponents;
            this.description = description;
        }

        public String getModule() {
            return module;
        }

        public ImpactLevel getLevel() {
            return level;
        }

        public List<String> getAffectedComponents() {
            return affectedComponents;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return String.format("Module: %s, Impact: %s, Components: %s, Description: %s",
                    module, level, affectedComponents, description);
        }
    }

    /**
     * Analyzes an impact analysis file and returns impact results
     * @param filePath Path to the impact analysis file
     * @return List of impact results
     * @throws IOException if file cannot be read
     */
    public static List<ImpactResult> analyzeImpactFile(String filePath) throws IOException {
        List<ImpactResult> results = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String currentModule = "";
        ImpactLevel currentLevel = ImpactLevel.LOW;
        List<String> currentComponents = new ArrayList<>();
        String currentDescription = "";

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue; // Skip empty lines and comments
            }

            if (line.startsWith("MODULE:")) {
                // Save previous impact result if exists
                if (!currentModule.isEmpty()) {
                    results.add(new ImpactResult(currentModule, currentLevel, 
                            new ArrayList<>(currentComponents), currentDescription));
                }
                // Start new module
                currentModule = line.substring(7).trim();
                currentComponents = new ArrayList<>();
                currentDescription = "";
                currentLevel = ImpactLevel.LOW;
            } else if (line.startsWith("IMPACT:")) {
                String impact = line.substring(7).trim().toUpperCase();
                currentLevel = ImpactLevel.valueOf(impact);
            } else if (line.startsWith("COMPONENTS:")) {
                String componentsStr = line.substring(11).trim();
                currentComponents = Arrays.asList(componentsStr.split(",\\s*"));
            } else if (line.startsWith("DESCRIPTION:")) {
                currentDescription = line.substring(12).trim();
            }
        }

        // Add the last module
        if (!currentModule.isEmpty()) {
            results.add(new ImpactResult(currentModule, currentLevel, 
                    new ArrayList<>(currentComponents), currentDescription));
        }

        reader.close();
        return results;
    }

    /**
     * Determines if a module requires sanity testing based on impact level
     * @param impactResult The impact analysis result
     * @return true if sanity testing is required
     */
    public static boolean requiresSanityTesting(ImpactResult impactResult) {
        return impactResult.getLevel() == ImpactLevel.CRITICAL;
    }

    /**
     * Determines if a module requires regression testing based on impact level
     * @param impactResult The impact analysis result
     * @return true if regression testing is required
     */
    public static boolean requiresRegressionTesting(ImpactResult impactResult) {
        return impactResult.getLevel() == ImpactLevel.CRITICAL || 
               impactResult.getLevel() == ImpactLevel.HIGH ||
               impactResult.getLevel() == ImpactLevel.MEDIUM;
    }

    /**
     * Gets test priority based on impact level
     * @param impactResult The impact analysis result
     * @return Priority level (1-4, where 1 is highest)
     */
    public static int getTestPriority(ImpactResult impactResult) {
        switch (impactResult.getLevel()) {
            case CRITICAL:
                return 1;
            case HIGH:
                return 2;
            case MEDIUM:
                return 3;
            case LOW:
                return 4;
            default:
                return 4;
        }
    }
}
