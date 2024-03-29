java -jar ./jars/jdicli.jar "$1"
rm -rf decomps
mkdir decomps
cd decomps
jar xf "$1"
rm -rf META-INF
rm .*
cd ..
for dir in ./decomps/*; do (java -jar ./jars/fernflower.jar "$dir" ./decomps); done
cd decomps
find . -name "*.class" -type f -delete
cd ..
cd SourcePrinter
java -cp .:../jars/json.jar SourcePrinter
mv sourcePrinterOut.json "../frontend/src"
cd ..
cd frontend
npm start