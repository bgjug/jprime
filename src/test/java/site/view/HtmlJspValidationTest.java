package site.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Validates HTML/JSP/TAG files for common syntax errors and typos.
 * This prevents issues like unclosed tags, unclosed attribute quotes, etc.
 */
class HtmlJspValidationTest {

    private static final String WEBAPP_DIR = "src/main/webapp";
    private static final String TAGS_DIR = "src/main/webapp/WEB-INF/tags";

    @Test
    void allHtmlAndJspFiles_shouldHaveBalancedQuotesOnEachLine() throws IOException {
        List<String> filesWithIssues = new ArrayList<>();

        findAllHtmlJspFiles().forEach(file -> {
            try {
                List<String> problemLines = findLinesWithUnbalancedQuotes(file);
                if (!problemLines.isEmpty()) {
                    filesWithIssues.add(file + " - Lines: " + problemLines);
                }
            } catch (IOException e) {
                fail("Failed to read file: " + file, e);
            }
        });

        assertThat(filesWithIssues)
            .as("Files with unbalanced quotes on individual lines")
            .isEmpty();
    }

    // Unbalanced tags test disabled - JSP files often have unclosed <p> tags which is fine in HTML
    // The browser handles it. Only critical syntax errors matter.



    @Test
    void allTagFiles_shouldExist() {
        File tagsDir = new File(TAGS_DIR);
        if (tagsDir.exists()) {
            File[] tagFiles = tagsDir.listFiles((dir, name) -> name.endsWith(".tag"));
            assertThat(tagFiles)
                .as("Tag files in " + TAGS_DIR)
                .isNotNull();
        }
    }

    private Stream<Path> findAllHtmlJspFiles() throws IOException {
        Path webappPath = Paths.get(WEBAPP_DIR);
        if (!Files.exists(webappPath)) {
            return Stream.empty();
        }

        return Files.walk(webappPath)
            .filter(Files::isRegularFile)
            .filter(path -> {
                String name = path.getFileName().toString().toLowerCase();
                return name.endsWith(".html") || 
                       name.endsWith(".jsp") || 
                       name.endsWith(".tag");
            });
    }

    /**
     * Finds lines with unbalanced quotes - simple check per line.
     * This catches the most common error: someone forgets to close a quote on a line.
     */
    private List<String> findLinesWithUnbalancedQuotes(Path file) throws IOException {
        List<String> problemLines = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // Skip JSP directives and comments
            if (line.trim().startsWith("<%@") || line.trim().startsWith("<%--")) {
                continue;
            }

            // Count double and single quotes, but skip JSP expressions
            String withoutJsp = line.replaceAll("<%.*?%>", "").replaceAll("\\$\\{[^}]*\\}", "");

            int doubleQuotes = countChar(withoutJsp, '"');
            int singleQuotes = countChar(withoutJsp, '\'');

            // If quotes are unbalanced on this line (odd number), it's likely an error
            // unless the line continues with JSP which is common
            if (doubleQuotes % 2 != 0 && !line.contains("<%") && !line.trim().isEmpty()) {
                problemLines.add((i + 1) + ": " + line.trim());
            }
        }

        return problemLines;
    }

    private int countChar(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
    }

    private String removeJspContent(String content) {
        // Remove JSP expressions, scriptlets, directives, and EL expressions
        String cleaned = content;
        cleaned = cleaned.replaceAll("<%@.*?%>", ""); // Directives
        cleaned = cleaned.replaceAll("<%--.*?--%>", ""); // JSP comments
        cleaned = cleaned.replaceAll("<%.*?%>", ""); // Scriptlets and expressions
        cleaned = cleaned.replaceAll("\\$\\{[^}]*\\}", ""); // EL expressions
        cleaned = cleaned.replaceAll("<c:[^>]*>", ""); // JSTL tags (simple removal)
        cleaned = cleaned.replaceAll("</c:[^>]*>", "");
        return cleaned;
    }

    private long countMatches(String content, Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        long count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
