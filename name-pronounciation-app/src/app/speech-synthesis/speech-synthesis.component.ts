import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-speech-synthesis',
  templateUrl: './speech-synthesis.component.html',
  styleUrls: ['./speech-synthesis.component.css']
})
export class SpeechSynthesisComponent implements OnInit {

  public textToSpeechForm: FormGroup = new FormGroup(
    {
      fname: new FormControl("", [Validators.required])
    });

  constructor() { }

  ngOnInit(): void {
  }

  public onSubmit(){
    // if(this.textToSpeechForm.invalid){
    //   return
    // }
  
    console.log(" tts Form", this.textToSpeechForm.value);
  }

  

}
