setup:
	javac ./agent/*.java
	jar cfm agent.jar ./agent/Manifest.txt ./agent/*.class
	rm ./agent/*.class
	rm -rf output
	mv agent.jar jars