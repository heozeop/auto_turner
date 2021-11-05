from fastapi import FastAPI
from . import stft

app = FastAPI()

@app.get("/")
async def root():
  return {"message": stft}
