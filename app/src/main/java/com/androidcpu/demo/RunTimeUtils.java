package com.androidcpu.demo;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunTimeUtils {
    //最好异步执行，demo就不设置了
    public static String runExec(String cmd) {
        BufferedReader br = null;
        Process process = null;
        try {
            String line = null;
            StringBuilder sb = new StringBuilder();
            //核心
            process = Runtime.getRuntime().exec(new String[]{
                    "sh", "-c", cmd
            });

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                Log.d("SJY", "line=" + line);
                sb.append(line).append("\n");
            }
            int status =  process.waitFor();
            if (TextUtils.isEmpty(sb.toString())) {
                Log.e("SJY", "结果空");
                return "";
            } else {
                return sb.toString();
            }
        } catch (Exception e) {
            Log.e("SJY", e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    public static String runExecCmd(String cmd) {
        BufferedReader br = null;
        Process process = null;
        try {
            String line = null;
            StringBuilder sb = new StringBuilder();
            //核心
            process = Runtime.getRuntime().exec(cmd);

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                Log.d("SJY", "line=" + line);
                sb.append(line).append("\n");
            }
            int status = process.waitFor();
            if (TextUtils.isEmpty(sb.toString())) {
                Log.e("SJY", "结果空");
                return "";
            } else {
                return sb.toString();
            }
        } catch (Exception e) {
            Log.e("SJY", e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    public static String do_exec(String[] cmds) {
        BufferedReader br = null;
        Process process = null;
        try {
            String line = null;
            StringBuilder sb = new StringBuilder();
            //核心
            process = Runtime.getRuntime().exec(cmds);

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            process.waitFor();
            if (TextUtils.isEmpty(sb.toString())) {
                return "";
            } else {
                return sb.toString();
            }
        } catch (Exception e) {
            Log.e("SJY", e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    /**
     * 按键灯，gpio控制
     * <p>
     * openGPIO("67");
     *
     * @param gpio
     */
    public static void openGPIO(String gpio) {
        String[] cmd1 = {"/system/bin/sh", "-c", "echo \"-wmode " + gpio + " 0\" > /sys/devices/virtual/misc/mtgpio/pin"};

        String[] cmd2 = {"/system/bin/sh", "-c", "echo \"-wdir " + gpio + " 1\" > /sys/devices/virtual/misc/mtgpio/pin"};

        String[] cmd3 = {"/system/bin/sh", "-c", "echo \"-wdout " + gpio + " 1\" > /sys/devices/virtual/misc/mtgpio/pin"};

        do_exec(cmd1);
        do_exec(cmd2);
        do_exec(cmd3);
    }

    /**
     * 测试值：
     * closeGPIO("67");
     *
     * @param gpio
     */
    public static void closeGPIO(String gpio) {
        String[] cmd4 = {"/system/bin/sh", "-c", "echo \"-wdout " + gpio + " 0\" > /sys/devices/virtual/misc/mtgpio/pin"};
        do_exec(cmd4);
    }

    /**
     * 测试值：
     * SwitchPower("829511")
     * SwitchPower("862510")
     *
     * @param paramString
     * @return
     */
    public static boolean SwitchPower(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("/system/bin/sh");
            String str = "echo " + paramString + " > /sys/devices/platform/module_power_ctl.0/power_enable\n" + "exit\n";
            localProcess.getOutputStream().write(str.getBytes());
            int i = localProcess.waitFor();
            if (i != 0)
                return false;
        } catch (Exception localException) {
            localException.printStackTrace();
            return false;
        }
        return true;
    }
}
