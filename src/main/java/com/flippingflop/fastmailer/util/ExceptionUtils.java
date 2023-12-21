package com.flippingflop.fastmailer.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionUtils {

    /**
     * Get traces of this project files only.
     * From the exception given, remove all the traces of external libraries, built-in packages etc. <p>
     *     Examples: <pre>
     *     Caused by: SomeException: this is example exception.
     *     org.springframework.some.somePackage.SomeClass.someMethod(SomeClass.java:560)
     *     com.flippingflop.fastmailer.service.EmailService.emailMethod(EmailService.java:120)
     *     org.springframework.some.otherPackage.OtherClass.otherMethod(SomeClass.java:76)
     *     com.flippingflop.fastmailer.util.SomeUtils.someMethod:280
     *     ...
     *
     *     returns <br>
     *     "SomeException
     *     - com.flippingflop.fastmailer.service.EmailService.emailMethod:120
     *     - com.flippingflop.fastmailer.util.SomeUtils.someMethod:280
     *     ..."
     *     </pre>
     *
     * </p>
     * @param exception
     * @return  the retrieved exception string.
     */
    public static String retrieveExceptionInfo(Exception exception) {
        StringBuilder sb = new StringBuilder();

        // Get class name from exception.
        sb.append(exception.getClass().toString().substring(exception.getClass().toString().lastIndexOf(".") + 1));

        // Filter the traces from this project files only.
        List<StackTraceElement> customElementList = Arrays.stream(exception.getStackTrace())
                .filter(i -> i.getClassName().startsWith("com.flippingflop."))
                .collect(Collectors.toList());

        for (StackTraceElement stackTraceElement: customElementList) {
            sb.append(" - ")
                    .append(stackTraceElement.getClassName())
                    .append(".")
                    .append(stackTraceElement.getMethodName())
                    .append(":")
                    .append(stackTraceElement.getLineNumber());
        }

        return sb.toString();
    }

}
