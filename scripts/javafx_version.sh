cd ..
javac --source-path src -d bin --module-path lib/javafx-sdk-16/lib --add-modules=javafx.controls src/GridDisplay.java
java -cp bin --module-path lib/javafx-sdk-16/lib --add-modules=javafx.controls GridDisplay
rm bin/GridDisplay.class bin/Main.class bin/MinHeap.class bin/Node.class bin/NodeButton.class