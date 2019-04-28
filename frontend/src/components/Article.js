import React, {PureComponent} from "react";

class Article extends PureComponent {

    render() {
        const {article, isOpen, onOpenClick} = this.props
        const body = isOpen && <selection className="card-text">{article.text}</selection>
        return (
            <div className='card mx-auto' style={{width: '50%'}}>
                <div className="card-header">
                    <h2>{article.title}
                        <button onClick={onOpenClick} className="btn btn-primary btn-lg float-right">
                            {isOpen ? 'close' : 'open'}
                        </button>
                    </h2>
                </div>
                <div className="card-body">

                    {body}
                    <h6 className="card-subtitle text-muted">Date: {(new Date(article.date)).toDateString()}</h6>
                </div>
            </div>
        )
    }
}


export default Article