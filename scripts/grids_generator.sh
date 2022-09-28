cd ..
javac --source-path src -d bin src/GridGenerator.java
java -cp bin GridGenerator
rm bin/GridGenerator.class