package ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * ReleaseNotesParser parses release notes to extract features, bug fixes,
 * and changes that need to be tested.
 */
public class ReleaseNotesParser {

    public enum ChangeType {
        NEW_FEATURE,
        BUG_FIX,
        ENHANCEMENT,
        SECURITY_FIX,
        PERFORMANCE_IMPROVEMENT,
        DEPRECATION,
        BREAKING_CHANGE
    }

    public static class ReleaseNote {
        private String version;
        private String date;
        private ChangeType type;
        private String title;
        private String description;
        private List<String> affectedModules;

        public ReleaseNote(String version, String date, ChangeType type, String title, 
                          String description, List<String> affectedModules) {
            this.version = version;
            this.date = date;
            this.type = type;
            this.title = title;
            this.description = description;
            this.affectedModules = affectedModules;
        }

        public String getVersion() {
            return version;
        }

        public String getDate() {
            return date;
        }

        public ChangeType getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getAffectedModules() {
            return affectedModules;
        }

        @Override
        public String toString() {
            return String.format("Version: %s, Type: %s, Title: %s, Modules: %s",
                    version, type, title, affectedModules);
        }
    }

    /**
     * Parses a release notes file and extracts structured information
     * @param filePath Path to the release notes file
     * @return List of release notes
     * @throws IOException if file cannot be read
     */
    public static List<ReleaseNote> parseReleaseNotes(String filePath) throws IOException {
        List<ReleaseNote> releaseNotes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String currentVersion = "";
        String currentDate = "";
        ChangeType currentType = ChangeType.ENHANCEMENT;
        String currentTitle = "";
        String currentDescription = "";
        List<String> currentModules = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue; // Skip empty lines and comments
            }

            if (line.startsWith("VERSION:")) {
                // Save previous release note if exists
                if (!currentVersion.isEmpty() && !currentTitle.isEmpty()) {
                    releaseNotes.add(new ReleaseNote(currentVersion, currentDate, currentType,
                            currentTitle, currentDescription, new ArrayList<>(currentModules)));
                }
                // Start new version
                currentVersion = line.substring(8).trim();
                currentDate = "";
                currentTitle = "";
                currentDescription = "";
                currentModules = new ArrayList<>();
                currentType = ChangeType.ENHANCEMENT;
            } else if (line.startsWith("DATE:")) {
                currentDate = line.substring(5).trim();
            } else if (line.startsWith("TYPE:")) {
                String typeStr = line.substring(5).trim().toUpperCase().replace(" ", "_");
                try {
                    currentType = ChangeType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    currentType = ChangeType.ENHANCEMENT;
                }
            } else if (line.startsWith("TITLE:")) {
                currentTitle = line.substring(6).trim();
            } else if (line.startsWith("DESCRIPTION:")) {
                currentDescription = line.substring(12).trim();
            } else if (line.startsWith("MODULES:")) {
                String modulesStr = line.substring(8).trim();
                currentModules = Arrays.asList(modulesStr.split(",\\s*"));
            } else if (line.startsWith("---")) {
                // Separator - save current note and reset
                if (!currentVersion.isEmpty() && !currentTitle.isEmpty()) {
                    releaseNotes.add(new ReleaseNote(currentVersion, currentDate, currentType,
                            currentTitle, currentDescription, new ArrayList<>(currentModules)));
                    currentTitle = "";
                    currentDescription = "";
                    currentModules = new ArrayList<>();
                    currentType = ChangeType.ENHANCEMENT;
                }
            }
        }

        // Add the last release note
        if (!currentVersion.isEmpty() && !currentTitle.isEmpty()) {
            releaseNotes.add(new ReleaseNote(currentVersion, currentDate, currentType,
                    currentTitle, currentDescription, new ArrayList<>(currentModules)));
        }

        reader.close();
        return releaseNotes;
    }

    /**
     * Determines if a release note requires sanity testing
     * @param note The release note
     * @return true if sanity testing is required
     */
    public static boolean requiresSanityTesting(ReleaseNote note) {
        return note.getType() == ChangeType.BREAKING_CHANGE ||
               note.getType() == ChangeType.SECURITY_FIX ||
               note.getType() == ChangeType.NEW_FEATURE;
    }

    /**
     * Determines if a release note requires regression testing
     * @param note The release note
     * @return true if regression testing is required
     */
    public static boolean requiresRegressionTesting(ReleaseNote note) {
        return note.getType() != ChangeType.DEPRECATION; // All except deprecation
    }

    /**
     * Gets test priority based on change type
     * @param note The release note
     * @return Priority level (1-4, where 1 is highest)
     */
    public static int getTestPriority(ReleaseNote note) {
        switch (note.getType()) {
            case BREAKING_CHANGE:
            case SECURITY_FIX:
                return 1;
            case NEW_FEATURE:
            case BUG_FIX:
                return 2;
            case ENHANCEMENT:
            case PERFORMANCE_IMPROVEMENT:
                return 3;
            case DEPRECATION:
                return 4;
            default:
                return 3;
        }
    }
}
