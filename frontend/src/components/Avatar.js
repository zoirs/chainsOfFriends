import React, {Component} from 'react'


class Avatar extends Component {

    render() {
        const {avaUrl} = this.props
        return (
            <div id="ava">
                <img src={avaUrl}/>
            </div>
        )
    }
}

export default Avatar