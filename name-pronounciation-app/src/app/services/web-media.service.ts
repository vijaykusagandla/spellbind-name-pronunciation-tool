import { Inject, Injectable } from "@angular/core";
import { NavigatorToken } from "./navigator-factory.service";
import { WindowToken } from "./window-factory.service";

declare var MediaRecorder: any;
declare var MediaStream: any;

@Injectable({
    providedIn: "root"
})
export class WebMediaService {

    private audioChunks: any[] = [];
    private audioRecorder: any = null;
    private mediaConstraints: object = {
        audio: true
    };

    constructor(
        @Inject(WindowToken) private browserWindow: Window,
        @Inject(NavigatorToken) private navigator: Navigator
    ) { }

    private getAudioRecorder(): Promise<MediaStream> {
        return this.navigator.mediaDevices.getUserMedia(this.mediaConstraints);
    }

    public startRecording(): void {
        if (!this.hasBrowserSupport()) {
            this.browserWindow.alert("Your browser does not support audio recording!");
        }

        this.getAudioRecorder()
        .then(stream => {
            this.clear(); // for safety
            // Initialize media recorder from stream
            this.audioRecorder = new MediaRecorder(stream);

            // Record stream
            this.audioRecorder.addEventListener('dataavailable', event => { this.onDataAvailable(event) });

            // Stop stream
            this.audioRecorder.addEventListener('stop', event => { this.onStopRecording(event) });

            // Start recording
            this.audioRecorder.start();
            console.info("Recorder state:", this.audioRecorder.state);
        }).catch(error => {
            this.browserWindow.alert("Error while  ");
            console.error(error);
        });
    }

    public stopRecording(): void {
        if (this.audioRecorder?.state === "inactive" || this.audioRecorder?.state == undefined) {
            return;
        }

        this.audioRecorder?.stop();
        console.info("Recorder state:", this.audioRecorder?.state);
        //this.audioRecorder?.removeAllListeners('stop');
        //this.audioRecorder?.removeAllListeners('dataavailable');
    }

    private onDataAvailable(event: any) {
        //console.dir(event);
        this.audioChunks.push(event.data);
    }

    private onStopRecording(event: any): void {
        const audioBlob = new Blob(this.audioChunks, { type: "audio/mpeg" });
        const audioUrl = URL.createObjectURL(audioBlob);
        const audio = new Audio(audioUrl);
        audio.play();
        this.clear();
    }

    private clear(): void {
        this.audioRecorder = null;
        this.audioChunks = [];
    }

    private hasBrowserSupport(): boolean {
        return !(typeof navigator.mediaDevices === 'undefined' || !navigator.mediaDevices.getUserMedia);
    }

}