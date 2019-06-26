import React, {Component} from "react";
// import {IndexRoute} from 'react-router';
import 'bootstrap/dist/css/bootstrap.css'
import InputUserInfo from "./InputUserInfo";
import Auth from "./Auth";
import ChainList from "./ChainList";

const AUTH_STATE = {
    no_data: 'no_data',
    need_auth: 'need_auth',
    auth: 'auth',
};

class App extends Component {


    constructor(props) {
        super(props);

        this.state = {
            reverted: false,
            isSearch: false,
            id_1: "",
            id_2: "",
            canFind: false,
            needAuth: AUTH_STATE.no_data,
            authUser: null,
            chainList: []
        };

    }

    render() {


        let spinner = this.state.isSearch && <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true" style={{margin:"5px"}} />;
        let authMsg = this.state.needAuth !== AUTH_STATE.auth && <span className="badge badge-warning">Необходимо авторизоваться</span>;
        let findButton =
            <button className="btn  btn-primary"
                    disabled={this.state.needAuth !== AUTH_STATE.auth || this.state.isSearch || this.state.id_1==="" || this.state.id_2===""}
                    onClick={this.findChain}>
                {spinner}
                Искать
            </button>;

        return (
            <div className="container">

                <div className="card text-center">
                    <div className="card-header text-right">
                        <Auth authCallback={this.authCallback}/>
                    </div>

                </div>
                {/*<div className="card-body">*/}
<br/>
                <div className="text-center" style={{margin:"50px"}}>
                <h5 className="card-title">Поиск общих знакомых</h5>
                <p className="card-text">По теории шести рукопожатий, каждый человек опосредованно знаком
                    с любым другим жителем планеты через цепочку общих знакомых, в среднем состоящую из пяти
                    человек. </p>
                </div>
                {/*</div>*/}
                <div className="container px-lg-5">
                    <div className="row mx-lg-n5">
                        <div className="col py-3 px-lg-5">
                            <InputUserInfo index={0} chooseCallback={this.userChosen}/>
                        </div>
                        <div className="col py-3 px-lg-5">
                            <InputUserInfo index={1} chooseCallback={this.userChosen}/>
                        </div>
                    </div>
                </div>
                <br/>


                <div className="card text-center">
                    <div className="card-header text-right">
                        {findButton}<br/>
                        {authMsg}
                    </div>
                    <div className="card-body">
                        <ChainList chainList={this.state.chainList}/>
                    </div>
                </div>



            </div>
        )
    }

    authCallback = (authState) => {
        console.log('authCallback ===================', authState)
        this.setState({needAuth: authState})
    };

    userChosen = (index, id) => {
        console.log("========", index, id)
        if (index === 0) {
            this.setState({
                id_1: id
            })
        } else {
            this.setState({
                id_2: id
            })
        }
        this.setState({
            canFind: this.state.id_1 !== "" && this.state.id_2 !== ""
        })
    };

    search = () => {
        var params = {
            firstUser: this.state.id_1,
            secondUser: this.state.id_2
        };
        this.setState({isSearch:true})
        var self = this;
        return fetch('/api/search?' + this.getUrlParams(params))
            .then(function (response) {
                self.setState({isSearch:false})
                if (response.status === 400) {
                    alert("Заполни id");
                } else if (response.status === 200) {
                    response.json().then(message => {
                        console.log("search", message);
                        self.setState({chainList: message});
                    });
                }
            })
    };

    getUrlParams(params) {
        var esc = encodeURIComponent;
        var query = Object.keys(params)
            .map(k => esc(k) + '=' + esc(params[k]))
            .join('&');
        return query;
    }

    findChain = () => {
        if (this.state.needAuth === AUTH_STATE.auth) {
            this.search();
        }
        // this.checkAuth().then(needAuth => {
        //     console.log("promise ", needAuth);
        //     if (needAuth) {
        //         this.auth().then(() => {
        //             console.log("можно искать1")
        //             this.search();
        //         });
        //     } else {
        //         console.log("можно искать2")
        //         this.search();
        //     }
        // });
    }
}

export default App