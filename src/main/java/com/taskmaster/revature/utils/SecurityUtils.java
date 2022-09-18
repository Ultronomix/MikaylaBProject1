package com.taskmaster.revature.utils;

import com.taskmaster.revature.users.*;

public class SecurityUtils {


    public static boolean isDirector(UserResponse subject) {
        return subject.getRole().equals("ADMIN");
    }

    public static boolean isFinanceMan(UserResponse subject) {
        return subject.getRole().equals("MANAGER");
    }

    // Only to be used with GET user requests
    public static boolean requesterOwned(UserResponse subject, String resourceId) {
        return subject.getUser_Id().equals(resourceId);


    }
}
