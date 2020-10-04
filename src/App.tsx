/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import DropdownMenu from "./DropdownMenu";
import Map from "./Map";
interface AppState {
    start : string;
    end : string;
    paths : any;
}
class App extends Component<{}, AppState> {
    constructor(props: any) {
        super(props);
        this.state = {
            start :"",
            end:"",
            paths: null,
        };
    }

    // manages dropdown for start location
    changeStart  = (e:any) => {
        this.setState({
            start: e.target.value,
        });
    }

    // manages dropdown for end location
    changeEnd  = (e:any) => {
        this.setState({
            end: e.target.value,
        });
    }

    // to restore to original state
    clearEverything = () => {
        this.setState ({
            start :"",
            end:"",
            paths: null,
        });
    }

    makePathRequest = async(start:string, end:string) => {
        if(start === undefined || end === undefined || start ===""|| end ==="" || start===end) {
            let result ="";
            if(start===undefined || end === undefined) {
                result ="";
            }else if(start === "" && end ==="") {
                result = "Origin and Destination buildings were not selected";
            } else if(start ==="") {
                result = "Origin building was not selected";
            } else if(end ==="") {
                result = "Destination building was not selected";
            } else if(start===end) {
                result = "Origin building was the same as Destination building";
            }
            alert(result);
            //this.setState({errorEntry:"You did not chose a valid option! " + result});
        }
        try {
            let response = await fetch("http://localhost:4567/path?from=" + start + "&to=" + end);
            if(!response.ok) {
                return ;
            }
            // get json object
            let j = await  response.json();
            this.setState({
               paths :j, //
            });
        } catch (e) {
            //alert("There was some error contacting the server.");
        }
    };

    render() {
        return (
            <div>
            <div className="text-center" style ={{width : "30%", margin:"0 auto",color :"red"}}>
                <h1 style={{fontSize:"30px"}}>Welcome To Campus Maps</h1>
                <DropdownMenu value = {this.state.start} onChange = {this.changeStart} check={true}/>
                <DropdownMenu value = {this.state.end} onChange = {this.changeEnd} check={false}/>
                <button style={{borderRadius:"30px", backgroundColor:"black",color : "red", width: "80%"}} onClick ={() => this.makePathRequest(this.state.start, this.state.end)}>Find Path</button>
                <div >
                <button style={{borderRadius:"30px", backgroundColor:"black",color : "red" ,width: "80%"}} onClick={this.clearEverything}>Clear</button>
                </div>
            </div>
                <Map paths={this.state.paths}/>
            </div>
        );
    }
}

export default App;
