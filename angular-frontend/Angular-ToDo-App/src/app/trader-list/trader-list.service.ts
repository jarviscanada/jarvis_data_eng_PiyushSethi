import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { Trader } from "../models/trader.model";

@Injectable({
    providedIn: 'root'
})

export class TraderListService {
    private traderList:Trader[] = [
        {
            key: "1",
            id: 1,
            firstName: "Mike",
            lastName: "Spencer",
            dob: new Date().toLocaleDateString(),
            country: "Canada",
            email: "mike@test.com",
            amount: 0,
            //actions: "<button (click)='deleteTrader'>Delete Trader</button>"
        },
        {
            key: "2",
            id: 2,
            firstName: "Hellen",
            lastName: "Miller",
            dob: new Date().toLocaleDateString(),
            country: "Austria",
            email: "hellen@test.com",
            amount: 0,
            //actions: "<button (click)='deleteTrader'>Delete Trader</button>",
        },
    ]
    constructor() {}
    getDataSource(): Observable<Trader[]> {
        return of(this.traderList);
    }
    getColumns(): string[] {
        return['firstName', 'lastName', 'dob', 'country', 'email', 'amount', 'actions']
    }

    addTrader(trader: Trader): void{
        trader.key=(this.traderList.length+1).toString();
        trader.id=this.traderList.length + 1;
        this.traderList.push(trader);
    } 

    getTraderList(): Trader[]{
        return[...this.traderList];
    }
}