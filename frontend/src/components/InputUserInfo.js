import React, {Component} from 'react'
import UserElement from "./UserElement";

const {URL, URLSearchParams} = require('url');


class InputUserInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            avaUrl: "",
            user: null
        };

        this.chooseCallback = this.props.chooseCallback;
        this.index = this.props.index;

        this.handleChange = this.handleChange.bind(this);
    }


    render() {
        const isUserChecked = this.state.user;
        let user;

        if (isUserChecked) {
            user = <UserElement data={this.state.user}/>;
        } else {
            user = <UserElement data={{photo:"https://vk.com/images/deactivated_100.png",name:"Пользователь не выбран"}}/>;
        }
        
        return (
            <div className="container">
                {user}
                <br/>

                <div className="input-group mb-3">

                    <div className="input-group-prepend">
                        <span className="input-group-text" id="basic-addon1">https://vk.com/</span>
                    </div>
                    <input type="text"
                           value={this.state.id}
                           onChange={this.handleChange}
                           onBlur={this.onGetUserId}
                           className="form-control"
                           placeholder="id140891700"/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-secondary" type="button" id="button-addon2">Ok</button>
                    </div>
                </div>
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
                    avaUrl: user.photo,
                    user: user
                })
                this.chooseCallback(this.index, user.id);
            });

    };

}

export default InputUserInfo