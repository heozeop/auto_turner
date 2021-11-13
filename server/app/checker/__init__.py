import numpy as np
from scipy.fftpack import fft
from .. import constants

class ToneChecker():
  ALL_NOTES = ["A","A#","B","C","C#","D","D#","E","F","F#","G","G#"]
  
  def __init__(self, **kwargs):
    self.interval = kwargs.get('interval', 440)
    self.freq = kwargs.get('freq', constants.SAMPLE_FREQ)
    self.size = kwargs.get('size', constants.SAMPLE_FREQ)
    self.targetToAnalysis = [0 for _ in range(self.size)]
  
  def find_closest_tone(self, maxFreq):
    tone_index = int(np.round(np.log2(maxFreq/self.freq)*12))
    closest_tone = self.ALL_NOTES[tone_index%12] + str(4 + (tone_index + 9) // 12)
    closest_pitch = self.interval*2**(tone_index/12)
    return closest_tone, closest_pitch
  
  def check_tone(self, data: np.ndarray, num_of_frames):
    if any(data):
      self.targetToAnalysis = np.concatenate((self.targetToAnalysis, data))
      self.targetToAnalysis = self.targetToAnalysis[len(data):]
      freqs = abs(fft(self.targetToAnalysis)[:len(self.targetToAnalysis)//2])
      
      for i in range(int(62/num_of_frames)):
        freqs[i] = 0
      
      maxIndex = np.argmax(freqs)
      maxFreq = maxIndex * num_of_frames
      closestTone, closestPitch = self.find_closest_tone(maxFreq)
      print(f"Closest note: {closestTone}   {maxFreq:.1f}/{closestPitch:.1f}")
