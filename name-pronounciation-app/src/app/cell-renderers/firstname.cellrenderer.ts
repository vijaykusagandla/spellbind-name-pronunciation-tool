import { Component } from "@angular/core";
import { ICellRendererAngularComp } from "ag-grid-angular";
import { ICellRendererParams } from "ag-grid-community";
import { WebMediaService } from "../services/web-media.service";

@Component({
    selector: 'npt-fname-cell',
    template: `
        <span>
            <span>{{ cellValue }}</span>&nbsp;
            <button type="button" class="btn btn-primary btn-sm" (click)="startRecording()">Record</button>
            <button type="button" class="btn btn-danger btn-sm" (click)="stopRecording()">Stop</button>
        </span>
    `
})
export class FirstNameCellRenderer implements ICellRendererAngularComp {

    cellValue: string = '';

    constructor(
        private webMediaService: WebMediaService
    ) { }

    agInit(params: ICellRendererParams): void {
        this.cellValue = this.getValueToDisplay(params);
    }

    refresh(params: ICellRendererParams): boolean {
        this.cellValue = this.getValueToDisplay(params);
        return true;
    }

    getValueToDisplay(params: ICellRendererParams) {
        return params.valueFormatted ? params.valueFormatted : params.value;
    }

    startRecording(): void {
        this.webMediaService.startRecording();
    }

    stopRecording(): void {
        this.webMediaService.stopRecording();
    }

}