mkdir decomps
cd decomps
jar xf "$1"
rm -rf META-INF
cd ..
java -jar ./jars/fernflower.jar ./decomps/*.class ./decomps
rm -rf ./decomps/*.class
