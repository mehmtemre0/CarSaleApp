package com.example.goapplication;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Classifier {

    private final Interpreter interpreter;
    private final int inputSize;

    public Classifier(Context context, String modelPath, int inputSize) throws IOException {
        this.inputSize = inputSize;

        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);

        interpreter = new Interpreter(loadModelFile(context, modelPath), options);
    }

    private ByteBuffer loadModelFile(Context context, String modelPath) throws IOException {
        InputStream inputStream = context.getAssets().open(modelPath);
        byte[] modelBytes = new byte[inputStream.available()];
        inputStream.read(modelBytes);
        ByteBuffer buffer = ByteBuffer.allocateDirect(modelBytes.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.put(modelBytes);
        return buffer;
    }

    public int recognizeImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false);

        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(resizedBitmap);

        // Normalize the image to 0-1 range
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[inputSize * inputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            int val = intValues[i];
            byteBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f);
            byteBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);
            byteBuffer.putFloat((val & 0xFF) / 255.0f);
        }

        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(new int[]{1, 15}, DataType.FLOAT32);
        interpreter.run(byteBuffer, outputBuffer.getBuffer().rewind());

        return getMaxResult(outputBuffer.getFloatArray());
    }

    private int getMaxResult(float[] probabilities) {
        int maxIndex = -1;
        float maxProb = -1;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > maxProb) {
                maxProb = probabilities[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}