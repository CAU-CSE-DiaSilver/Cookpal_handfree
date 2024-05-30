import {RhinoManager,RhinoErrors, RhinoInference} from '@picovoice/rhino-react-native';

export class VoiceCommend {
    getCommend;
    
    async _makeManager() {
        try {
        this._rhinoManager = await RhinoManager.create(
            "l4JNPFdU9yDOKlWFf4ZzOjmFkDAe5XBiEpuiRNJECtyXaA7kV4mTgQ==",
            "VoiceModel/cookpal_ko_android_v3_0_0.rhn",
            this.inferenceCallback.bind(this),
            (error) => {
            this.errorCallback(error.message);
            },
            "VoiceModel/rhino_params_ko.pv",
        );
        } catch (err) {
        let errorMessage;
        if (err instanceof RhinoErrors.RhinoInvalidArgumentError) {
            errorMessage = err.message;
        } else if (err instanceof RhinoErrors.RhinoActivationError) {
            errorMessage = 'AccessKey activation error';
        } else if (err instanceof RhinoErrors.RhinoActivationLimitError) {
            errorMessage = 'AccessKey reached its device limit';
        } else if (err instanceof RhinoErrors.RhinoActivationRefusedError) {
            errorMessage = 'AccessKey refused';
        } else if (err instanceof RhinoErrors.RhinoActivationThrottledError) {
            errorMessage = 'AccessKey has been throttled';
        } else {
            errorMessage = err.toString();
        }
        this.errorCallback(errorMessage);
        }
    }

    inferenceCallback(inference) {
        if(inference.isUnderstood){
            console.log(inference.intent)
            this.getCommend(inference.intent)
        }
    }

    errorCallback(error) {
        console.log(error)
    }

    componentWillUnmount() {
        this._rhinoManager?.delete();
    }

    async _startProcessing(getCommend) {
        this.getCommend = getCommend;
        try {
            await this._rhinoManager?.process();
        } catch (e) {}
    }

}

