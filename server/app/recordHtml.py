recordHtml = """
<!DOCTYPE html>
<html>
    <head>
        <title>Chat</title>
    </head>
    <body>
        <h1>WebSocket Chat</h1>
        <form action="" onsubmit="sendMessage(event)">
            <input type="text" id="messageText" autocomplete="off"/>
            <button>Send</button>
        </form>
        <ul id='messages'>
        </ul>

        <h1>Web Audio Stream</h1>
        <button id="startBtn">start</button>
        <button id="stopBtn">end</button>
        <ul id='download'>
        </ul>
        <script>
            var ws = new WebSocket("ws://localhost:8000/ws");
            ws.onmessage = function(event) {
                var messages = document.getElementById('messages')
                var message = document.createElement('li')
                var content = document.createTextNode(event.data)
                message.appendChild(content)
                messages.appendChild(message)
            };
            function sendMessage(event) {
                var input = document.getElementById("messageText")
                ws.send(input.value)
                input.value = ''
                event.preventDefault()
            }
            window.onload = () => {
  const startBtn = document.getElementById('startBtn');
  const stopBtn = document.getElementById('stopBtn');
  
  let blobs;
  let blob; // 데이터
  let rec; // 스트림을 기반으로 동작하는 mediarecorder 객체
  let voiceStream; // 오디오스트림
  
  const getAudioStreams = (voiceStream) => { // 비디오, 오디오스트림 연결
    const context = new AudioContext();
    const destination = context.createMediaStreamDestination();
    let hasVoice = false;
    
    if (voiceStream && voiceStream.getAudioTracks().length > 0) {
      const source2 = context.createMediaStreamSource(voiceStream);
      const voiceGain = context.createGain();
      voiceGain.gain.value = 0.7;
      source2.connect(voiceGain).connect(destination);
      hasVoice = true;
    }
      
    return hasVoice ? destination.stream.getAudioTracks() : [];
  };
 
  startBtn.onclick = async () => { // 녹화 시작 버튼을 누른 경우
    
    voiceStream = await navigator.mediaDevices.getUserMedia({ video: false, audio: true }); // 오디오스트림 생성
  
    const tracks = [
      ...getAudioStreams(voiceStream)
    ];
    
    stream = new MediaStream(tracks);
      
    blobs = [];
 
    rec = new MediaRecorder(stream, {mimeType: 'video/webm; codecs=vp9,opus'}); // mediaRecorder객체 생성
    rec.ondataavailable = (e) => blobs.push(e.data);
    rec.onstop = async () => {
      
      blob = new Blob(blobs, {type: 'video/webm'});
      let url = window.URL.createObjectURL(blob);
      
                var messages = document.getElementById('download')
                var message = document.createElement('li')
                var content = document.createTextNode(url)
                message.appendChild(content)
                messages.appendChild(message)
    };
    startBtn.disabled = true; // 시작 버튼 비활성화
    stopBtn.disabled = false; // 종료 버튼 활성화
    rec.start(); // 녹화 시작
  };
 
  stopBtn.onclick = () => { // 종료 버튼을 누른 경우
    // 버튼 비활성화
    startBtn.disabled = true;
    stopBtn.disabled = true;
    
    rec.stop(); // 화면녹화 종료 및 녹화된 영상 다운로드
    
    voiceStream.getTracks().forEach(s=>s.stop())
    voiceStream = null;
 
    startBtn.disabled = false; // 시작 버튼 활성화
  };
};

        </script>
    </body>
</html>
"""
