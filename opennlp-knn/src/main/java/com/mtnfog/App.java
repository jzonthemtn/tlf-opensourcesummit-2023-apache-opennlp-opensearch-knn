package com.mtnfog;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import opennlp.dl.vectors.SentenceVectorsDL;

import ai.onnxruntime.OrtException;

public class App {
    public static void main(String[] args) throws IOException, OrtException {

        final String onnxPath = args[0];

        final File MODEL_FILE_NAME = new File(onnxPath, "model.onnx");
        final File VOCAB_FILE_NAME = new File(onnxPath, "vocab.txt");

        final String sentence = "george washington was president";

        final SentenceVectorsDL sv = new SentenceVectorsDL(MODEL_FILE_NAME, VOCAB_FILE_NAME);

        final float[] vector = sv.getVectors(sentence);

        makeStringOutput(vector);

        normalize(vector);

        System.out.println("Normalized:");
        makeStringOutput(vector);

        System.out.println(index(vector, 1));

    }

    private static String index(final float[] vector, final int id) {

        final String v = "{\"my_vector\": " + Arrays.toString(vector) + "}";
        final String i = "{\"index\": {\"_index\": \"vectors\", \"_id\": \"" + id + "\"}}";

        return i + "\n" + v;

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
