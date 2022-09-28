cd ..
javac --source-path src -d bin src/Main.java
java -cp bin Main
rm bin/Main.class bin/MinHeap.class bin/Node.class