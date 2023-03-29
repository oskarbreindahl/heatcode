import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SourcePrinter {

    public static void main(String[] args) {
        try {
            makeJSONOutput("../test.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makeJSONOutput(String profilerFilePath) throws IOException {;
        FileWriter fw = new FileWriter("./sourcePrinterOut.json");
        JSONWriter jsonWriter = new JSONWriter(fw);
        jsonWriter.object();
        jsonWriter.key("methods");
        jsonWriter.array();

        String outputString = Files.readString(Path.of(profilerFilePath));
        JSONObject outputJSON = new JSONObject(outputString);
        JSONArray jsonArr = outputJSON.getJSONArray("methods");

        for (int i = 0; i < jsonArr.length(); i++)
        {
            jsonWriter.object();

            jsonWriter.key("name");
            String[] nameArr = jsonArr.getJSONObject(i).getString("name").split("[.]");
            String name = nameArr[nameArr.length - 1];
            jsonWriter.value(name);

            jsonWriter.key("calls");
            jsonWriter.value(jsonArr.getJSONObject(i).getInt("calls"));

            jsonWriter.key("source");
            jsonWriter.value(findSourceCode(name));

            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
        fw.close();

    }

    public static String findSourceCode(String methodSignature) throws IOException {
        File dir = new File("../decomps");
        File[] files = dir.listFiles();
        StringBuilder sb = new StringBuilder();
        String code = "";
        if (files != null) {
            code = buildCodeString(files, sb);
        } else {
            System.out.println("Decomps is empty");
        }
        sb = new StringBuilder();
        Scanner sc = new Scanner(code);
        while (sc.hasNext()) {
            String nextLine = sc.nextLine().trim();
            if (isMethod(nextLine) && nextLine.contains(methodSignature)) {
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
            System.out.println("Method not found: " + methodSignature);
            return "";
        } else {
            return sb.toString();
        }
    }

    public static String buildCodeString(File[] files, StringBuilder sb) throws IOException {
        for (File f : files) {
            if (!f.isDirectory()) {
                sb.append(Files.readString(Path.of(f.getAbsolutePath())));
            } else {
                if (f.listFiles() != null) {
                    sb.append(buildCodeString(f.listFiles(), sb));
                }
            }
        }
        return sb.toString();
    }

    private static boolean isMethod(String signature) {
        return signature.contains("public")
                || signature.contains("private")
                || signature.contains("protected")
                || signature.contains("void");

    }
}
