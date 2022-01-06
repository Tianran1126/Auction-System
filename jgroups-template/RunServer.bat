@echo off
javac -cp "./jgroups-3.6.20.Final.jar"; ./client/*.java
javac -cp "./jgroups-3.6.20.Final.jar"; ./frontend/*.java
javac -cp "./jgroups-3.6.20.Final.jar"; ./backend/*.java