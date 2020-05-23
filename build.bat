rmdir /Q/S build
mkdir build
javac -d build src/edu/sapi/mestint/*.java
cd build
jar cvfe ../Amoba.jar edu.sapi.mestint.Amoba edu/sapi/mestint/*.class
cd ..