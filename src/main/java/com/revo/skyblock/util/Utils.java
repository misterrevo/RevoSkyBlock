package com.revo.skyblock.util;

import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;

import java.io.File;
import java.util.logging.Logger;

@Singleton
public class Utils {

    private static final Logger log = Plugin.getApplicationContext().getLogger();

    public String getPluginPath() {
        try {
            final String path = getClass().getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath();
            final StringBuilder stringBuilder = new StringBuilder();
            final String[] pathSplit = path.split("/");
            for (int x = 0; (x + 1) < pathSplit.length; x++) {
                stringBuilder.append(pathSplit[x] + "/");
            }
            return stringBuilder.toString().replaceFirst("/", "");
        } catch (Exception exception) {
            log.info("Utils - getPluginPath() - error");
            exception.printStackTrace();
            return null;
        }
    }

    public long getLastId(String folderName) {
        final File file = new File(getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + folderName);
        long max = 0;
        for (File target : file.listFiles()) {
            long id = Long.valueOf(target.getName().split(Constants.DOT)[0]);
            if(id > max) {
                max = id;
            }
        }
        return max;
    }

    public String replaceColors(String string){
        return string.replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
    }
}
