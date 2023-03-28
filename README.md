# Using Apache OpenNLP with OpenSearch k-NN Vector Search

This repository is for the talk of the same name that was [presented](https://sched.co/1K5E7) at the Linux Foundation Open Source Summit 2023. This repository shows how to use [Apache OpenNLP](https://opennlp.apache.org/) to generate sentence vectors and then use those vectors for k-NN search in [OpenSearch](https://opensearch.org/).

For the presentation see [Using Apache OpenNLP with OpenSearch k-NN Vector Search](https://sched.co/1K5E7).

## Running OpenSearch

First, set:

```bash
sudo sysctl -w vm.max_map_count=262144
```

Now start OpenSearch:

```bash
docker-compose up
```

Verify OpenSearch is running:

```bash
curl -k -u admin:admin https://localhost:9200
```

Verify the `opensearch-knn` plugin is installed:

```bash
curl -k -u admin:admin https://localhost:9200/_cat/plugins
```

## Creating the Index

```bash
curl -k -u admin:admin -X PUT -H "Content-type: application/json" https://localhost:9200/vectors -d '
{
  "settings": {
    "index.knn": true
  },
  "mappings": {
    "properties": {
      "my_vector": {
        "type": "knn_vector",
        "dimension": 384
      }
    }
  }
}'
```

## Converting a Model to ONNX

This converts the model to a directory called `onnx`.

```bash
python3 -m pip install -r requirements.txt
python3 convert-model.py
```

## Build the Java App

```bash
cd opennlp-knn
mvn clean install
```

## Run the Java App

Run the Java app, passing in the path to the `onnx` directory that was created above.

```bash
java -jar ./target/opennnlp-knn-jar-with-dependencies.jar /path/to/onnx/
```
