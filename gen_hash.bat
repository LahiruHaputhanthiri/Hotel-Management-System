
@echo off
set JBCRYPT="c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\src\main\webapp\WEB-INF\lib\jbcrypt-0.4.jar"
cd /d "c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\src\main\java"
javac -cp %JBCRYPT% GenHash.java
java -cp ".;%JBCRYPT%" GenHash
