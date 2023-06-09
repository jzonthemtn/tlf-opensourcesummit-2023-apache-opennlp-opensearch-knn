# Using Apache OpenNLP with OpenSearch k-NN Vector Search

This repository is for the talk of the same name that was [presented](https://sched.co/1K5E7) at the Linux Foundation Open Source Summit 2023. This repository shows how to use [Apache OpenNLP](https://opennlp.apache.org/) to generate sentence vectors and then use those vectors for k-NN search in [OpenSearch](https://opensearch.org/).

For the presentation see [Using Apache OpenNLP with OpenSearch k-NN Vector Search](https://sched.co/1K5E7).

If you have any questions please reach out to me through [LinkedIn](https://www.linkedin.com/in/jeffzemerick/).

## Dependencies

You need a few things to run the commands listed in this file:

* Docker and `docker-compose`
* Java 11 and Maven
* Python3

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

The Java app generates vectors for three sentences:

```
sentences.add("george washington was president");
sentences.add("abraham lincoln was president");
sentences.add("john likes ice cream");
```

The output (vectors for each sentence) will be written to a file `out.txt` ready to be indexed into OpenSearch. You can now index the vectors into OpenSearch:

```bash
curl -s -k -u admin:admin -X POST -H "Content-type: application/x-ndjson" https://localhost:9200/vectors/_bulk --data-binary @out.txt
```

Now with the vectors indexed, you can search by sending a vector to OpenSearch to find similar documents:

```bash
curl -s -k -u admin:admin -X GET -H "Content-type: application/json" https://localhost:9200/vectors/_search -d '
{
  "size": 10,
  "query": {
    "knn": {
      "my_vector": {
        "vector": [0.23664545, 0.16271955, 0.2174448, 0.19018926, 0.14418952, 0.13174078, 0.14475523, 0.15135369, 0.13017027, 0.18495294, 0.15273653, 0.21680894, 0.15522662, 0.13694441, 0.11260824, 0.12069248, 0.124871716, 0.21574062, 0.12304607, 0.26746073, 0.22132963, 0.17709397, 0.13960555, 0.060655076, 0.114867084, 0.19016309, 0.15640156, 0.13960022, 0.16447519, 0.10776763, 0.13393763, 0.15837277, 0.19648154, 0.25433046, 0.09048271, 0.15899889, 0.27460718, 0.23531353, 0.26636258, 0.17056502, 0.15411225, 0.18631229, 0.18292066, 0.15764469, 0.11144164, 0.15515296, 0.14647679, 0.12992007, 0.19755481, 0.21127276, 0.16773675, 0.17822684, 0.081488326, 0.19486889, 0.11746454, 0.18362841, 0.10810352, 0.095823295, 0.18721107, 0.16446202, 0.09478745, 0.17543244, 0.09723724, 0.17882656, 0.14108664, 0.16814047, 0.09164065, 0.16521196, 0.19185877, 0.12102438, 0.20289262, 0.17702778, 0.1477192, 0.18535486, 0.14254645, 0.13670816, 0.27466482, 0.21628429, 0.23626985, 0.20824929, 0.14723091, 0.29158756, 0.16650334, 0.170777, 0.17382859, 0.16168734, 0.14707841, 0.15071529, 0.16275497, 0.19760016, 0.119973764, 0.16246775, 0.22451362, 0.17063412, 0.12662533, 0.14431766, 0.1835509, 0.23468848, 0.18764499, 1.0, 0.13367075, 0.17335148, 0.23693828, 0.20538032, 0.17373805, 0.16211696, 0.0998079, 0.116707265, 0.1830955, 0.14858359, 0.15820478, 0.15011069, 0.20348215, 0.18964784, 0.18103087, 0.15561956, 0.095463276, 0.16301574, 0.09802429, 0.09372587, 0.1933215, 0.15122011, 0.16783695, 0.13272944, 0.18347937, 0.0, 0.1815874, 0.17167109, 0.09428583, 0.1925427, 0.24836546, 0.18353534, 0.121468276, 0.3457675, 0.1355196, 0.12590978, 0.21900332, 0.18979128, 0.15065387, 0.21686985, 0.18482178, 0.23940022, 0.18947776, 0.2031004, 0.15762848, 0.16114101, 0.22075693, 0.23564969, 0.173029, 0.13671051, 0.29958567, 0.15742525, 0.25908074, 0.17523195, 0.15779102, 0.14940053, 0.19008367, 0.10765594, 0.10944032, 0.11613366, 0.105877146, 0.14264658, 0.18766277, 0.19525541, 0.23629734, 0.04603964, 0.19965075, 0.11592721, 0.23894139, 0.16100037, 0.1681287, 0.18925342, 0.12981479, 0.14560045, 0.20460646, 0.20139179, 0.20177117, 0.19033647, 0.17518646, 0.19974054, 0.1689669, 0.13102426, 0.0840263, 0.22153068, 0.22257482, 0.16642016, 0.1255874, 0.2541051, 0.1869613, 0.16180694, 0.18619464, 0.18035275, 0.13024777, 0.19522472, 0.02552168, 0.22151403, 0.17530297, 0.20385198, 0.17834094, 0.07808495, 0.16007194, 0.12479354, 0.14123559, 0.20516622, 0.16084681, 0.117723696, 0.14159155, 0.16063575, 0.14099841, 0.1765709, 0.29642156, 0.11697753, 0.15479986, 0.18462579, 0.18700477, 0.21281801, 0.19642152, 0.12790817, 0.12610824, 0.18212147, 0.13186763, 0.119399115, 0.20349103, 0.17167109, 0.15752763, 0.11593006, 0.16146657, 0.19028728, 0.17608745, 0.21866994, 0.18162717, 0.15089077, 0.12592393, 0.1736157, 0.24570778, 0.19349174, 0.13993415, 0.17995381, 0.19037879, 0.19429448, 0.15939124, 0.14427215, 0.13817333, 0.10171517, 0.115659416, 0.22828655, 0.16872443, 0.16765508, 0.18964003, 0.18936592, 0.17748052, 0.13721555, 0.19756551, 0.209285, 0.16828321, 0.2243201, 0.19638564, 0.20979631, 0.18657446, 0.21446039, 0.16728161, 0.08388079, 0.24585138, 0.22565176, 0.12493765, 0.16055486, 0.2030657, 0.14127095, 0.14577648, 0.16496988, 0.19037668, 0.21545793, 0.12634592, 0.07807021, 0.15814641, 0.18368497, 0.1840515, 0.11190097, 0.19126022, 0.19897985, 0.06268184, 0.14517978, 0.16868734, 0.15939514, 0.107347146, 0.0878329, 0.15592113, 0.20570728, 0.15630648, 0.12607224, 0.13068745, 0.14428177, 0.08001451, 0.1419112, 0.1917735, 0.14215901, 0.2179921, 0.19925006, 0.14066926, 0.12932129, 0.12169988, 0.11029747, 0.17215972, 0.119957775, 0.16751705, 0.15364987, 0.16617599, 0.1051671, 0.117208436, 0.2093214, 0.18148111, 0.15815775, 0.17999752, 0.14196743, 0.14687419, 0.2184067, 0.23346452, 0.18894196, 0.057921283, 0.17167108, 0.1822196, 0.16115609, 0.26758492, 0.2018112, 0.1500529, 0.18790597, 0.16545667, 0.12878121, 0.19523199, 0.13644966, 0.1815596, 0.11932636, 0.18732114, 0.19135337, 0.17326991, 0.13787106, 0.12483077, 0.2034319, 0.2388653, 0.2278496, 0.14538608, 0.20477888, 0.088797055, 0.23211145, 0.23524137, 0.19275272, 0.2570222, 0.2044691, 0.18843903, 0.16659254, 0.19449286, 0.14957592, 0.15855056, 0.16775526, 0.14744045, 0.20881936, 0.2503084, 0.17591618, 0.1580938, 0.21269342, 0.16027167, 0.22504497, 0.059246995, 0.19432402, 0.1312063, 0.23117721, 0.13519742, 0.17674147, 0.23158675, 0.22360769, 0.14149134, 0.28885874, 0.17583111, 0.07876832, 0.14243649, 0.1814256, 0.1749298, 0.193659, 0.15650244, 0.11954998, 0.17497128, 0.20174696, 0.12325848, 0.21539776],
        "k": 10
      }
    }
  },
  "stored_fields": [
    "id"
  ]
}
'
```

In this example we just searched for a vector that we already had, but we could have re-run the Java app to generate a vector for a different sentence and used that vector to search.

In this response we get the indexed documents back, along with a score for each document:

```
{
  "took": 3,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 3,
      "relation": "eq"
    },
    "max_score": 1,
    "hits": [
      {
        "_index": "vectors",
        "_id": "1",
        "_score": 1
      },
      {
        "_index": "vectors",
        "_id": "2",
        "_score": 0.75490916
      },
      {
        "_index": "vectors",
        "_id": "3",
        "_score": 0.4180176
      }
    ]
  }
}
```

We see the first document is a match 1, followed by documents 2 and 3. This visibly makes sense given the similarity of those first two sentences and the difference in the third sentence.
