import React, {Component} from 'react'
import Avatar from "./Avatar";

const {URL, URLSearchParams} = require('url');


class InputUserInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            avaUrl: ""
        };

        this.chooseCallback = this.props.chooseCallback;
        this.index = this.props.index;

        this.handleChange = this.handleChange.bind(this);
    }


    render() {
        return (
            <div className="container">
                <Avatar avaUrl={this.state.avaUrl}/>
                <input type="text" value={this.state.id} onChange={this.handleChange}
                       onBlur={this.onGetUserId}/>
            </div>
        )
    }

    handleChange = (e) => {
        this.setState({id: e.target.value});
    };

    onGetUserId = () => {

        var params = {id: this.state.id};

        var esc = encodeURIComponent;
        var query = Object.keys(params)
            .map(k => esc(k) + '=' + esc(params[k]))
            .join('&');

        console.log(query);

        fetch('/api/user?' + query)
            .then(response => {
                return response.json();
            })
            .then(user => {
                this.setState({
                    avaUrl: user.photo
                })
                this.chooseCallback(this.index, user.id);
            });

    };

}

export default InputUserInfo