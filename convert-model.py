#!/bin/bash -e

from optimum.onnxruntime import ORTModelForFeatureExtraction
from transformers import AutoTokenizer
from pathlib import Path

model_id="sentence-transformers/all-MiniLM-L6-v2"
onnx_path = Path("onnx")

model = ORTModelForFeatureExtraction.from_pretrained(model_id, from_transformers=True)
tokenizer = AutoTokenizer.from_pretrained(model_id)

model.save_pretrained(onnx_path)
tokenizer.save_pretrained(onnx_path)