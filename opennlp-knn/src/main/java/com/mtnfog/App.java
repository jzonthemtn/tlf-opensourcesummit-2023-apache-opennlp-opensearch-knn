package com.mtnfog;

import java.io.File;
import java.io.IOException;

import opennlp.dl.vectors.SentenceVectorsDL;

import ai.onnxruntime.OrtException;

public class App {
    public static void main(String[] args) throws IOException, OrtException {

        final File MODEL_FILE_NAME = new File("/mtnfog/code/jzonthemtn/tlf-opensourcesummit-2023-apache-opennlp-opensearch-knn/onnx/model.onnx");
        final File VOCAB_FILE_NAME = new File("/mtnfog/code/jzonthemtn/tlf-opensourcesummit-2023-apache-opennlp-opensearch-knn/onnx/vocab.txt");

        final String sentence = "george washington was president";

        final SentenceVectorsDL sv = new SentenceVectorsDL(MODEL_FILE_NAME, VOCAB_FILE_NAME);

        final float[] vectors = sv.getVectors(sentence);

        makeStringOutput(vectors);

    }


    private static void makeStringOutput(float[] v) {

        final StringBuilder sb = new StringBuilder();

        sb.append("[[");

        int count = 1;

        for (int j = 0; j < v.length; j++) {

            sb.append(" " + v[j] + " ");

            count++;

            if (count == 5) {
                count = 1;
                sb.append("\n");
            }

        }

        sb.append("]]");
        System.out.println(sb);

    }

}
