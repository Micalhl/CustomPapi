package me.mical.custompapi.utils;

import lombok.NonNull;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.sql.DaoManager;

import java.util.UUID;

public class StorageUtil {

    public static void initPlayerData(@NonNull final UUID uuid) {
        ParamManager.getInstance().getParams().forEach((name, param) -> DaoManager.getDao().create(uuid.toString(), name));
    }
}
