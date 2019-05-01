import React, {Component} from 'react'


class InputUserInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            inputValue: ''
        };

        this.handleChange = this.handleChange.bind(this);
    }


    render() {
        return (
            <div className="container">
                {/*<label>{this.state.name} {this.state.surname}</label>*/}
                <input type="text" value={this.state.inputValue} onChange={this.handleChange}
                       onBlur={this.onGetUserId}/>
            </div>
        )
    }

    handleChange = (e) => {
        console.log(e.target.value);
        this.setState({inputValue: e.target.value});
    };

    onGetUserId = () => {
        console.log(this.state.inputValue);
        alert(this.state.inputValue);
    };

}

export default InputUserInfo