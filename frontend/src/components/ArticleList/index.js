import React, {PureComponent} from 'react'
import Article from "../Article";
import './style.css'

export default class ArticleList extends PureComponent {

    state = {
        openArticleId: null
    }

    render() {
        const articleElements = this.props.articles.map(article =>
            <li key={article.id} className="article-list_li">
                <Article article={article}
                         isOpen={this.state.openArticleId === article.id}
                         onOpenClick={this.handleClick.bind(this, article.id)}/>
            </li>
        );
        return (
            <ul>
                {articleElements}
            </ul>
        )
    }

    handleClick = openArticleId => {
        this.setState({
            openArticleId: this.state.openArticleId === openArticleId ? null : openArticleId
        })
    }
}