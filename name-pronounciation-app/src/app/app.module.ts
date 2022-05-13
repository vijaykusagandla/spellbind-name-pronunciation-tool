import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AgGridModule } from 'ag-grid-angular';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { navigatorProvider, NavigatorToken } from './services/navigator-factory.service';
import { windowProvider, WindowToken } from './services/window-factory.service';
import { SpeechSynthesisComponent } from './speech-synthesis/speech-synthesis.component';

@NgModule({
  declarations: [
    AppComponent,
    SpeechSynthesisComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AgGridModule.withComponents([]),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: WindowToken,
      useFactory: windowProvider
    },
    {
      provide: NavigatorToken, 
      useFactory: navigatorProvider
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
