recordHtml = """
<!DOCTYPE html>
<html>
    <head>
        <title>Chat</title>
    </head>
    <body>
                        
        <section class="main">
            <button id="startBtn">시작</button>
            <button id="stopBtn">끝</button>
        </section>
        <script>
            const audioContext = new window.AudioContext();
            let voiceStream;
            let ws;
            let rec;
            
            const startBtn = document.getElementById('startBtn');
            const stopBtn = document.getElementById('stopBtn');

            window.onload = () => {
                ws = new WebSocket("ws://localhost:8000/audio");
                ws.onopen = function() {
                    console.log('Connected');
                };

                startBtn.onclick = async () => {
                    voiceStream = await navigator.mediaDevices.getUserMedia({ video: false, audio: true }); // 오디오스트림 생성
                    
                    rec = new MediaRecorder(voiceStream, {mimeType: 'audio/webm'}); 

                    rec.ondataavailable = (e) => {
                        console.log(e)
                        let blob = new Blob([e.data], {
                            type: "audio/wav"
                        });
                        console.log('send', blob);
                        ws.send(blob);
                    }
                    rec.onstop = async () => {
                        console.log("stoped");
                    };
                    startBtn.disabled = true;
                    stopBtn.disabled = false; 
                    rec.start(1000);
                };
            
                stopBtn.onclick = () => {
                    startBtn.disabled = true;
                    stopBtn.disabled = true;
                    
                    rec.stop();
                    
                    voiceStream.getTracks().forEach(s=>s.stop())
                    voiceStream = null;
                
                    startBtn.disabled = false;
                };
            }
        </script>
    </body>
</html>
"""
