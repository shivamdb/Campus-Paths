import React, {Component} from 'react';


interface DropdownMenuState {
    buildings : any
}

interface DropdownMenuProps{
    value : string;
    onChange(e:any) : void;
    check : boolean; // to know if origin used dropdown or destination dropdown used
}
class DropdownMenu extends Component<DropdownMenuProps, DropdownMenuState> {
    constructor(props :any) {
        super(props);
        this.state ={
            buildings :[],
        };
    }

    componentDidMount() {
        // Might want to do something here?
        fetch("http://localhost:4567/buildings")
            .then((response) => {
            return response.json();
            })
        .then(dataValues =>  {
            let buildings = Object.keys(dataValues).map(building => {
                return {value: building, display :dataValues[building]}
            });
            // string depending on destination box or origin box being selected
            let r =" Select Your Destination Building";
            if(this.props.check) { // origin
                r = "Select Your Origin Building";
            }
            // to set state
            this.setState({
                buildings :[{value: '', display: r}].concat(buildings)
            });
        }).catch(error => {
            console.log(error);
        });
    }

    render() {
        return (
            <div>
                <select value={this.props.value} onChange={this.props.onChange}>
                    {this.state.buildings.map((building:any) =>
                        <option key = {building.value} value={building.value} >{building.display}</option>)}
                    </select>
            </div>
            );
    }
}
export default DropdownMenu;