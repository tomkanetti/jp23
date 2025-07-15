package io.jenkins.plugins.jp23;

import hudson.Extension;
import hudson.model.RootAction;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class OpenAISettingsEndpoint implements RootAction {

    @Override
    public String getUrlName() {
        return "jp23-settings"; // יהיה זמין ב־ /jenkins/jp23-settings/settings
    }

    @Override
    public String getIconFileName() {
        return null; // לא מציג אייקון בתפריט
    }

    @Override
    public String getDisplayName() {
        return null; // לא מוצג בתפריט
    }

    // חשיפה של ההגדרות ב־/jenkins/jp23-settings/settings
    public void doSettings(StaplerRequest req, StaplerResponse rsp) throws IOException {
        OpenAIConfiguration config = OpenAIConfiguration.get();

        String endpoint = config.getOpenaiEndpoint();
        String model = config.getOpenaiModel();
        String apiKeyUrl = config.getOpenaiApiKeyUrl();

        String apiKey = "";
        if (apiKeyUrl != null && !apiKeyUrl.isEmpty()) {
            try {
                java.net.URL url = new java.net.URL(apiKeyUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int status = conn.getResponseCode();
                if (status == 200) {
                    try (java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        apiKey = reader.readLine(); // מצפה שהמפתח יהיה בשורה אחת
                    }
                } else {
                    System.err.println("Failed to get API key: HTTP " + status);
                }
            } catch (Exception e) {
                System.err.println("Error fetching API key: " + e.getMessage());
            }
        }

        // escape "
        endpoint = endpoint != null ? endpoint.replace("\"", "\\\"") : "";
        model = model != null ? model.replace("\"", "\\\"") : "";
        apiKey = apiKey != null ? apiKey.replace("\"", "\\\"") : "";

        String json =
                String.format("{\"endpoint\": \"%s\", \"apiKey\": \"%s\", \"model\": \"%s\"}", endpoint, apiKey, model);

        rsp.setContentType("application/json");
        rsp.setStatus(HttpServletResponse.SC_OK);
        rsp.getWriter().write(json);
    }
}
