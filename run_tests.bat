
@echo off
set JBCRYPT="c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\src\main\webapp\WEB-INF\lib\jbcrypt-0.4.jar"
set MYSQL="c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\src\main\webapp\WEB-INF\lib\mysql-connector-j-8.3.0.jar"
set OUT="c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\test_output.txt"

cd /d "c:\Users\lahir\OneDrive\Desktop\Ocean Resort\OceanViewResort\src\main\java"

echo Testing... > %OUT%

echo --- BCRYPT TEST --- >> %OUT%
javac -cp %JBCRYPT% BCryptTest.java >> %OUT% 2>&1
java -cp ".;%JBCRYPT%" BCryptTest >> %OUT% 2>&1

echo --- DB CHECK --- >> %OUT%
javac -cp ".;%JBCRYPT%;%MYSQL%" com\oceanview\util\DBCheck.java >> %OUT% 2>&1
java -cp ".;%JBCRYPT%;%MYSQL%" com.oceanview.util.DBCheck >> %OUT% 2>&1

type %OUT%
