from fastapi import FastAPI, WebSocket
from fastapi.responses import HTMLResponse
import json
import os
from . import recordHtml as html
from . import parser
from . import checker

app = FastAPI()
dirname = os.path.dirname(__file__)
jsonFileName = './test_sheet.json'
filename = os.path.join(dirname, jsonFileName)

@app.get("/")
async def get():
  return HTMLResponse(html.recordHtml)

@app.get("/sheet")
async def get():
  with open(filename) as json_file:
    return json_file.read()

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
        if parsed_data is not None:
            estimate_result= toneChecker.check_tone(parsed_data)
            json_result = tuneParser.parse_2d_list(estimate_result)
            await websocket.send_text(json_result)
    except Exception as e:
        print(e)