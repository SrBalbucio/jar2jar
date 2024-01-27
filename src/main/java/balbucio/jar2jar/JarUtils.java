package balbucio.jar2jar;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JarUtils {


    public static void directoryToJar(File jarFile, Manifest manifest, List<String> ignorePackages,  File... directory) throws Exception {

        JarOutputStream jarOutputStream;

        if(manifest != null){
            jarOutputStream = new JarOutputStream(new FileOutputStream(jarFile), manifest);
        } else{
            jarOutputStream = new JarOutputStream(new FileOutputStream(jarFile));
        }

        Set<String> addedPath = new HashSet<>();


        for(File dir : directory) {
            if(dir != null) {
                if(dir.listFiles() != null && dir.listFiles().length != 0) {
                    for (File file : dir.listFiles()) {
                        if (file != null) {
                            addFilesToJar(file, "", jarOutputStream, addedPath, ignorePackages);
                        }
                    }
                }
            }
        }
        jarOutputStream.flush();
        jarOutputStream.close();
    }

    private static void addFilesToJar(File source, String parentPath, JarOutputStream jarOutputStream, Set<String> files, List<String> ignorePackages) throws Exception {
        byte[] buffer = new byte[1024];
        String entryName = parentPath + source.getName();

        if(ignorePackages.contains(entryName)){
            return;
        }


        if (source.isDirectory()) {
            if (!entryName.isEmpty()) {
                if (!entryName.endsWith("/")) {
                    entryName += "/";
                }

                if(files.contains(entryName)){
                    for (File file : source.listFiles()) {
                        addFilesToJar(file, entryName, jarOutputStream, files, ignorePackages);
                    }
                    return;
                }

                files.add(entryName);
                JarEntry jarEntry = new JarEntry(entryName.replace("\\", "/"));
                jarOutputStream.putNextEntry(jarEntry);
                jarOutputStream.closeEntry();
            }
            for (File file : source.listFiles()) {
                addFilesToJar(file, entryName, jarOutputStream, files, ignorePackages);
            }
        } else {
            if(files.contains(entryName)){
                return;
            }
            files.add(entryName);
            JarEntry jarEntry = new JarEntry(entryName.replace("\\", "/"));
            jarOutputStream.putNextEntry(jarEntry);

            FileInputStream inputStream = new FileInputStream(source);
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                jarOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            jarOutputStream.closeEntry();
        }
    }

    public static void extractJar(File jarFilePath, File destDir) throws Exception {

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            for (JarEntry entry : jarFile.stream().toArray(JarEntry[]::new)) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    if(!entryName.contains("MANIFEST.MF")) {
                        File outFile = new File(destDir, entryName);
                        File parent = outFile.getParentFile();
                        if (!parent.exists()) {
                            parent.mkdirs();
                        }
                        try (InputStream inputStream = jarFile.getInputStream(entry);
                             FileOutputStream outputStream = new FileOutputStream(outFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }
            }
        }
    }
}
