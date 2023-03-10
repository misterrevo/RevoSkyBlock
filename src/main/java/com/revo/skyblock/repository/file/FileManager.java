package com.revo.skyblock.repository.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class FileManager {

    private final Utils utils;

    public void checkFiles() {
        checkMainFolder();
        checkIslandsFolder();
        checkUsersFolder();
    }

    private void checkMainFolder() {
        final File folder = new File(utils.getPluginPath() + Constants.MAIN_FOLDER);
        if (!folder.exists()) {
            log.info(Constants.TAG + " FileManager - checkFiles - mkdir " + Constants.MAIN_FOLDER);
            folder.mkdir();
        }
    }

    private void checkIslandsFolder() {
        final File islandsFolder = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.ISLANDS_FOLDER);
        if (!islandsFolder.exists()) {
            log.info(Constants.TAG + " FileManager - checkFiles - mkdir " + Constants.ISLANDS_FOLDER);
            islandsFolder.mkdir();
        }
    }

    private void checkUsersFolder() {
        final File usersFolder = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.USERS_FOLDER);
        if (!usersFolder.exists()) {
            log.info(Constants.TAG + " FileManager - checkFiles - mkdir " + Constants.USERS_FOLDER);
            usersFolder.mkdir();
        }
    }

    public boolean checkFile(final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                log.error(Constants.TAG + " FileManager - checkFile() - error", exception);
                return false;
            }
        }
        return true;
    }
}
