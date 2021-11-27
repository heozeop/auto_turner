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
    tuneParser = parser.TuneParser()
    toneChecker = checker.ToneChecker()
    try:
      while True:
        data = await websocket.receive_bytes()
        parsed_data = tuneParser.parse_bytes(data)
        estimate_result = toneChecker.check_tone(parsed_data, 3)
        json_result = tuneParser.parse_2d_list(estimate_result)
        print(json_result)
        await websocket.send_bytes(data)
    except Exception as e:
        print(e)

