@echo off
set JBCRYPT="..\webapp\WEB-INF\lib\jbcrypt-0.4.jar"
set MYSQL="..\webapp\WEB-INF\lib\mysql-connector-j-8.3.0.jar"

cd /d "c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\src\main\java"

echo Compiling ResetAdmin...
javac -cp ".;%JBCRYPT%;%MYSQL%" com\oceanview\util\ResetAdmin.java com\oceanview\util\PasswordUtil.java com\oceanview\util\DBConnection.java com\oceanview\model\User.java

echo Running ResetAdmin...
java -cp ".;%JBCRYPT%;%MYSQL%" com.oceanview.util.ResetAdmin
