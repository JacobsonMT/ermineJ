

@echo off
@REM enable echoing my setting ERMINEJ_BATCH_ECHO to 'on'
@if "%ERMINEJ_BATCH_ECHO%" == "on"  echo %ERMINEJ_BATCH_ECHO%

@REM Execute a user defined script before this one
if exist "%HOME%\ermineJrc_pre.bat" call "%HOME%\ermineJrc_pre.bat"

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto end

:OkJHome
if exist %JAVA_HOME%\nul goto chkMHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto end

:chkMHome
if not "%ERMINEJ_HOME%"=="" goto valMHome

echo.
echo ERROR: ERMINEJ_HOME not found in your environment.
echo Please set the ERMINEJ_HOME variable in your environment to match the
echo location of the ermineJ installation
echo.
goto end

:valMHome
if exist "%ERMINEJ_HOME%\bin\ermineJ.bat" goto init

echo.
echo ERROR: ERMINEJ_HOME is set to an invalid directory.
echo ERMINEJ_HOME = %ERMINEJ_HOME%
echo Please set the ERMINEJ_HOME variable in your environment to match the
echo location of the ermineJ installation
echo.
goto end
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set ERMINEJ_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set ERMINEJ_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set ERMINEJ_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set ERMINEJ_CMD_LINE_ARGS=%ERMINEJ_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
if "%ERMINEJ_OPTS%"=="" SET ERMINEJ_OPTS="-Xmx256m"
SET ERMINEJ_JAVA_EXE="%JAVA_HOME%\bin\javaw.exe"
SET ERMINEJ_CLASSPATH="%ERMINEJ_HOME%\lib\forehead-1.0-beta-5.jar"
SET ERMINEJ_MAIN_CLASS="com.werken.forehead.Forehead"
SET ERMINEJ_ENDORSED="%JAVA_HOME%\lib\endorsed;%ERMINEJ_HOME%\lib"
if not "%ERMINEJ_HOME_LOCAL%" == "" goto StartMHL

@REM Start ERMINEJ without ERMINEJ_HOME_LOCAL override
%ERMINEJ_JAVA_EXE% -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl "-DermineJ.home=%ERMINEJ_HOME%" "-Dtools.jar=%JAVA_HOME%\lib\tools.jar" "-Dforehead.conf.file=%ERMINEJ_HOME%\bin\forehead.conf" -Djava.endorsed.dirs=%ERMINEJ_ENDORSED% %ERMINEJ_OPTS% -classpath %ERMINEJ_CLASSPATH% %ERMINEJ_MAIN_CLASS% %ERMINEJ_CMD_LINE_ARGS%
@REM %ERMINEJ_JAVA_EXE% -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl "-DermineJ.home=%ERMINEJ_HOME%" "-Dtools.jar=%JAVA_HOME%\lib\tools.jar" "-Dforehead.conf.file=%ERMINEJ_HOME%\bin\forehead.conf" -Djava.endorsed.dirs=%ERMINEJ_ENDORSED% %ERMINEJ_OPTS% -classpath %ERMINEJ_CLASSPATH% %ERMINEJ_MAIN_CLASS% %ERMINEJ_CMD_LINE_ARGS%
goto :end

@REM Start ERMINEJ with ERMINEJ_HOME_LOCAL override
:StartMHL
%ERMINEJ_JAVA_EXE% -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl "-DermineJ.home=%ERMINEJ_HOME%" "-DermineJ.home.local=%ERMINEJ_HOME_LOCAL%" "-Dtools.jar=%JAVA_HOME%\lib\tools.jar" "-Dforehead.conf.file=%ERMINEJ_HOME%\bin\forehead.conf" -Djava.endorsed.dirs=%ERMINEJ_ENDORSED% %ERMINEJ_OPTS% -classpath %ERMINEJ_CLASSPATH% %ERMINEJ_MAIN_CLASS% %ERMINEJ_CMD_LINE_ARGS%
@REM %ERMINEJ_JAVA_EXE% -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl "-DermineJ.home=%ERMINEJ_HOME%" "-DermineJ.home.local=%ERMINEJ_HOME_LOCAL%" "-Dtools.jar=%JAVA_HOME%\lib\tools.jar" "-Dforehead.conf.file=%ERMINEJ_HOME%\bin\forehead.conf" -Djava.endorsed.dirs=%ERMINEJ_ENDORSED% %ERMINEJ_OPTS% -classpath %ERMINEJ_CLASSPATH% %ERMINEJ_MAIN_CLASS% %ERMINEJ_CMD_LINE_ARGS%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set ERMINEJ_JAVA_EXE=
set ERMINEJ_CLASSPATH=
set ERMINEJ_MAIN_CLASS=
set ERMINEJ_CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec
@REM if exist "%HOME%\ermineJrc_post.bat" call "%HOME%\ermineJrc_post.bat"
@REM pause the batch file if ERMINEJ_BATCH_PAUSE is set to 'on'
@REM if "%ERMINEJ_BATCH_PAUSE%" == "on" pause

