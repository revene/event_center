package com.blanc.event.timer.impl;


import org.jctools.queues.MpscUnboundedArrayQueue;

import java.util.Locale;
import java.util.Queue;

/**
 * 功能：平台支撑类
 *
 * @author chuchengyi
 */
public final class PlatformDependent {


    /**
     * 功能：定义队列有多少个chunk
     */
    private static final int CHUNK_SIZE = 1024;

    /**
     * 功能：判断系统是否是osx
     */
    private static final boolean IS_OSX = isOsx0();

    /**
     * 功能：判断系统是否是windows
     */
    private static final boolean IS_WINDOWS = isWindows0();


    /**
     * 功能：判断是否是windows系统
     *
     * @return
     */
    private static boolean isWindows0() {
        boolean windows = System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
        return windows;
    }

    /**
     * 功能：判断系统是否是osx
     *
     * @return
     */
    private static boolean isOsx0() {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.US)
                .replaceAll("[^a-z0-9]+", "");
        boolean osx = osName.startsWith("macosx") || osName.startsWith("osx");

        return osx;
    }

    /**
     * 功能：判断是否是windows
     *
     * @return
     */
    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    /**
     * 功能：获取到类的名称
     *
     * @param clazz
     * @return
     */
    public static String simpleClassName(Class<?> clazz) {
        String className = clazz.getName();
        final int lastDotIdx = className.lastIndexOf(".");
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }

    /**
     * 功能：构造一个队列
     *
     * @param <T>
     * @return
     */
    public static <T> Queue<T> newMpscQueue() {
        return new MpscUnboundedArrayQueue<T>(CHUNK_SIZE);
    }

    /**
     * 转换成2^n 数据
     * @param ticksPerWheel 时间轮的总格子数量
     * @return 应该的格子数量:是2^n
     */
    public static int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel = 1;
        // *2 直到操作到刚好 > ticksPerWheel的第一个2^n的数
        while (normalizedTicksPerWheel < ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        return normalizedTicksPerWheel;
    }


}
