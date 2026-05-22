package com.mpcz.deposit_scheme.backend.api.util;

import java.net.InetAddress;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SchedulerCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata metadata) {

        try {

            String ip = InetAddress.getLocalHost().getHostAddress();
            String hostName = InetAddress.getLocalHost().getHostName();
//            System.err.println("Host Name : " +hostName);

            // Local system IP
            if ("172.16.17.199".equals(ip)) {

                System.out.println("Scheduler disabled for local system");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}