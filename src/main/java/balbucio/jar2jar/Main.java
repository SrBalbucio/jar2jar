package balbucio.jar2jar;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.jar.Manifest;

public class Main {
    public static void main(String[] args) throws Exception {
        String jar1 = getFile("--jar", args);
        String jar2 = getFile("--jar2", args);
        String target = getFile("--target", args);
        String outJar = getFile("--out", args);
        if(jar1 != null && jar2 != null && target != null){
            File t = new File(target);
            t.mkdirs();
            File o = new File(outJar);
            o.createNewFile();
            JarUtils.extractJar(new File(jar1), t);
            JarUtils.extractJar(new File(jar2), t);
            File manifest = new File(target+"/META-INF", "MANIFEST.MF");
            JarUtils.directoryToJar(o, new Manifest(Files.newInputStream(manifest.toPath())), t);
        }
    }

    public static String getFile(String arg, String[] args){
        for (String s : args) {
            String[] cmd = s.split("=");
            if(cmd[0].equalsIgnoreCase(arg)){
                return cmd[1].replace("\"", "");
            }
        }
        return null;
    }
}