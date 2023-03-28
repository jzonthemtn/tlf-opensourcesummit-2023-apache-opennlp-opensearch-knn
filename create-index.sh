#!/bin/bash

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