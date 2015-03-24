package com.soa.zeus.web.common;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shizhizhong
 * Date: 14-1-14
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class VelocityToolbox20View extends VelocityToolboxView {

    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        // Create a ChainedContext instance.
        ViewToolContext ctx;

        ctx = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());

        ctx.putAll(model);
        if (this.getToolboxConfigLocation() != null) {
            ToolManager tm = new ToolManager();
            tm.setVelocityEngine(getVelocityEngine());
            tm.configure(getServletContext().getRealPath(getToolboxConfigLocation()));
            if (tm.getToolboxFactory().hasTools(Scope.REQUEST)) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.REQUEST));
            }
            if (tm.getToolboxFactory().hasTools(Scope.APPLICATION)) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.APPLICATION));
            }
            if (tm.getToolboxFactory().hasTools(Scope.SESSION)) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.SESSION));
            }
        }
        mergeContext(ctx);
        return ctx;
    }

    private void mergeContext(Context context) {
        if (velocityUrl != null) {
            for (Map.Entry<String, Object> entry : velocityUrl.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }
        if (velocityTools != null) {
            for (Map.Entry<String, Object> entry : velocityTools.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }

    }

    private Map<String, Object> velocityTools;
    private Map<String, Object> velocityUrl;

    public void setVelocityTools(Map<String, Object> velocityTools) {
        this.velocityTools = velocityTools;
    }

    public void setVelocityUrl(Map<String, Object> velocityUrl) {
        this.velocityUrl = velocityUrl;
    }
}
