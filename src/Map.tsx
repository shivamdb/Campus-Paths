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
import "./Map.css";

interface MapState {
    backgroundImage: HTMLImageElement | null;
}

interface  MapProps {
    paths : any
}

class Map extends Component<MapProps, MapState> {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    canvas: React.RefObject<HTMLCanvasElement>;

    constructor(props: MapProps) {
        super(props);
        this.state = {
            backgroundImage: null
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        // Might want to do something here?
        this.fetchAndSaveImage();
        this.drawBackgroundImage()
    }

    componentDidUpdate() {
        // Might want something here too...
        this.drawBackgroundImage();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    drawBackgroundImage() {
        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
        // to drawPath
        this.drawLine(ctx, this.props.paths);
    }

    // to draw circle at a point
    drawCircle =(ctx:any,coordinate : any) => {
        ctx.fillStyle = "blue"; //change color
        let r = 15;
        ctx.beginPath();
        ctx.arc(coordinate[0], coordinate[1], r, 0, 2*Math.PI); // to draw a full circle
        ctx.fill();
    }

    // to draw path from origin to destination
    drawLine(ctx : any, path: any) {
        if(path === null || this.props.paths ===null || this.props.paths ===''|| this.props.paths === undefined) {
            return;
        }
        let startPoint: any = [];
        startPoint.push(this.props.paths.start.x, this.props.paths.start.y);

        let pathPoint = [];
        pathPoint = this.props.paths.path;
        let endPoint = pathPoint[pathPoint.length - 1];
        // if end point undefined exits
        if(endPoint === undefined)  {
            return;
        }
        let end =[];
        end.push(endPoint.end.x,endPoint.end.y);

        this.drawCircle(ctx, startPoint);
        this.drawCircle(ctx, end);

        ctx.strokeStyle ="red";
        ctx.beginPath();
        ctx.lineWidth = 8; // change width

        for(let  i =0;i<pathPoint.length; i++) {
            ctx.moveTo(pathPoint[i].start.x,pathPoint[i].start.y);
            ctx.lineTo(pathPoint[i].end.x,pathPoint[i].end.y);
            ctx.stroke()
        }
    };

    render() {
        return (
            <div>
            <canvas ref={this.canvas}/>
            </div>
        )
    }
}

export default Map;