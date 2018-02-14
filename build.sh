mkdir -p output
javac -d ./output ./src/*.java
cp client_config.properties ./output
cp server_config.properties ./output
