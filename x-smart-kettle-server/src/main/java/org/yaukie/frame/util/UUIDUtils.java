package org.yaukie.frame.util;
import java.util.UUID;

public class UUIDUtils {

    /**
     * 得到32位的uuid
     *
     * @return UUID
     */
    public static String getUUID32() {

        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


}
