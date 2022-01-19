package studio.thamessia.CustomConjecture;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CustomCommandManager {
    public static void manager() {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        javaCompiler.run(null, null, null, "divineCustomCommand.txt");
    }
}
