import React, {PureComponent} from 'react'
import Article from "../Article";
import './style.css'

export default class ChainElement extends PureComponent {

    render() {
        const data = this.props.data;
        return (
            <div className="chain-element_div">
                <img className="chain-element_img" src={data.photo}/><br/>
                {data.name}<br/> {data.lastName}
            </div>
        )
    }
}