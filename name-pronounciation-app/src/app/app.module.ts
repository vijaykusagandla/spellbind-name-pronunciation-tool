import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AgGridModule } from 'ag-grid-angular';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { navigatorProvider, NavigatorToken } from './services/navigator-factory.service';
import { windowProvider, WindowToken } from './services/window-factory.service';
import { SpeechSynthesisComponent } from './speech-synthesis/speech-synthesis.component';
import { ErrorInterceptor, JwtInterceptor, defaultRolesProvider } from './helpers';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    SpeechSynthesisComponent,
    LoginComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AgGridModule.withComponents([]),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    defaultRolesProvider,
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
