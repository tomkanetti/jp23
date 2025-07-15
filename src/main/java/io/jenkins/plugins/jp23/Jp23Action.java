package io.jenkins.plugins.jp23;

import hudson.Extension;
import hudson.model.RootAction;
import java.io.IOException;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

@Extension
public class Jp23Action implements RootAction {

    @Override
    public String getIconFileName() {
        return null;
    } // לא צריך לינק בתפריט

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getUrlName() {
        return "jp23";
    }

    @RequirePOST
    public void doChat(StaplerRequest req, StaplerResponse rsp, @QueryParameter String message) throws IOException {
        rsp.setContentType("text/plain");
        rsp.getWriter().write("Bot response to: " + message); // תוכל להחליף בקריאה ל־OpenAI
    }
}
