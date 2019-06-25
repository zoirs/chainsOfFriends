import React, {Component} from 'react'
import UserElement from "./UserElement";

const {URL, URLSearchParams} = require('url');


class InputUserInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            user: null,
            error: null
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
            user = <UserElement
                data={{photo: "https://vk.com/images/deactivated_100.png", name: "Пользователь не выбран"}}/>;
        }

        var error = this.state.error &&
            <div className="input-group-append alert alert-danger" role="alert">
                {this.state.error}
            </div>;


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
                    {error}
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

        var self = this;
        fetch('/api/user?' + query)
            .then(response => {
                if (response.status === 400) {
                    self.setState({
                        user: null,
                        error: "Не корректный id или профиль скрыт"
                    });
                } else if (response.status === 200) {
                    response.json().then(user => {
                        self.setState({
                            user: user,
                            error: null
                        });
                        this.chooseCallback(this.index, user.id);
                    });
                }
            })

    };

}

export default InputUserInfo