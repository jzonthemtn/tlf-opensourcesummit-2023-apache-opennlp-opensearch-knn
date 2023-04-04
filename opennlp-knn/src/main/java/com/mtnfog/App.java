package com.mtnfog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import opennlp.dl.vectors.SentenceVectorsDL;

import ai.onnxruntime.OrtException;

public class App {
    public static void main(String[] args) throws IOException, OrtException {

        final String onnxPath = args[0];

        final File MODEL_FILE_NAME = new File(onnxPath, "model.onnx");
        final File VOCAB_FILE_NAME = new File(onnxPath, "vocab.txt");

        final SentenceVectorsDL sv = new SentenceVectorsDL(MODEL_FILE_NAME, VOCAB_FILE_NAME);

        final List<String> sentences = new LinkedList<>();
        sentences.add("george washington was president");
        sentences.add("abraham lincoln was president");
        sentences.add("john likes ice cream");

        int index = 1;

        final BufferedWriter out = new BufferedWriter(new FileWriter("vectors", true));

        for(final String sentence : sentences) {

            final float[] vector = sv.getVectors(sentence);

            //makeStringOutput(vector);

            normalize(vector);

            //System.out.println("Normalized:");
            //makeStringOutput(vector);

            out.write(index(vector, index++));

        }

        out.close();

    }

    private static String index(final float[] vector, final int id) {

        final String v = "{\"my_vector\": " + Arrays.toString(vector) + "}";
        final String i = "{\"index\": {\"_index\": \"vectors\", \"_id\": \"" + id + "\"}}";

        return i + "\n" + v + "\n";

    }

    private static void normalize(float[] array) {
        float min = min(array);
        float div = max(array) - min;

        for (int i = 0; i < array.length; i++) {
            array[i] = (array[i] - min) / div;
        }

    }

    private static float min(float[] array) {
        int i, size = array.length;
        float m = array[0];

        for (i = 1; i < size; i++) {
            m = Math.min(m, array[i]);
        }

        return m;
    }

    private static float max(float[] array) {
        int i, size = array.length;
        float m = array[0];

        for (i = 1; i < size; i++) {
            m = Math.max(m, array[i]);
        }

        return m;
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
