package com.revo.skyblock.repository.file;

import com.google.inject.Singleton;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class FileManager {

    private static final Logger log = Plugin.getApplicationContext().getLogger();

    public void checkFiles(){
        log.info("FileManager - checkFiles() - enter ");
        final File folder = new File(Utils.getPluginPath() + Constants.MAIN_FOLDER);
        if (!folder.exists()) {
            log.info("FileManager - checkFiles - mkdir " + Constants.MAIN_FOLDER);
            folder.mkdir();
        }
        final File islandsFolder = new File(Utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.ISLANDS_FOLDER);
        if (!folder.exists()) {
            log.info("FileManager - checkFiles - mkdir " + Constants.ISLANDS_FOLDER);
            islandsFolder.mkdir();
        }
        final File usersFolder = new File(Utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.USERS_FOLDER);
        if (!folder.exists()) {
            log.info("FileManager - checkFiles - mkdir " + Constants.USERS_FOLDER);
            usersFolder.mkdir();
        }
    }

    public boolean checkFile(final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.info("FileManager - checkFile() - error");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
