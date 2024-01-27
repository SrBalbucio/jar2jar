package balbucio.jar2jar;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Manifest;

public class Main {
    public static void main(String[] args) throws Exception {
        String mode = getArg("--mode", args);
        if(mode == null){
             mode = "unir";
        }
        String jar1 = getArg("--jar", args);
        String jar2 = getArg("--jar2", args);
        String target = getArg("--target", args);
        String outJar = getArg("--out", args);
        String mn = getArg("--manifest", args);
        String ignored = getArg("--ignorepackage", args);
        List<String> ignorePackages = new ArrayList<>();
        if(ignored != null){
            ignorePackages.addAll(Arrays.asList(ignored.replace(".", "/").split(",")));
        }
        if(target == null){
            System.out.println("Target is null!");
            System.exit(2);
        }
        if(mn == null){
            System.out.println("Manifest is null!");
            System.exit(2);
        }
        if(jar1 == null){
            System.out.println("Jar is null!");
            System.exit(2);
        }
        File t = new File(target);
        t.mkdirs();
        File o = new File(outJar);
        o.createNewFile();
        if(mode.equalsIgnoreCase("unir")) {
            if (jar1 != null && jar2 != null && target != null) {
                JarUtils.extractJar(new File(jar1), t);
                JarUtils.extractJar(new File(jar2), t);
                //File manifest = new File(target+"/META-INF", "MANIFEST.MF");
                JarUtils.directoryToJar(o, new Manifest(Files.newInputStream(new File(mn).toPath())), ignorePackages, t);
            }
        } else if(mode.equalsIgnoreCase("remover")){
            if(jar1 != null && target != null){
                JarUtils.extractJar(new File(jar1), t);
                JarUtils.directoryToJar(o, new Manifest(Files.newInputStream(new File(mn).toPath())), ignorePackages, t);
            }
        }
    }

    public static String getArg(String arg, String[] args){
        for (String s : args) {
            String[] cmd = s.split("=");
            if(cmd[0].equalsIgnoreCase(arg)){
                return cmd[1].replace("\"", "");
            }
        }
        return null;
    }
}