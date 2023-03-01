# QuickLogger Java Class
QuickLogger is a simple logger class with text formatting for Java.

## Usage
### Syntax
    class QuickLogger {
        /* Methods */
        public void log(
            String loggingLevel = <"OFF" | "FATAL" | "ERROR" | "WARN" | "INFO" | "DEBUG" | "TRACE" | "ALL">, 
            String format [, ... parameters])
        public void  error(String format [, ... parameters])
        public void   warn(String format [, ... parameters])
        public void   info(String format [, ... parameters])
        public void  debug(String format [, ... parameters])
        public QuickLogger(String logFilename [, String loggingLevel, boolean hasDateTime])
    }

### Paramenters
**format**
- standard text formatting - example: https://docs.oracle.com/javase/tutorial/java/data/numberformat.html

**logFilename**
- Specifies the path and filename for where the logging will be done. If blank (""), it will log to standard & error output.

**hasDateTime**
- If the output should show timestamp or not. Default: true


## Examples
  - Declaration: 
    - This will write to a file
  
      `log = new QuickLogger("/opt/log/application.log", "INFO");`

    - This will write to the standard output

       `log = new QuickLogger("", "INFO");`

  - Usage:
 
        log.info("Connection details: Host: %s, User: %s. Port: %d", host, user, port);
        log.error("Fatal error! Exiting...");
