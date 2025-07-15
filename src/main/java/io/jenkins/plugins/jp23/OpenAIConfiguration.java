package io.jenkins.plugins.jp23;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class OpenAIConfiguration extends GlobalConfiguration {

    private String openaiEndpoint = "https://api.openai.com/v1/chat/completions";
    private String openaiModel = "gpt-3.5-turbo";
    private String openaiApiKeyUrl;

    public String getOpenaiApiKeyUrl() {
        return openaiApiKeyUrl;
    }

    public void setOpenaiApiKeyUrl(String openaiApiKeyUrl) {
        this.openaiApiKeyUrl = openaiApiKeyUrl;
        save();
    }

    public OpenAIConfiguration() {
        load(); // טוען מה דיסק
    }

    public String getOpenaiEndpoint() {
        return openaiEndpoint;
    }

    public void setOpenaiEndpoint(String openaiEndpoint) {
        this.openaiEndpoint = openaiEndpoint;
        save();
    }

    public String getOpenaiModel() {
        return openaiModel;
    }

    public void setOpenaiModel(String openaiModel) {
        this.openaiModel = openaiModel;
        save();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public static OpenAIConfiguration get() {
        return GlobalConfiguration.all().get(OpenAIConfiguration.class);
    }
}
