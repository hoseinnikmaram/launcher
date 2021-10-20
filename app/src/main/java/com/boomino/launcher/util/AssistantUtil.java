package com.boomino.launcher.util;

import android.content.ComponentName;
import android.content.Context;
import android.os.UserHandle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AssistantUtil {

    public static String getCurrentAssistWithReflection(Context context) {
        try {
            Method myUserIdMethod = UserHandle.class.getDeclaredMethod("myUserId");
            myUserIdMethod.setAccessible(true);
            Integer userId = (Integer) myUserIdMethod.invoke(null);

            if (userId != null) {
                Constructor constructor = Class.forName("com.android.internal.app.AssistUtils").getConstructor(Context.class);
                Object assistUtils = constructor.newInstance(context);

                Method getAssistComponentForUserMethod = assistUtils.getClass().getDeclaredMethod("getAssistComponentForUser", int.class);
                getAssistComponentForUserMethod.setAccessible(true);
                ComponentName componentName =  (ComponentName) getAssistComponentForUserMethod.invoke(assistUtils, userId);
                return componentName.getPackageName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
