package toolkit.helpers;

import common.OperationSystem;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ProcessProvider {
    private static Logger log = Logger.getLogger(ProcessProvider.class);
    private static final String GET_PROCESS_LIST_LINUX = "ps aux";
    private static final String KILL_SOME_TASK_LINUX = "kill -9 %s";
    private static final String PATH_TO_TASKLIST = "\\system32\\tasklist.exe";
    private static final String KILL_SOME_TASK = "taskkill /IM %s /F /t";
    private static final String GET_ARGUMENTS_OF_PROCESS = "wmic.exe PROCESS where \"name like '%PROCESSNAME%'\" get Commandline";


    public static int execCmd(String cmdLine) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmdLine);
            process.waitFor();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        log.info("Cmd [" + cmdLine + "] execution return code: " + process.exitValue());
        if (process.exitValue() != 0) {
            log.error("Cmd [" + cmdLine + "] execution return error code: " + process.exitValue());
            throw new RuntimeException("Cmd [" + cmdLine + "] execution return error code: " + process.exitValue());
        } else {
            log.info("Cmd [" + cmdLine + "] execution return code: " + process.exitValue());
        }
        return process.exitValue();
    }

    public static int execCmd(String cmdLine, boolean ignoreErrors, String argument) {
        Process process;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmdLine, argument);
            process = pb.start();
            process.waitFor();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        log.info("Cmd [" + cmdLine + " " + argument + "] execution return code: " + process.exitValue());
        if (process.exitValue() != 0 && !ignoreErrors) {
            log.error("Cmd [" + cmdLine + " " + argument + "] execution return error code: " + process.exitValue());
            throw new RuntimeException(
                    "Cmd [" + cmdLine + " " + argument + "] execution return error code: " + process.exitValue());
        } else {
            log.info("Cmd [" + cmdLine + " " + argument + "] execution return code: " + process.exitValue());
        }
        return process.exitValue();
    }

    public static void killTask(final String processName) {
        if (processName == null || processName.equals("")) {
            throw new IllegalArgumentException("The process name is null or empty: processName = " + processName);
        }
        boolean flag;
        if (flag = isProcessPresent(processName)) {
            final int MAX_COUNT_ATTEMPTS = 5;
            int countAttempts = 0;
            while (flag && countAttempts < MAX_COUNT_ATTEMPTS) {
                execCmd(String.format(OperationSystem.instance.isLinux() ? KILL_SOME_TASK_LINUX : KILL_SOME_TASK, processName));
                OperationsHelper.sendPause(1);
                flag = isProcessPresent(processName);
                countAttempts++;
            }
            if (isProcessPresent(processName))
                throw new RuntimeException(
                        "Process " + processName + " didn't close. Check this process in the task list.");
        }
    }


    private static List<String> getProcessList() throws IOException {
        Process process;
        try {
            process = Runtime.getRuntime().exec(OperationSystem.instance.isLinux() ? GET_PROCESS_LIST_LINUX : System.getenv("windir") + PATH_TO_TASKLIST);
        } catch (IOException e) {
            throw new RuntimeException("Can't find tasklist by path: " + PATH_TO_TASKLIST + ". Change it in the code.");
        }
        List<String> processList = new ArrayList<>();
        String line;
        try (InputStream is = process.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {
                String proc = line.split(" ")[0];
                processList.add(proc);
            }
        }
        return processList;
    }

    public static boolean isProcessPresent(String processName) {
        List<String> processList = new ArrayList<>();
        try {
            processList = getProcessList();
        } catch (IOException e) {
            log.error("Exception intercepted during process list loading: " + e.getMessage());
        }
        return !processList.contains(processName);
    }


}
                            
