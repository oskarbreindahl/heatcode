package agent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.io.File;

public class CodePrinter implements ClassFileTransformer {

    //Written by ChatGPT
    private final String outputDir;

    //Written by ChatGPT
    public CodePrinter(String outputDir) {
        this.outputDir = outputDir;
    }

    //Written by ChatGPT
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (!isJavaClass(className)) {
            try {
                // Compute output file path
                String outputFilePath = outputDir + File.separator + className.replace('.', File.separatorChar) + ".class";


                // Create output directory if it doesn't exist
                new File(outputFilePath).getParentFile().mkdirs();


                // Write transformed class to output file
                try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                    fos.write(classfileBuffer);
                }

                // Print a message indicating where the class file was written
                System.out.println("Class file written to: " + outputFilePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Return the original byte code unchanged
        return classfileBuffer;
    }

    //Written by Oskar Breindahl
    private boolean isJavaClass(String className) {
        return className.startsWith("java")
                || className.startsWith("sun")
                || className.startsWith("jdk")
                || className.startsWith("apple")
                || className.startsWith("com")
                || className.startsWith("javax");
    }

    //Written by Oskar Breindahl
    public static void deleteDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file.getAbsolutePath());
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    //Written by ChatGPT
    public static void premain(String agentArgs, Instrumentation inst) {
        deleteDirectory("./output");
        String outputDir = "./output";
        inst.addTransformer(new CodePrinter(outputDir));
    }
}