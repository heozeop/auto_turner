import numpy as np
import struct
import wave

from app import constants
from .. import constants 

class ByteParser():
  tempFileName = "temp.wav"
  def __init__(self, **kwargs):
    self.channels = kwargs.get('channels', 1)
    self.width = kwargs.get('width', 2)
    self.framrate = kwargs.get('framerate', constants.SAMPLE_FREQ)
  
  def save_file(self, data):
    wav_file = wave.open("sound.wav", "wb")
    wav_file.setnchannels(self.channels)
    wav_file.setsampwidth(self.width) # number of bytes
    wav_file.setframerate(self.framrate)
    wav_file.writeframesraw(data)
    wav_file.close()
    
  def load_file(self, callback):
    wav_file = wave.open("sound.wav", 'r')
    num_of_frames = wav_file.getnframes()
    parsed_data = wav_file.readframes(num_of_frames)
    wav_file.close()
    parsed_data = struct.unpack('{n}h'.format(n=num_of_frames), parsed_data)
    parsed_data = np.array(parsed_data)
    callback(parsed_data, num_of_frames)
  