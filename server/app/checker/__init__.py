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
    
    i = int(np.round(estimated_tone*12))
    tone_index = i % 12
    octave = 4 + (i + 9) // 12
    return (tone_index, octave)
  
  def check_tone(self, data: np.ndarray):
    if any(data):
      self.targetToAnalysis = np.concatenate((self.targetToAnalysis, data))
      self.targetToAnalysis = self.targetToAnalysis[len(data):]
      freqs = abs(fft(self.targetToAnalysis)[:len(self.targetToAnalysis)//2])

      # 상위 10개 amplitude를 가진 주파수 추출
      
      if(np.max(freqs) > 10000 and 20 * np.log10(np.max(freqs)) > 100):
        select_freqs = np.where(freqs > np.max(freqs) * 0.8)[0]
        result = []
        for frequency in select_freqs:
          result.append(self.find_closest_tone(frequency))
      
        result = list(set(result))
      else :
        result = []

      return result
    else :
      return []