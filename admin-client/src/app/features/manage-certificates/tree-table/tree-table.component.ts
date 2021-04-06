import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-tree-table',
  templateUrl: './tree-table.component.html',
  styleUrls: ['./tree-table.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class TreeTableComponent implements OnInit {

  
  @Input() dataSource = [];
  @Input() columnsToDisplay = [];
  @Input() columnsToIterate = [];
  @Output() Delete = new EventEmitter<any>();
  @Output() Click = new EventEmitter<any>();
  @Output() DoubleClick = new EventEmitter<number>();
  @Output() Add = new EventEmitter<number>();

  expandedElement: any;

  constructor() {

  }
  ngOnInit(): void {
  }

  deleted(element){
    this.Delete.emit(element);
  }

  

  

}

