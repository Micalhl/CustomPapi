package me.mical.custompapi.utils;

import me.mical.custompapi.sql.DaoManager;

import java.util.UUID;

public class ParamUtil {

    public static double get(String param, UUID player) {
        if (DaoManager.getDao().has(player.toString(), param)) {
            return DaoManager.getDao().getValue(player.toString(), param);
        }
        return 0;
    }

    public static boolean add(String param, UUID player, double value) {
        if (DaoManager.getDao().has(player.toString(), param)) {
            double old = DaoManager.getDao().getValue(player.toString(), param);
            long timestamp = DaoManager.getDao().getTimestamp(player.toString(), param);
            DaoManager.getDao().edit(player.toString(), param, timestamp, old + value);
            return true;
        }
        return false;
    }

    public static boolean take(String param, UUID player, double value) {
        if (DaoManager.getDao().has(player.toString(), param)) {
            double old = DaoManager.getDao().getValue(player.toString(), param);
            long timestamp = DaoManager.getDao().getTimestamp(player.toString(), param);
            DaoManager.getDao().edit(player.toString(), param, timestamp, old - value);
            return true;
        }
        return false;
    }

    public static boolean set(String param, UUID player, double value) {
        if (DaoManager.getDao().has(player.toString(), param)) {
            long timestamp = DaoManager.getDao().getTimestamp(player.toString(), param);
            DaoManager.getDao().edit(player.toString(), param, timestamp, value);
            return true;
        }
        return false;
    }
}
