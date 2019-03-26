@echo off
@title ScapeSoft
:run
java -Xmx6G -Xms2048m -server -Dsun.java2d.noddraw=true -XX:+DisableExplicitGC -XX:-OmitStackTraceInFastThrow -XX:+AggressiveOpts -XX:+UseAdaptiveGCBoundary -XX:MaxGCPauseMillis=300 -XX:SurvivorRatio=16 -XX:+UseParallelGC -classpath bin;data/lib/* org.Server
goto run