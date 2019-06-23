import React, {PureComponent} from 'react'
import './style.css'

export default class UserElement extends PureComponent {

    render() {
        const data = this.props.data ? this.props.data : {};
        console.log('this.props', data)
        var photoImg = data.photo && <img className="chain-element_img" src={data.photo} alt="-"/>
        return (
            <div className="chain-element_div">
                {photoImg}<br/>
                {data.name}<br/> {data.lastName}
            </div>
        )
    }
}