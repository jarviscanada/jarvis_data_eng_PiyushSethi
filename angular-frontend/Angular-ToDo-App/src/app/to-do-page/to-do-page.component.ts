import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-to-do-page',
  templateUrl: './to-do-page.component.html',
  styleUrls: ['./to-do-page.component.css']
})
export class ToDoPageComponent implements OnInit {
  toDoInput: string= '';
  toDoList: string[] = [];

  constructor() { }

  ngOnInit(): void { }

  addToDo(): void{
    const trimInput = this.toDoInput.trim();
    if(trimInput){
      this.toDoList.push(trimInput);
      this.toDoInput = '';}
  }

  deleteToDo(index: number): void{
    this.toDoList.splice(index, 1);
  }

}
