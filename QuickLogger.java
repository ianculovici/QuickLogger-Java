import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Quick Logger class for Java with text formatting
 * 
 * Examples:
 *  - Declaration: 
 *        log = new QuickLogger("/opt/log/application.log", "INFO");
 *  - Usage:
 *        log.info("Connection details: Host: %s, User: %s. Port: %d", host, user, port);
 *        log.error("Fatal error! Exiting..."");
 *
 * @author ianculovici
 */

class QuickLogger{
    private String LEVEL = "WARN";
    private String[] logLevels = new String[] {"OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"};
    private PrintWriter output = null;
    private boolean includeDate = true;

    QuickLogger(String logfile, String level, boolean includeDate) throws IOException{
        this.LEVEL       = level;
        this.includeDate = includeDate;
        if(logfile != null && logfile != ""){
            this.output = new PrintWriter(new FileWriter(logfile, true), true);
        }
    }

    QuickLogger() throws IOException{

    }

    QuickLogger(String logfile) throws IOException{
        this(logfile, "WARN", true);
    }

    QuickLogger(String logfile, String level) throws IOException{
        this(logfile, level, true);
    }

    void close() throws IOException{
        if(output != null){
            output.close();
        }
    }

    public static int findElement(String t, String[] arr)
    {
         if (arr == null) {
            return -1;
        }
        int len = arr.length;
        int i = 0;
        while (i < len) {
            if (t.equals(arr[i])) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    private Object[] prependObj(String e, Object... o){
        Object[] newO = new Object[o.length + 1];
        int idx=0;
        newO[idx] = e;
        for(Object v : o){
            newO[++idx] = v;
        }
        return newO;
    }

    private void outputText(String type, String s, Object... o){
        /*
         * type can be 'err' or 'out'
         */

        try {
            if(output == null){
                s = s.replace("%n", "\n");
                Class<?> sysClass = Class.forName("java.lang.System");
                Field outField = sysClass.getDeclaredField(type);
                Class<?> c = outField.getType();
                Method printfMethod = c.getDeclaredMethod("printf", String.class, Object[].class);
                Object object = outField.get(null);
                printfMethod.invoke(object, s, (Object) o);
            } else {
                s = s.replace("%n", "\n");
                Class<?> c = Class.forName("java.io.PrintWriter");
                Method printfMethod = c.getMethod("printf", String.class, Object[].class);
                printfMethod.invoke(output, s, (Object) o);
            }
        } catch ( ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void log(String messageLevel, String s, Object... o) {
        o = prependObj(messageLevel, o);
        s = "%-5s:" + s + "%n";
        if(includeDate){
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            String dateText =  dateTime.format(dateTimeFormat) + ":";
            s = "%s" + s;
            o = prependObj(dateText, o);
        }

        if(findElement(LEVEL.toUpperCase(), logLevels) >= findElement(messageLevel.toUpperCase(), logLevels)){
            if(findElement(messageLevel.toUpperCase(), logLevels) <= findElement("ERROR", logLevels)) {
                outputText("err", s, o);
            } else {
                outputText("out", s, o);
            }
        }
    }

    public void debug(String s, Object ...o) {
        log("DEBUG", s, o);
    }
 
    public void info(String s, Object ...o) {
        log("INFO", s, o);
    }
 
    public void warn(String s, Object ...o) {
        log("WARN", s, o);
    }
 
    public void error(String s, Object ...o) {
        log("ERROR", s, o);
    }
}
