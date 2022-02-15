/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN DECEMBER 2021.
*/
package studio.thamessia.CustomConjecture;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

public class CustomCommandManager {
    public static void manager() {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        javaCompiler.run(null, null, null, "CustomCommandExecutor.java");

        CustomCommandExecutor customCommandExecutor = new CustomCommandExecutor();
        customCommandExecutor.execute();

        try {
            Process runtime = Runtime.getRuntime().exec("java CustomCommandExecutor");

            BufferedReader bufferedReaderManagerONE = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
            BufferedReader bufferedReaderManagerTWO = new BufferedReader(new InputStreamReader(runtime.getErrorStream()));

            String str = "sesso";
            String ing = "pazzo";


            while ((str = bufferedReaderManagerONE.readLine()) != null) {
                System.out.println(str);
            }

            while ((ing = bufferedReaderManagerTWO.readLine()) != null) {
                System.out.println(ing);
            }

            //System.out.println("hello worldt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
