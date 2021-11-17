import wave
import soundfile as sf
from fastapi import FastAPI, WebSocket
from fastapi.responses import HTMLResponse
from . import recordHtml as html
from . import parser
from . import checker

app = FastAPI()

@app.get("/")
async def get():
  return HTMLResponse(html.recordHtml)

@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
  await websocket.accept()
  while True:
    # websocket.receive_bytes() 로 audio byte 받을 수 있습니다. 
    data = await websocket.receive_text()
    await websocket.send_text(f"Message text was: {data}")

@app.websocket("/audio")
async def websocket_audio_endpoint(websocket: WebSocket):
    await websocket.accept()
    byteParser = parser.ByteParser()
    toneChecker = checker.ToneChecker()
    try:
      while True:
        data = await websocket.receive_bytes()
        byteParser.save_file(data)
        byteParser.load_file(toneChecker.check_tone)
        await websocket.send_bytes(data)
    except Exception as e:
        print(e)

