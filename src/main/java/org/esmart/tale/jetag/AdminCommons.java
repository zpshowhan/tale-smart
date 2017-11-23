package org.esmart.tale.jetag;

import org.esmart.tale.model.Tmetas;
import org.esmart.tale.utils.Tools;

import jetbrick.template.JetAnnotations.Functions;

/**
 * 后台公共函数
 * <p>
 * Created by biezhi on 2017/2/21.
 */
@Functions
public final class AdminCommons {

    /**
     * 判断category和cat的交集
     *
     * @param cats
     * @return
     */
	
    public static boolean exist_cat(Tmetas category, String cats) {
        String[] arr = cats.split(",");
        if (null != arr && arr.length > 0) {
            for (String c : arr) {
                if (c.trim().equals(category.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final String[] COLORS = {"default", "primary", "success", "info", "warning", "danger", "inverse", "purple", "pink"};

    public static String rand_color() {
        int r = Tools.rand(0, COLORS.length - 1);
        return COLORS[r];
    }

}
