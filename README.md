# Linux Foundation Open Source Summit - "Using Apache OpenNLP with OpenSearch k-NN Vector Search"

## Converting a Model to ONNX

This converts the model to a directory called `onnx`.

```
python3 -m pip install -r requirements.txt
python3 convert-model.py
```

## Build the Java App

```
cd opennlp-knn
mvn clean install
```

## Run the Java App

Run the Java app, passing in the path to the `onnx` directory that was created above.

```
java -jar ./target/opennnlp-knn-jar-with-dependencies.jar /path/to/onnx/
```
