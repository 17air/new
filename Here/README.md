# Cardify OCR + NER

This project demonstrates how to integrate a TensorFlow Lite model for performing OCR and named entity recognition.
ML Kit is used for on-device text recognition with support for Korean business cards.

## Model placement

Place the TensorFlow Lite model named `ocr_ner_model.tflite` under `app/src/main/assets/` so it is packaged with the app.

## Using the processor

`OcrNerProcessor` loads the model from the assets folder and exposes a simple API:

```kotlin
val processor = OcrNerProcessor(context)
processor.run(inputBuffer, outputBuffer)
processor.close()
```

`inputBuffer` and `outputBuffer` should match your model's input and output tensors.
