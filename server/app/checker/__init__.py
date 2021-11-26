import numpy as np
from scipy.fftpack import fft
from matplotlib import pyplot as plt
from .. import constants

class ToneChecker():
  ALL_NOTES = ["A","A#","B","C","C#","D","D#","E","F","F#","G","G#"]
  
  def __init__(self, **kwargs):
    self.interval = kwargs.get('interval', constants.CONCERT_PITCH)
    self.freq = kwargs.get('freq', constants.SAMPLE_FREQ)
    self.size = kwargs.get('size', constants.WINDOW_SIZE)
    self.targetToAnalysis = [0 for _ in range(self.size)]

  def find_closest_tone(self, maxFreq):
    estimated_tone = maxFreq/self.interval
    if estimated_tone > 0:
      estimated_tone = np.log2(estimated_tone)
    
    tone_index = int(np.round(estimated_tone*12))
    octave = 4 + (tone_index + 9) // 12
    return {tone_index, octave}
  
  def check_tone(self, data: np.ndarray):
    if any(data):
      self.targetToAnalysis = np.concatenate((self.targetToAnalysis, data))
      self.targetToAnalysis = self.targetToAnalysis[len(data):]
      freqs = abs(fft(self.targetToAnalysis)[:len(self.targetToAnalysis)//2])
      
      for i in range(int(62/(self.freq / self.size))):
        freqs[i] = 0
      
      maxIndex = np.argmax(freqs)
      maxFreq = maxIndex * (self.freq / self.size)
      return self.find_closest_tone(maxFreq)
