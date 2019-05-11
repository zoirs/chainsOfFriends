import React, {Component} from "react";
import {IndexRoute} from 'react-router';

import ArticleList from './ArticleList'
import articles from '../fixture'
import 'bootstrap/dist/css/bootstrap.css'
import InputUserInfo from "./InputUserInfo";


class App extends Component {
    constructor(props) {
        super(props);

        this.state = {
            reverted: false,
            id_1: "",
            id_2: "",
            canFind: false,
            needAuth: true
        };

        this.checkAuth().then(needAuth => {
            console.log("promise ", needAuth);
            this.setState({needAuth: needAuth})
        });
    }

    render() {
        let url = new URL(window.location.href);

        if (url.searchParams.get("authSuccess")) {
            window.close();
        }

        return (
            <div className="container">
                <InputUserInfo index={0} chooseCallback={this.userChosen}/>
                <InputUserInfo index={1} chooseCallback={this.userChosen}/>
                <button disabled={!this.state.canFind} className="btn  btn-primary" onClick={this.findChain}>Искать</button>
                <div className="jumbotron">
                    <h1 className="display-3">
                        App name
                        <button className="btn  btn-primary" onClick={this.revert}>Revert</button>
                    </h1>
                </div>
                <ArticleList articles={this.state.reverted ? articles.slice().reverse() : articles}/>
            </div>
        )
    }

    revert = () => this.setState({
        reverted: !this.state.reverted
    })

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

    auth = () => {
        var params = {
            firstUser: this.state.id_1,
            secondUser: this.state.id_2
        };

        return fetch('/api/authurl?' + this.getUrlParams(params))
            .then(response => {
                console.log(response);
                return response.text()
            })
            .then(message => {
                console.log(message);
                window.open(message, 'sharer', 'toolbar=0,status=0,width=548,height=325');
            });
    };

    search = () => {
        var params = {
            firstUser: this.state.id_1,
            secondUser: this.state.id_2
        };
        return fetch('/api/search?' + this.getUrlParams(params))
            .then(response => {
                console.log("search", response);
                return response.text()
            })
            .then(message => {
                console.log("search", message);
            });
    };

    getUrlParams(params) {
        var esc = encodeURIComponent;
        var query = Object.keys(params)
            .map(k => esc(k) + '=' + esc(params[k]))
            .join('&');
        return query;
    }

    findChain = () => {

        this.checkAuth().then(needAuth => {
            console.log("promise ", needAuth);
            if (needAuth) {
                this.auth().then(() => {
                    console.log("можно искать1")
                    // this.search();
                });
            } else {
                console.log("можно искать2")
                this.search();
            }
        });
    }

    checkAuth = () => {
        return fetch('/api/needAuth')
            .then(response => {
                console.log(response);
                return response.text()
            })
            .then(needAuth => {
                console.log(needAuth);
                return needAuth === 'true';
            });
    };
}

export default App