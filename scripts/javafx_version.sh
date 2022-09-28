cd ..
javac --source-path src -d bin --module-path "$1" --add-modules=javafx.controls src/GridDisplay.java
java -cp bin --module-path "$1" --add-modules=javafx.controls GridDisplay
rm bin/GridDisplay.class bin/Main.class bin/MinHeap.class bin/Node.class bin/NodeButton.class