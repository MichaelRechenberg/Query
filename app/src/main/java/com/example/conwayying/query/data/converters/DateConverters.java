package com.example.conwayying.query.data.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Converts between Java Date objects (for use in our code)
 *  and the Long timestamp equivalents (number of milliseconds since Linux epoch, for being
 *      stored by Room)
 */
public class DateConverters {

    // Conversion code taken from Android documentation
    // https://developer.android.com/training/data-storage/room/referencing-data

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
