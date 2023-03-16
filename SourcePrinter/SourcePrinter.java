package sourceprinter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SourcePrinter {

    public static void findSourceCode(String methodSignature) throws IOException {
        File dir = new File("./decomps");
        File[] files = dir.listFiles();
        StringBuilder sb = new StringBuilder();
        if (files != null) {
            for (File f : files) {
                sb.append(Files.readString(Path.of(f.getAbsolutePath())));
            }
        } else {
            System.out.println("Decomps is empty");
        }
        String code = sb.toString();
        System.out.println("Full code:");
        System.out.println(code);
        System.out.println();
        sb = new StringBuilder();
        Scanner sc = new Scanner(code);
        while (sc.hasNext()) {
            String nextLine = sc.nextLine();
            if (nextLine.contains("public") && nextLine.contains(methodSignature)) {
                sb.append(nextLine);
                int nestings = 1;
                while (nestings != 0) {
                    nextLine = sc.nextLine();
                    sb.append("\n");
                    if (nextLine.contains("{") && (nextLine.contains("}"))) {
                        sb.append(nextLine);
                        sb.append("\n");
                    } else if (nextLine.contains("{")) {
                        sb.append(nextLine);
                        sb.append("\n");
                        nestings++;
                    } else if (nextLine.contains("}")) {
                        sb.append(nextLine);
                        sb.append("\n");
                        nestings--;
                    } else {
                        sb.append(nextLine);
                        sb.append("\n");
                    }
                }
                break;
            }
        }
        if(sb.isEmpty()) {
            System.out.println("Method not found");
        } else {
            System.out.println("Method " + methodSignature + ":");
            System.out.println(sb);
        }
    }
}
