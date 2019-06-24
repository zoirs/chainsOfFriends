import React, {Component} from "react";
import UserElement from "./UserElement";

const AUTH_STATE = {
    no_data: 'no_data',
    need_auth: 'need_auth',
    auth: 'auth',
};

class Auth extends Component {


    constructor(props) {
        super(props);

        this.state = {
            needAuth: AUTH_STATE.no_data,
            authUser: null
        }

        this.authCallback = this.props.authCallback;

    }

    render() {
        let url = new URL(window.location.href);

        if (url.searchParams.get("authSuccess")) {
            this.setState({needAuth: AUTH_STATE.auth})
            window.close();
        }

        console.log('this.state.needAuth', this.state.needAuth)

        let authState;
        if (this.state.needAuth === AUTH_STATE.no_data) {
            authState = <div>Проверка авторизации...</div>
            this.checkAuth();
        } else if (this.state.needAuth === AUTH_STATE.need_auth) {
            authState = <button className="btn  btn-primary" onClick={this.auth}>Авторизоваться</button>
        } else if (this.state.needAuth === AUTH_STATE.auth) {
            if (!this.state.authUser) {
                this.getAuthUser();
            } else {
                console.log('this.state.authUser', this.state.authUser);
                authState = <div>
                    <div style={{display: "inline-block", margin: "1em"}}> Учитываются те друзья, которых может
                        видеть:
                    </div>
                    <div style={{display: "inline-block", margin: "1em"}}>
                        <UserElement data={this.state.authUser}/>
                    </div>
                </div>
            }
        }

        return (<div>
            {/*<div className="card text-right" >*/}
            {/*<div className="card-body">*/}
            {/*<p className="card-text">Требуется авторизация, аккаунты и списки друзей могут быть скрыты.</p>*/}
            {authState}
            {/*</div>*/}
            {/*</div>*/}
        </div>);


    }

    checkAuth = () => {
        return fetch('/api/needAuth')
            .then(response => {
                console.log(response);
                return response.text();
            })
            .then(needAuth => {
                console.log('get res needAuth', needAuth);
                this.setState({needAuth: needAuth === 'true' ? AUTH_STATE.need_auth : AUTH_STATE.auth})
                this.authCallback(this.state.needAuth);

            });
    };

    getAuthUser = () => {
        return fetch('/api/getAuthUser')
            .then(response => {
                console.log(response);
                return response.json();
            })
            .then(user => {
                console.log('get res user', user);
                this.setState({authUser: user})
                // this.authCallback();
            });
    };


    auth = () => {
        return fetch('/api/authurl')
            .then(response => {
                console.log(response);
                return response.text()
            })
            .then(message => {
                console.log('get res auth', message);
                var self = this;
                var win = window.open(message, 'sharer', 'toolbar=0,status=0,width=548,height=325');
                var timer = setInterval(function () {
                    if (win.closed) {
                        clearInterval(timer);
                        self.checkAuth();
                        self.getAuthUser();
                    }
                }, 1000);
            });
    };
}

export default Auth