import { Component, OnInit } from '@angular/core';
import{MatDialogRef} from '@angular/material/dialog';
import { Trader } from 'src/app/models/trader.model';

@Component({
  selector: 'app-add-trader-dialog',
  templateUrl: './add-trader-dialog.component.html',
  styleUrls: ['./add-trader-dialog.component.css']
})
export class AddTraderDialogComponent implements OnInit {
  trader: Trader = {
    key:'',
    id: 0,
    firstName:'',
    lastName:'',
    dob:'',
    country:'',
    email:'',
    amount:0
  };
  constructor(private dialogRef:MatDialogRef<AddTraderDialogComponent>) { }

  onCancel(): void{
    this.dialogRef.close();
  }

  onSubmit(): void{
    this.dialogRef.close(this.trader)
  }
  ngOnInit(): void {
  }

}
