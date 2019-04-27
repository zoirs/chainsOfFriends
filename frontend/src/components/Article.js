import React, {Component} from "react";

class Article extends Component {

    // constructor(props) {
    //     super(props)
    //
    //     this.state = {
    //         isOpen: true
    //     }
    // }

    state = {
        isOpen: false
    }

    handleClick = () => {
        this.setState({
            isOpen: !this.state.isOpen
        })
        console.log("===")
    }

    render() {
        const {article} = this.props
        const body = this.state.isOpen && <selection>{article.text}</selection>
        return (
            <div className='hello'>
                <h2>{article.title}
                    <button onClick={this.handleClick}>
                        {this.state.isOpen ? 'close' : 'open'}
                    </button>
                </h2>
                {body}
                <h3>Date: {(new Date(article.date)).toDateString()}</h3>
            </div>
        )
    }
}

function handleClick() {

}

export default Article