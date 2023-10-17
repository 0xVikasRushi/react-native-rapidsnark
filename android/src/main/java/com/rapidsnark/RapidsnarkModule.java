package com.rapidsnark;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.nio.charset.StandardCharsets;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

@ReactModule(name = RapidsnarkModule.NAME)
public class RapidsnarkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Rapidsnark";

  public RapidsnarkModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void groth16_prover(String zkeyBytes1, String wtnsBytes1, Promise promise) {
    long startTime = System.currentTimeMillis(); // Capture start time

    // Decode base64
    byte[] zkeyBytes = Base64.decode(zkeyBytes1, Base64.DEFAULT);
    byte[] wtnsBytes = Base64.decode(wtnsBytes1, Base64.DEFAULT);

    // Create buffers to get results
    // TODO: Replace with actual buffer sizes if necessary
    byte[] proof_buffer = new byte[16384];
    byte[] public_buffer = new byte[16384];
    byte[] error_msg = new byte[256];

    Log.e("RapidsnarkModule", "zkeyBytes: " + zkeyBytes.length);
    Log.e("RapidsnarkModule", "wtnsBytes: " + wtnsBytes.length);

    new Thread(() -> {
      // This will require you to write a JNI bridge to your C library.
      new GrothProver().groth16Prover(
        zkeyBytes, zkeyBytes.length,
        wtnsBytes, wtnsBytes.length,
        proof_buffer, new long[]{proof_buffer.length},
        public_buffer, new long[]{public_buffer.length},
        error_msg, error_msg.length
      );

      long endTime = System.currentTimeMillis(); // Capture end time
      long executionTime = endTime - startTime;

      // Convert byte arrays to strings
      String proofResult = new String(proof_buffer, StandardCharsets.UTF_8);
      String publicResult = new String(public_buffer, StandardCharsets.UTF_8);

      if (!proofResult.isEmpty()) {
        promise.resolve(proofResult);
      } else {
        String errorString = new String(error_msg, StandardCharsets.UTF_8);
        promise.reject("PROVER_ERROR", errorString);
      }
    }).start();
  }
}

class GrothProver {

  static {
    System.loadLibrary("rapidsnark_module");
  }

  public native int groth16Prover(byte[] zkeyBuffer, long zkeySize,
                                  byte[] wtnsBuffer, long wtnsSize,
                                  byte[] proofBuffer, long[] proofSize,
                                  byte[] publicBuffer, long[] publicSize,
                                  byte[] errorMsg, long errorMsgMaxSize);
}
