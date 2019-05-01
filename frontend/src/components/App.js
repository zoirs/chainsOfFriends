import React, {Component} from "react";
import {Route, IndexRoute} from 'react-router';

import ArticleList from './ArticleList'
import articles from '../fixture'
import 'bootstrap/dist/css/bootstrap.css'

class App extends Component {
    state = {
        reverted: false
    }

    render() {
        return (
            <div className="container">
                <button className="btn  btn-primary" onClick={this.hello}>Залогинетсяww</button>
                {/*<div onClick={this.props.history.push('/path')}> some stuff </div>*/}
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

    hello = () => {
        fetch('/api/authurl')
        .then(response => {
            console.log(response);
            return response.text()
        })
        .then(message => {
            console.log(message);
                window.location = message;
        });
    };
}

export default App