import numpy as np
import json
from app import constants
from .. import constants 

class TuneParser():
  def parse_bytes(self, data: bytes):
    return np.frombuffer(data, dtype=np.int16)
  def parse_2d_list(self, data: list):
    tone_list = []
    for i in data:
      tone_list.append({
        'note': i[0],
        'pitch': i[1],
      })
    return json.dumps(tone_list)