
import { PorcupineManager, PorcupineErrors, Porcupine, } from '@picovoice/porcupine-react-native';

//보이스 처리를 위한 상수
const _accessKey = "l4JNPFdU9yDOKlWFf4ZzOjmFkDAe5XBiEpuiRNJECtyXaA7kV4mTgQ=="; 
const keywordPath = "VoiceModel/쿡펠_ko_android_v3_0_0.ppn";
const modelPath = "VoiceModel/porcupine_params_ko.pv";

let isListening = false;
export class VoiceControl{
  _porcupineManager;  

  constructor(callCookPal){
    this.callCookPal = callCookPal
  }
  
  async componentWillUnmount() {
    if (isListening) {
      await this._stopProcessing();
    }
    this._porcupineManager?.delete();
  }

  async _startProcessing() {
    try {
      await this._porcupineManager?.start();
      isListening = true
    } catch (e) {console.log(e)}
  }

  async _stopProcessing() {
    try {
      await this._porcupineManager?.stop();
      isListening = false
    } catch (e) {console.log(e)}
  }

  async _makeManager() {
    
    const detectionCallback = () => {
      console.log("COOKPAL!!!!")
      this.callCookPal(true);
    };

    const processErrorCallback = (error) => {
      console.log(error)
      this._stopProcessing();
    };
    
    try {
      this._porcupineManager = await PorcupineManager.fromKeywordPaths (
        _accessKey,
        [keywordPath],
        detectionCallback,
        processErrorCallback,
        modelPath
      );
    } catch (err) {
      let errorMessage;
      if (err instanceof PorcupineErrors.PorcupineInvalidArgumentError) {
        errorMessage = `${err.message}`;
      } else if (err instanceof PorcupineErrors.PorcupineActivationError) {
        errorMessage = 'AccessKey activation error';
      } else if (err instanceof PorcupineErrors.PorcupineActivationLimitError) {
        errorMessage = 'AccessKey reached its device limit';
      } else if (err instanceof PorcupineErrors.PorcupineActivationRefusedError) {
        errorMessage = 'AccessKey refused';
      } else if (err instanceof PorcupineErrors.PorcupineActivationThrottledError) {
        errorMessage = 'AccessKey has been throttled';
      } else {
        errorMessage = err.toString();
        console.error(err)
      }
      console.log(errorMessage)
    }

  }

}

  
