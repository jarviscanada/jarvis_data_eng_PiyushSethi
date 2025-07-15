import { Component, OnInit } from '@angular/core';
import { Trader } from '../models/trader.model';
import { TraderListService } from './trader-list.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { AddTraderDialogComponent } from './add-trader-dialog/add-trader-dialog.component';

@Component({
  selector: 'app-trader-list',
  templateUrl: './trader-list.component.html',
  styleUrls: ['./trader-list.component.css']
})
export class TraderListComponent implements OnInit {
  displayedColumns: string[] =[];
  dataSource = new MatTableDataSource<Trader>();

  constructor(private traderService: TraderListService, private dialog:MatDialog) { }

  openAddTraderDialog(): void{
    const dialogRef= this.dialog.open(AddTraderDialogComponent);

    dialogRef.afterClosed().subscribe((newTrader) => {
      if(newTrader){
        this.traderService.addTrader(newTrader);
        this.dataSource.data=this.traderService.getTraderList();
      }
    });
  }
  ngOnInit(): void {
    this.traderService.getColumns().forEach(col => this.displayedColumns.push(col));
    this.traderService.getDataSource().subscribe(data => {
      this.dataSource.data = data;
    });
  }

  deleteTrader(index:number): void{
    this.dataSource.data.splice(index, 1);
    this.dataSource.data=[...this.dataSource.data];
  }
}
