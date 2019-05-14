import React, {Component} from "react";
import {IndexRoute} from 'react-router';
import 'bootstrap/dist/css/bootstrap.css'
import InputUserInfo from "./InputUserInfo";
import ChainList from "./ChainList";


class App extends Component {
    constructor(props) {
        super(props);

        this.state = {
            reverted: false,
            id_1: "",
            id_2: "",
            canFind: false,
            needAuth: true,
            chainList: [[{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 1954372,
                "name": "Alisa",
                "lastName": "Bukharova",
                "photo": "https://pp.userapi.com/c852128/v852128484/2bc5/8_KPDD_zTEc.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 2055823,
                "name": "Ilya",
                "lastName": "Fedorchak",
                "photo": "https://vk.com/images/deactivated_200.png"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 2561981,
                "name": "Denis",
                "lastName": "Kochetkov",
                "photo": "https://pp.userapi.com/xilfP_G--7GDsrAG8j7k6o7e_2uvowqsVzdjhg/EzSYl9ObWT0.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 2671854,
                "name": "Anya",
                "lastName": "Khitrova",
                "photo": "https://pp.userapi.com/c846120/v846120039/f93db/Wegn2cT7u-I.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 3739582,
                "name": "Mikhail",
                "lastName": "Andreev",
                "photo": "https://pp.userapi.com/c848616/v848616796/7da69/AVEoqOiaV-c.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 3998808,
                "name": "Maria",
                "lastName": "Tishonkova",
                "photo": "https://pp.userapi.com/c845321/v845321049/1f86a4/H-JqEkdVOBY.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 4166962,
                "name": "Alexander",
                "lastName": "Loginov",
                "photo": "https://sun1-21.userapi.com/c852128/v852128656/dc39f/T0J3xJcmCOg.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 4310356,
                "name": "Natalya",
                "lastName": "Alexandrova",
                "photo": "https://pp.userapi.com/c625130/v625130356/31a85/WY_P9ZbXF1Q.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }], [{
                "id": 140891700,
                "name": "Denis",
                "lastName": "Chernyshev",
                "photo": "https://pp.userapi.com/c845323/v845323617/10b9d1/vdXPyzuNZPQ.jpg?ava=1"
            }, {
                "id": 4836403,
                "name": "Anton",
                "lastName": "Zudov",
                "photo": "https://pp.userapi.com/c840629/v840629387/66d2d/GvXmRGBgnUE.jpg?ava=1"
            }, {
                "id": 11316310,
                "name": "Ekaterina",
                "lastName": "Chernysheva",
                "photo": "https://pp.userapi.com/c846221/v846221387/103080/FOfwBTE1RVY.jpg?ava=1"
            }]]

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
                <div style={{display: "inline-block", margin: "1em"}}>
                    <InputUserInfo index={0} chooseCallback={this.userChosen}/>
                </div>
                <div style={{display: "inline-block", margin: "1em"}}>
                    <InputUserInfo index={1} chooseCallback={this.userChosen}/>
                </div>
                <br/>
                <button disabled={!this.state.canFind} className="btn  btn-primary" onClick={this.findChain}>Искать
                </button>
                {/*<div className="jumbotron">*/}
                {/*<h1 className="display-3">*/}
                {/*App name*/}
                {/*<button className="btn  btn-primary" onClick={this.revert}>Revert</button>*/}
                {/*</h1>*/}
                {/*</div>*/}
                {/*<ArticleList articles={this.state.reverted ? articles.slice().reverse() : articles}/>*/}
                <ChainList chainList={this.state.chainList}/>
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
                return response.json()
            })
            .then(message => {
                console.log("search", message);
                this.setState({chainList: message});
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