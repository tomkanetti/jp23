package io.jenkins.plugins.jp23;

import hudson.Extension;
import hudson.model.RootAction;
import java.io.IOException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class OpenAISettingsEndpoint implements RootAction {

    @Override
    public String getUrlName() {
        return "jp23-settings";
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    public void doEndpoint(StaplerRequest req, StaplerResponse rsp) throws IOException {
        String url = OpenAIConfiguration.get().getOpenaiEndpoint();
        rsp.setContentType("application/json");
        rsp.getWriter().write("{\"endpoint\": \"" + url + "\"}");
    }
}
