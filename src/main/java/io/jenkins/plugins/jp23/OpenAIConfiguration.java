package io.jenkins.plugins.jp23;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class OpenAIConfiguration extends GlobalConfiguration {

    private String openaiEndpoint;

    public OpenAIConfiguration() {
        load(); // טוען מהקונפיגורציה
    }

    public String getOpenaiEndpoint() {
        return openaiEndpoint;
    }

    public void setOpenaiEndpoint(String openaiEndpoint) {
        this.openaiEndpoint = openaiEndpoint;
        save(); // שומר לקובץ config.xml
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
