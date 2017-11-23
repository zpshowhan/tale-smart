package org.esmart.tale.jetag;

import jetbrick.template.JetAnnotations.Tags;
import jetbrick.template.runtime.JetTagContext;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

/**
 * 主题公共标签
 * <p>
 * Created by biezhi on 2017/2/23.
 */
@Tags
public class JetTag {

    public static void social(JetTagContext ctx, String name) throws IOException {
        String value = Commons.site_option("social_" + name);
        if (StringUtils.isNotBlank(value)) {
            value = ctx.getBodyContent();
        }
        ctx.getWriter().print(value.toString());
    }

}
