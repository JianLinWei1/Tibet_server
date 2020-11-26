package com.jian.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @auther JianLinWei
 * @date 2020-11-26 23:57
 */
public class LocalDateUtil {


    /**
     * 获取两个日期间隔的所有日期 c除去周六周日
     *
     * @param start 格式必须为'2018-01-25'
     * @param end   格式必须为'2018-01-25'
     * @return
     */
    public static List<LocalDate> getBetweenDate(LocalDate start, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();

        long distance = ChronoUnit.DAYS.between(start, end);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(start, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            if (f.getDayOfWeek() != DayOfWeek.SATURDAY && f.getDayOfWeek() != DayOfWeek.SUNDAY)
                list.add(f);
        });
        return list;
    }


}
